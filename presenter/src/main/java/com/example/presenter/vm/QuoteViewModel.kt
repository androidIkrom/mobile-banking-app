package com.example.presenter.vm

import androidx.lifecycle.ViewModel
import com.example.entity.Quote
import com.example.usecase.FetchNewQuoteUseCase
import com.example.usecase.GetLocalNoteUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject

@HiltViewModel
class QuoteViewModel @Inject constructor(
    private val getQuotesUseCase: GetLocalNoteUseCase,
    private val fetchNewQuoteUseCase: FetchNewQuoteUseCase
) : ContainerHost<QuoteViewModel.QuoteState, QuoteViewModel.QuoteSideEffect> , ViewModel() {
    override val container = container<QuoteState, QuoteSideEffect>(QuoteState())

    init {
        getQuotes()
    }

    private fun getQuotes() = intent {
        getQuotesUseCase().collect { quotes ->
            reduce {
                state.copy(quotes = quotes)
            }
        }
    }
    fun getNewQuote() = intent{
        reduce {
            state.copy(isLoading = true)
        }
        try {
            fetchNewQuoteUseCase()
        }
        catch (e : Exception){
            postSideEffect(QuoteSideEffect.ShowToast(e.message.toString()))
        }finally {
            reduce {
                state.copy(isLoading = false)
            }
        }
    }
    data class QuoteState(
        val quotes : List<Quote> = emptyList(),
        val isLoading : Boolean = false
    )
    sealed class QuoteSideEffect{
        data class ShowToast(val message: String) : QuoteSideEffect()
    }
}