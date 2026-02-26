package nl.codingwithlinda.smartstep.features.main.presentation.permissions

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update

class PermissionsViewModel: ViewModel() {

    private val _permissionUiState = MutableStateFlow(PermissionUiState.NA)
    val permissionUiState = _permissionUiState.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), PermissionUiState.NA)

    fun setPermissionState(uiState: PermissionUiState) {
        _permissionUiState.update {
           uiState
        }
    }

    val shouldShowUserInteraction = _permissionUiState.map {
        it != PermissionUiState.NA
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), false)



}

