package com.dannbrown.deltaboxlib.common.init

import com.dannbrown.deltaboxlib.common.DeltaboxLibCommon
import com.dannbrown.deltaboxlib.common.content.block.FlammablePillarBlock
import com.dannbrown.deltaboxlib.common.content.block.FlammableSandBlock
import com.dannbrown.deltaboxlib.common.content.block.GenericDoublePlantBlock
import com.dannbrown.deltaboxlib.common.content.block.GenericGrassBlock
import com.dannbrown.deltaboxlib.common.content.block.GenericSaplingBlock
import com.dannbrown.deltaboxlib.common.content.block.GenericTallGrassBlock
import com.dannbrown.deltaboxlib.common.content.block.StrippableFlammablePillarBlock
import com.dannbrown.deltaboxlib.common.content.worldgen.tree.DeltaboxTreeGrower
import com.dannbrown.deltaboxlib.platform.registrate.generators.block.BlockGenerator
import com.dannbrown.deltaboxlib.platform.registrate.generators.family.BlockFamily
import com.dannbrown.deltaboxlib.platform.registrate.transformers.BlockLootPresets
import com.dannbrown.deltaboxlib.platform.util.DeltaboxUtil
import com.tterrag.registrate.util.entry.BlockEntry
import net.minecraft.tags.BlockTags
import net.minecraft.world.item.Items
import net.minecraft.world.item.crafting.Ingredient
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.FlowerPotBlock
import net.minecraft.world.level.block.state.properties.BlockSetType
import net.minecraft.world.level.block.state.properties.WoodType
import net.minecraft.world.level.material.MapColor

object DeltaboxBlocks {
  val BLOCKS = BlockGenerator(DeltaboxLibCommon.REGISTRATE)

  val ADAMANTIUM_BLOCK: BlockEntry<Block> =
    BLOCKS.storageBlock<Block>("adamantium", { Items.IRON_INGOT }, { Ingredient.of(Items.FLINT) })
      .copyFrom { Blocks.IRON_BLOCK }
      .loot(BlockLootPresets.dropItselfLoot())
      .toolAndTier(BlockTags.MINEABLE_WITH_PICKAXE, BlockTags.NEEDS_IRON_TOOL)
      .register()

  val FLAMMABLE_BLOCK: BlockEntry<FlammablePillarBlock> = BLOCKS.create<FlammablePillarBlock>("flammable_block")
    .blockFactory { p, c -> FlammablePillarBlock(p, c.flammability!!.first, c.flammability.second) }
    .flammable()
    .register()

  val FLAMMABLE_BLOCK_2: BlockEntry<StrippableFlammablePillarBlock> =
    BLOCKS.create<StrippableFlammablePillarBlock>("flammable_block_2")
      .blockFactory { p, c ->
        StrippableFlammablePillarBlock(
          p,
          c.strippedBlock!!,
          c.flammability!!.first,
          c.flammability.second
        )
      }
      .flammable()
      .strippable { FLAMMABLE_BLOCK.get() }
      .register()

  val FLAMMABLE_SAND: BlockEntry<FlammableSandBlock> = BLOCKS.create<FlammableSandBlock>("flammable_sand")
    .blockFactory { p, c -> FlammableSandBlock(p, 123, c.flammability!!.first, c.flammability.second) }
    .flammable()
    .register()

  val LEMON_SAPLING: BlockEntry<GenericSaplingBlock> =
    BLOCKS.saplingBlock("lemon", DeltaboxTreeGrower.SAMPLE) { blockState, _, _ -> blockState.`is`(BlockTags.DIRT) }
      .register()
  val POTTED_LEMON_SAPLING: BlockEntry<FlowerPotBlock> = BLOCKS.pottedBlock("lemon", LEMON_SAPLING, "_sapling")
    .register()

  val SIMPLE_GRASS: BlockEntry<GenericGrassBlock> = BLOCKS.grassBlock("simple_grass", { Items.WHEAT_SEEDS })
    .register()
  val SIMPLE_FLOWER: BlockEntry<GenericGrassBlock> =
    BLOCKS.flowerBlock("simple_flower", true, true, true, { blockState, _, _ -> blockState.`is`(BlockTags.SAND) })
      .register()

  val POTTED_SIMPLE_GRASS: BlockEntry<FlowerPotBlock> = BLOCKS.pottedBlock("simple_grass", SIMPLE_GRASS)
    .register()
  val POTTED_SIMPLE_FLOWER: BlockEntry<FlowerPotBlock> = BLOCKS.pottedBlock("simple_flower", SIMPLE_FLOWER)
    .register()

  // GRASS
  val TALL_SPARSE_DRY_GRASS: BlockEntry<GenericDoublePlantBlock> = BLOCKS.createDoubleTallGrassBlock(
    "sparse_dry_grass",
    { Items.BEETROOT_SEEDS },
    null,
    { blockState, _, _ -> blockState.`is`(BlockTags.SAND) })
    .color(MapColor.TERRACOTTA_YELLOW)
    .register()
  val SPARSE_DRY_GRASS: BlockEntry<GenericTallGrassBlock> = BLOCKS.createSmallTallGrassBlock(
    "sparse_dry_grass",
    { TALL_SPARSE_DRY_GRASS.get() },
    { Items.BEETROOT_SEEDS },
    false,
    { blockState, _, _ -> blockState.`is`(BlockTags.SAND) })
    .color(MapColor.TERRACOTTA_YELLOW)
    .register()

  // Leaves
  val ACAI_LEAVES = BLOCKS.createPalmLeavesBlock("acai", { LEMON_SAPLING.get() })
    .color(MapColor.COLOR_LIGHT_GREEN)
    .register()

  val CROP_LEAVES = BLOCKS.createCropLeavesBlock("budding_lemon", { LEMON_SAPLING.get() }, { Items.EMERALD })
    .color(MapColor.COLOR_LIGHT_GREEN)
    .register()

  val BUDDING_LEMON_LEAVES =
    BLOCKS.createBuddingLeavesBlock("coconut", { LEMON_SAPLING.get() }, { Blocks.MANGROVE_PROPAGULE })
      .color(MapColor.COLOR_LIGHT_GREEN)
      .register()

  // Crops
  val GARLIC_CROP = BLOCKS.createCropBlock(
    "garlic",
    "garlic_clove",
    "Garlic Crop",
    "Garlic Clove",
    { DeltaboxItems.WARP_CRYSTAL.get() },
    false,
    false
  )
    .color(MapColor.COLOR_LIGHT_GREEN)
    .register()

  val CASSAVINHA_CROP =
    BLOCKS.createCropBlock("cassavinha", "cassavinha", "Cassavinha Crop", "Cassavinha", null, true, true, 1f, 3)
      .color(MapColor.COLOR_LIGHT_GREEN)
      .register()

  val CARIOCA_BEANS_CROP = BLOCKS.createCropBlock(
    "bean",
    "carioca_beans",
    "Carioca Beans Crop",
    "Carioca Beans",
    { DeltaboxItems.BEAN_POD.get() },
    true,
    false
  )
    .color(MapColor.COLOR_LIGHT_GREEN)
    .register()

  val BLACK_BEANS_CROP = BLOCKS.createCropBlock(
    "bean",
    "black_beans",
    "Black Beans Crop",
    "Black Beans",
    { DeltaboxItems.BEAN_POD.get() },
    true,
    false
  )
    .color(MapColor.COLOR_LIGHT_GREEN)
    .register()

//  val CORN_CROP = BLOCKS.createDoubleCropBlock("corn", "kernels", "Corn Crop", "Kernels", {DeltaboxItems.BEAN_POD.get()}, true, false)
//    .color(MapColor.COLOR_LIGHT_GREEN)
//    .register()
//
//  val CASSAVA_CROP = BLOCKS.createDoubleCropBlock("cassava", "cassava_root", "Cassava Crop", "Cassava Root", null, false, true)
//    .color(MapColor.COLOR_LIGHT_GREEN)
//    .register()


  // Common Blocks for testing
  val ROTATED_PILLAR = BLOCKS.createRotatedPillar("pale_oak_log")
    .toolAndTier(BlockTags.MINEABLE_WITH_AXE, null, false)
    .register()
  val BOTTOM_TOP = BLOCKS.createBottomTop<Block>("roseate_sandstone")
    .toolAndTier(BlockTags.MINEABLE_WITH_PICKAXE, null, true)
    .register()
  val WOODEN_STAIRS = BLOCKS.createStairs("pale_oak", "pale_oak_planks",false, true)
    .toolAndTier(BlockTags.MINEABLE_WITH_AXE, null, false)
    .register()
  val BOTTOM_TOP_STAIRS = BLOCKS.createStairs("roseate_sandstone", "roseate_sandstone", true, false)
    .toolAndTier(BlockTags.MINEABLE_WITH_PICKAXE, null, true)
    .register()
  val WOODEN_SLAB = BLOCKS.createSlab("pale_oak", "pale_oak_planks", false, true)
    .toolAndTier(BlockTags.MINEABLE_WITH_AXE, null, false)
    .register()
  val BOTTOM_TOP_SLAB = BLOCKS.createSlab("roseate_sandstone", "roseate_sandstone", true, false)
    .toolAndTier(BlockTags.MINEABLE_WITH_PICKAXE, null, true)
    .register()
  val WOODEN_WALL = BLOCKS.createWall("pale_oak", "pale_oak_planks", false, true)
    .toolAndTier(BlockTags.MINEABLE_WITH_AXE, null, false)
    .register()
  val BOTTOM_TOP_WALL = BLOCKS.createWall("roseate_sandstone", "roseate_sandstone",true, true)
    .toolAndTier(BlockTags.MINEABLE_WITH_PICKAXE, null, true)
    .register()
  val BOTTOM_TOP_WALL2 = BLOCKS.createWall("pale_oak_stem", "pale_oak_log",true, false)
    .toolAndTier(BlockTags.MINEABLE_WITH_AXE, null, false)
    .register()
  val WOODEN_FENCE = BLOCKS.createFence("pale_oak", "pale_oak_planks", true)
    .toolAndTier(BlockTags.MINEABLE_WITH_AXE, null, false)
    .register()
  val WOODEN_FENCE_GATE = BLOCKS.createFenceGate("pale_oak", "pale_oak_planks", WoodType.BIRCH)
    .toolAndTier(BlockTags.MINEABLE_WITH_AXE, null, false)
    .register()
  val WOODEN_PRESSURE_PLATE = BLOCKS.createPressurePlate("pale_oak", "pale_oak_planks", BlockSetType.OAK, true)
    .toolAndTier(BlockTags.MINEABLE_WITH_AXE, null, false)
    .register()
  val WOODEN_BUTTON = BLOCKS.createButton("pale_oak", "pale_oak_planks", BlockSetType.OAK, true)
    .toolAndTier(BlockTags.MINEABLE_WITH_AXE, null, false)
    .register()
  val WOODEN_TRAPDOOR = BLOCKS.createWoodenTrapdoor("pale_oak", BlockSetType.OAK)
    .toolAndTier(BlockTags.MINEABLE_WITH_AXE, null, false)
    .register()
  val WOODEN_DOOR = BLOCKS.createDoor("pale_oak", BlockSetType.OAK, true)
    .toolAndTier(BlockTags.MINEABLE_WITH_AXE, null, false)
    .register()

  // Family blocks test
  val LONG_FAMILY_TEST = BLOCKS.createFamily("pyrite")
    .toolAndTier(BlockTags.MINEABLE_WITH_PICKAXE, null, true)
    .denyList(BlockFamily.Type.PILLAR)
    .longBlockFamily()

  fun register() {
    DeltaboxUtil.logInfo("Registering blocks...")
  }
}