package com.example.presenter.vm.transaction

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.entity.model.transaction.TransactionData
import com.example.usecase.GetTransactionsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TransactionViewModel @Inject constructor(
    private val getTransactionsUseCase: GetTransactionsUseCase
) : ViewModel() {

    private val _transactions = MutableStateFlow<List<TransactionData>>(emptyList())
    val transactions: StateFlow<List<TransactionData>> = _transactions

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    fun fetchTransactions(cardId: String? = null, type: String? = null) {
        viewModelScope.launch {
            _isLoading.value = true
            getTransactionsUseCase(page = 1, pageSize = 20, cardId = cardId, type = type)
                .onSuccess {
                    _transactions.value = it
                }
            _isLoading.value = false
        }
    }
}
