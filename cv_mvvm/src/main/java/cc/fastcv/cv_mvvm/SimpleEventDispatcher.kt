package cc.fastcv.cv_mvvm

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.collect

/**
 *
 * @author xcl
 * create at 2022/9/9 11:15
 * 简单事件分发器
 *
 */

object SimpleEventDispatcher {
    private val event = MutableSharedFlow<String>()

    suspend fun emit(eventMsg:String) {
        event.emit(eventMsg)
    }

    suspend fun observerEvent(receiver:((String)->Unit)) {
        event.collect {
            receiver(it)
        }
    }
}