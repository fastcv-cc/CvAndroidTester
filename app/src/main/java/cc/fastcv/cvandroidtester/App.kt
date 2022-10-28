package cc.fastcv.cvandroidtester

import cc.fastcv.cv_base.CvApp
import com.alibaba.android.arouter.launcher.ARouter

class App : CvApp() {
    override fun onCreate() {
        super.onCreate()
        // 这两行必须写在init之前，否则这些配置在init过程中将无效
        if (BuildConfig.DEBUG) {
            ARouter.openLog();     // 打印日志
            ARouter.openDebug();   // 开启调试模式(如果在InstantRun模式下运行，必须开启调试模式！线上版本需要关闭,否则有安全风险)
        }
        ARouter.init(this); // 尽可能早，推荐在Application中初始化
    }
}