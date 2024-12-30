package com.dannbrown.deltaboxlib.platform.registrate

import com.dannbrown.deltaboxlib.platform.registrate.generators.trades.*
import com.dannbrown.deltaboxlib.platform.util.DeltaboxUtil
import com.tterrag.registrate.AbstractRegistrate
import com.tterrag.registrate.util.entry.BlockEntry
import net.minecraft.resources.ResourceLocation
import net.minecraft.server.packs.resources.PreparableReloadListener
import net.minecraft.world.entity.npc.VillagerProfession
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.trading.MerchantOffer
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.FlowerPotBlock
import java.util.function.BiConsumer
import java.util.function.Supplier

/*? if fabric {*/
/*import net.fabricmc.fabric.api.registry.FlammableBlockRegistry
import net.fabricmc.fabric.api.registry.StrippableBlockRegistry
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap
import net.fabricmc.fabric.api.resource.IdentifiableResourceReloadListener
import net.fabricmc.fabric.api.resource.ResourceManagerHelper
import net.fabricmc.fabric.api.`object`.builder.v1.trade.TradeOfferHelper
import net.minecraft.server.packs.PackType
import net.minecraft.server.packs.resources.PreparableReloadListener.PreparationBarrier
import net.minecraft.server.packs.resources.ResourceManager
import net.minecraft.util.profiling.ProfilerFiller
import java.util.concurrent.CompletableFuture
import java.util.concurrent.Executor

*//*?}*/

class DeltaboxRegistrate(modId: String): AbstractRegistrate<DeltaboxRegistrate>(modId) {

  // Special Blocks
  private val FLAMMABLE_BLOCKS: MutableList<Triple<BlockEntry<out Block>, Int, Int>> = mutableListOf()
  private val STRIPPABLE_BLOCKS: MutableList<Pair<BlockEntry<out Block>, Supplier<out Block>>> = mutableListOf()
  private val POTTED_BLOCKS: MutableList<Pair<BlockEntry<out Block>, BlockEntry<out Block>>> = mutableListOf()
  private val CUTOUT_RENDERS: MutableList<BlockEntry<out Block>> = mutableListOf()


  // flammable
  fun addFlammableBlock(block: BlockEntry<out Block>, burnChance: Int, spreadChance: Int) {
    FLAMMABLE_BLOCKS.add(Triple(block, burnChance, spreadChance))
  }

  fun getFlammableBlocks(): List<Triple<BlockEntry<out Block>, Number, Number>> {
    return FLAMMABLE_BLOCKS
  }

  // strippable
  fun addStrippableBlock(block: BlockEntry<out Block>, strippedBlock: Supplier<out Block>) {
    STRIPPABLE_BLOCKS.add(Pair(block, strippedBlock))
  }

  fun getStrippableBlocks(): List<Pair<BlockEntry<out Block>, Supplier<out Block>>> {
    return STRIPPABLE_BLOCKS
  }

  // potted
  fun addPottedBlock(block: BlockEntry<out Block>, pottedBlock: BlockEntry<out Block>) {
    POTTED_BLOCKS.add(Pair(block, pottedBlock))
  }

  fun getPottedBlocks(): List<Pair<BlockEntry<out Block>, BlockEntry<out Block>>> {
    return POTTED_BLOCKS
  }

  // cutout
  fun addCutoutRender(block: BlockEntry<out Block>) {
    CUTOUT_RENDERS.add(block)
  }

  fun getCutoutRenders(): List<BlockEntry<out Block>> {
    return CUTOUT_RENDERS
  }

  // Data providers
  val TRADES: MutableList<VillagerTradeCodec> = ArrayList()

  fun villagerTrade(
    profession: VillagerProfession,
    level: VillagerLevel,
    tradeCosts: List<VillagerTradeItem>,
    tradeSells: List<VillagerTradeItem>,
    maxUses: Int,
    xpAmount: Int,
    priceMultiplier: Float
  ) {
      TRADES.add(VillagerTradeCodec(profession, level, tradeCosts, tradeSells, maxUses, xpAmount, priceMultiplier))
  }

  fun updateTradesData(trades: List<VillagerTradeCodec>) {
    TRADES.clear()
    TRADES.addAll(trades)
  }


  // FABRIC SPECIFIC BLOCKS FEATURES REGISTRATION
  /*? if fabric {*/
  /*override fun register() {
    super.register()

    val registry: (ResourceLocation, PreparableReloadListener) -> Unit = { id, listener ->
        ResourceManagerHelper.get(PackType.SERVER_DATA).registerReloadListener(object : IdentifiableResourceReloadListener {
            override fun getFabricId(): ResourceLocation = id

            override fun reload(
                synchronizer: PreparationBarrier,
                manager: ResourceManager,
                prepareProfiler: ProfilerFiller,
                applyProfiler: ProfilerFiller,
                prepareExecutor: Executor,
                applyExecutor: Executor
            ): CompletableFuture<Void> {
                return listener.reload(synchronizer, manager, prepareProfiler, applyProfiler, prepareExecutor, applyExecutor)
            }
        })
    }

    registry(DeltaboxUtil.resourceLocation(modid, VillagerTradeProvider.PATH), VillagerTradeDeserializer(this))

    // register strippable blocks
    STRIPPABLE_BLOCKS.map {
      StrippableBlockRegistry.register(it.first.get(), it.second.get())
    }

    // register flammable blocks
    FLAMMABLE_BLOCKS.map {
      FlammableBlockRegistry.getDefaultInstance().add(it.first.get(), it.second.toInt(), it.third.toInt())
    }

    fun injectTrades() {
      val tradesByProfession: MutableMap<Pair<VillagerProfession, VillagerLevel>, MutableList<VillagerTradeCodec>> = mutableMapOf()
      TRADES.map {
        val pair = Pair(it.profession, it.level)
        val currentList = (tradesByProfession[pair]?: mutableListOf())
        currentList.add(it)
        tradesByProfession[pair] = currentList
      }

      tradesByProfession.forEach { t, u ->
        TradeOfferHelper.registerVillagerOffers(t.first, t.second.toInt(), {factories ->
          u.forEach {
            factories.add({e, r->
              MerchantOffer(
                ItemStack(it.tradeCosts.first().item.get(), it.tradeCosts.first().amount),
                ItemStack(it.tradeSells.first().item.get(), it.tradeSells.first().amount),
                it.maxUses,
                it.xpAmount,
                it.priceMultiplier
              )
            })
          }
        })
      }
    }

    net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents.SERVER_STARTED.register({server -> injectTrades()});
    net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents.END_DATA_PACK_RELOAD.register({server, resourceManager, success -> injectTrades()});
  }

  @Environment(EnvType.CLIENT)
  fun registerClient(){
    BlockRenderLayerMap.INSTANCE.putBlocks(net.minecraft.client.renderer.RenderType.cutout(), *CUTOUT_RENDERS.map { it.get() }.toTypedArray())
  }
  *//*?}*/

  /*? if forge {*/
  public fun register(bus: net.minecraftforge.eventbus.api.IEventBus, forgeBus: net.minecraftforge.eventbus.api.IEventBus) {
    super.registerEventListeners(bus)

    // register pot plants
    bus.addListener { e: net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent ->
      POTTED_BLOCKS.forEach { (plant, pot) ->
        try {
          (Blocks.FLOWER_POT as FlowerPotBlock).addPlant(plant.id, pot)
        } catch (e: Exception) {
          println("Failed to add plant ${plant.get().name} to flower pot ${pot.get().name}")
        }
      }
    }

    // datapack reload listeners
    forgeBus.addListener(net.minecraftforge.eventbus.api.EventPriority.HIGH) { event: net.minecraftforge.event.AddReloadListenerEvent ->
      val registry = BiConsumer<ResourceLocation, PreparableReloadListener> { id, listener -> event.addListener(listener) }
      // deserialize villager trades
      registry.accept(DeltaboxUtil.resourceLocation(modid, VillagerTradeProvider.PATH), VillagerTradeDeserializer(this))
    }

    // load villager trades
    forgeBus.addListener { event: net.minecraftforge.event.village.VillagerTradesEvent ->
      TRADES.forEach { trade ->
        if(event.type == trade.profession){
          event.trades[trade.level.toInt()].add { _, _ -> MerchantOffer(ItemStack(trade.tradeCosts.first().item.get(), trade.tradeCosts.first().amount), ItemStack(trade.tradeSells.first().item.get(), trade.tradeSells.first().amount), trade.maxUses, trade.xpAmount, trade.priceMultiplier) }
        }
      }
    }
  }
  /*?} elif neoforge {*/
  /*fun register(bus: net.neoforged.bus.api.IEventBus, forgeBus: net.neoforged.bus.api.IEventBus) {
    super.registerEventListeners(bus)

    // register pot plants
    bus.addListener { e: net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent ->
      POTTED_BLOCKS.forEach { (plant, pot) ->
        try {
          (Blocks.FLOWER_POT as FlowerPotBlock).addPlant(plant.id, pot)
        } catch (e: Exception) {
          println("Failed to add plant ${plant.get().name} to flower pot ${pot.get().name}")
        }
      }
    }
  }
  *//*?}*/


}