package com.dannbrown.deltaboxlib.common

import com.dannbrown.deltaboxlib.common.init.DeltaboxBlocks
import com.dannbrown.deltaboxlib.common.init.DeltaboxLang
import com.dannbrown.deltaboxlib.platform.registrate.DeltaboxRegistrate
import com.tterrag.registrate.providers.ProviderType
import net.minecraft.data.DataGenerator
import org.slf4j.LoggerFactory

object DeltaboxLib {
    const val MOD_ID: String = "deltaboxlib"
    @JvmField
    val LOGGER = LoggerFactory.getLogger(MOD_ID)
    val REGISTRATE = DeltaboxRegistrate(MOD_ID)

    fun init() {
        DeltaboxBlocks.register()
    }

    fun gatherData(gen: DataGenerator.PackGenerator) {
        REGISTRATE.addDataGenerator(ProviderType.LANG, DeltaboxLang::addLang)
    }
}