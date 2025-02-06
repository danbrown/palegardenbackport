package com.dannbrown.palegardenbackport.common.content.blocks

import com.dannbrown.deltaboxlib.common.content.block.FlammableLeavesBlock
import com.dannbrown.palegardenbackport.common.init.ModParticles
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
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
        val d = blockPos.x + randomSource.nextDouble()
        val e = blockPos.y - 0.05
        val f = blockPos.z + randomSource.nextDouble()
        level.addParticle(ModParticles.PALE_OAK_LEAVES.get(), d, e, f, 0.0, 0.0, 0.0);
      }
    }
  }
}