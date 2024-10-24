package com.dannbrown.palegardenbackport.content.treeDecorator

import com.dannbrown.palegardenbackport.ModContent
import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.core.BlockPos
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.levelgen.feature.Feature
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider
import net.minecraft.world.level.levelgen.feature.treedecorators.AlterGroundDecorator
import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecoratorType
import net.minecraftforge.event.ForgeEventFactory
import kotlin.math.sqrt

class PaleOakGroundDecorator(
  val blockProvider: BlockStateProvider,
  val secondaryBlockProvider: BlockStateProvider // Second BlockStateProvider
) : AlterGroundDecorator(blockProvider) {

  override fun type(): TreeDecoratorType<*> {
    return ModContent.GROUND_DECORATOR.get()
  }

  companion object {
    val CODEC: Codec<PaleOakGroundDecorator> = RecordCodecBuilder.create { instance ->
      instance.group(
        BlockStateProvider.CODEC.fieldOf("provider").forGetter { it.blockProvider },
        BlockStateProvider.CODEC.fieldOf("secondary_provider").forGetter { it.secondaryBlockProvider }
      ).apply(instance) { blockProvider, secondaryProvider ->
        PaleOakGroundDecorator(blockProvider, secondaryProvider)
      }
    }
  }

  override fun place(pContext: Context) {
    val list: MutableList<BlockPos> = mutableListOf()
    val list1: List<BlockPos> = pContext.roots()
    val list2: List<BlockPos> = pContext.logs()

    if (list1.isEmpty()) {
      list.addAll(list2)
    } else if (list2.isNotEmpty() && list1[0].y == list2[0].y) {
      list.addAll(list2)
      list.addAll(list1)
    } else {
      list.addAll(list1)
    }

    if (list.isNotEmpty()) {
      val i = list[0].y
      list.stream()
        .filter { it.y == i }
        .forEach { pos ->
          // Adjusting the offsets for a smaller spread
          this.placeCircle(pContext, pos.west().north())
          this.placeCircle(pContext, pos.east().north())
          this.placeCircle(pContext, pos.west().south())
          this.placeCircle(pContext, pos.east().south())

          // Limit the random placements to stay closer
          for (j in 0..1) {
            val k = pContext.random().nextInt(4)
            val l = k % 2
            val i1 = k / 2
            this.placeCircle(pContext, pos.offset(-l, 0, -i1))
          }

          // Sprinkle blocks from the secondary provider
          this.placeCircleFromSecondaryProvider(pContext, pos)
        }
    }
  }

  private fun placeCircleFromSecondaryProvider(pContext: Context, pPos: BlockPos) {
    val radius = 2.5 // Adjust radius as needed
    for (i in -2..2) {
      for (j in -2..2) {
        val distance = Math.sqrt((i * i + j * j).toDouble()) // Calculate distance from center
        if (distance <= radius) { // Only place blocks within the circular area
          if (pContext.random().nextFloat() < 0.3f) { // 50% chance to place from the second provider
            this.placeBlockAtSecondaryProvider(pContext, pPos.offset(i, 0, j))
          }
        }
      }
    }
  }

  private fun placeBlockAtSecondaryProvider(pContext: Context, pPos: BlockPos) {
    // Check if the block directly below is solid and if the position above is air
    val blockPosBelow = pPos.below()
    val blockPosAbove = pPos.above()
    if (pContext.level().isStateAtPosition(blockPosBelow) { it.`is`(Blocks.DIRT) || it.`is`(Blocks.GRASS_BLOCK) } && pContext.isAir(blockPosAbove)) {
      pContext.setBlock(blockPosAbove, ForgeEventFactory.alterGround(pContext.level(), pContext.random(), blockPosAbove, secondaryBlockProvider.getState(pContext.random(), pPos)))
    }
  }

  private fun placeCircle(pContext: Context, pPos: BlockPos) {
    val radius = 1.5 // Radius for the circle
    val amorphousRadius = 3 // Max range for random amorphous positions

    // Place blocks within the circle
    for (i in -2..2) {
      for (j in -2..2) {
        val distance = Math.sqrt((i * i + j * j).toDouble())
        if (distance <= radius) {
          this.placeBlockAt(pContext, pPos.offset(i, 0, j))
        }
      }
    }

    // Place blocks randomly beyond the circle to create an amorphous effect
    for (k in 0..3) { // Adjust the number of random placements
      val randomOffsetX = pContext.random().nextInt(amorphousRadius * 2 + 1) - amorphousRadius
      val randomOffsetZ = pContext.random().nextInt(amorphousRadius * 2 + 1) - amorphousRadius
      val randomDistance = sqrt((randomOffsetX * randomOffsetX + randomOffsetZ * randomOffsetZ).toDouble())

      // Only place blocks beyond the circle radius
      if (randomDistance > radius && randomDistance <= amorphousRadius) {
        this.placeBlockAt(pContext, pPos.offset(randomOffsetX, 0, randomOffsetZ))
      }
    }
  }

  private fun placeBlockAt(pContext: Context, pPos: BlockPos) {
    for (i in 2 downTo -2) { // Reduced the range for placing blocks
      val blockPos = pPos.above(i)
      if (Feature.isGrassOrDirt(pContext.level(), blockPos)) {
        pContext.setBlock(blockPos, ForgeEventFactory.alterGround(pContext.level(), pContext.random(), blockPos, blockProvider.getState(pContext.random(), pPos)))
        break
      }
      if (!pContext.isAir(blockPos) && i < 0) {
        break
      }
    }
  }
}
