package com.dannbrown.deltaboxlib.platform.registrate.generators.block

import com.dannbrown.deltaboxlib.common.content.block.GenericSaplingBlock
import com.dannbrown.deltaboxlib.common.content.tree.DeltaboxTreeGrower
import com.dannbrown.deltaboxlib.platform.registrate.generators.BlockGenerator
import com.dannbrown.deltaboxlib.platform.registrate.generators.BlockGeneratorBuilder
import com.dannbrown.deltaboxlib.platform.registrate.transformers.BlockstatePresets
import com.dannbrown.deltaboxlib.platform.registrate.transformers.ItemModelPresets
import net.minecraft.core.BlockPos
import net.minecraft.tags.BlockTags
import net.minecraft.tags.ItemTags
import net.minecraft.world.level.BlockGetter
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.SoundType
import net.minecraft.world.level.block.state.BlockState

class SaplingBlockPreset(
  private val _name: String,
  private val treeGrower: DeltaboxTreeGrower,
  private val placeOn: ((blockState: BlockState, blockGetter: BlockGetter, blockPos: BlockPos) -> Boolean)? = null
): IBlockBuilderPreset<GenericSaplingBlock>() {
  override fun create(generator: BlockGenerator): BlockGeneratorBuilder<GenericSaplingBlock> {
    return generator.create<GenericSaplingBlock>(_name + "_sapling")
      .blockFactory { p -> GenericSaplingBlock(treeGrower, p, placeOn) }
      .copyFrom { Blocks.OAK_SAPLING }
      .properties { p ->
        p
          .sound(SoundType.GRASS)
          .strength(0.0f)
          .randomTicks()
          .noCollission()
          .noOcclusion()
      }
      .cutoutRender()
      .blockstate(BlockstatePresets.simpleCrossBlock(_name + "_sapling"))
      .transform { t ->
        t
          .tag(BlockTags.SAPLINGS)
          .item()
          .tag(ItemTags.SAPLINGS)
          .model(ItemModelPresets.simpleLayerItem(_name + "_sapling"))
          .build()
      }
  }
}