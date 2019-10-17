package jp.yn.light.repeattimer

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer

fun <T> LiveData<T>.observeOnChangedNonNull(owner: LifecycleOwner, observer: (T) -> Unit) {
    var prev : T? = null
    this.observe(owner, Observer<T> { observeValue ->
        observeValue?.let {
            if (it != prev) {
                observer(it)
            }
        }
        prev = observeValue
    })
}