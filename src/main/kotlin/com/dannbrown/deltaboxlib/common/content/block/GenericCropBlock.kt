package com.dannbrown.deltaboxlib.common.content.block

import net.minecraft.core.BlockPos
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.BlockGetter
import net.minecraft.world.level.ItemLike
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.CropBlock
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.phys.shapes.CollisionContext
import net.minecraft.world.phys.shapes.VoxelShape
import java.util.function.Supplier

open class GenericCropBlock(props: Properties, private val seedItem: Supplier<ItemLike>): CropBlock(props) {
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
    return seedItem.get()
  }

  override fun getShape(state: BlockState, level: BlockGetter, pos: BlockPos, context: CollisionContext): VoxelShape {
    return SHAPE_BY_AGE[state.getValue(this.ageProperty)]
  }

  /*? if >1.21 {*/
  /*override fun getCloneItemStack(arg: net.minecraft.world.level.LevelReader, pos: BlockPos, state: BlockState): ItemStack {
    return ItemStack(seedItem.get())
  }
  *//*?} else {*/
  override fun getCloneItemStack(arg: BlockGetter, pos: BlockPos, state: BlockState): ItemStack {
    return ItemStack(seedItem.get())
  }
  /*?}*/

  /*? if forge {*/
  override fun getPlant(level: BlockGetter, pos: BlockPos): BlockState {
    return this.defaultBlockState()
  }
  /*?}*/
}