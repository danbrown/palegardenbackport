package com.dannbrown.deltaboxlib.common.content.block

import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.world.level.BlockGetter
import net.minecraft.world.level.block.RotatedPillarBlock
import net.minecraft.world.level.block.SimpleWaterloggedBlock
import net.minecraft.world.level.block.state.BlockState

open class FlammablePillarBlock(props: Properties, private val flammability: Int = 20, private val fireSpread: Int = 5): RotatedPillarBlock(props), SimpleWaterloggedBlock {
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
}