package com.example.presenter.vm.settings

import androidx.lifecycle.ViewModel
import com.example.entity.model.user.UserProfile
import com.example.usecase.GetProfileUseCase
import com.example.usecase.UpdateProfileUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val getProfileUseCase: GetProfileUseCase,
    private val updateProfileUseCase: UpdateProfileUseCase
) : ContainerHost<SettingsViewModel.SettingsState, SettingsViewModel.SettingsSideEffect>, ViewModel() {

    override val container = container<SettingsState, SettingsSideEffect>(SettingsState())

    init {
        loadProfile()
    }

    fun loadProfile() = intent {
        reduce { state.copy(isLoading = true) }
        val result = getProfileUseCase()
        reduce { state.copy(isLoading = false) }

        result.onSuccess { profile ->
            reduce { state.copy(userProfile = profile) }
        }.onFailure {
            postSideEffect(SettingsSideEffect.ShowError(it.message ?: "Fail"))
        }
    }

    fun updateProfile(fullName: String) = intent {
        reduce { state.copy(isLoading = true) }
        val result = updateProfileUseCase(fullName)
        reduce { state.copy(isLoading = false) }

        result.onSuccess { profile ->
            reduce { state.copy(userProfile = profile) }
            postSideEffect(SettingsSideEffect.ShowSuccess("Done !"))
        }.onFailure {
            postSideEffect(SettingsSideEffect.ShowError(it.message ?: "Update error"))
        }
    }

    data class SettingsState(
        val userProfile: UserProfile? = null,
        val isLoading: Boolean = false
    )

    sealed class SettingsSideEffect {
        data class ShowError(val message: String) : SettingsSideEffect()
        data class ShowSuccess(val message: String) : SettingsSideEffect()
    }
}
