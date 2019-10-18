package com.xd.core.data_event

import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.Observer
import android.content.Context
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity
import com.xd.core.base.getViewModel

interface DataEvent

interface DataEventCallBack


fun Context.sendEvent(dataEvent: DataEvent){
    getViewModel<DataEventVM>().liveData.value = dataEvent
}

fun Fragment.sendEvent(dataEvent: DataEvent){
    getViewModel<DataEventVM>().liveData.value = dataEvent
}

fun LifecycleOwner.sendEvent(dataEvent: DataEvent){
    when(this){
        is Context -> (this as Context).sendEvent(dataEvent)
        is Fragment -> this.sendEvent(dataEvent)
    }
}

fun Context.postEvent(dataEvent: DataEvent){
    getViewModel<DataEventVM>().liveData.postValue(dataEvent)
}

fun Fragment.postEvent(dataEvent: DataEvent){
    getViewModel<DataEventVM>().liveData.postValue(dataEvent)
}

fun LifecycleOwner.postEvent(dataEvent: DataEvent){
    when(this){
        is Context -> (this as Context).postEvent(dataEvent)
        is Fragment -> this.postEvent(dataEvent)
    }
}

fun FragmentActivity.registDataEvent(cb: DataEventCallBack){
    (this as Context).getViewModel<DataEventVM>().liveData
        .observe(this, DataEventCallBackWrapper(cb))
}

fun Fragment.registDataEvent(cb: DataEventCallBack){
    getViewModel<DataEventVM>().liveData
        .observe(this, DataEventCallBackWrapper(cb))
}

private class DataEventCallBackWrapper(val cb: DataEventCallBack): Observer<DataEvent> {

    init {
        DataEventMethodCache.registClass(cb::class.java)
    }

    override fun onChanged(event: DataEvent?) {
        event?.apply{
            var clazz: Class<*> = event::class.java
            while (DataEvent::class.java.isAssignableFrom(clazz)){
                DataEventMethodCache.getCallBackInfo(cb::class.java)
                    ?.methods
                    ?.get(event::class.java)
                    ?.onDataEvent(cb,event)
                clazz = clazz.superclass
            }
        }
    }
}
