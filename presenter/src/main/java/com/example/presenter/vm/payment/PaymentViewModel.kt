package com.example.presenter.vm.payment

import androidx.lifecycle.ViewModel
import com.example.entity.model.payment.PaymentProvider
import com.example.entity.model.payment.PaymentRequest
import com.example.usecase.GetProvidersUseCase
import com.example.usecase.MakePaymentUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject

@HiltViewModel
class PaymentViewModel @Inject constructor(
    private val getProvidersUseCase : GetProvidersUseCase,
    private val makePaymentUseCase: MakePaymentUseCase
) : ViewModel() , ContainerHost<PaymentViewModel.PaymentState, PaymentViewModel.PaymentSideEffect>{
    override val container = container<PaymentState, PaymentSideEffect>(PaymentState())
    
    data class PaymentState(
        val isLoading :  Boolean = false,
        val providers : List<PaymentProvider> = emptyList(),
        val error : String ?= null
    )

    sealed class PaymentSideEffect {
        data class ShowError(val message: String) : PaymentSideEffect()
        data class ShowSuccess(val message: String) : PaymentSideEffect()
    }

    fun fetchProviders(category : String? = null) = intent{
        reduce {
            state.copy(isLoading = true)
        }
        val result = getProvidersUseCase(category)
        result.onSuccess {
            providers ->
            reduce {
                state.copy(isLoading = false,providers = providers,error = null)
            }
        }.onFailure {
            reduce {
                state.copy(isLoading = false, error= it.message)
            }
        }
    }

    fun makePayment(providerId: String, cardId: String, amount: Int, account: String) = intent {
        reduce { state.copy(isLoading = true) }
        val request = PaymentRequest(providerId, cardId, amount, account)
        val result = makePaymentUseCase(request)
        reduce { state.copy(isLoading = false) }
        result.onSuccess {
            postSideEffect(PaymentSideEffect.ShowSuccess(it))
        }.onFailure {
            postSideEffect(PaymentSideEffect.ShowError(it.message ?: "Xatolik yuz berdi"))
        }
    }
}