package com.example.presenter.vm.auth

import androidx.lifecycle.ViewModel
import com.example.usecase.RefreshTokenUseCase
import com.example.usecase.SendOtpUseCase
import com.example.usecase.SetPinUseCase
import com.example.usecase.VerifyOtpUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val sendOtpUseCase: SendOtpUseCase,
    private val verifyOtpUseCase: VerifyOtpUseCase,
    private val refreshTokenUseCase: RefreshTokenUseCase,
    private val setPinUseCase: SetPinUseCase
) : ContainerHost<AuthViewModel.AuthState, AuthViewModel.AuthSideEffect>, ViewModel() {

    override val container = container<AuthState, AuthSideEffect>(AuthState())

    fun onPhoneNumberChanged(phone: String) = intent {
        reduce { state.copy(phoneNumber = phone) }
    }

    fun onOtpChanged(otp: String) = intent {
        reduce { state.copy(otp = otp) }
    }

    fun sendOtp() = intent {
        val phone = state.phoneNumber
        if (!phone.startsWith("+998")) {
            postSideEffect(AuthSideEffect.ShowToast("Phone number must start with +998"))
            return@intent
        }

        reduce { state.copy(isLoading = true) }
        val result = sendOtpUseCase(phone)
        reduce { state.copy(isLoading = false) }

        result.onSuccess {
            postSideEffect(AuthSideEffect.NavigateToOtp)
        }.onFailure {
            postSideEffect(AuthSideEffect.ShowToast(it.message ?: "OTP send failed"))
        }
    }

    fun verifyOtp() = intent {
        val phone = state.phoneNumber
        val otp = state.otp
        if (otp.length < 6) {
            postSideEffect(AuthSideEffect.ShowToast("Enter full OTP"))
            return@intent
        }

        reduce { state.copy(isLoading = true) }
        val result = verifyOtpUseCase(phone, otp)
        reduce { state.copy(isLoading = false) }

        result.onSuccess {
            postSideEffect(AuthSideEffect.NavigateToHome)
        }.onFailure {
            postSideEffect(AuthSideEffect.ShowToast(it.message ?: "Verification failed"))
        }
    }

    fun refresh(refreshToken: String) = intent {
        reduce { state.copy(isLoading = true) }
        val result = refreshTokenUseCase(refreshToken)
        reduce { state.copy(isLoading = false) }

        result.onFailure {
            postSideEffect(AuthSideEffect.ShowToast(it.message ?: "Refresh failed"))
        }
    }

    fun setPin(pin: String) = intent {
        reduce { state.copy(isLoading = true) }
        val result = setPinUseCase(pin)
        reduce { state.copy(isLoading = false) }

        result.onSuccess {
            postSideEffect(AuthSideEffect.SetPinSuccess)
        }.onFailure {
            postSideEffect(AuthSideEffect.ShowToast(it.message ?: "PIN set failed"))
        }
    }

    data class AuthState(
        val phoneNumber: String = "+998",
        val otp: String = "",
        val isLoading: Boolean = false
    )

    sealed class AuthSideEffect {
        data class ShowToast(val message: String) : AuthSideEffect()
        object NavigateToOtp : AuthSideEffect()
        object NavigateToHome : AuthSideEffect()
        object SetPinSuccess : AuthSideEffect()
    }
}