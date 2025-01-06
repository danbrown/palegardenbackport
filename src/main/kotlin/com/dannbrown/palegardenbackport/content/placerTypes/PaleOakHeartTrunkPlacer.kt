package com.dannbrown.palegardenbackport.content.placerTypes

import com.dannbrown.palegardenbackport.ModContent
import com.dannbrown.palegardenbackport.content.block.creakingHeart.CreakingHeartBlock
import com.dannbrown.palegardenbackport.init.ModCommonConfig
import com.google.common.collect.Lists
import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.tags.BlockTags
import net.minecraft.util.RandomSource
import net.minecraft.world.level.LevelSimulatedReader
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.levelgen.feature.TreeFeature
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacer
import net.minecraft.world.level.levelgen.feature.trunkplacers.DarkOakTrunkPlacer
import net.minecraft.world.level.levelgen.feature.trunkplacers.TrunkPlacerType
import java.util.function.BiConsumer
import java.util.function.Predicate

class PaleOakHeartTrunkPlacer(baseHeight: Int, heightRandA: Int, heightRandB: Int) : DarkOakTrunkPlacer(baseHeight, heightRandA, heightRandB) {
  companion object {
    val CODEC: Codec<PaleOakHeartTrunkPlacer> = RecordCodecBuilder.create { instance ->
      trunkPlacerParts(instance)
        .apply(instance) { baseHeight, heightRandA, heightRandB ->
          PaleOakHeartTrunkPlacer(baseHeight!!, heightRandA!!, heightRandB!!)
        }
    }
  }

  override fun placeTrunk(
    level: LevelSimulatedReader,
    blockConsumer: BiConsumer<BlockPos, BlockState>,
    random: RandomSource,
    height: Int,
    startPos: BlockPos,
    config: TreeConfiguration
  ): MutableList<FoliagePlacer.FoliageAttachment> {
    val foliageAttachments = Lists.newArrayList<FoliagePlacer.FoliageAttachment>()
    val groundPos = startPos.below()

    // Prepare dirt foundation
    setDirtAt(level, blockConsumer, random, groundPos, config)
    setDirtAt(level, blockConsumer, random, groundPos.east(), config)
    setDirtAt(level, blockConsumer, random, groundPos.south(), config)
    setDirtAt(level, blockConsumer, random, groundPos.south().east(), config)

    // Main trunk logic
    val horizontalDirection = Direction.Plane.HORIZONTAL.getRandomDirection(random)
    val branchStartHeight = height - random.nextInt(4)
    var branchLength = 2 - random.nextInt(3)
    val startX = startPos.x
    val startY = startPos.y
    val startZ = startPos.z
    var currentX = startX
    var currentZ = startZ
    val topY = startY + height - 1

    for (currentHeight in 0 until height) {
      if (currentHeight >= branchStartHeight && branchLength > 0) {
        currentX += horizontalDirection.stepX
        currentZ += horizontalDirection.stepZ
        branchLength--
      }

      val trunkPos = BlockPos(currentX, startY + currentHeight, currentZ)

      if (level.isStateAtPosition(trunkPos,{ state: BlockState -> state.isAir || state.canBeReplaced() || state.`is`(BlockTags.LEAVES) })) {
        // Check if we're on the second-to-last layer
        val isSecondToLastLayer = currentHeight == height - 2
        val randomChanceOfHeart = ModCommonConfig.CREAKING_HEART_CHANCE?.get() ?: 0.15
        val randomLogIndex = if(random.nextDouble() <= randomChanceOfHeart) random.nextInt(4) else -1

        val logBlock = if (isSecondToLastLayer && randomLogIndex == 0) CreakingHeartBlock.getNaturalState() else config.trunkProvider.getState(random, trunkPos)
        blockConsumer.accept(trunkPos, logBlock)

        val logBlockEast = if (isSecondToLastLayer && randomLogIndex == 1) CreakingHeartBlock.getNaturalState() else config.trunkProvider.getState(random, trunkPos.east())
        blockConsumer.accept(trunkPos.east(), logBlockEast)

        val logBlockSouth = if (isSecondToLastLayer && randomLogIndex == 2) CreakingHeartBlock.getNaturalState() else config.trunkProvider.getState(random, trunkPos.south())
        blockConsumer.accept(trunkPos.south(), logBlockSouth)

        val logBlockDiagonal = if (isSecondToLastLayer && randomLogIndex == 3) CreakingHeartBlock.getNaturalState() else config.trunkProvider.getState(random, trunkPos.east().south())
        blockConsumer.accept(trunkPos.east().south(), logBlockDiagonal)
      }
    }

    foliageAttachments.add(FoliagePlacer.FoliageAttachment(BlockPos(currentX, topY, currentZ), 0, true))


    // Side branches
    for (offsetX in -1..2) {
      for (offsetZ in -1..2) {
        if ((offsetX < 0 || offsetX > 1 || offsetZ < 0 || offsetZ > 1) && random.nextInt(3) <= 0) {
          val branchHeight = random.nextInt(3) + 2
          for (branchStep in 0 until branchHeight) {
            placeLog(level, blockConsumer, random, BlockPos(startX + offsetX, topY - branchStep - 1, startZ + offsetZ), config)
          }
          foliageAttachments.add(FoliagePlacer.FoliageAttachment(BlockPos(currentX + offsetX, topY, currentZ + offsetZ), 0, false))
        }
      }
    }

    return foliageAttachments
  }

  override fun type(): TrunkPlacerType<*> {
    return ModContent.PALE_OAK_HEART_TRUNK_PLACER.get()
  }
}
