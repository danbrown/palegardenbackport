package com.dannbrown.palegardenbackport.content.block

import com.dannbrown.palegardenbackport.ModContent
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.world.item.context.BlockPlaceContext
import net.minecraft.world.level.BlockGetter
import net.minecraft.world.level.LevelAccessor
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.MultifaceBlock
import net.minecraft.world.level.block.MultifaceSpreader
import net.minecraft.world.level.block.SimpleWaterloggedBlock
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.block.state.StateDefinition
import net.minecraft.world.level.block.state.properties.BlockStateProperties
import net.minecraft.world.level.block.state.properties.BooleanProperty
import net.minecraft.world.level.block.state.properties.Property
import net.minecraft.world.level.material.FluidState
import net.minecraft.world.level.material.Fluids

class ResinClumpBlock(pProperties: Properties) : MultifaceBlock(pProperties), SimpleWaterloggedBlock {
  private val spreader = MultifaceSpreader(this)
  override fun createBlockStateDefinition(pBuilder: StateDefinition.Builder<Block, BlockState>) {
    super.createBlockStateDefinition(pBuilder.add(*arrayOf<Property<*>>(WATERLOGGED)))
  }

  override fun updateShape(pState: BlockState, pDirection: Direction, pNeighborState: BlockState, pLevel: LevelAccessor, pPos: BlockPos, pNeighborPos: BlockPos): BlockState {
    if (pState.getValue(WATERLOGGED) as Boolean) {
      pLevel.scheduleTick(pPos, Fluids.WATER, Fluids.WATER.getTickDelay(pLevel))
    }

    return super.updateShape(pState, pDirection, pNeighborState, pLevel, pPos, pNeighborPos)
  }

  override fun canBeReplaced(pState: BlockState, pUseContext: BlockPlaceContext): Boolean {
    return !pUseContext.itemInHand.`is`(ModContent.RESIN_CLUMP.asItem()) || super.canBeReplaced(pState, pUseContext)
  }

  override fun getFluidState(pState: BlockState): FluidState {
    return if (pState.getValue(WATERLOGGED) as Boolean) Fluids.WATER.getSource(false) else super.getFluidState(pState)
  }

  override fun propagatesSkylightDown(pState: BlockState, pLevel: BlockGetter, pPos: BlockPos): Boolean {
    return pState.fluidState.isEmpty
  }

  override fun getSpreader(): MultifaceSpreader {
    return this.spreader
  }

  init {
    this.registerDefaultState(defaultBlockState().setValue(WATERLOGGED, false))
  }

  companion object {
    private val WATERLOGGED: BooleanProperty = BlockStateProperties.WATERLOGGED
  }
}
