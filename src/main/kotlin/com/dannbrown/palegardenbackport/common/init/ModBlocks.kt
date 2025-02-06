package com.dannbrown.palegardenbackport.common.init

import com.dannbrown.deltaboxlib.common.content.worldgen.tree.DeltaboxTreeGrower
import com.dannbrown.palegardenbackport.common.ModCommon
import com.dannbrown.deltaboxlib.platform.registrate.generators.block.BlockGenerator
import com.dannbrown.deltaboxlib.platform.registrate.generators.family.BlockFamily
import com.dannbrown.deltaboxlib.platform.registrate.transformers.BlockLootPresets
import com.dannbrown.deltaboxlib.platform.registrate.transformers.BlockstatePresets
import com.dannbrown.deltaboxlib.platform.util.DeltaboxUtil
import com.dannbrown.palegardenbackport.common.content.blocks.PaleMossBlock
import com.dannbrown.palegardenbackport.common.content.blocks.PaleOakLeavesBlock
import net.minecraft.tags.BlockTags
import net.minecraft.tags.ItemTags
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.SoundType
import net.minecraft.world.level.material.MapColor
import net.minecraft.world.level.material.PushReaction

object ModBlocks {
  val BLOCKS = BlockGenerator(ModCommon.REGISTRATE)

  val PALE_WOOD_FAMILY = BLOCKS.createFamily("pale_oak")
    .color(MapColor.SNOW, MapColor.COLOR_GRAY)
    .copyFrom { Blocks.OAK_LOG }
    .toolAndTier(BlockTags.MINEABLE_WITH_AXE, null)
    .denyList(BlockFamily.Type.LEAVES)
    .woodFamily(
      ModWoodTypes.PALE_OAK,
      ModWoodTypes.PALE_OAK_SET,
      DeltaboxTreeGrower.SAMPLE,
      { blockState, _, _ -> blockState.`is`(BlockTags.DIRT) })

  val PALE_OAK_LEAVES = BLOCKS.create<PaleOakLeavesBlock>("pale_oak_leaves")
    .blockFactory { p -> PaleOakLeavesBlock(p) }
    .color(MapColor.COLOR_GREEN)
    .copyFrom { Blocks.OAK_LEAVES }
    .properties { p ->
      p.randomTicks()
        .noOcclusion()
        .isSuffocating { s, b, p -> false }
        .isViewBlocking { s, b, p -> false }
        .isRedstoneConductor { s, b, p -> false }
        .pushReaction(PushReaction.DESTROY)
        .ignitedByLava()
    }
    .blockTags(listOf(BlockTags.LEAVES, *DeltaboxUtil.TAGS.modloaderBlockTag("leaves").toTypedArray(), BlockTags.MINEABLE_WITH_HOE))
    .itemTags(listOf(ItemTags.LEAVES, *DeltaboxUtil.TAGS.modloaderItemTag("leaves").toTypedArray()))
    .loot(BlockLootPresets.leavesLoot { PALE_WOOD_FAMILY.blockFamily.blocks[BlockFamily.Type.SAPLING]!!.get() })
    .blockstate(BlockstatePresets.leavesBlock("pale_oak_leaves"))
    .register()

  val PALE_MOSS_BLOCK = BLOCKS.create<PaleMossBlock>("pale_moss_block")
    .copyFrom { Blocks.MOSS_BLOCK }
    .blockFactory { p -> PaleMossBlock(p) }
    .color(MapColor.SNOW)
    .blockTags(listOf(BlockTags.DIRT, BlockTags.MOSS_REPLACEABLE, BlockTags.SNIFFER_DIGGABLE_BLOCK, BlockTags.SMALL_DRIPLEAF_PLACEABLE, BlockTags.SNIFFER_EGG_HATCH_BOOST))
    .itemTags(listOf(ItemTags.DIRT))
    .toolAndTier(BlockTags.MINEABLE_WITH_HOE, null, false)
    .properties { p -> p.strength(0.1F).sound(SoundType.MOSS).pushReaction(PushReaction.DESTROY) }
    .blockstate(BlockstatePresets.simpleBlock())
    .register()

  fun register() {
    DeltaboxUtil.logInfo("Registering blocks...")
  }
}