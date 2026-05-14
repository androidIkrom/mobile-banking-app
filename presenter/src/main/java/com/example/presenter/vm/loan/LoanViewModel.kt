package com.example.presenter.vm.loan

import androidx.lifecycle.ViewModel
import com.example.entity.model.loan.ApplyLoanRequest
import com.example.entity.model.loan.LoanData
import com.example.entity.model.loan.RepayLoanRequest
import com.example.usecase.ApplyLoanUseCase
import com.example.usecase.GetLoansUseCase
import com.example.usecase.RepayLoanUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject

@HiltViewModel
class LoanViewModel @Inject constructor(
    private val getLoansUseCase: GetLoansUseCase,
    private val applyLoanUseCase: ApplyLoanUseCase,
    private val repayLoanUseCase: RepayLoanUseCase
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

    fun applyLoan(amount: Double, termMonths: Int, cardId: String) = intent {
        reduce { state.copy(isLoading = true) }
        val result = applyLoanUseCase(ApplyLoanRequest(amount, termMonths, cardId))
        reduce { state.copy(isLoading = false) }

        result.onSuccess {
            postSideEffect(LoanSideEffect.LoanApplied)
            fetchLoans()
        }.onFailure {
            postSideEffect(LoanSideEffect.ShowError(it.message ?: "Kredit olishda xatolik"))
        }
    }

    fun repayLoan(loanId: String, cardId: String, amount: Double) = intent {
        reduce { state.copy(isLoading = true) }
        val result = repayLoanUseCase(loanId, RepayLoanRequest(cardId, amount))
        reduce { state.copy(isLoading = false) }

        result.onSuccess {
            postSideEffect(LoanSideEffect.LoanRepaid)
            fetchLoans()
        }.onFailure {
            postSideEffect(LoanSideEffect.ShowError(it.message ?: "Kreditni so'ndirishda xatolik"))
        }
    }

    data class LoanState(
        val loans: List<LoanData> = emptyList(),
        val isLoading: Boolean = false,
        val error: String? = null
    )

    sealed class LoanSideEffect {
        data class ShowError(val message: String) : LoanSideEffect()
        object LoanApplied : LoanSideEffect()
        object LoanRepaid : LoanSideEffect()
    }
}
