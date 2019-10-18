package com.xd.core.data_event

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel


class DataEventVM: ViewModel(){
    val liveData = MutableLiveData<DataEvent>()
}