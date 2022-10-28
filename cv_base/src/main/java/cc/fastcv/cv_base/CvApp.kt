package cc.fastcv.cv_base

import android.app.Application
import me.jessyan.autosize.AutoSizeConfig
import me.jessyan.autosize.unit.Subunits

open class CvApp : Application() {

    override fun onCreate() {
        super.onCreate()
        AutoSizeConfig.getInstance().setExcludeFontScale(true).setLog(false).unitsManager.supportSubunits = Subunits.PT
    }
}