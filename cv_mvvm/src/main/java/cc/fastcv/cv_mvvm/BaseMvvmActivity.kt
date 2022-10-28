package cc.fastcv.cv_mvvm

import android.os.Bundle
import android.view.LayoutInflater
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.lifecycleScope
import cc.fastcv.cv_base.CvActivity
import com.google.android.material.transition.platform.MaterialSharedAxis
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

abstract class BaseMvvmActivity<VM : BaseViewModel, DB : ViewDataBinding> : CvActivity() {

    companion object {
        /** Activity 界面跳转动画时长 ms */
        protected const val ACTIVITY_ANIM_DURATION = 230L
    }


    /** 当前界面 ViewModel 对象 */
    abstract val viewModel: VM

    /** DataBinding 对象 */
    protected lateinit var mBinding: DB

    override fun onCreate(savedInstanceState: Bundle?) {
        beforeOnCreate()
        super.onCreate(savedInstanceState)
    }

    /** [onCreate] 之前执行，可用于配置动画 */
    protected open fun beforeOnCreate() {
        window.run {
            enterTransition = MaterialSharedAxis(MaterialSharedAxis.X, true).apply {
                duration = ACTIVITY_ANIM_DURATION
            }
            exitTransition = MaterialSharedAxis(MaterialSharedAxis.X, false).apply {
                duration = ACTIVITY_ANIM_DURATION
            }
        }
    }

    override fun setContentView(layoutResID: Int) {
        // 初始化 DataBinding
        mBinding = DataBindingUtil.inflate(
            LayoutInflater.from(this),
            layoutResID, null, false
        )

        // 绑定生命周期管理
        mBinding.lifecycleOwner = this

        // 设置布局
        super.setContentView(mBinding.root)
        setObServer()
    }

    private fun setObServer() {
        //处理Activity关闭事件
        observerFlow{
            viewModel.closeUI.collect {
                onBackPressed()
            }
        }

        //监听全局Simple事件
        observerFlow {
            SimpleEventDispatcher.observerEvent {
                receiverSimpleEvent(it)
            }
        }

        viewModel.loading.observe(this) {
            if (it) {
                showLoading()
            } else {
                hideLoading()
            }
        }
    }

    protected fun showLoading() {

    }

    protected fun hideLoading() {

    }

    protected fun sendSimpleEvent(msg: String) {
        lifecycleScope.launch(Dispatchers.IO) {
            SimpleEventDispatcher.emit(msg)
        }
    }

    protected fun receiverSimpleEvent(msg: String) {

    }

    protected fun observerFlow(block:suspend () -> Unit) {
        lifecycleScope.launchWhenCreated {
            block()
        }
    }

}