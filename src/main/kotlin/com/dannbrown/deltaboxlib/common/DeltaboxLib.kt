package com.dannbrown.deltaboxlib.common

import com.dannbrown.deltaboxlib.platform.util.Util
import org.slf4j.LoggerFactory

object DeltaboxLib {
    const val MOD_ID: String = "deltaboxlib"

    @JvmField
    val LOGGER = LoggerFactory.getLogger(MOD_ID)

    fun id(path: String) = Util.resourceLocation(MOD_ID, path)

    private var initialized = false

    fun init() {
        println("HELLO WORLD IM A MOD INITIALIZER")
    }

    @JvmStatic
    fun postInit() {
        if (initialized) return
        initialized = true
    }
}