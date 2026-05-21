package com.example.presenter.vm.exchange

import androidx.lifecycle.ViewModel
import com.example.entity.model.exchange.ExchangeRate
import com.example.usecase.GetExchangeRatesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject

@HiltViewModel
class ExchangeViewModel @Inject constructor(
    private val getExchangeRatesUseCase: GetExchangeRatesUseCase
) : ContainerHost<ExchangeViewModel.ExchangeState, ExchangeViewModel.ExchangeSideEffect>, ViewModel() {

    override val container = container<ExchangeState, ExchangeSideEffect>(ExchangeState())

    init {
        fetchExchangeRates()
    }

    fun fetchExchangeRates() = intent {
        reduce { state.copy(isLoading = true) }
        val result = getExchangeRatesUseCase()
        reduce { state.copy(isLoading = false) }

        result.onSuccess { rates ->
            reduce { state.copy(rates = rates) }
        }.onFailure {
            postSideEffect(ExchangeSideEffect.ShowError(it.message ?: "Valyuta kurslarini yuklashda xatolik"))
        }
    }

    data class ExchangeState(
        val rates: List<ExchangeRate> = emptyList(),
        val isLoading: Boolean = false
    )

    sealed class ExchangeSideEffect {
        data class ShowError(val message: String) : ExchangeSideEffect()
    }
}
