package com.example.presenter.vm.transfer

import androidx.lifecycle.ViewModel
import com.example.entity.model.transfer.InitiateTransferRequest
import com.example.usecase.CheckCardUseCase
import com.example.usecase.ConfirmOtpUseCase
import com.example.usecase.ConfirmPinUseCase
import com.example.usecase.InitiateTransferUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject

@HiltViewModel
class TransferViewModel @Inject constructor(
    private val initiateTransferUseCase: InitiateTransferUseCase,
    private val confirmPinUseCase: ConfirmPinUseCase,
    private val checkCardUseCase: CheckCardUseCase,
    private val confirmOtpUseCase: ConfirmOtpUseCase
) : ContainerHost<TransferViewModel.TransferState, TransferViewModel.TransferSideEffect>,
    ViewModel() {
    override val container = container<TransferState, TransferSideEffect>(TransferState())

    data class TransferState(
        val isLoading: Boolean = false,
        val confirmToken: String? = null,
        val recipientName: String? = null,
        val phoneNumber: String = "",
        val fromCardId: String = "",
        val toCardNumber: String = "",
        val amount: Double = 0.0
    )

    sealed class TransferSideEffect {
        data class ShowError(val message: String) : TransferSideEffect()
        data class ShowSuccess(val message: String) : TransferSideEffect()
        object NavigateToConfirm : TransferSideEffect()
        object NavigateToPinConfirm : TransferSideEffect()
    }

    fun setDraft(fromCardId: String, toCardNumber: String, amount: Double) = intent {
        reduce {
            state.copy(
                fromCardId = fromCardId,
                toCardNumber = toCardNumber,
                amount = amount
            )
        }
    }

    fun setPhone(phoneNumber: String) = intent {
        reduce {
            state.copy(phoneNumber = phoneNumber)
        }
    }

    fun checkCard(cardNumber: String) = intent {
        if (cardNumber.length < 16) {
            reduce {
                state.copy(recipientName = null)
            }
            return@intent
        }
        val result = checkCardUseCase(cardNumber)
        result.onSuccess { data ->
            reduce {
                state.copy(recipientName = data.ownerName)
            }
        }.onFailure {
            reduce {
                state.copy(recipientName = "Topilmadi")
            }
        }
    }


    fun initiateTransfer(
        fromCardId: String,
        toCardNumber: String,
        amount: Double,
        pin: String,
        description: String,
        isPinMethod: Boolean = false
    ) = intent {
        reduce {
            state.copy(isLoading = true)
        }
        val request = InitiateTransferRequest(fromCardId, toCardNumber, amount, pin, description)
        val result = initiateTransferUseCase(request)
        reduce { state.copy(isLoading = false) }
        result.onSuccess { data ->
            if (data.requiresConfirmation) {
                reduce {
                    state.copy(confirmToken = data.confirmToken)
                }
                if (isPinMethod) {
                    postSideEffect(TransferSideEffect.NavigateToPinConfirm)
                } else {
                    postSideEffect(TransferSideEffect.NavigateToConfirm)
                }
            } else {
                postSideEffect(TransferSideEffect.ShowSuccess("O'tkazma muvaffaqiyatli yakunlandi!"))
            }
        }.onFailure {
            postSideEffect(TransferSideEffect.ShowError(it.message ?: "Xatolik yuz berdi"))
        }
    }

    fun confirmWithOtp(otp: String) = intent {
        val token = state.confirmToken ?: return@intent
        reduce { state.copy(isLoading = true) }
        val result = confirmOtpUseCase(token, otp)
        reduce {
            state.copy(isLoading = false)
        }
        result.onSuccess {
            postSideEffect(TransferSideEffect.ShowSuccess("O`tkazma muvaffaqiyatli bo`ldi"))
        }.onFailure { exception ->
            postSideEffect(TransferSideEffect.ShowError(exception.message ?: "Tasdiqlashda xato!"))
        }
    }

    fun confirmWithPin(pin: String) = intent {
        val token = state.confirmToken ?: return@intent
        reduce { state.copy(isLoading = true) }
        val result = confirmPinUseCase(token, pin)
        reduce { state.copy(isLoading = false) }
        result.onSuccess {
            postSideEffect(TransferSideEffect.ShowSuccess("O'tkazma muvaffaqiyatli bo'ldi"))
        }.onFailure { exception ->
            postSideEffect(TransferSideEffect.ShowError(exception.message ?: "Tasdiqlashda xato!"))
        }
    }
}
