package com.example.presenter.vm.loan

import androidx.lifecycle.ViewModel
import com.example.entity.model.loan.LoanData
import com.example.usecase.GetLoansUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject

@HiltViewModel
class LoanViewModel @Inject constructor(
    private val getLoansUseCase: GetLoansUseCase
) : ViewModel(), ContainerHost<LoanViewModel.LoanState, LoanViewModel.LoanSideEffect> {

    override val container = container<LoanState, LoanSideEffect>(LoanState())

    init {
        fetchLoans()
    }

    fun fetchLoans() = intent {
        reduce { state.copy(isLoading = true) }
        val result = getLoansUseCase()
        reduce { state.copy(isLoading = false) }

        result.onSuccess { loans ->
            reduce { state.copy(loans = loans, error = null) }
        }.onFailure {
            reduce { state.copy(error = it.message) }
            postSideEffect(LoanSideEffect.ShowError(it.message ?: "Kreditlarni yuklashda xatolik"))
        }
    }

    data class LoanState(
        val loans: List<LoanData> = emptyList(),
        val isLoading: Boolean = false,
        val error: String? = null
    )

    sealed class LoanSideEffect {
        data class ShowError(val message: String) : LoanSideEffect()
    }
}
