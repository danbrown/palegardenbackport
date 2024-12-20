package com.dannbrown.deltaboxlib.common.content.block

import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.server.level.ServerLevel
import net.minecraft.util.RandomSource
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.BlockGetter
import net.minecraft.world.level.Level
import net.minecraft.world.level.LevelReader
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.BonemealableBlock
import net.minecraft.world.level.block.FaceAttachedHorizontalDirectionalBlock
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.block.state.properties.AttachFace
import net.minecraft.world.phys.HitResult
import java.util.function.Supplier

open class BuddingLeavesBlock(props: Properties, private val fruitBlock: Supplier<FaceAttachedHorizontalDirectionalBlock>, private val flammability: Int = 20, private val fireSpread: Int = 5): FlammableLeavesBlock(props, flammability, fireSpread), BonemealableBlock {
  override fun randomTick(state: BlockState, level: ServerLevel, pos: BlockPos, random: RandomSource) {
    val blockBelow = level.getBlockState(pos.below())
    // check if block below is air
    if (random.nextInt(100) % 20 == 0) { // 5% chance // TODO: make this configurable
      if (blockBelow.isAir) {
        // place fruit block below
        level.setBlock(pos.below(), fruitBlock.get().defaultBlockState().setValue(FaceAttachedHorizontalDirectionalBlock.FACE, AttachFace.CEILING), Block.UPDATE_CLIENTS)
      }
    } else{
      super.randomTick(state, level, pos, random)
    }
  }

  override fun isRandomlyTicking(pState: BlockState): Boolean {
    return true
  }

  /*? if >1.21 {*/
  /*override fun getCloneItemStack(levelReader: LevelReader, pos: BlockPos, state: BlockState): ItemStack {
    return fruitBlock.get().getCloneItemStack(levelReader, pos, state)
  }

  override fun isValidBonemealTarget(p0: LevelReader, p1: BlockPos, p2: BlockState): Boolean {
    return p0.getBlockState(p1.below()).isAir
  }
  *//*?} else {*/
  override fun getCloneItemStack(level: BlockGetter, pos: BlockPos, state: BlockState): ItemStack {
    return fruitBlock.get().getCloneItemStack(level, pos, state)
  }

  override fun isValidBonemealTarget(p0: LevelReader, p1: BlockPos, p2: BlockState, p3: Boolean): Boolean {
    return p0.getBlockState(p1.below()).isAir
  }
  /*?}*/

  override fun isBonemealSuccess(p0: Level, p1: RandomSource, p2: BlockPos, p3: BlockState): Boolean {
    return p0.getBlockState(p2.below()).isAir
  }

  override fun performBonemeal(p0: ServerLevel, p1: RandomSource, p2: BlockPos, p3: BlockState) {
    if (p0.getBlockState(p2.below()).isAir) {
      p0.setBlock(p2.below(), fruitBlock.get().defaultBlockState().setValue(FaceAttachedHorizontalDirectionalBlock.FACE, AttachFace.CEILING), Block.UPDATE_CLIENTS)
    }
  }
}