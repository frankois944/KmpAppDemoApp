package com.cacd2.cacdgame

import kotlin.coroutines.coroutineContext
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.isActive

/**
 * Created by francois.dabonot@cacd2.fr on 01/06/2023.
 */
object EventBus {
    val mEvents = MutableSharedFlow<Any>(replay = 1)
    val events = mEvents.asSharedFlow()

    suspend fun publish(event: Any) {
        mEvents.emit(event)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    suspend inline fun <reified T> subscribe(crossinline onEvent: (T) -> Unit) {
        events.filterIsInstance<T>()
            .collectLatest { event ->
                if (coroutineContext.isActive) {
                    onEvent(event)
                    mEvents.resetReplayCache()
                }
            }
    }
}
