package com.dannbrown.palegardenbackport.content.block

import net.minecraft.core.BlockPos
import net.minecraft.tags.BlockTags
import net.minecraft.world.level.LevelReader
import net.minecraft.world.level.block.GrowingPlantHeadBlock
import net.minecraft.world.level.block.WeepingVinesPlantBlock
import net.minecraft.world.level.block.state.BlockState
import java.util.function.Supplier

class PaleVinePlantBlock(private val vineBlock: Supplier<out GrowingPlantHeadBlock>, props: Properties): WeepingVinesPlantBlock(props) {
  override fun getHeadBlock(): GrowingPlantHeadBlock {
    return vineBlock.get();
  }
  override fun canSurvive(pState: BlockState, pLevel: LevelReader, pPos: BlockPos): Boolean {
    val blockPos = pPos.relative(growthDirection.opposite)
    val blockState = pLevel.getBlockState(blockPos)
    return if (!this.canAttachTo(blockState)) {
      false
    }
    else {
      blockState.`is`(this.headBlock) || blockState.`is`(this.bodyBlock) || blockState.isFaceSturdy(pLevel, blockPos, this.growthDirection) || blockState.`is`(BlockTags.LEAVES)
    }
  }
}