package cc.fastcv.cvandroidtester

import android.os.Bundle
import androidx.core.view.GravityCompat
import androidx.lifecycle.ViewModelProvider
import cc.fastcv.cv_mvvm.CvBaseMvvmActivity
import cc.fastcv.cv_mvvm.CvBaseViewModel
import cc.fastcv.cvandroidtester.databinding.ActivityMainBinding
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter

@Route(path = "/app/MainActivity")
class MainActivity : CvBaseMvvmActivity<CvBaseViewModel,ActivityMainBinding>() {

    override val layoutResID: Int
        get() = R.layout.activity_main

    override val viewModel: CvBaseViewModel
        get() = ViewModelProvider(this).get(CvBaseViewModel::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //注入
        ARouter.getInstance().inject(this)
        mBinding.drawerLayout.openDrawer(GravityCompat.START, true)
    }
}