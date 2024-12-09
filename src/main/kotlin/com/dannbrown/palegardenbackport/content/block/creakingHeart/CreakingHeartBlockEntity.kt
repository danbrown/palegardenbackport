package com.dannbrown.palegardenbackport.content.block.creakingHeart

import net.minecraft.core.BlockPos
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.entity.BlockEntityType
import net.minecraft.world.level.block.state.BlockState

class CreakingHeartBlockEntity(type: BlockEntityType<CreakingHeartBlockEntity>, blockPos: BlockPos, blockState: BlockState): BlockEntity(type, blockPos, blockState) {
  val outputSignal: Int = 0



  fun getAnalogOutputSignal(): Int {
    return this.outputSignal
  }

  companion object{
    fun serverTick(level: Level, blockPos: BlockPos, blockState: BlockState, blockEntity: BlockEntity) {

    }
  }
}