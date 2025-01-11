package com.dannbrown.deltaboxlib.common

import com.dannbrown.deltaboxlib.common.init.*
import com.dannbrown.deltaboxlib.platform.registrate.DeltaboxRegistrate
import com.tterrag.registrate.providers.ProviderType
import net.minecraft.data.DataGenerator
import org.slf4j.LoggerFactory

object DeltaboxLibCommon {
    const val MOD_ID: String = "deltaboxlib"
    @JvmField
    val LOGGER = LoggerFactory.getLogger(MOD_ID)
    val REGISTRATE = DeltaboxRegistrate(MOD_ID)

    fun init() {
        DeltaboxBlocks.register()
        DeltaboxItems.register()
        DeltaboxTrades.register()
        DeltaboxCreativeTabs.register()
        DeltaboxRecipes.register()
    }

    fun gatherData(gen: DataGenerator) {
        REGISTRATE.addDataGenerator(ProviderType.LANG, DeltaboxLang::addLang)
    }
}