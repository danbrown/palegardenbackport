package com.dannbrown.deltaboxlib.common.init

import com.dannbrown.deltaboxlib.common.DeltaboxLib
import com.dannbrown.deltaboxlib.platform.registrate.transformers.BlockLootPresets
import com.dannbrown.deltaboxlib.platform.registrate.transformers.BlockstatePresets
import com.dannbrown.deltaboxlib.platform.registrate.transformers.RecipePresets
import com.tterrag.registrate.util.DataIngredient
import com.tterrag.registrate.util.entry.BlockEntry
import net.minecraft.world.item.Items
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.Blocks

object DeltaboxBlocks {
  val ADAMANTIUM_BLOCK: BlockEntry<Block> = DeltaboxLib.REGISTRATE.block<Block>("adamantium_block", ::Block)
    .initialProperties { Blocks.IRON_BLOCK }
    .loot(BlockLootPresets.dropItselfLoot())
    .blockstate(BlockstatePresets.simpleBlock())
    .recipe { c, p ->
      RecipePresets.simpleStonecuttingRecipe(c, p, { DataIngredient.items(Items.IRON_BLOCK) })
    }
    .item()
    .build()
    .register()

  fun register() {
    println("HELLO WORLD IM A BLOCK REGISTRATOR")
  }
}