package com.dannbrown.deltaboxlib.common

import com.dannbrown.deltaboxlib.common.init.DeltaboxBlocks
import com.dannbrown.deltaboxlib.common.init.DeltaboxLang
import com.dannbrown.deltaboxlib.platform.registrate.DeltaboxRegistrate
import com.dannbrown.deltaboxlib.platform.util.Util
import com.tterrag.registrate.providers.ProviderType
import net.minecraft.data.DataGenerator
import org.slf4j.LoggerFactory

object DeltaboxLib {
    const val MOD_ID: String = "deltaboxlib"

    @JvmField
    val LOGGER = LoggerFactory.getLogger(MOD_ID)

    val REGISTRATE = DeltaboxRegistrate(MOD_ID)

    fun id(path: String) = Util.resourceLocation(MOD_ID, path)

    private var initialized = false

    fun init() {
        println("HELLO WORLD IM A MOD INITIALIZER")
        DeltaboxBlocks.register()
    }

    fun gatherData(gen: DataGenerator.PackGenerator) {
        REGISTRATE.addDataGenerator(ProviderType.LANG, DeltaboxLang::addLang)
    }

    @JvmStatic
    fun postInit() {
        if (initialized) return
        initialized = true
    }
}