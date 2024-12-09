package com.dannbrown.palegardenbackport.content.block.creakingHeart

import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.block.state.StateDefinition
import net.minecraft.world.level.block.state.properties.BlockStateProperties

class CreakingHeartBlock(props: Properties): Block(props) {
  override fun createBlockStateDefinition(pBuilder: StateDefinition.Builder<Block, BlockState>) {
    super.createBlockStateDefinition(pBuilder.add(BlockStateProperties.ENABLED))
  }

  init {
    this.registerDefaultState(this.stateDefinition.any().setValue(BlockStateProperties.ENABLED, false))
  }

//  override fun ti
}