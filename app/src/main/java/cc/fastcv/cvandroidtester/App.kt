package cc.fastcv.cvandroidtester

import cc.fastcv.cv_base.CvApp

class App : CvApp() {
    override fun isDebug(): Boolean {
        return BuildConfig.DEBUG
    }
}