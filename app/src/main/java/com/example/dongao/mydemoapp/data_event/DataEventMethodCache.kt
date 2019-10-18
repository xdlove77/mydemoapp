package com.xd.core.data_event

import java.lang.reflect.Method

object DataEventMethodCache {
    private val callBackMap = mutableMapOf<Class<*>, CallBackInfo>()

    fun getCallBackInfo(clazz: Class<*>) = callBackMap[clazz]

    fun registClass(clazz: Class<*>) {
        if (callBackMap[clazz] != null)
            return
        val methods = resolveClass(clazz)
        if (methods.isNotEmpty()) {
            callBackMap[clazz] =
                CallBackInfo(methods)
        }
    }

    private fun resolveClass(clazz: Class<*>): Map<Class<*>, MethodReference> {
        val methods = mutableMapOf<Class<*>, MethodReference>()
        clazz.declaredMethods.forEach {
            if (it.parameterTypes.size == 1
                && it.getAnnotation(DataEventAnno::class.java) != null
            ) {
                methods[it.parameterTypes[0]] = MethodReference(it)
            }
        }
        return methods
    }
}

class CallBackInfo(val methods: Map<Class<*>, MethodReference>)

class MethodReference(private val method: Method) {
    fun onDataEvent(target: Any, event: DataEvent) {
        method.invoke(target, event)
    }
}