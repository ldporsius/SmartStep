package nl.codingwithlinda.smartstep.application.di.viewmodel_service

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

inline fun <reified VM: ViewModel>viewModelFactoryHelper(crossinline initializer: () -> VM) : ViewModelProvider.Factory{
    return object : ViewModelProvider.Factory{
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return initializer() as T
        }
    }
}