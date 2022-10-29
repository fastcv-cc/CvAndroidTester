package cc.fastcv.cvandroidtester

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.splashscreen.SplashScreen
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import cc.fastcv.cv_mvvm.CvBaseMvvmActivity
import cc.fastcv.cv_mvvm.CvBaseViewModel
import cc.fastcv.cvandroidtester.databinding.ActivitySplashBinding
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.concurrent.atomic.AtomicBoolean

@SuppressLint("CustomSplashScreen")
@Route(path = "/app/SplashActivity")
class SplashActivity : CvBaseMvvmActivity<CvBaseViewModel, ActivitySplashBinding>() {

    companion object {
        private const val TAG = "SplashActivity"
    }

    private lateinit var mSplashScreen: SplashScreen

    // 数据
    private var mKeepOnAtomicBool = AtomicBoolean(true)

    override val viewModel: CvBaseViewModel
        get() = ViewModelProvider(this).get(CvBaseViewModel::class.java)


    override val layoutResID: Int
        get() = R.layout.activity_splash

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //注入
        ARouter.getInstance().inject(this)

        // 每次UI绘制前，会判断 Splash 是否有必要继续展示在屏幕上；直到不再满足条件时，隐藏Splash。
        mSplashScreen.setKeepVisibleCondition { mKeepOnAtomicBool.get() }

        // Splash展示完毕的监听方法
        mSplashScreen.setOnExitAnimationListener {
            it.view.visibility = View.GONE
            Log.d(TAG, "onCreate: start!!!")
            lifecycleScope.launch {
                // Splash展示2秒钟
                delay(2000)
                Log.d(TAG, "onCreate: finish!!!")
                withContext(Dispatchers.Main) {
                    ARouter.getInstance().build("/app/MainActivity").navigation()
                }
            }
        }
        Log.d(TAG, "onCreate: init!!!")
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