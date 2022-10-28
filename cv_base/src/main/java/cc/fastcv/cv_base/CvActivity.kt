package cc.fastcv.cv_base

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.gyf.immersionbar.ImmersionBar

open class CvActivity : AppCompatActivity(), CvReceiverCallback {

    /**
     * 防止连续点击按钮
     * @return
     */
    private var lastClickTime: Long = 0

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

    protected fun getObServerActions(): Array<String> {
        return arrayOf()
    }

    protected val receiver by lazy {
        CvBroadcastReceiver()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ImmersionBar.with(this).transparentStatusBar().transparentNavigationBar().statusBarDarkFont(statusBarIconIsDark())
            .navigationBarDarkIcon(navigationBarIconIsDark()).init()
        compatFix()

        if (getObServerActions().isNotEmpty()) {
            receiver
                .withFilter {
                    for (obServerAction in getObServerActions()) {
                        it.addAction(obServerAction)
                    }
                }
                .setCallback(this)
                .bind(this, lifecycle)
        }
    }

    private fun compatFix() {
        //解决安卓8.1版本输入框获取焦点时奔溃的问题
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1) {
            window.decorView.importantForAutofill =
                View.IMPORTANT_FOR_AUTOFILL_NO_EXCLUDE_DESCENDANTS
        }
    }

    /**
     * 判断是否是 从右到左 布局
     */
    fun isRtl(): Boolean {
        return window.decorView.layoutDirection == View.LAYOUT_DIRECTION_RTL
    }

    /**
     * 设置虚拟导航栏图标的颜色
     * 默认是 暗色
     * 返回  false则变为亮色
     */
    protected fun navigationBarIconIsDark(): Boolean {
        return true
    }

    /**
     * 设置状态栏图标的颜色
     * 默认是 暗色
     * 返回  false则变为亮色
     */
    protected fun statusBarIconIsDark(): Boolean {
        return true
    }

    /**
     * 触摸无事件处理区域是否隐藏软键盘
     */
    protected fun touchToHideKeyboard(): Boolean {
        return true
    }

    override fun onPause() {
        super.onPause()
        // 移除当前获取焦点控件的焦点，防止下个界面软键盘顶起布局
        currentFocus?.clearFocus()
    }

    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        if (touchToHideKeyboard()) {
            if (ev.action == MotionEvent.ACTION_DOWN) {
                if (shouldHideInput(currentFocus, ev)) {
                    // 需要隐藏软键盘
                    hideSoftKeyboard(currentFocus)
                }
                return super.dispatchTouchEvent(ev)
            }
            if (window.superDispatchTouchEvent(ev)) {
                return true
            }
            return onTouchEvent(ev)
        } else {
            return super.dispatchTouchEvent(ev)
        }
    }

    /** 根据当前焦点控件[v]、触摸事件[ev]判断是否需要隐藏软键盘 */
    private fun shouldHideInput(v: View?, ev: MotionEvent): Boolean {
        if (v is EditText) {
            // 是输入框
            val leftTop = intArrayOf(0, 0)
            // 获取输入框当前的位置
            v.getLocationInWindow(leftTop)
            val top = leftTop[1]
            val bottom = top + v.height
            // 触摸位置不在输入框范围内，需要隐藏
            return !(ev.y > top && ev.y < bottom)
        }
        return false
    }

    private fun hideSoftKeyboard(view: View?) {
        if (view == null) {
            return
        }
        view.clearFocus()
        (view.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager)
            .hideSoftInputFromWindow(view.windowToken, 0)
    }

    /**
     * 广播回调
     */
    override fun onReceiver(context: Context, intent: Intent) {

    }
}