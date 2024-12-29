package com.dannbrown.deltaboxlib.common.content.block

import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerLevel
import net.minecraft.sounds.SoundEvents
import net.minecraft.sounds.SoundSource
import net.minecraft.world.InteractionResult
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.ItemLike
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.phys.BlockHitResult
import java.util.function.Supplier

class BushCropBlock(
  props: Properties,
  private val isBush: Boolean = false,
  private val seedItem: Supplier<ItemLike>,
  private val fruitItem: Supplier<ItemLike>? = null,
  private val chance: Float = 1f,
  private val multiplier: Int = 1
): GenericCropBlock(props, seedItem) {
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
    if (seedsToDrop > 0) Block.popResource(pLevel, pPos, ItemStack(seedItem.get(), seedsToDrop))

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