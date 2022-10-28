package cc.fastcv.cv_mvvm

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableSharedFlow

open class BaseViewModel : ViewModel() {

    /**
     * 是否关闭Activity
     */
    val closeUI = MutableSharedFlow<Boolean>()

    /**
     * 是否处于加载状态
     */
    var loading = MutableLiveData<Boolean>()

}