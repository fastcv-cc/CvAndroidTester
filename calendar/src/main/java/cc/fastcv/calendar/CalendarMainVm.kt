package cc.fastcv.calendar

import androidx.lifecycle.MutableLiveData
import cc.fastcv.cv_mvvm.CvBaseViewModel

class CalendarMainVm : CvBaseViewModel() {

    /**
     * 是否授予日历权限
     */
    val hasPermission = MutableLiveData(false)



}