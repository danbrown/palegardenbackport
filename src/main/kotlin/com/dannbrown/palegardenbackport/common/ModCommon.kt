package com.dannbrown.palegardenbackport.common

import com.dannbrown.palegardenbackport.common.init.*
import com.dannbrown.deltaboxlib.platform.registrate.DeltaboxRegistrate
import net.minecraft.data.DataGenerator
import org.slf4j.LoggerFactory

object ModCommon {
    const val MOD_ID: String = "palegardenbackport"
    @JvmField
    val LOGGER = LoggerFactory.getLogger(MOD_ID)
    val REGISTRATE = DeltaboxRegistrate(MOD_ID)

    fun init() {
        ModBlocks.register()
        ModItems.register()
        ModTags.register()
        ModWoodTypes.register()
        ModParticles.register()
        ModTrades.register()
        ModCreativeTabs.register()
        ModRecipes.register()
        ModLang.register()
        ModPlacerTypes.register()
        ModConfiguredFeatures.register()
        ModPlacedFeatures.register()
        ModBiomeModifiers.register()
    }

    fun gatherData(gen: DataGenerator) {
    }
}