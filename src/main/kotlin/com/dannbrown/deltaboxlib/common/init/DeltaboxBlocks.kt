package com.dannbrown.deltaboxlib.common.init

import com.dannbrown.deltaboxlib.common.DeltaboxLib
import com.tterrag.registrate.util.entry.BlockEntry
import net.minecraft.world.item.CreativeModeTabs
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.Blocks

object DeltaboxBlocks {
  val ANY_BLOCK: BlockEntry<Block> = DeltaboxLib.REGISTRATE.block<Block>("adamantium_block", ::Block)
    .initialProperties { Blocks.IRON_BLOCK }
//      .loot(BlockLootPresets.dropItselfLoot())
//      .blockstate(BlockstatePresets.simpleBlock())
    .item()
    .transform { t ->
      t.tab(CreativeModeTabs.BUILDING_BLOCKS)
    }
    .build()
    .register()

  fun register() {
    println("HELLO WORLD IM A BLOCK REGISTRATOR")
  }
}