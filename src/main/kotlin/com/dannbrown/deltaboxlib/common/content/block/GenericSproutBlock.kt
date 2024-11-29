package com.dannbrown.deltaboxlib.common.content.block

import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.tags.BlockTags
import net.minecraft.tags.FluidTags
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.SwordItem
import net.minecraft.world.item.context.BlockPlaceContext
import net.minecraft.world.level.BlockGetter
import net.minecraft.world.level.LevelAccessor
import net.minecraft.world.level.LevelReader
import net.minecraft.world.level.block.BambooSaplingBlock
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.BonemealableBlock
import net.minecraft.world.level.block.SimpleWaterloggedBlock
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.block.state.StateDefinition
import net.minecraft.world.level.block.state.properties.BlockStateProperties
import net.minecraft.world.level.material.FluidState
import net.minecraft.world.level.material.Fluids
import net.minecraft.world.phys.HitResult

/**
 * A generic sprout block that grows into a tall plant.
 * It can also be waterlogged.
 */
open class GenericSproutBlock(
  props: Properties,
  private val placeOn: ((blockState: BlockState, blockPos: BlockPos) -> Boolean) = { blockState, _ -> blockState.`is`(BlockTags.DIRT) || blockState.`is`(Blocks.SAND) || blockState.`is`(Blocks.RED_SAND) },
  ) : BambooSaplingBlock(props), SimpleWaterloggedBlock, BonemealableBlock {

  companion object {
    val WATERLOGGED = BlockStateProperties.WATERLOGGED
  }
  init {
    registerDefaultState(stateDefinition.any().setValue(WATERLOGGED, false))
  }

  override fun createBlockStateDefinition(builder: StateDefinition.Builder<Block?, BlockState?>) {
    builder.add(WATERLOGGED)
  }

  override fun getStateForPlacement(blockPlaceContext: BlockPlaceContext): BlockState {
    val fluidState = blockPlaceContext.level.getFluidState(blockPlaceContext.clickedPos)
    return if (fluidState.type === Fluids.WATER) {
      super.getStateForPlacement(blockPlaceContext)!!.setValue(WATERLOGGED, true)
    }
    else defaultBlockState()
  }

  override fun getFluidState(blockState: BlockState): FluidState {
    return if (blockState.getValue(WATERLOGGED)) Fluids.WATER.getSource(false)
    else super.getFluidState(blockState)
  }

  override fun canSurvive(blockState: BlockState, levelReader: LevelReader, blockPos: BlockPos): Boolean {
    val state = levelReader.getBlockState(blockPos)
    val blockState2 = levelReader.getBlockState(blockPos.below())
    if (levelReader.getBlockState(blockPos.above(1)) === Blocks.AIR.defaultBlockState() && (blockState2.`is`(BlockTags.DIRT) || blockState2.`is`(Blocks.SAND) || blockState2.`is`(Blocks.RED_SAND)) && state === Blocks.WATER.defaultBlockState()) {
      return true
    }
    if (placeOn.invoke(blockState, blockPos)) {
      val blockPos2 = blockPos.below()
      for (direction in Direction.Plane.HORIZONTAL) {
        val blockState3 = levelReader.getBlockState(blockPos2.relative(direction))
        val fluidState = levelReader.getFluidState(blockPos2.relative(direction))
        if (fluidState.`is`(FluidTags.WATER) || blockState3.`is`(Blocks.FROSTED_ICE)) {
          return true
        }
      }
    }
    return false
  }

  override fun updateShape(blockState: BlockState, direction: Direction, blockState2: BlockState, levelAccessor: LevelAccessor, blockPos: BlockPos, blockPos2: BlockPos): BlockState {
    if (blockState.getValue(WATERLOGGED)) {
      levelAccessor.scheduleTick(blockPos, Fluids.WATER, Fluids.WATER.getTickDelay(levelAccessor))
    }
    return super.updateShape(blockState, direction, blockState2, levelAccessor, blockPos, blockPos2)
  }

  override fun getDestroyProgress(blockState: BlockState, player: Player, blockGetter: BlockGetter, blockPos: BlockPos): Float {
    return if (player.mainHandItem.item is SwordItem) 1.0f
    else super.getDestroyProgress(blockState, player, blockGetter, blockPos)
  }
}

