package jp.yn.light.repeatTimer

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

@Suppress("UNCHECKED_CAST")
class DisplayRemainingTimeFragmentViewModelFactory(
    private val listener: DisplayRemainingTimeFragmentActionCreator.Listener,
    private val store: DisplayRemainingTimeFragmentStore,
    private val owner: () -> LifecycleOwner
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {

        if (modelClass == DisplayRemainingTimeFragmentViewModel::class.java) {
            return DisplayRemainingTimeFragmentViewModel(listener, store, owner) as T
        }

        throw IllegalArgumentException("${modelClass.name} : not found")
    }
}