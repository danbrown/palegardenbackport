package com.dannbrown.palegardenbackport.content.treeDecorator

import com.dannbrown.palegardenbackport.ModContent
import com.mojang.serialization.Codec
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction.Plane
import net.minecraft.world.level.block.MultifaceBlock
import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecorator
import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecoratorType

class ResinTreeDecorator(private val probability: Float) : TreeDecorator() {
  override fun type(): TreeDecoratorType<*> {
    return ModContent.RESIN_DECORATOR.get()
  }

  override fun place(pContext: Context) {
    val randomSource = pContext.random()
    val logPosList: List<BlockPos> = pContext.logs()
    val firstY = logPosList[0].y
    logPosList.stream()
      .forEach { pos: BlockPos ->
        val horizontalDirections = Plane.HORIZONTAL.iterator()
        while (horizontalDirections.hasNext()) {
          val direction = horizontalDirections.next()
          if (!(randomSource.nextFloat() >= this.probability)) {
            val direction1 = direction.opposite
            val offsetPos = pos.relative(direction)
            if (pContext.isAir(offsetPos)) {
              val blockState = ModContent.RESIN_CLUMP.get().defaultBlockState()
                .setValue(MultifaceBlock.getFaceProperty(direction1), true)
              pContext.setBlock(offsetPos, blockState)
            }
          }
        }
      }
  }

  companion object {
    val CODEC: Codec<ResinTreeDecorator> = Codec.floatRange(0.0f, 1.0f)
      .fieldOf("probability")
      .xmap({ prob: Float -> ResinTreeDecorator(prob) }, { decorator: ResinTreeDecorator -> decorator.probability })
      .codec()
  }
}
