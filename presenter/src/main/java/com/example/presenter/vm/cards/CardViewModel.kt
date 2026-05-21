package com.example.presenter.vm.cards

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.entity.model.card.CardData
import com.example.usecase.AttachCardUseCase
import com.example.usecase.GetCardsUseCase
import com.example.usecase.SetMainCardUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CardViewModel @Inject constructor(
    private val getCardsUseCase: GetCardsUseCase,
    private val attachCardUseCase: AttachCardUseCase,
    private val setMainCardUseCase: SetMainCardUseCase
) : ViewModel() {

    private val _cards = MutableStateFlow<List<CardData>>(emptyList())
    val cards: StateFlow<List<CardData>> = _cards

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    fun fetchCards() {
        viewModelScope.launch {
            _isLoading.value = true
            getCardsUseCase().onSuccess {
                _cards.value = it
                _error.value = null
            }.onFailure {
                _error.value = it.message
            }
            _isLoading.value = false
        }
    }

    fun attachCard(cardNumber: String) {
        viewModelScope.launch {
            _isLoading.value = true
            attachCardUseCase(cardNumber).onSuccess {
                fetchCards()
                _error.value = null
            }.onFailure {
                _error.value = it.message
            }
            _isLoading.value = false
        }
    }

    fun setMainCard(cardId: String) {
        viewModelScope.launch {
            _isLoading.value = true
            setMainCardUseCase(cardId).onSuccess {
                fetchCards()
                _error.value = null
            }.onFailure {
                _error.value = it.message
            }
            _isLoading.value = false
        }
    }
}
