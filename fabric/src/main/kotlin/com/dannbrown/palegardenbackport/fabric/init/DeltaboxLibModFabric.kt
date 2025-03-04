package com.dannbrown.palegardenbackport.fabric.init

import com.dannbrown.palegardenbackport.init.ModContent
import net.fabricmc.api.ModInitializer


object DeltaboxLibModFabric: ModInitializer {
    override fun onInitialize() {
        ModContent.init()
    }
}
