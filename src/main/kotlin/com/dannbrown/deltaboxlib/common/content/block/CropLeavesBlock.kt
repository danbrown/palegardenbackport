package com.dannbrown.deltaboxlib.common.content.block

import net.minecraft.core.BlockPos
import net.minecraft.core.BlockPos.MutableBlockPos
import net.minecraft.core.Direction
import net.minecraft.server.level.ServerLevel
import net.minecraft.sounds.SoundEvents
import net.minecraft.sounds.SoundSource
import net.minecraft.tags.BlockTags
import net.minecraft.util.Mth
import net.minecraft.util.RandomSource
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResult
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items
import net.minecraft.world.level.BlockGetter
import net.minecraft.world.level.ItemLike
import net.minecraft.world.level.Level
import net.minecraft.world.level.LevelAccessor
import net.minecraft.world.level.LevelReader
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.CropBlock
import net.minecraft.world.level.block.LeavesBlock
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.block.state.StateDefinition
import net.minecraft.world.level.block.state.properties.BlockStateProperties
import net.minecraft.world.level.block.state.properties.BooleanProperty
import net.minecraft.world.level.block.state.properties.IntegerProperty
import net.minecraft.world.level.gameevent.GameEvent
import net.minecraft.world.phys.BlockHitResult
import net.minecraft.world.phys.shapes.CollisionContext
import net.minecraft.world.phys.shapes.Shapes
import net.minecraft.world.phys.shapes.VoxelShape
import java.util.function.Supplier
import kotlin.math.min

open class CropLeavesBlock(props: Properties, private val itemToDrop: Supplier<ItemLike>, private val flammability: Int = 20, private val fireSpread: Int = 5): CropBlock(props) {
  companion object{
    val MAX_AGE: Int = 3
    val MAX_DISTANCE: Int = 7
    val AGE: IntegerProperty = BlockStateProperties.AGE_3
    val DISTANCE: IntegerProperty = BlockStateProperties.DISTANCE
    val WATERLOGGED: BooleanProperty = BlockStateProperties.WATERLOGGED
  }

  init{
    registerDefaultState(
      stateDefinition.any()
        .setValue(AGE, 0)
        .setValue(DISTANCE, MAX_DISTANCE)
        .setValue(WATERLOGGED, false)
    )
  }

  /*? if forge || neoforge {*/
  override fun isFlammable(state: BlockState, level: BlockGetter, pos: BlockPos, direction: Direction): Boolean {
    return true
  }

  override fun getFlammability(state: BlockState, level: BlockGetter, pos: BlockPos, direction: Direction): Int {
    return flammability
  }

  override fun getFireSpreadSpeed(state: BlockState, level: BlockGetter, pos: BlockPos, direction: Direction): Int {
    return fireSpread
  }
  /*?}*/

  override fun getLightBlock(state: BlockState, world: BlockGetter, pos: BlockPos): Int {
    return 1
  }

  private fun updateDistanceFromLogs(state: BlockState, world: LevelAccessor, pos: BlockPos): BlockState {
    var distance = MAX_DISTANCE
    val mutablePos = MutableBlockPos()
    val directions: Array<Direction> = Direction.values()

    for (direction in directions) {
      mutablePos.setWithOffset(pos, direction)
      distance = min(distance.toDouble(), (getDistanceFromLog(world.getBlockState(mutablePos)) + 1).toDouble())
        .toInt()
      if (distance == 1) {
        break
      }
    }

    return state.setValue(DISTANCE, distance)
  }

  private fun getDistanceFromLog(state: BlockState): Int {
    return if (state.`is`(BlockTags.LOGS)) {
      0
    }
    else {
      if (state.block is CropLeavesBlock || state.block is LeavesBlock) state.getValue(DISTANCE) else MAX_DISTANCE
    }
  }
  override fun getMaxAge(): Int {
    return 3
  }

  public override fun getAgeProperty(): IntegerProperty {
    return AGE
  }

  override fun createBlockStateDefinition(builder: StateDefinition.Builder<Block?, BlockState?>) {
    builder.add(AGE, DISTANCE, WATERLOGGED)
  }

  override fun isRandomlyTicking(state: BlockState): Boolean {
    return true
  }

  override fun randomTick(state: BlockState, world: ServerLevel, pos: BlockPos, random: RandomSource) {
    if (world.getRawBrightness(pos, 0) >= 9) {
      val i = this.getAge(state)
      if (i < this.maxAge) {
        if (random.nextInt(100) % 25 == 0) { // 4% chance
          world.setBlock(pos, state.setValue(ageProperty, i + 1), Block.UPDATE_CLIENTS)
        }
      }
    }
    if (state.getValue(DISTANCE) == 7) {
      dropResources(state, world, pos)
      world.removeBlock(pos, false)
    }
  }

  override fun tick(state: BlockState, world: ServerLevel, pos: BlockPos, random: RandomSource) {
    world.setBlock(pos, updateDistanceFromLogs(state, world, pos), Block.UPDATE_ALL)
  }

  override fun getShape(state: BlockState, world: BlockGetter, pos: BlockPos, context: CollisionContext): VoxelShape {
    return Shapes.block()
  }

  /*? if >1.21 {*/
  /*override fun getCloneItemStack(levelReader: LevelReader, pos: BlockPos, state: BlockState): ItemStack {
    return ItemStack(itemToDrop.get())
  }

  override fun useWithoutItem(blockState: BlockState, level: Level, blockPos: BlockPos, player: Player, blockHitResult: BlockHitResult): InteractionResult {
    if (!isMature(blockState) && player.getItemInHand(InteractionHand.MAIN_HAND).`is`(Items.BONE_MEAL)) {
      return InteractionResult.PASS
    }
    else if (isMature(blockState)) {
      Block.popResource(level, blockPos, ItemStack(itemToDrop.get()))
      level.playSound(null as Player?, blockPos, SoundEvents.SWEET_BERRY_BUSH_PICK_BERRIES, SoundSource.BLOCKS, 1.0f, 0.8f + level.random.nextFloat() * 0.4f)
      val state = blockState.setValue(ageProperty, 0)
      level.setBlock(blockPos, state, 2)
      level.gameEvent(GameEvent.BLOCK_CHANGE, blockPos, GameEvent.Context.of(player, state))
      return InteractionResult.sidedSuccess(level.isClientSide)
    }
    else {
      return super.useWithoutItem(blockState, level, blockPos, player, blockHitResult)
    }
  }
  *//*?} else {*/
  override fun getCloneItemStack(pLevel: BlockGetter, pos: BlockPos, state: BlockState): ItemStack {
    return ItemStack(itemToDrop.get())
  }
  override fun use(pState: BlockState, pLevel: Level, pos: BlockPos, player: Player, pHand: InteractionHand, pHit: BlockHitResult): InteractionResult {
    if (!isMature(pState) && player.getItemInHand(pHand).`is`(Items.BONE_MEAL)) {
      return InteractionResult.PASS
    }
    else if (isMature(pState)) {
      Block.popResource(pLevel, pos, ItemStack(itemToDrop.get()))
      pLevel.playSound(null as Player?, pos, SoundEvents.SWEET_BERRY_BUSH_PICK_BERRIES, SoundSource.BLOCKS, 1.0f, 0.8f + pLevel.random.nextFloat() * 0.4f)
      val blockState = pState.setValue(ageProperty, 0)
      pLevel.setBlock(pos, blockState, 2)
      pLevel.gameEvent(GameEvent.BLOCK_CHANGE, pos, GameEvent.Context.of(player, blockState))
      return InteractionResult.sidedSuccess(pLevel.isClientSide)
    }
    else {
      return super.use(pState, pLevel, pos, player, pHand, pHit)
    }
  }
  /*?}*/

  override fun mayPlaceOn(floor: BlockState, world: BlockGetter, pos: BlockPos): Boolean {
    return true
  }

  override fun canSurvive(state: BlockState, world: LevelReader, pos: BlockPos): Boolean {
    return true
  }

  override fun updateShape(state: BlockState, direction: Direction, neighborState: BlockState, world: LevelAccessor, pos: BlockPos, neighborPos: BlockPos): BlockState {
    val distance = getDistanceFromLog(neighborState) + 1
    if (distance != 1 || state.getValue(DISTANCE) != distance) {
      world.scheduleTick(pos, this, 1)
    }
    return state
  }

  private fun isMature(state: BlockState): Boolean {
    return state.getValue(ageProperty) == maxAge
  }

  override fun getBonemealAgeIncrease(pLevel: Level): Int {
    return Mth.nextInt(pLevel.random, 1, 2)
  }

  override fun performBonemeal(pLevel: ServerLevel, pRandom: RandomSource, pPos: BlockPos, pState: BlockState) {
    var i = this.getAge(pState) + this.getBonemealAgeIncrease(pLevel)
    val j = this.maxAge
    if (i > j) {
      i = j
    }
    pLevel.setBlock(pPos, pState.setValue(this.ageProperty, i), 2)
  }
}