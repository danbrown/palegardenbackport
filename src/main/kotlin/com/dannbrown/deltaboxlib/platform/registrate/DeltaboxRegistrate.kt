package com.dannbrown.deltaboxlib.platform.registrate

import com.dannbrown.deltaboxlib.platform.util.DeltaboxUtil
import com.tterrag.registrate.AbstractRegistrate
import com.tterrag.registrate.util.entry.BlockEntry
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.FlowerPotBlock
import java.util.function.Supplier

/*? if fabric {*/
/*import net.fabricmc.fabric.api.registry.FlammableBlockRegistry
import net.fabricmc.fabric.api.registry.StrippableBlockRegistry
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap
*//*?}*/

class DeltaboxRegistrate(modId: String): AbstractRegistrate<DeltaboxRegistrate>(modId) {
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

  // FABRIC SPECIFIC BLOCKS FEATURES REGISTRATION
  /*? if fabric {*/
  /*override fun register() {
    super.register()

    // register strippable blocks
    STRIPPABLE_BLOCKS.map {
      StrippableBlockRegistry.register(it.first.get(), it.second.get())
    }

    // register flammable blocks
    FLAMMABLE_BLOCKS.map {
      FlammableBlockRegistry.getDefaultInstance().add(it.first.get(), it.second.toInt(), it.third.toInt())
    }
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