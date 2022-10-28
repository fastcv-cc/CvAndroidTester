package cc.fastcv.cv_base

import androidx.localbroadcastmanager.content.LocalBroadcastManager


class CvLocalBroadcastReceiver : CvBroadcastReceiver() {

    override fun register(shouldRegister: Boolean) {
        context?.let {
            if (shouldRegister && !hasRegisterReceiver) {
                LocalBroadcastManager.getInstance(it).registerReceiver(this, intentFilter)
                hasRegisterReceiver = true
            }
        }
    }

    override fun unregister(shouldUnregister: Boolean) {
        context?.let {
            if (shouldUnregister && hasRegisterReceiver) {
                LocalBroadcastManager.getInstance(it).unregisterReceiver(this)
                hasRegisterReceiver = false
            }
        }
    }
}