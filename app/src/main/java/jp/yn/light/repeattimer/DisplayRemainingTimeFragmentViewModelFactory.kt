package jp.yn.light.repeattimer

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

@Suppress("UNCHECKED_CAST")
class DisplayRemainingTimeFragmentViewModelFactory(
    private val vibratorAccessor: VibratorAccessor,
    private val store: DisplayRemainingTimeFragmentStore,
    private val owner: () -> LifecycleOwner
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {

        if (modelClass == DisplayRemainingTimeFragmentViewModel::class.java) {
            return DisplayRemainingTimeFragmentViewModel(vibratorAccessor, store, owner) as T
        }

        throw IllegalArgumentException("${modelClass.name} : not found")
    }
}