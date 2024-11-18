package com.dannbrown.deltaboxlib.platform

import com.dannbrown.deltaboxlib.common.*
import com.dannbrown.deltaboxlib.platform.util.Util
/*? if fabric {*/

/*import net.fabricmc.api.ModInitializer;

object DeltaboxLibWrapper : ModInitializer {
    override fun onInitialize() {
        DeltaboxLib.init()
        DeltaboxLib.REGISTRATE.register() // fabric exclusive registrate
    }
}
*//*?} elif forge {*/
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.eventbus.api.IEventBus
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext
import net.minecraftforge.common.MinecraftForge

@Mod(DeltaboxLib.MOD_ID)
class DeltaboxLibWrapper {
    companion object {
        init {
            Util.LOGGER.info("$DeltaboxLib.MOD_ID has started!")
            val eventBus = FMLJavaModLoadingContext.get().modEventBus
            val forgeEventBus = MinecraftForge.EVENT_BUS
            register(eventBus, forgeEventBus)
        }

        fun register(modBus: IEventBus, forgeEventBus: IEventBus) {
            DeltaboxLib.init()
            DeltaboxLib.REGISTRATE.registerEventListeners(modBus) // forge exclusive registrate
        }
    }
}
/*?} else {*/
/*import net.neoforged.fml.common.Mod

@Mod(DeltaboxLib.MOD_ID)
class DeltaboxLibWrapper() {
    init {
        DeltaboxLib.init()
    }
}
*//*?}*/