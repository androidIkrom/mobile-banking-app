package com.example.presenter.vm.kyc

import androidx.lifecycle.ViewModel
import com.example.entity.model.kyc.KycStatusData
import com.example.usecase.GetKycStatusUseCase
import com.example.usecase.SubmitKycUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject

@HiltViewModel
class KycViewModel @Inject constructor(
    private val getKycStatusUseCase: GetKycStatusUseCase,
    private val submitKycUseCase: SubmitKycUseCase
) : ContainerHost<KycViewModel.KycState, KycViewModel.KycSideEffect>, ViewModel() {

    override val container = container<KycState, KycSideEffect>(KycState())

    fun fetchKycStatus() = intent {
        reduce { state.copy(isLoading = true) }
        val result = getKycStatusUseCase()
        reduce { state.copy(isLoading = false) }

        result.onSuccess { data ->
            reduce { state.copy(kycStatus = data) }
        }.onFailure {
            postSideEffect(KycSideEffect.ShowError(it.message ?: "Failed to fetch KYC status"))
        }
    }

    fun submitKyc(
        passportSeries: String,
        passportNumber: String,
        birthDate: String,
        selfieBase64: String
    ) = intent {
        reduce { state.copy(isLoading = true) }
        val result = submitKycUseCase(
            passportSeries = passportSeries,
            passportNumber = passportNumber,
            birthDate = birthDate,
            selfieBase64 = selfieBase64
        )
        reduce { state.copy(isLoading = false) }

        result.onSuccess {
            postSideEffect(KycSideEffect.SubmitSuccess)
            fetchKycStatus()
        }.onFailure {
            postSideEffect(KycSideEffect.ShowError(it.message ?: "Failed to submit KYC"))
        }
    }

    data class KycState(
        val isLoading: Boolean = false,
        val kycStatus: KycStatusData? = null
    )

    sealed class KycSideEffect {
        data class ShowError(val message: String) : KycSideEffect()
        object SubmitSuccess : KycSideEffect()
    }
}
