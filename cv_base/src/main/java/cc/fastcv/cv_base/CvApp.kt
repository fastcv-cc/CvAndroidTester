package cc.fastcv.cv_base

import android.app.Application
import android.os.Build
import android.util.Config
import com.alibaba.android.arouter.BuildConfig
import com.alibaba.android.arouter.launcher.ARouter
import me.jessyan.autosize.AutoSizeConfig
import me.jessyan.autosize.unit.Subunits

abstract class CvApp : Application() {

    override fun onCreate() {
        super.onCreate()
        AutoSizeConfig.getInstance().setExcludeFontScale(true).setLog(false).unitsManager.supportSubunits = Subunits.PT
        // 这两行必须写在init之前，否则这些配置在init过程中将无效
        if (isDebug()) {
            ARouter.openLog();     // 打印日志
            ARouter.openDebug();   // 开启调试模式(如果在InstantRun模式下运行，必须开启调试模式！线上版本需要关闭,否则有安全风险)
        }
        ARouter.init(this); // 尽可能早，推荐在Application中初始化

    }

    abstract fun isDebug() : Boolean


}