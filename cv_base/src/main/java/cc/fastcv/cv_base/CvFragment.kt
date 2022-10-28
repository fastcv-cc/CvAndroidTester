package cc.fastcv.cv_base

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment

class CvFragment : Fragment(), CvReceiverCallback {

    /**
     * 防止连续点击按钮
     * @return
     */
    private var lastClickTime: Long = 0

    protected val receiver by lazy {
        CvBroadcastReceiver()
    }

    protected fun getObServerActions(): Array<String> {
        return arrayOf()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if (getObServerActions().isNotEmpty()) {
            activity?.let { activity ->
                receiver
                    .withFilter {
                        for (obServerAction in getObServerActions()) {
                            it.addAction(obServerAction)
                        }
                    }
                    .setCallback(this)
                    .bind(activity, lifecycle)
            }
        }
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    /**
     * 判断是否是快速点击
     * 返回 true 是快速点击
     *     false 不是快速点击
     */
    fun checkFastClick(): Boolean {
        val time = System.currentTimeMillis()
        val dTime = time - lastClickTime
        if (dTime in 0..499) {
            return true
        }

        lastClickTime = time
        return false
    }

    /**
     * 判断是否是 从右到左 布局
     */
    fun isRtl(): Boolean {
        return view?.layoutDirection == View.LAYOUT_DIRECTION_RTL
    }

    override fun onReceiver(context: Context, intent: Intent) {

    }
}