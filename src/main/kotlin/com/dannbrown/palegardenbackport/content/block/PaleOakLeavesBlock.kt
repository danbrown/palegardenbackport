package com.dannbrown.palegardenbackport.content.block

import com.dannbrown.deltaboxlib.content.block.FlammableLeavesBlock
import com.dannbrown.palegardenbackport.init.ModParticles
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.util.ParticleUtils
import net.minecraft.util.RandomSource
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.state.BlockState

class PaleOakLeavesBlock(props:Properties): FlammableLeavesBlock(props, 60, 30) {
  override fun animateTick(blockState: BlockState, level: Level, blockPos: BlockPos, randomSource: RandomSource) {
    super.animateTick(blockState, level, blockPos, randomSource)
    if (randomSource.nextInt(10) == 0) {
      val bellow = blockPos.below()
      val bellowState = level.getBlockState(bellow)
      if (!Block.isFaceFull(bellowState.getCollisionShape(level, bellow), Direction.UP)) {
        ParticleUtils.spawnParticleBelow(level, blockPos, randomSource, ModParticles.PALE_OAK_PARTICLE.get())
      }
    }
  }
}