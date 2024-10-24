package com.dannbrown.palegardenbackport.content.treeDecorator

import com.dannbrown.palegardenbackport.ModContent
import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.Util
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.util.ExtraCodecs
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider
import net.minecraft.world.level.levelgen.feature.treedecorators.AttachedToLeavesDecorator
import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecoratorType

class PaleOakVineDecorator(
  p_225988_: Float,
  p_225989_: Int,
  p_225990_: Int,
  p_225991_: BlockStateProvider,
  p_225992_: Int,
  p_225993_: MutableList<Direction>,
  private val tipBlockProvider: BlockStateProvider // New parameter for tip block provider
) : AttachedToLeavesDecorator(p_225988_, p_225989_, p_225990_, p_225991_, p_225992_, p_225993_) {

  override fun type(): TreeDecoratorType<*> {
    return ModContent.VINE_DECORATOR.get()
  }

  companion object {
    val CODEC: Codec<PaleOakVineDecorator> = RecordCodecBuilder.create { p_225996_: RecordCodecBuilder.Instance<PaleOakVineDecorator> ->
      p_225996_.group(
        Codec.floatRange(0.0f, 1.0f)
          .fieldOf("probability")
          .forGetter { p_226014_: PaleOakVineDecorator -> p_226014_.probability },
        Codec.intRange(0, 16)
          .fieldOf("exclusion_radius_xz")
          .forGetter { p_226012_: PaleOakVineDecorator -> p_226012_.exclusionRadiusXZ },
        Codec.intRange(0, 16)
          .fieldOf("exclusion_radius_y")
          .forGetter { p_226010_: PaleOakVineDecorator -> p_226010_.exclusionRadiusY },
        BlockStateProvider.CODEC.fieldOf("block_provider")
          .forGetter { p_226008_: PaleOakVineDecorator -> p_226008_.blockProvider },
        Codec.intRange(1, 16)
          .fieldOf("required_empty_blocks")
          .forGetter { p_226006_: PaleOakVineDecorator -> p_226006_.requiredEmptyBlocks },
        ExtraCodecs.nonEmptyList(Direction.CODEC.listOf())
          .fieldOf("directions")
          .forGetter { p_225998_: PaleOakVineDecorator -> p_225998_.directions },
        BlockStateProvider.CODEC.fieldOf("tip_block_provider") // New codec for the tip block provider
          .forGetter { p_225998_: PaleOakVineDecorator -> p_225998_.tipBlockProvider }
      ).apply(p_225996_) {
          p_225988_: Float,
          p_225989_: Int,
          p_225990_: Int,
          p_225991_: BlockStateProvider,
          p_225992_: Int,
          p_225993_: MutableList<Direction>,
          p_225994_: BlockStateProvider -> // New parameter in apply
        PaleOakVineDecorator(
          p_225988_, p_225989_, p_225990_, p_225991_, p_225992_, p_225993_, p_225994_
        )
      }
    }
  }

  override fun place(pContext: Context) {
    val set: MutableSet<BlockPos> = HashSet()
    val randomsource = pContext.random()

    for (blockpos in Util.shuffledCopy(pContext.leaves(), randomsource)) {
      val direction = Util.getRandom(this.directions, randomsource)
      val blockpos1 = blockpos.relative(direction)
      if (!set.contains(blockpos1) && randomsource.nextFloat() < this.probability && this.hasRequiredEmptyBlocks(pContext, blockpos, direction)) {
        val blockpos2 = blockpos1.offset(-this.exclusionRadiusXZ, -this.exclusionRadiusY, -this.exclusionRadiusXZ)
        val blockpos3 = blockpos1.offset(this.exclusionRadiusXZ, this.exclusionRadiusY, this.exclusionRadiusXZ)

        for (blockpos4 in BlockPos.betweenClosed(blockpos2, blockpos3)) {
          set.add(blockpos4.immutable())
        }

        // Determine how many blocks to place (1, 2, or 3)
        val stackCount = randomsource.nextInt(3) + 1 // Randomly pick between 1 and 3

        // Place the determined number of blocks at the target position directly below each other
        for (i in 0 until stackCount) {
          val stackedPos = blockpos1.below(i) // Stack below the original position
          pContext.setBlock(stackedPos, blockProvider.getState(randomsource, stackedPos))
        }

        // Place the tip block on top of the last stacked block
        val tipPos = blockpos1.below(stackCount) // Position for the tip block
        pContext.setBlock(tipPos, tipBlockProvider.getState(randomsource, tipPos)) // Use the new provider for the tip block
      }
    }
  }

  private fun hasRequiredEmptyBlocks(pContext: Context, pPos: BlockPos, pDirection: Direction): Boolean {
    for (i in 1..this.requiredEmptyBlocks) {
      val blockpos = pPos.relative(pDirection, i)
      if (!pContext.isAir(blockpos)) {
        return false
      }
    }

    return true
  }
}
