package com.dannbrown.deltaboxlib.common.content.block

import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerLevel
import net.minecraft.sounds.SoundEvents
import net.minecraft.sounds.SoundSource
import net.minecraft.world.InteractionResult
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.BlockGetter
import net.minecraft.world.level.ItemLike
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.CropBlock
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.phys.BlockHitResult
import net.minecraft.world.phys.shapes.CollisionContext
import net.minecraft.world.phys.shapes.VoxelShape
import java.util.function.Supplier

class GenericCropBlock(
  props: Properties,
  private val isBush: Boolean = false,
  private val includeSeedOnDrop: Boolean = false,
  private val fruitItem: Supplier<ItemLike>,
  private val chance: Float = 1f,
  private val multiplier: Int = 1
): CropBlock(props) {
  private val SHAPE_BY_AGE = arrayOf<VoxelShape>(
    Block.box(0.0, 0.0, 0.0, 16.0, 2.0, 16.0),
    Block.box(0.0, 0.0, 0.0, 16.0, 3.0, 16.0),
    Block.box(0.0, 0.0, 0.0, 16.0, 5.0, 16.0),
    Block.box(0.0, 0.0, 0.0, 16.0, 7.0, 16.0),
    Block.box(0.0, 0.0, 0.0, 16.0, 8.0, 16.0),
    Block.box(0.0, 0.0, 0.0, 16.0, 9.0, 16.0),
    Block.box(0.0, 0.0, 0.0, 16.0, 9.0, 16.0),
    Block.box(0.0, 0.0, 0.0, 16.0, 10.0, 16.0)
  )

  override fun getBaseSeedId(): ItemLike {
    return fruitItem.get()
  }

  override fun getShape(state: BlockState, level: BlockGetter, pos: BlockPos, context: CollisionContext): VoxelShape {
    return SHAPE_BY_AGE[state.getValue(this.ageProperty)]
  }

  /*? if >1.21 {*/
  /*override fun getCloneItemStack(arg: net.minecraft.world.level.LevelReader, pos: BlockPos, state: BlockState): ItemStack {
    return ItemStack(fruitItem.get())
  }
  *//*?} else {*/
  override fun getCloneItemStack(arg: BlockGetter, pos: BlockPos, state: BlockState): ItemStack {
    return ItemStack(fruitItem.get())
  }
  /*?}*/

  /*? if forge {*/
  override fun getPlant(level: BlockGetter, pos: BlockPos): BlockState {
    return this.defaultBlockState()
  }
  /*?}*/


  /*? if >1.21 {*/
  /*override fun useWithoutItem(blockState: BlockState, level: Level, blockPos: BlockPos, player: Player, blockHitResult: BlockHitResult): InteractionResult {
    if (!level.isClientSide && blockState.getValue(this.ageProperty) == maxAge) {
      if (isBush) {
        dropResources(level as ServerLevel, blockPos)
        level.setBlockAndUpdate(blockPos, blockState.setValue(this.ageProperty, 4))
        return InteractionResult.SUCCESS
      }
    }
    return super.useWithoutItem(blockState, level, blockPos, player, blockHitResult)
  }
  *//*?} else {*/
  override fun use(blockState: BlockState, level: Level, blockPos: BlockPos, player: Player, interactionHand: net.minecraft.world.InteractionHand, blockHitResult: BlockHitResult): InteractionResult {
    if (!level.isClientSide && blockState.getValue(this.ageProperty) == maxAge) {
      if (isBush && interactionHand == net.minecraft.world.InteractionHand.MAIN_HAND && player.getItemInHand(interactionHand).isEmpty) {
        dropResources(level as ServerLevel, blockPos)
        level.setBlockAndUpdate(blockPos, blockState.setValue(this.ageProperty, 4))
        return InteractionResult.SUCCESS
      }
    }
    return super.use(blockState, level, blockPos, player, interactionHand, blockHitResult)
  }
  /*?}*/

  private fun dropResources(pLevel: ServerLevel, pPos: BlockPos) {
    // if no seed and drop item is set, at least one is required
    if (!isBush) return

    // if have a seed item, do 'multiplier' rows of 'chance' to pop a seed
    var seedsToDrop = 0
    for (i in 0 until multiplier) {
      if (pLevel.random.nextFloat() < chance) {
        seedsToDrop++
      }
    }
    if (seedsToDrop > 0 && includeSeedOnDrop) Block.popResource(pLevel, pPos, ItemStack(this.asItem(), seedsToDrop))

    // if have a drop item, do 'multiplier' rows of 'chance' to pop a drop item
    if (fruitItem != null) {
      var dropsToDrop = 1
      for (i in 0 until multiplier-1) {
        if (pLevel.random.nextFloat() < chance) {
          dropsToDrop++
        }
      }
      if (dropsToDrop > 0) Block.popResource(pLevel, pPos, ItemStack(fruitItem.get(), dropsToDrop))
    }

    pLevel.playSound(
      null,
      pPos,
      SoundEvents.SWEET_BERRY_BUSH_PICK_BERRIES,
      SoundSource.BLOCKS,
      1.0f,
      0.8f + pLevel.random.nextFloat() * 0.4f
    )
  }
}