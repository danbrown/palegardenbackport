package com.dannbrown.deltaboxlib.common.init

import com.dannbrown.deltaboxlib.common.DeltaboxLib
import com.dannbrown.deltaboxlib.common.content.block.FlammablePillarBlock
import com.dannbrown.deltaboxlib.common.content.block.FlammableSandBlock
import com.dannbrown.deltaboxlib.common.content.block.GenericDoublePlantBlock
import com.dannbrown.deltaboxlib.common.content.block.GenericGrassBlock
import com.dannbrown.deltaboxlib.common.content.block.GenericSaplingBlock
import com.dannbrown.deltaboxlib.common.content.block.GenericTallGrassBlock
import com.dannbrown.deltaboxlib.common.content.block.StrippableFlammablePillarBlock
import com.dannbrown.deltaboxlib.common.content.tree.DeltaboxTreeGrower
import com.dannbrown.deltaboxlib.platform.registrate.generators.block.BlockGenerator
import com.dannbrown.deltaboxlib.platform.registrate.transformers.BlockLootPresets
import com.dannbrown.deltaboxlib.platform.util.DeltaboxUtil
import com.tterrag.registrate.util.entry.BlockEntry
import net.minecraft.tags.BlockTags
import net.minecraft.world.item.Items
import net.minecraft.world.item.crafting.Ingredient
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.FlowerPotBlock
import net.minecraft.world.level.material.MapColor

object DeltaboxBlocks {
  val BLOCKS = BlockGenerator(DeltaboxLib.REGISTRATE)

  val ADAMANTIUM_BLOCK: BlockEntry<Block> = BLOCKS.storageBlock<Block>("adamantium", { Items.IRON_INGOT }, { Ingredient.of(Items.FLINT) })
    .copyFrom { Blocks.IRON_BLOCK }
    .loot(BlockLootPresets.dropItselfLoot())
    .toolAndTier(BlockTags.MINEABLE_WITH_PICKAXE, BlockTags.NEEDS_IRON_TOOL)
    .register()

  val FLAMMABLE_BLOCK: BlockEntry<FlammablePillarBlock> = BLOCKS.create<FlammablePillarBlock>("flammable_block")
    .blockFactory { p, c -> FlammablePillarBlock(p, c.flammability!!.first, c.flammability.second) }
    .flammable()
    .register()

  val FLAMMABLE_BLOCK_2: BlockEntry<StrippableFlammablePillarBlock> = BLOCKS.create<StrippableFlammablePillarBlock>("flammable_block_2")
    .blockFactory { p, c -> StrippableFlammablePillarBlock(p, c.strippedBlock!!, c.flammability!!.first, c.flammability.second) }
    .flammable()
    .strippable { FLAMMABLE_BLOCK.get() }
    .register()

  val FLAMMABLE_SAND: BlockEntry<FlammableSandBlock> = BLOCKS.create<FlammableSandBlock>("flammable_sand")
    .blockFactory { p, c -> FlammableSandBlock(p, 123, c.flammability!!.first, c.flammability.second) }
    .flammable()
    .register()

  val LEMON_SAPLING: BlockEntry<GenericSaplingBlock> = BLOCKS.saplingBlock("lemon", DeltaboxTreeGrower.SAMPLE) { blockState, _, _ -> blockState.`is`(BlockTags.SAND) }
    .register()
  val POTTED_LEMON_SAPLING: BlockEntry<FlowerPotBlock> = BLOCKS.pottedBlock("lemon", LEMON_SAPLING, "_sapling")
    .register()

  val SIMPLE_GRASS: BlockEntry<GenericGrassBlock> = BLOCKS.grassBlock("simple_grass", { Items.WHEAT_SEEDS })
    .register()
  val SIMPLE_FLOWER: BlockEntry<GenericGrassBlock> = BLOCKS.flowerBlock("simple_flower", true, true, true, { blockState, _, _ -> blockState.`is`(BlockTags.SAND) })
    .register()

  val POTTED_SIMPLE_GRASS: BlockEntry<FlowerPotBlock> = BLOCKS.pottedBlock("simple_grass", SIMPLE_GRASS)
    .register()
  val POTTED_SIMPLE_FLOWER: BlockEntry<FlowerPotBlock> = BLOCKS.pottedBlock("simple_flower", SIMPLE_FLOWER)
    .register()

  // GRASS
  val TALL_SPARSE_DRY_GRASS: BlockEntry<GenericDoublePlantBlock> = BLOCKS.createDoubleTallGrassBlock("sparse_dry_grass", { Items.BEETROOT_SEEDS }, null, { blockState, _, _ -> blockState.`is`(BlockTags.SAND) })
    .color(MapColor.TERRACOTTA_YELLOW)
    .register()
  val SPARSE_DRY_GRASS: BlockEntry<GenericTallGrassBlock> = BLOCKS.createSmallTallGrassBlock("sparse_dry_grass", { TALL_SPARSE_DRY_GRASS.get() }, { Items.BEETROOT_SEEDS }, false, { blockState, _, _ -> blockState.`is`(BlockTags.SAND) })
    .color(MapColor.TERRACOTTA_YELLOW)
    .register()

  fun register() {
    DeltaboxUtil.logInfo("Registering blocks...")
  }
}