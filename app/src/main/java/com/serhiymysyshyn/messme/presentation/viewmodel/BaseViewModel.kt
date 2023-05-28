package com.serhiymysyshyn.messme.presentation.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.serhiymysyshyn.messme.domain.type.Failure
import com.serhiymysyshyn.messme.domain.type.HandleOnce

abstract class BaseViewModel : ViewModel() {

    var failureData: MutableLiveData<HandleOnce<Failure>> = MutableLiveData()
    var progressData: MutableLiveData<Boolean> = MutableLiveData()

    protected fun handleFailure(failure: Failure) {
        this.failureData.value = HandleOnce(failure)
        updateProgress(false)
    }

    protected fun updateProgress(progress: Boolean) {
        this.progressData.value = progress
    }
}