package com.dannbrown.palegardenbackport.platform

import com.dannbrown.palegardenbackport.common.*
import com.dannbrown.deltaboxlib.platform.util.DeltaboxUtil
import com.dannbrown.deltaboxlib.platform.util.ModStatus

/*? if fabric {*/
/*import net.fabricmc.api.ModInitializer;
import net.fabricmc.api.ClientModInitializer
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment

object ModWrapper : ModInitializer, ClientModInitializer {
    override fun onInitialize() {
        DeltaboxUtil.logInfo("mod has started!")
        ModCommon.init()
        ModCommon.REGISTRATE.register() // fabric exclusive registrate
    }

    @Environment(EnvType.CLIENT)
    override fun onInitializeClient() {
        DeltaboxUtil.logInfo("client mod has started!")
        ModCommon.REGISTRATE.registerClient() // fabric exclusive registrate
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

@Mod(ModCommon.MOD_ID)
class ModWrapper {
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
            ModCommon.init()
            modBus.addListener(EventPriority.LOWEST) { event: GatherDataEvent ->
                ModCommon.gatherData(event.generator)
                ModCommon.REGISTRATE.gatherData(event)
            }
            ModCommon.REGISTRATE.register(modBus, forgeEventBus) // forge exclusive registrate
        }

        fun registerClient(modBus: IEventBus, forgeEventBus: IEventBus) {
            ModCommon.REGISTRATE.registerClient(modBus, forgeEventBus) // forge exclusive registrate
        }
    }
}
/*?} else {*/
/*import net.neoforged.fml.common.Mod
import net.neoforged.bus.api.IEventBus
import net.neoforged.fml.ModContainer
import net.neoforged.neoforge.common.NeoForge
import net.neoforged.neoforge.data.event.GatherDataEvent

@Mod(ModCommon.MOD_ID)
class ModWrapper(eventBus: IEventBus, modContainer: ModContainer) {
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
        ModCommon.init()
        modBus.addListener { event: GatherDataEvent ->
            ModCommon.gatherData(event.generator)
            ModCommon.REGISTRATE.gatherData(event)
        }
        ModCommon.REGISTRATE.register(modBus, forgeEventBus) // neoforged exclusive registrate
    }

    fun registerClient(modBus: IEventBus, forgeEventBus: IEventBus) {
        ModCommon.REGISTRATE.registerClient(modBus, forgeEventBus) // neoforged exclusive registrate
    }
}
*//*?}*/