package com.dannbrown.palegardenbackport.content.block.creakingHeart

import com.dannbrown.deltaboxlib.registry.generators.BlockFamily
import com.dannbrown.palegardenbackport.ModContent
import com.dannbrown.palegardenbackport.content.particle.TrailParticleOption
import com.dannbrown.palegardenbackport.init.ModSounds
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.server.level.ServerLevel
import net.minecraft.sounds.SoundSource
import net.minecraft.util.RandomSource
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResult
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.context.BlockPlaceContext
import net.minecraft.world.level.Level
import net.minecraft.world.level.LevelAccessor
import net.minecraft.world.level.LevelReader
import net.minecraft.world.level.block.BaseEntityBlock
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.RenderShape
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.entity.BlockEntityTicker
import net.minecraft.world.level.block.entity.BlockEntityType
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.block.state.StateDefinition
import net.minecraft.world.level.block.state.properties.BlockStateProperties
import net.minecraft.world.level.block.state.properties.BooleanProperty
import net.minecraft.world.phys.BlockHitResult
import net.minecraft.world.phys.Vec3

class CreakingHeartBlock(props: Properties): BaseEntityBlock(props) {
  init {
    this.registerDefaultState(this.stateDefinition.any().setValue(NATURAL, false).setValue(ACTIVE, false).setValue(AXIS, Direction.Axis.Y))
  }

  override fun createBlockStateDefinition(pBuilder: StateDefinition.Builder<Block, BlockState>) {
    super.createBlockStateDefinition(pBuilder.add(NATURAL, ACTIVE, AXIS))
  }

  override fun getStateForPlacement(context: BlockPlaceContext): BlockState {
    return updateState((defaultBlockState().setValue(AXIS, context.clickedFace.axis) as BlockState), context.level, context.clickedPos)
  }

  override fun newBlockEntity(blockPos: BlockPos, blockState: BlockState): BlockEntity {
    return CreakingHeartBlockEntity(ModContent.CREAKING_HEART_BLOCK_ENTITY.get(), blockPos, blockState)
  }

  override fun getRenderShape(pState: BlockState): RenderShape {
    return RenderShape.MODEL
  }

  override fun use(
    pState: BlockState,
    level: Level,
    pPos: BlockPos,
    pPlayer: Player,
    pHand: InteractionHand,
    pHit: BlockHitResult
  ): InteractionResult {
    if (pPlayer.isShiftKeyDown) {
      val blockEntity = level.getBlockEntity(pPos)
      if (blockEntity is CreakingHeartBlockEntity) {
        level.setBlockAndUpdate(pPos, pState.setValue(ACTIVE, !pState.getValue(ACTIVE)))

        // Emit trail of particles
        if (level is ServerLevel) {
          val isActive = pState.getValue(ACTIVE)
          val particleColor = if (isActive) -10526881 else -231406
          emitParticleTrail(level, pPlayer.boundingBox.center, Vec3.atCenterOf(pPos), particleColor)
        }

        return InteractionResult.SUCCESS
      }
    }
    return super.use(pState, level, pPos, pPlayer, pHand, pHit)
  }

  override fun updateShape(blockState: BlockState, direction: Direction, blockState2: BlockState, pLevel: LevelAccessor, blockPos: BlockPos, blockPos2: BlockPos): BlockState {
    return updateState(super.updateShape(blockState, direction, blockState2, pLevel, blockPos, blockPos2), pLevel, blockPos)
  }

  override fun onRemove(pState: BlockState, pLevel: Level, pPos: BlockPos, pNewState: BlockState, pMovedByPiston: Boolean) {
    super.onRemove(pState, pLevel, pPos, pNewState, pMovedByPiston)
    val blockEntity = pLevel.getBlockEntity(pPos)
    if (blockEntity is CreakingHeartBlockEntity) {
      blockEntity.removeProtector(null)
    }
  }

  private fun tryAwardExperience(player: Player?, blockState: BlockState, level: Level, blockPos: BlockPos) {
    if (blockState.getValue(NATURAL) as Boolean && level is ServerLevel) {
      if(player !== null && (player.isCreative || player.isSpectator)) return // Don't award experience to creative or spectator players
      this.popExperience(level, blockPos, level.random.nextIntBetweenInclusive(20, 24))
    }
  }

  override fun playerWillDestroy(level: Level, blockPos: BlockPos, blockState: BlockState, player: Player) {
    val blockEntity = level.getBlockEntity(blockPos)
    if (blockEntity is CreakingHeartBlockEntity) {
      blockEntity.removeProtector(level.damageSources().playerAttack(player))
      this.tryAwardExperience(player, blockState, level, blockPos)
    }

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
      val blockEntity = level.getBlockEntity(blockPos)
      return if (blockEntity is CreakingHeartBlockEntity) {
        blockEntity.getAnalogOutputSignal()
      }
      else {
        0
      }
    }
  }

  override fun animateTick(state: BlockState, level: Level, blockPos: BlockPos, randomSource: RandomSource) {
    if (isNaturalNight(level)) {
      if (state.getValue(ACTIVE) as Boolean) {
        if (randomSource.nextInt(16) == 0 && isSurroundedByLogs(level, blockPos)) {
          level.playLocalSound(blockPos.x.toDouble(), blockPos.y.toDouble(), blockPos.z.toDouble(), ModSounds.CREAKING_HEART_IDLE.get(), SoundSource.BLOCKS, 1.0f, 1.0f, false)
        }
      }
    }
  }

  override fun <T : BlockEntity?> getTicker(level: Level, blockState: BlockState, pBlockEntityType: BlockEntityType<T>): BlockEntityTicker<T>? {
    if (level.isClientSide) return null
    return if (blockState.getValue(ACTIVE) as Boolean) {
      createTickerHelper(pBlockEntityType, ModContent.CREAKING_HEART_BLOCK_ENTITY.get(), CreakingHeartBlockEntity::serverTick)
    } else {
      null
    }
  }

  companion object{
    val NATURAL = BooleanProperty.create("natural")
    val ACTIVE = BooleanProperty.create("active")
    val AXIS = BlockStateProperties.AXIS

    fun getNaturalState(): BlockState {
      return ModContent.CREAKING_HEART.get().defaultBlockState().setValue(NATURAL, true).setValue(ACTIVE, true)
    }

    fun emitParticleTrail(level: ServerLevel, startPos: Vec3, endPos: Vec3, particleColor: Int, duration: Int = 10) {
      val direction = endPos.subtract(startPos)
      val distance = direction.length()
      val step = 0.25
      val steps = (distance / step).toInt()
      val unitDirection = direction.normalize().scale(step)

      for (i in 0..steps) {
        val currentPos = startPos.add(unitDirection.scale(i.toDouble()))
        val trailParticle = TrailParticleOption(
          endPos,
          particleColor,
          level.random.nextInt(30) + duration // Lifespan
        )
        level.sendParticles(
          trailParticle,
          currentPos.x,
          currentPos.y,
          currentPos.z,
          1,
          0.0 + level.random.nextDouble() * 0.4,
          0.0 + level.random.nextDouble() * 0.4,
          0.0 + level.random.nextDouble() * 0.4,
          0.008
        )
      }
    }

    fun isNaturalNight(level: Level): Boolean {
      return level.dimensionType().natural() && level.isNight
    }

    fun isPaleOakLog(blockState: BlockState): Boolean {
      return blockState.`is`(ModContent.WOOD_FAMILY.blocks[BlockFamily.Type.LOG]!!.get()) || blockState.`is`(ModContent.WOOD_FAMILY.blocks[BlockFamily.Type.STRIPPED_LOG]!!.get())
    }

    fun hasRequiredLogs(blockState: BlockState, levelReader: LevelReader, blockPos: BlockPos): Boolean {
      val axis = blockState.getValue(AXIS)
      val directions: Array<Direction> = when (axis) {
        Direction.Axis.X -> arrayOf(Direction.EAST, Direction.WEST)
        Direction.Axis.Y -> arrayOf(Direction.UP, Direction.DOWN)
        Direction.Axis.Z -> arrayOf(Direction.SOUTH, Direction.NORTH)
        else -> throw IllegalStateException("Invalid axis: $axis")
      }

      for (dir in directions) {
        val relative = levelReader.getBlockState(blockPos.relative(dir))
        if (!isPaleOakLog(relative) || relative.getValue(AXIS) !== axis) {
          return false
        }
      }

      return true
    }


    private fun updateState(blockState: BlockState, levelReader: LevelReader, blockPos: BlockPos): BlockState {
      val hasLogs = hasRequiredLogs(blockState, levelReader, blockPos)
      return if (hasLogs && !blockState.getValue(ACTIVE)) blockState.setValue(ACTIVE, true) else blockState
    }

    private fun isSurroundedByLogs(levelAccessor: LevelAccessor, blockPos: BlockPos): Boolean {
      val directions = Direction.values()
      for (direction in directions) {
        val relative = blockPos.relative(direction)
        val state = levelAccessor.getBlockState(relative)
        if (!isPaleOakLog(state)) {
          return false
        }
      }
      return true
    }

    // ----
  }
}