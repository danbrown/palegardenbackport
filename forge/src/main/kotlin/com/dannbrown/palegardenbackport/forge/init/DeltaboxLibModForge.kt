package com.dannbrown.palegardenbackport.forge.init

import com.dannbrown.palegardenbackport.init.ModContent
import dev.architectury.platform.forge.EventBuses
import net.minecraftforge.fml.common.Mod
import thedarkcolour.kotlinforforge.forge.MOD_BUS

@Mod(ModContent.MOD_ID)
object DeltaboxLibModForge {
    init {
        // Submit our event bus to let architectury register our content on the right time
        EventBuses.registerModEventBus(ModContent.MOD_ID, MOD_BUS)
        ModContent.init()
    }
}