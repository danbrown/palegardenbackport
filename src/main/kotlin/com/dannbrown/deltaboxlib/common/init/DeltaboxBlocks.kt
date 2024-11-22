package com.dannbrown.deltaboxlib.common.init

import com.dannbrown.deltaboxlib.common.DeltaboxLib
import com.dannbrown.deltaboxlib.common.content.block.FlammableBlock
import com.dannbrown.deltaboxlib.common.content.block.FlammableSandBlock
import com.dannbrown.deltaboxlib.platform.registrate.generators.BlockGenerator
import com.dannbrown.deltaboxlib.platform.registrate.transformers.BlockLootPresets
import com.dannbrown.deltaboxlib.platform.registrate.transformers.RecipePresets
import com.dannbrown.deltaboxlib.platform.util.DeltaboxUtil
import com.tterrag.registrate.util.entry.BlockEntry
import net.minecraft.tags.BlockTags
import net.minecraft.world.item.Items
import net.minecraft.world.item.crafting.Ingredient
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.Blocks

object DeltaboxBlocks {
  val BLOCKS = BlockGenerator(DeltaboxLib.REGISTRATE)

  val ADAMANTIUM_BLOCK: BlockEntry<Block> = BLOCKS.storageBlock<Block>("adamantium", { Items.IRON_INGOT }, { Ingredient.of(Items.FLINT) })
    .copyFrom { Blocks.IRON_BLOCK }
    .loot(BlockLootPresets.dropItselfLoot())
    .toolAndTier(BlockTags.MINEABLE_WITH_PICKAXE, BlockTags.NEEDS_IRON_TOOL)
    .register()

  val FLAMMABLE_BLOCK: BlockEntry<FlammableBlock> = BLOCKS.create<FlammableBlock>("flammable_block")
    .blockFactory { p -> FlammableBlock(p) }
    .flammable(20, 5)
    .register()

  val FLAMMABLE_SAND: BlockEntry<FlammableSandBlock> = BLOCKS.create<FlammableSandBlock>("flammable_sand")
    .blockFactory { p -> FlammableSandBlock(p, 123) }
    .flammable(20, 5)
    .strippable { FLAMMABLE_BLOCK.get() }
    .register()

  fun register() {
    DeltaboxUtil.logInfo("Registering blocks...")
  }
}