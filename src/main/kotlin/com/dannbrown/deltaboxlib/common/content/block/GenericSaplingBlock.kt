package com.dannbrown.deltaboxlib.common.content.block

import com.dannbrown.deltaboxlib.common.content.tree.DeltaboxTreeGrower
import net.minecraft.core.BlockPos
import net.minecraft.world.level.BlockGetter
import net.minecraft.world.level.LevelReader
import net.minecraft.world.level.block.SaplingBlock
import net.minecraft.world.level.block.state.BlockState

class GenericSaplingBlock(
  treeGrower: DeltaboxTreeGrower,
  props: Properties,
  private val placeOn: ((blockState: BlockState, blockGetter: BlockGetter, blockPos: BlockPos) -> Boolean)? = null
) : SaplingBlock(treeGrower.getTreeGrower(), props) {
  /*? if forge {*/
  override fun getPlant(world: BlockGetter, pos: BlockPos): BlockState {
    val state = world.getBlockState(pos)
    return if (state.block !== this) defaultBlockState() else state
  }
  /*?}*/

  override fun mayPlaceOn(blockState: BlockState, blockGetter: BlockGetter, blockPos: BlockPos): Boolean {
    if (placeOn !== null) { return placeOn.invoke(blockState, blockGetter, blockPos) }
    return super.mayPlaceOn(blockState, blockGetter, blockPos)
  }

  override fun canSurvive(blockState: BlockState, levelReader: LevelReader, blockPos: BlockPos): Boolean {
    val below = blockPos.below()
    return mayPlaceOn(levelReader.getBlockState(below), levelReader, below)
  }
}