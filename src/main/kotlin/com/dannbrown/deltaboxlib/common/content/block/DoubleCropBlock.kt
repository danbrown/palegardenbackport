package com.dannbrown.deltaboxlib.common.content.block

import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.server.level.ServerLevel
import net.minecraft.util.Mth
import net.minecraft.util.RandomSource
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.context.BlockPlaceContext
import net.minecraft.world.level.ItemLike
import net.minecraft.world.level.Level
import net.minecraft.world.level.LevelAccessor
import net.minecraft.world.level.LevelReader
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.block.state.StateDefinition
import net.minecraft.world.level.block.state.properties.BlockStateProperties
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf
import net.minecraft.world.level.block.state.properties.Property
import net.minecraft.world.level.material.Fluids
import java.util.function.Supplier

class DoubleCropBlock(
  props: Properties,
  seedItem: Supplier<ItemLike>
): GenericCropBlock(props, seedItem) {
//  init {
//    registerDefaultState(defaultBlockState().setValue(HALF, DoubleBlockHalf.LOWER))
//  }
//
//  override fun updateShape(blockState: BlockState, direction: Direction, blockState2: BlockState, levelAccessor: LevelAccessor, blockPos: BlockPos, blockPos2: BlockPos): BlockState {
//    val doubleBlockHalf = blockState.getValue(HALF)
//    return if (direction.axis === Direction.Axis.Y && doubleBlockHalf == DoubleBlockHalf.LOWER == (direction == Direction.UP) && (!blockState2.`is`(this) || blockState2.getValue(HALF) == doubleBlockHalf)) {
//      Blocks.AIR.defaultBlockState()
//    }
//    else {
//      if (doubleBlockHalf == DoubleBlockHalf.LOWER && direction == Direction.DOWN && !blockState.canSurvive(levelAccessor, blockPos)) Blocks.AIR.defaultBlockState() else super.updateShape(blockState, direction, blockState2, levelAccessor, blockPos, blockPos2)
//    }
//  }
//
//  override fun getStateForPlacement(blockPlaceContext: BlockPlaceContext): BlockState? {
//    val blockPos = blockPlaceContext.clickedPos
//    val level = blockPlaceContext.level
//    return if (blockPos.y < level.maxBuildHeight - 1 && level.getBlockState(blockPos.above()).canBeReplaced(blockPlaceContext)) super.getStateForPlacement(blockPlaceContext)
//    else null
//  }
//
//  override fun setPlacedBy(level: Level, blockPos: BlockPos, blockState: BlockState, livingEntity: LivingEntity?, itemStack: ItemStack) {
//    val blockPos2 = blockPos.above()
//    level.setBlock(blockPos2, copyWaterloggedFrom(level, blockPos2, defaultBlockState().setValue(HALF, DoubleBlockHalf.UPPER)), 3)
//  }
//
//  override fun canSurvive(state: BlockState, worldIn: LevelReader, pos: BlockPos): Boolean {
//    return (worldIn.getRawBrightness(pos, 0) >= 8 || worldIn.canSeeSky(pos)) && canSurviveDouble(state, worldIn, pos)
//  }
//
//  private fun canSurviveDouble(pState: BlockState, pLevel: LevelReader, pPos: BlockPos): Boolean {
//    if (pState.getValue(HALF) != DoubleBlockHalf.UPPER) {
//      return super.canSurvive(pState, pLevel, pPos)
//    }
//    else {
//      val blockState = pLevel.getBlockState(pPos.below())
//      return if (pState.block !== this) {
//        super.canSurvive(pState, pLevel, pPos)
//      }
//      else {
//        blockState.`is`(this) && blockState.getValue(HALF) == DoubleBlockHalf.LOWER
//      }
//    }
//  }
//
//  private fun resetAge(serverLevel: ServerLevel, lowerPos: BlockPos, upperPos: BlockPos, blockState: BlockState, upperState: BlockState) {
//    serverLevel.setBlock(lowerPos, blockState.setValue(ageProperty, 0), 3)
//    serverLevel.setBlock(upperPos, upperState.setValue(ageProperty, 0), 3)
//  }
//
//  override fun playerWillDestroy(level: Level, blockPos: BlockPos, blockState: BlockState, player: Player): BlockState {
//    if (!level.isClientSide) {
//      if (player.isCreative) {
//        preventCreativeDropFromBottomPart(level, blockPos, blockState, player)
//      }
//      else {
//        dropResources(blockState, level, blockPos, null, player, player.mainHandItem)
//      }
//    }
//    return super.playerWillDestroy(level, blockPos, blockState, player)
//  }
//
//  override fun playerDestroy(level: Level, player: Player, blockPos: BlockPos, blockState: BlockState, blockEntity: BlockEntity?, itemStack: ItemStack) {
//    super.playerDestroy(level, player, blockPos, Blocks.AIR.defaultBlockState(), blockEntity, itemStack)
//  }
//
//  private fun preventCreativeDropFromBottomPart(level: Level, blockPos: BlockPos, blockState: BlockState, player: Player?) {
//    val doubleBlockHalf = blockState.getValue(HALF)
//    if (doubleBlockHalf == DoubleBlockHalf.UPPER) {
//      val blockPos2 = blockPos.below()
//      val blockState2 = level.getBlockState(blockPos2)
//      if (blockState2.`is`(blockState.block) && blockState2.getValue(HALF) == DoubleBlockHalf.LOWER) {
//        val blockState3 = if (blockState2.fluidState.`is`(Fluids.WATER)) Blocks.WATER.defaultBlockState() else Blocks.AIR.defaultBlockState()
//        level.setBlock(blockPos2, blockState3, 35)
//        level.levelEvent(player, 2001, blockPos2, getId(blockState2))
//      }
//    }
//  }
//
//  override fun createBlockStateDefinition(builder: StateDefinition.Builder<Block?, BlockState?>) {
//    builder.add(*arrayOf<Property<*>>(HALF))
//  }
//
//  override fun getSeed(blockState: BlockState, blockPos: BlockPos): Long {
//    return Mth.getSeed(blockPos.x, blockPos.below(if (blockState.getValue(HALF) == DoubleBlockHalf.LOWER) 0 else 1).y, blockPos.z)
//  }
//
//  // Plant growth
//  override fun randomTick(blockState: BlockState, serverLevel: ServerLevel, blockPos: BlockPos, randomSource: RandomSource) {
//    if (serverLevel.getRawBrightness(blockPos, 0) >= 9) {
//      val i = this.getAge(blockState)
//      if (i < this.maxAge) {
//        val f = getGrowthSpeed(this, serverLevel, blockPos)
//        if (randomSource.nextInt((25.0f / f).toInt() + 1) == 0) {
//          serverLevel.setBlock(blockPos, this.getStateForAge(i + 1), 2)
//        }
//      }
//    }
//  }
//
//  companion object {
//    val HALF = BlockStateProperties.DOUBLE_BLOCK_HALF
//
//    fun placeAt(levelAccessor: LevelAccessor, blockState: BlockState, blockPos: BlockPos, i: Int) {
//      val blockPos2 = blockPos.above()
//      levelAccessor.setBlock(blockPos, copyWaterloggedFrom(levelAccessor, blockPos, blockState.setValue(HALF, DoubleBlockHalf.LOWER)), i)
//      levelAccessor.setBlock(blockPos2, copyWaterloggedFrom(levelAccessor, blockPos2, blockState.setValue(HALF, DoubleBlockHalf.UPPER)), i)
//    }
//
//    fun copyWaterloggedFrom(levelReader: LevelReader, blockPos: BlockPos?, blockState: BlockState): BlockState {
//      return if (blockState.hasProperty(BlockStateProperties.WATERLOGGED)) blockState.setValue(BlockStateProperties.WATERLOGGED, levelReader.isWaterAt(blockPos)) else blockState
//    }
//  }
}