package com.dannbrown.palegardenbackport.content.block.creakingHeart

import com.dannbrown.deltaboxlib.registry.generators.BlockFamily
import com.dannbrown.palegardenbackport.ModContent
import com.dannbrown.palegardenbackport.ModContent.Companion.WOOD_FAMILY
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.entity.player.Player
import net.minecraft.world.level.Level
import net.minecraft.world.level.LevelReader
import net.minecraft.world.level.block.BaseEntityBlock
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.entity.BlockEntityTicker
import net.minecraft.world.level.block.entity.BlockEntityType
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.block.state.StateDefinition
import net.minecraft.world.level.block.state.properties.BlockStateProperties
import net.minecraft.world.level.block.state.properties.BooleanProperty

class CreakingHeartBlock(props: Properties): BaseEntityBlock(props) {
  override fun createBlockStateDefinition(pBuilder: StateDefinition.Builder<Block, BlockState>) {
    super.createBlockStateDefinition(pBuilder.add(NATURAL, ACTIVE))
  }

  override fun newBlockEntity(blockPos: BlockPos, blockState: BlockState): BlockEntity {
    return CreakingHeartBlockEntity(ModContent.CREAKING_HEART_BLOCK_ENTITY.get(), blockPos, blockState)
  }

  init {
    this.registerDefaultState(this.stateDefinition.any().setValue(NATURAL, false).setValue(ACTIVE, false))
  }

  fun hasRequiredLogs(blockState: BlockState, levelReader: LevelReader, blockPos: BlockPos): Boolean {
    val blockAbove = levelReader.getBlockState(blockPos.above())
    val blockBelow = levelReader.getBlockState(blockPos.below())

    val blockAbovePaleLog = blockAbove.`is`(WOOD_FAMILY.blocks[BlockFamily.Type.LOG]!!.get()) || blockAbove.`is`(WOOD_FAMILY.blocks[BlockFamily.Type.STRIPPED_LOG]!!.get())
    val blockBelowPaleLog = blockBelow.`is`(WOOD_FAMILY.blocks[BlockFamily.Type.LOG]!!.get()) || blockBelow.`is`(WOOD_FAMILY.blocks[BlockFamily.Type.STRIPPED_LOG]!!.get())

    if(blockAbovePaleLog && blockBelowPaleLog) {
      if(blockAbove.getValue(BlockStateProperties.AXIS) == Direction.Axis.Y && blockBelow.getValue(BlockStateProperties.AXIS) == Direction.Axis.Y) {
        return true
      }
    }

    return false
  }

  override fun onRemove(pState: BlockState, pLevel: Level, pPos: BlockPos, pNewState: BlockState, pMovedByPiston: Boolean) {
    super.onRemove(pState, pLevel, pPos, pNewState, pMovedByPiston)
//    tryAwardExperience(pLevel, pPos)
    // TODO: Remove spawned creakings
  }

  private fun tryAwardExperience(player: Player, blockState: BlockState, level: Level, blockPos: BlockPos) {
    if (!player.isCreative && !player.isSpectator && blockState.getValue(NATURAL) as Boolean && level is ServerLevel) {
      this.popExperience(level, blockPos, level.random.nextIntBetweenInclusive(20, 24))
    }
  }

  override fun playerWillDestroy(level: Level, blockPos: BlockPos, blockState: BlockState, player: Player) {
//    val var6 = p_361112_.getBlockEntity(p_368479_)
//    if (var6 is CreakingHeartBlockEntity) {
////      var6.removeProtector(p_362626_.damageSources().playerAttack(p_362626_))
//      this.tryAwardExperience(p_362626_, p_363792_!!, p_361112_, p_368479_!!)
//    }

    return super.playerWillDestroy(level, blockPos, blockState, player)
  }

  override fun hasAnalogOutputSignal(blockState: BlockState): Boolean {
    return true
  }

  override fun getAnalogOutputSignal(blockState: BlockState, level: Level, blockPos: BlockPos): Int {
    if (!blockState.getValue(ACTIVE)) {
      return 0
    }
    else {
//      val var5 = level.getBlockEntity(blockPos)
//      if (var5 is CreakingHeartBlockEntity) {
//        val `$$3` = var5 as CreakingHeartBlockEntity
//        return `$$3`.getAnalogOutputSignal()
        return 15
//      }
//      else {
//        return 0
//      }
    }
  }

  override fun <T : BlockEntity?> getTicker(level: Level, blockState: BlockState, pBlockEntityType: BlockEntityType<T>): BlockEntityTicker<T>? {
    if (level.isClientSide) return null

    return if (blockState.getValue<Boolean>(ACTIVE) as Boolean) {
      createTickerHelper<CreakingHeartBlockEntity, T>(pBlockEntityType, ModContent.CREAKING_HEART_BLOCK_ENTITY.get(), CreakingHeartBlockEntity::serverTick)
    } else {
      null
    }

  }

  companion object{
    val NATURAL = BooleanProperty.create("natural")
    val ACTIVE = BooleanProperty.create("active")
  }
}