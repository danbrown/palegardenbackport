package com.dannbrown.deltaboxlib.common.content.block

import com.mojang.serialization.MapCodec
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.world.level.BlockGetter
import net.minecraft.world.level.block.state.BlockState

/*? if >=1.21 {*/
/*import net.minecraft.world.level.block.ColoredFallingBlock
import net.minecraft.util.ColorRGBA

open class FlammableSandBlock(props: Properties, tone: Int, private val flammability: Int = 20, private val fireSpread: Int = 5): ColoredFallingBlock(ColorRGBA(tone),  props) {
  /^? if forge || neoforge {^/
  /^override fun isFlammable(state: BlockState, level: BlockGetter, pos: BlockPos, direction: Direction): Boolean {
    return true
  }

  override fun getFlammability(state: BlockState, level: BlockGetter, pos: BlockPos, direction: Direction): Int {
    return flammability
  }

  override fun getFireSpreadSpeed(state: BlockState, level: BlockGetter, pos: BlockPos, direction: Direction): Int {
    return fireSpread
  }
  ^//^?}^/
}

*//*?} else {*/
import net.minecraft.world.level.block.SandBlock

open class FlammableSandBlock(props: Properties, tone: Int,  private val flammability: Int = 20, private val fireSpread: Int = 5): SandBlock(tone, props) {
  /*? if forge || neoforge {*/
  override fun isFlammable(state: BlockState?, level: BlockGetter?, pos: BlockPos?, direction: Direction?): Boolean {
    return true
  }

  override fun getFlammability(state: BlockState?, level: BlockGetter?, pos: BlockPos?, direction: Direction?): Int {
    return flammability
  }

  override fun getFireSpreadSpeed(state: BlockState?, level: BlockGetter?, pos: BlockPos?, direction: Direction?): Int {
    return fireSpread
  }
  /*?}*/
}
/*?}*/


