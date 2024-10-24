package com.dannbrown.palegardenbackport.content.block

import net.minecraft.core.BlockPos
import net.minecraft.tags.BlockTags
import net.minecraft.world.level.BlockGetter
import net.minecraft.world.level.LevelReader
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.WeepingVinesBlock
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.phys.shapes.CollisionContext
import net.minecraft.world.phys.shapes.VoxelShape
import java.util.function.Supplier

class PaleVineBlock(private val vinePlantBlock: Supplier<Block>, props: Properties): WeepingVinesBlock(props) {
  companion object{
    val SHAPE: VoxelShape = box(1.0, 4.0, 1.0, 15.0, 16.0, 15.0);
  }

  override fun getBodyBlock(): Block {
    return vinePlantBlock.get();
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

  override fun getShape(pState: BlockState, pLevel: BlockGetter, pPos: BlockPos, pContext: CollisionContext): VoxelShape {
    return SHAPE
  }
}