package com.dannbrown.deltaboxlib.platform.registrate

import com.tterrag.registrate.AbstractRegistrate
import com.tterrag.registrate.util.entry.BlockEntry
import net.minecraft.world.level.block.Block
import java.util.function.Supplier

/*? if fabric {*/
/*import net.fabricmc.fabric.api.registry.FlammableBlockRegistry
import net.fabricmc.fabric.api.registry.StrippableBlockRegistry
*//*?}*/

class DeltaboxRegistrate(modId: String): AbstractRegistrate<DeltaboxRegistrate>(modId) {
  private val FLAMMABLE_BLOCKS: MutableList<Triple<BlockEntry<out Block>, Int, Int>> = mutableListOf()
  private val STRIPPABLE_BLOCKS: MutableList<Pair<BlockEntry<out Block>, Supplier<out Block>>> = mutableListOf()

  fun addFlammableBlock(block: BlockEntry<out Block>, burnChance: Int, spreadChance: Int) {
    FLAMMABLE_BLOCKS.add(Triple(block, burnChance, spreadChance))
  }

  fun getFlammableBlocks(): List<Triple<BlockEntry<out Block>, Number, Number>> {
    return FLAMMABLE_BLOCKS
  }

  fun addStrippableBlock(block: BlockEntry<out Block>, strippedBlock: Supplier<out Block>) {
    STRIPPABLE_BLOCKS.add(Pair(block, strippedBlock))
  }

  fun getStrippableBlocks(): List<Pair<BlockEntry<out Block>, Supplier<out Block>>> {
    return STRIPPABLE_BLOCKS
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
  *//*?}*/



  /*? if forge {*/
  public fun register(bus: net.minecraftforge.eventbus.api.IEventBus, forgeBus: net.minecraftforge.eventbus.api.IEventBus): DeltaboxRegistrate {
    return super.registerEventListeners(bus)
  }
  /*?} elif neoforge {*/
  /*fun register(bus: net.neoforged.bus.api.IEventBus, forgeBus: net.neoforged.bus.api.IEventBus): DeltaboxRegistrate {
    return super.registerEventListeners(bus)
  }
  *//*?}*/
}