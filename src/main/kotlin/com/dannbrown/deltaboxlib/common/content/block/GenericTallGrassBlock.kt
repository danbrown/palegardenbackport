package com.dannbrown.deltaboxlib.common.content.block

import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerLevel
import net.minecraft.tags.BlockTags
import net.minecraft.util.RandomSource
import net.minecraft.world.level.BlockGetter
import net.minecraft.world.level.LevelReader
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.BonemealableBlock
import net.minecraft.world.level.block.DoublePlantBlock
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf
import org.jetbrains.annotations.NotNull
import java.util.function.Supplier

/**
 * A generic small tall grass block that grows into a tall plant.
 * It can also be waterlogged.
 * @param plantBlock The block to grow into.
 */
open class GenericTallGrassBlock(
  private val plantBlock: Supplier<out Block>,
  properties: Properties,
  private val needBonemeal: Boolean = false,
  private val placeOn: ((blockState: BlockState, blockGetter: BlockGetter, blockPos: BlockPos) -> Boolean)? = null
) : GenericSproutBlock(properties), BonemealableBlock {

  override fun randomTick(blockState: BlockState, serverLevel: ServerLevel, blockPos: BlockPos, @NotNull random: RandomSource) {
    if(needBonemeal) return
    val i = random.nextInt(150)
    if (i == 1) {
      if (serverLevel.getBlockState(blockPos.above()) === Blocks.AIR.defaultBlockState()) {
        growTallGrass(serverLevel, blockPos)
      }
    }
  }

  override fun canSurvive(blockState: BlockState, levelReader: LevelReader, blockPos: BlockPos): Boolean {
    val below = blockPos.below()
    return mayPlaceOn(levelReader.getBlockState(below), levelReader, below)
  }

  open fun mayPlaceOn(blockState: BlockState, blockGetter: BlockGetter, blockPos: BlockPos): Boolean {
    if (placeOn !== null) { return placeOn.invoke(blockState, blockGetter, blockPos) }
    return blockState.`is`(BlockTags.DIRT) || blockState.`is`(Blocks.GRASS_BLOCK)
  }

  open fun growTallGrass(@NotNull serverLevel: ServerLevel, @NotNull blockPos: BlockPos) {
    serverLevel.setBlock(
      blockPos,
      plantBlock.get().defaultBlockState().setValue(DoublePlantBlock.HALF, DoubleBlockHalf.LOWER),
      3
    )
    serverLevel.setBlock(
      blockPos.above(),
      plantBlock.get().defaultBlockState().setValue(DoublePlantBlock.HALF, DoubleBlockHalf.UPPER),
      3
    )
  }

  override fun performBonemeal(
    @NotNull serverLevel: ServerLevel,
    @NotNull random: RandomSource,
    @NotNull blockPos: BlockPos,
    @NotNull blockState: BlockState
  ) {
    if (serverLevel.getBlockState(blockPos.above()).isAir && canSurvive(blockState, serverLevel, blockPos)) {
      growTallGrass(serverLevel, blockPos)
    }
  }
}

