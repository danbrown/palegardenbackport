package com.dannbrown.deltaboxlib.platform

import com.dannbrown.deltaboxlib.common.*
import com.dannbrown.deltaboxlib.platform.util.DeltaboxUtil

/*? if fabric {*/
/*import net.fabricmc.api.ModInitializer;
import net.fabricmc.api.ClientModInitializer
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment

object DeltaboxLibWrapper : ModInitializer, ClientModInitializer {
    override fun onInitialize() {
        DeltaboxUtil.logInfo("mod has started!")
        DeltaboxLibCommon.init()
        DeltaboxLibCommon.REGISTRATE.register() // fabric exclusive registrate
    }

    @Environment(EnvType.CLIENT)
    override fun onInitializeClient() {
        DeltaboxUtil.logInfo("client mod has started!")
        DeltaboxLibCommon.REGISTRATE.registerClient() // fabric exclusive registrate
    }
}
*//*?} elif forge {*/
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.eventbus.api.IEventBus
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.data.event.GatherDataEvent
import net.minecraftforge.eventbus.api.EventPriority
import thedarkcolour.kotlinforforge.forge.DIST

@Mod(DeltaboxLibCommon.MOD_ID)
class DeltaboxLibWrapper {
    companion object {
        init {
            DeltaboxUtil.logInfo("mod has started!")
            val eventBus = FMLJavaModLoadingContext.get().modEventBus
            val forgeEventBus = MinecraftForge.EVENT_BUS
            register(eventBus, forgeEventBus)
            // client
            if (DIST.isClient) {
              // register main mod client content
              registerClient(eventBus, forgeEventBus)
            }
        }

        fun register(modBus: IEventBus, forgeEventBus: IEventBus) {
            DeltaboxLibCommon.init()
            modBus.addListener(EventPriority.LOWEST) { event: GatherDataEvent ->
                DeltaboxLibCommon.gatherData(event.generator)
                DeltaboxLibCommon.REGISTRATE.gatherData(event)
            }
            DeltaboxLibCommon.REGISTRATE.register(modBus, forgeEventBus) // forge exclusive registrate
        }

        fun registerClient(modBus: IEventBus, forgeEventBus: IEventBus) {
            DeltaboxLibCommon.REGISTRATE.registerClient(modBus, forgeEventBus) // forge exclusive registrate
        }
    }
}
/*?} else {*/
/*import net.neoforged.fml.common.Mod
import net.neoforged.bus.api.IEventBus
import net.neoforged.fml.ModContainer
import net.neoforged.neoforge.common.NeoForge
import net.neoforged.neoforge.data.event.GatherDataEvent

@Mod(DeltaboxLibCommon.MOD_ID)
class DeltaboxLibWrapper(eventBus: IEventBus, modContainer: ModContainer) {
    init {
        DeltaboxUtil.logInfo("mod has started!")
        val forgeEventBus = NeoForge.EVENT_BUS
        register(eventBus, forgeEventBus)
        // client
        if (ModStatus.isClient) {
          // register main mod client content
          registerClient(eventBus, forgeEventBus)
        }
    }
    fun register(modBus: IEventBus, forgeEventBus: IEventBus) {
        DeltaboxLibCommon.init()
        modBus.addListener { event: GatherDataEvent ->
            DeltaboxLibCommon.gatherData(event.generator)
            DeltaboxLibCommon.REGISTRATE.gatherData(event)
        }
        DeltaboxLibCommon.REGISTRATE.register(modBus, forgeEventBus) // neoforged exclusive registrate
    }

    fun registerClient(modBus: IEventBus, forgeEventBus: IEventBus) {
        DeltaboxLibCommon.REGISTRATE.registerClient(modBus, forgeEventBus) // neoforged exclusive registrate
    }
}
*//*?}*/