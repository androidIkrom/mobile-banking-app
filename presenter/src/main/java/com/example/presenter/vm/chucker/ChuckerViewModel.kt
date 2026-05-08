package com.example.presenter.vm.chucker

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.usecase.ClearLogsUseCase
import com.example.usecase.GetNetworkLogsUseCase
import com.example.usecase.model.NetworkLog
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject
@HiltViewModel
class ChuckerViewModel @Inject constructor(
    private val getAllLogsUseCase : GetNetworkLogsUseCase,
    private val clearLogsUseCase: ClearLogsUseCase
) : ViewModel() {
    val logs : StateFlow<List<NetworkLog>> = getAllLogsUseCase().stateIn(viewModelScope,
        SharingStarted.WhileSubscribed(5000),emptyList())
    fun clearLogs(){
        viewModelScope.launch {
            clearLogsUseCase()
        }
    }
}