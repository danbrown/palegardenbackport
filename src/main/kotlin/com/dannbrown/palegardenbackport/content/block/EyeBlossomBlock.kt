package com.dannbrown.palegardenbackport.content.block

import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerLevel
import net.minecraft.util.RandomSource
import net.minecraft.world.effect.MobEffect
import net.minecraft.world.level.block.ChangeOverTimeBlock
import net.minecraft.world.level.block.FlowerBlock
import net.minecraft.world.level.block.state.BlockState
import java.util.*
import java.util.function.Supplier

class EyeBlossomBlock(private val other: Supplier<EyeBlossomBlock>, private val currentAge: EyeBlossomState, mobEffect: Supplier<MobEffect>, duration: Int, props: Properties): FlowerBlock(mobEffect, duration, props), ChangeOverTimeBlock<EyeBlossomBlock.EyeBlossomState> {
 enum class EyeBlossomState {
   CLOSED,
   OPEN,
 }

  override fun randomTick(pState: BlockState, pLevel: ServerLevel, pPos: BlockPos, pRandom: RandomSource) {
    this.onRandomTick(pState, pLevel, pPos, pRandom)
  }

  override fun isRandomlyTicking(pState: BlockState): Boolean {
    return true
  }

  override fun getNext(p0: BlockState): Optional<BlockState> {
    return Optional.of(other.get().defaultBlockState())
  }

  override fun getChanceModifier(): Float {
    return 1f
  }

  override fun getAge(): EyeBlossomState {
    return currentAge
  }

  override fun onRandomTick(pState: BlockState, pLevel: ServerLevel, pPos: BlockPos, pRandom: RandomSource) {
    this.applyChangeOverTime(pState, pLevel, pPos, pRandom)
  }

  fun getState(): EyeBlossomState {
    return currentAge
  }

  override fun applyChangeOverTime(pState: BlockState, pLevel: ServerLevel, pPos: BlockPos, pRandom: RandomSource) {
    var otherBlocks = 0
    var sameBlocks = 0

    val nearbyBlocks = BlockPos.withinManhattan(pPos, 4, 4, 4).iterator()

    while (nearbyBlocks.hasNext()) {
      val blockpos = nearbyBlocks.next() as BlockPos
      val distance = blockpos.distManhattan(pPos)

      if (distance > 4) {
        break
      }

      if (blockpos != pPos) {
        val blockState = pLevel.getBlockState(blockpos)
        val block = blockState.block
        if (block is EyeBlossomBlock) {
          val nearbyAge = block.getState()
          when {
            nearbyAge == currentAge -> sameBlocks++
            nearbyAge != currentAge -> otherBlocks++
          }
        }
      }
    }

    val ratio = sameBlocks.toFloat() / (sameBlocks + otherBlocks).toFloat()
    val baseChance = 0.9f  // Increased base chance for faster transitions
    val chance = baseChance * ratio

    if (currentAge == EyeBlossomState.CLOSED && pLevel.isNight) {
      if (pRandom.nextFloat() < chance) {
        pLevel.setBlockAndUpdate(pPos, other.get().defaultBlockState())
      }
    } else if (currentAge == EyeBlossomState.OPEN && !pLevel.isNight) {
      if (pRandom.nextFloat() < chance) {
        pLevel.setBlockAndUpdate(pPos, other.get().defaultBlockState())
      }
    }
  }

}