package com.example.dongao.mydemoapp.data_event

import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.ViewModel
import android.content.Context
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity
import java.lang.IllegalArgumentException

inline fun <reified T : ViewModel> Context.getViewModel(): T {
    return ViewModelProviders.of(this as FragmentActivity).get(T::class.java)
}

inline fun <reified T : ViewModel> Fragment.getViewModel(): T {
    return ViewModelProviders.of(this).get(T::class.java)
}

inline fun <reified T : ViewModel> LifecycleOwner.getViewModel(): T {
    return when (this) {
        is Context -> (this as Context).getViewModel<T>()
        is Fragment -> getViewModel<T>()
        else -> throw IllegalArgumentException("getViewModel failed")
    }
}