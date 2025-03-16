package com.dannbrown.palegardenbackport.content.blocks.creakingHeart

import net.minecraft.core.BlockPos
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.entity.BlockEntityType
import net.minecraft.world.level.block.state.BlockState

class CreakingHeartBlockEntity(
  type: BlockEntityType<CreakingHeartBlockEntity>,
  blockPos: BlockPos,
  blockState: BlockState
) : BlockEntity(type, blockPos, blockState) {

}