package com.dannbrown.deltaboxlib.common.content.block

import net.minecraft.core.BlockPos
import net.minecraft.world.level.BlockGetter
import net.minecraft.world.level.LevelReader
import net.minecraft.world.level.block.DoublePlantBlock
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf

/**
 * A generic double plant block, like double tall grass.
 */
open class GenericDoublePlantBlock(
  props: Properties,
  private val placeOn: ((blockState: BlockState, blockGetter: BlockGetter, blockPos: BlockPos) -> Boolean)? = null
): DoublePlantBlock(props) {

  override fun mayPlaceOn(blockState: BlockState, blockGetter: BlockGetter, blockPos: BlockPos): Boolean {
    if (placeOn !== null) { return placeOn.invoke(blockState, blockGetter, blockPos) }
    return super.mayPlaceOn(blockState, blockGetter, blockPos)
  }

  override fun canSurvive(blockState: BlockState, levelReader: LevelReader, blockPos: BlockPos): Boolean {
    val below = blockPos.below()
    if (blockState.getValue(HALF) != DoubleBlockHalf.UPPER) {
      return mayPlaceOn(levelReader.getBlockState(below), levelReader, below)
    }
    else {
      val belowState: BlockState = levelReader.getBlockState(blockPos.below())
      return if (blockState.block !== this) {
        return mayPlaceOn(levelReader.getBlockState(below), levelReader, below)
      }
      else {
        belowState.`is`(this) && belowState.getValue(HALF) == DoubleBlockHalf.LOWER
      }
    }
  }
}