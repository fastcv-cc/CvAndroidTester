package cc.fastcv.cv_base

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent

open class CvBroadcastReceiver : BroadcastReceiver() {

    protected var hasRegisterReceiver = false

    protected val intentFilter: IntentFilter by lazy { IntentFilter() }
    private lateinit var callback: CvReceiverCallback
    protected var context: Context? = null

    override fun onReceive(context: Context, intent: Intent) {
        callback.onReceiver(context, intent)
    }

    fun withFilter(withFilter: (intentFilter: IntentFilter) -> Unit): CvBroadcastReceiver {
        withFilter(intentFilter)
        return this
    }

    fun setCallback(callback: CvReceiverCallback): CvBroadcastReceiver {
        this.callback = callback
        return this
    }

    fun register() {
        register(true)
    }

    fun unregister() {
        unregister(true)
    }

    fun bind(
        context: Context,
        lifecycle: Lifecycle,
        event: Lifecycle.Event = Lifecycle.Event.ON_CREATE
    ): CvBroadcastReceiver {
        this.context = context
        lifecycle.addObserver(object : LifecycleObserver {
            @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
            fun onCreate() {
                register(event == Lifecycle.Event.ON_CREATE)
            }

            @OnLifecycleEvent(Lifecycle.Event.ON_START)
            fun onStart() {
                register(event == Lifecycle.Event.ON_START)
            }

            @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
            fun onResume() {
                register(event == Lifecycle.Event.ON_RESUME)
            }

            @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
            fun onPause() {
                unregister(event == Lifecycle.Event.ON_RESUME)
            }

            @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
            fun onStop() {
                unregister(event == Lifecycle.Event.ON_START)
            }

            @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
            fun onDestroy() {
                unregister(event == Lifecycle.Event.ON_CREATE)
            }
        })

        return this
    }

    protected open fun register(shouldRegister: Boolean) {
        context?.let {
            if (shouldRegister && !hasRegisterReceiver) {
                it.registerReceiver(this, intentFilter)
                hasRegisterReceiver = true
            }
        }
    }

    protected open fun unregister(shouldUnregister: Boolean) {
        context?.let {
            if (shouldUnregister && hasRegisterReceiver) {
                it.unregisterReceiver(this)
                hasRegisterReceiver = false
            }
        }
    }
}

interface CvReceiverCallback {
    fun onReceiver(context: Context, intent: Intent)
}