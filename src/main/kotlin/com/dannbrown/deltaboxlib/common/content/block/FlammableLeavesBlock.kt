package com.dannbrown.deltaboxlib.common.content.block

import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.world.level.BlockGetter
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.LeavesBlock
import net.minecraft.world.level.block.SimpleWaterloggedBlock
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.block.state.StateDefinition

open class FlammableLeavesBlock(props: Properties, private val flammability: Int = 20, private val fireSpread: Int = 5): LeavesBlock(props), SimpleWaterloggedBlock {
  /*? if forge || neoforge {*/
  override fun isFlammable(state: BlockState, level: BlockGetter, pos: BlockPos, direction: Direction): Boolean {
    return true
  }

  override fun getFlammability(state: BlockState, level: BlockGetter, pos: BlockPos, direction: Direction): Int {
    return flammability
  }

  override fun getFireSpreadSpeed(state: BlockState, level: BlockGetter, pos: BlockPos, direction: Direction): Int {
    return fireSpread
  }
  /*?}*/

  override fun createBlockStateDefinition(stateBuilder: StateDefinition.Builder<Block?, BlockState?>) {
    stateBuilder.add(DISTANCE, PERSISTENT, WATERLOGGED)
  }

  init{
    registerDefaultState(
      stateDefinition.any().setValue(DISTANCE, 7).setValue(PERSISTENT, false).setValue(WATERLOGGED, false)
    )
  }
}