package cc.fastcv.cvandroidtester

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.splashscreen.SplashScreen
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import cc.fastcv.cv_mvvm.BaseMvvmActivity
import cc.fastcv.cv_mvvm.BaseViewModel
import cc.fastcv.cvandroidtester.databinding.ActivitySplashBinding
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.concurrent.atomic.AtomicBoolean

@SuppressLint("CustomSplashScreen")
class SplashActivity : BaseMvvmActivity<BaseViewModel,ActivitySplashBinding>() {

    companion object {
        private const val TAG = "SplashActivity"
    }

    private lateinit var mSplashScreen: SplashScreen

    // 数据
    private var mKeepOnAtomicBool = AtomicBoolean(true)

    override val viewModel: BaseViewModel
        get() = ViewModelProvider(this).get(BaseViewModel::class.java)


    override val layoutResID: Int
        get() = R.layout.activity_splash

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // 每次UI绘制前，会判断 Splash 是否有必要继续展示在屏幕上；直到不再满足条件时，隐藏Splash。
        mSplashScreen.setKeepVisibleCondition { mKeepOnAtomicBool.get() }

        // Splash展示完毕的监听方法
        mSplashScreen.setOnExitAnimationListener {
            Log.d(TAG, "onCreate: ExitAnimationListener")
            it.view.visibility = View.GONE
        }

        Log.d(TAG, "onCreate: init")
        lifecycleScope.launch {
            // Splash展示2秒钟
            delay(1000)
            // Splash 展示完毕
            mKeepOnAtomicBool.compareAndSet(true, false)
        }
    }

    override fun beforeOnCreate() {
        super.beforeOnCreate()
        // 初始化操作(必须放在setContentView()之前)
        mSplashScreen = installSplashScreen()
    }

}