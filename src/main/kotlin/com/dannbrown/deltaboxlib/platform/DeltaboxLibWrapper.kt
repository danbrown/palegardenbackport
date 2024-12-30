package com.dannbrown.deltaboxlib.platform

import com.dannbrown.deltaboxlib.common.*
import com.dannbrown.deltaboxlib.platform.registrate.generators.trades.VillagerTradeProvider
import com.dannbrown.deltaboxlib.platform.util.DeltaboxUtil


/*? if fabric {*/
/*import net.fabricmc.api.ModInitializer;
import net.fabricmc.api.ClientModInitializer
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment

object DeltaboxLibWrapper : ModInitializer, ClientModInitializer {
    override fun onInitialize() {
        DeltaboxUtil.logInfo("mod has started!")
        DeltaboxLib.init()
        DeltaboxLib.REGISTRATE.register() // fabric exclusive registrate
    }

    @Environment(EnvType.CLIENT)
    override fun onInitializeClient() {
        DeltaboxUtil.logInfo("client mod has started!")
        DeltaboxLib.REGISTRATE.registerClient() // fabric exclusive registrate
    }
}
*//*?} elif forge {*/
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.eventbus.api.IEventBus
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.data.event.GatherDataEvent
import net.minecraftforge.eventbus.api.EventPriority

@Mod(DeltaboxLib.MOD_ID)
class DeltaboxLibWrapper {
    companion object {
        init {
            DeltaboxUtil.logInfo("mod has started!")
            val eventBus = FMLJavaModLoadingContext.get().modEventBus
            val forgeEventBus = MinecraftForge.EVENT_BUS
            register(eventBus, forgeEventBus)
        }

        fun register(modBus: IEventBus, forgeEventBus: IEventBus) {
            DeltaboxLib.init()
            modBus.addListener(EventPriority.LOWEST) { event: GatherDataEvent ->
                DeltaboxLib.gatherData(event.generator)
                event.generator.addProvider(event.includeServer(), VillagerTradeProvider(DeltaboxLib.REGISTRATE, event.generator))
            }
            DeltaboxLib.REGISTRATE.register(modBus, forgeEventBus) // forge exclusive registrate
        }
    }
}
/*?} else {*/
/*import net.neoforged.fml.common.Mod
import net.neoforged.bus.api.IEventBus
import net.neoforged.fml.ModContainer
import net.neoforged.neoforge.common.NeoForge
import net.neoforged.neoforge.data.event.GatherDataEvent

@Mod(DeltaboxLib.MOD_ID)
class DeltaboxLibWrapper(eventBus: IEventBus, modContainer: ModContainer) {
    init {
        DeltaboxUtil.logInfo("mod has started!")
        val forgeEventBus = NeoForge.EVENT_BUS
        register(eventBus, forgeEventBus)
    }
    fun register(modBus: IEventBus, forgeEventBus: IEventBus) {
        DeltaboxLib.init()
        modBus.addListener { event: GatherDataEvent ->
            DeltaboxLib.gatherData(event.generator)
            event.generator.addProvider(event.includeServer(), VillagerTradeProvider(DeltaboxLib.REGISTRATE, event.generator))
        }
        DeltaboxLib.REGISTRATE.register(modBus, forgeEventBus) // neoforged exclusive registrate
    }
}
*//*?}*/