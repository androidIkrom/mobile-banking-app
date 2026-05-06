package com.example.presenter.vm.profile

import androidx.lifecycle.ViewModel
import com.example.entity.local.PrefsManager
import com.example.entity.model.user.UserProfile
import com.example.usecase.GetProfileUseCase
import com.example.usecase.UpdateProfileUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val getProfileUseCase: GetProfileUseCase,
    private val updateProfileUseCase: UpdateProfileUseCase,
    private val prefsManager: PrefsManager
) : ContainerHost<ProfileViewModel.ProfileState, ProfileViewModel.ProfileSideEffect>, ViewModel() {

    override val container = container<ProfileState, ProfileSideEffect>(ProfileState(
        isNewUser = prefsManager.isNewUser
    ))

    init {
        if (!prefsManager.isNewUser) {
            loadProfile()
        }
    }

    fun loadProfile() = intent {
        reduce { state.copy(isLoading = true) }
        val result = getProfileUseCase()
        reduce { state.copy(isLoading = false) }

        result.onSuccess { profile ->
            reduce { state.copy(userProfile = profile) }
        }.onFailure {
            postSideEffect(ProfileSideEffect.ShowError(it.message ?: "Profilni yuklashda xatolik"))
        }
    }

    fun updateProfile(fullName: String) = intent {
        reduce { state.copy(isLoading = true) }
        val result = updateProfileUseCase(fullName)
        reduce { state.copy(isLoading = false) }

        result.onSuccess { profile ->
            prefsManager.isNewUser = false
            reduce { state.copy(userProfile = profile, isNewUser = false) }
            postSideEffect(ProfileSideEffect.ShowSuccess("Profil muvaffaqiyatli yangilandi"))
            loadProfile()
        }.onFailure {
            postSideEffect(ProfileSideEffect.ShowError(it.message ?: "Profilni yangilashda xatolik"))
        }
    }

    data class ProfileState(
        val userProfile: UserProfile? = null,
        val isLoading: Boolean = false,
        val isNewUser: Boolean = false
    )

    sealed class ProfileSideEffect {
        data class ShowError(val message: String) : ProfileSideEffect()
        data class ShowSuccess(val message: String) : ProfileSideEffect()
    }
}
