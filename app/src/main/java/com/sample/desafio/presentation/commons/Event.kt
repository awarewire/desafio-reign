package com.sample.desafio.presentation.commons

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer


/**
 * Used as a wrapper for data that is exposed via a LiveData that represents an event.
 */
open class Event<out T>(private val content: T) {
    companion object {
        fun <T> buildEvent(value: T): Event<T> {
            return Event(value)
        }
    }

    var hasBeenHandled = false
        private set // Allow external read but not write

    /**
     * Returns the content and prevents its use again.
     */
    fun getContentIfNotHandled(): T? {
        return if (hasBeenHandled) {
            null
        } else {
            hasBeenHandled = true
            content
        }
    }

    /**
     * Returns the content, even if it's already been handled.
     */
    fun peekContent(): T = content
}

class EventObserver<T>(private val onEventUnhandledContent: (T) -> Unit) : Observer<Event<T>> {
    override fun onChanged(event: Event<T>?) {
        event?.getContentIfNotHandled()?.let {
            onEventUnhandledContent(it)
        }
    }
}

inline fun <T> LiveData<Event<T>>.observeEvent(
    lifecycleOwner: LifecycleOwner,
    crossinline observer: (T) -> Unit
): Observer<Event<T>> = EventObserver<T> { t -> observer(t) }.also {
    this.observe(lifecycleOwner, it)
}