package com.dannbrown.deltaboxlib.common.content.worldgen.tree

import net.minecraft.data.worldgen.features.TreeFeatures
import net.minecraft.resources.ResourceKey
import net.minecraft.util.RandomSource
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature
import java.util.*

class DeltaboxTreeGrower(
  private val _name: String,
  private val secondaryChance: Float,
  private val megaTree: Optional<ResourceKey<ConfiguredFeature<*, *>>>,
  private val secondaryMegaTree: Optional<ResourceKey<ConfiguredFeature<*, *>>>,
  private val tree: Optional<ResourceKey<ConfiguredFeature<*, *>>>,
  private val secondaryTree: Optional<ResourceKey<ConfiguredFeature<*, *>>>,
  private val flowers: Optional<ResourceKey<ConfiguredFeature<*, *>>>,
  private val secondaryFlowers: Optional<ResourceKey<ConfiguredFeature<*, *>>>
) {
  constructor(
    _name: String,
    megaTree: Optional<ResourceKey<ConfiguredFeature<*, *>>>,
    tree: Optional<ResourceKey<ConfiguredFeature<*, *>>>,
    flowers: Optional<ResourceKey<ConfiguredFeature<*, *>>>
  ): this(_name,
    0.0f,
    megaTree,
    Optional.empty<ResourceKey<ConfiguredFeature<*, *>>>(),
    tree,
    Optional.empty<ResourceKey<ConfiguredFeature<*, *>>>(),
    flowers,
    Optional.empty<ResourceKey<ConfiguredFeature<*, *>>>()
  )

  fun getConfiguredFeatures(randomSource: RandomSource, hasFlowers: Boolean): ResourceKey<ConfiguredFeature<*, *>>? {
    if (randomSource.nextFloat() < this.secondaryChance) {
      if (hasFlowers && secondaryFlowers.isPresent) {
        return secondaryFlowers.get()
      }
      if (secondaryTree.isPresent) {
        return secondaryTree.get()
      }
    }
    return if (hasFlowers && flowers.isPresent) flowers.get() else tree.orElse(null)
  }

  fun getConfiguredMegaFeatures(randomSource: RandomSource): ResourceKey<ConfiguredFeature<*, *>>? {
    return if (secondaryMegaTree.isPresent && randomSource.nextFloat() < this.secondaryChance) secondaryMegaTree.get() else megaTree.orElse(null)
  }


  /*? if >=1.21 {*/
  /*fun getTreeGrower(): net.minecraft.world.level.block.grower.TreeGrower {
    return net.minecraft.world.level.block.grower.TreeGrower(_name, secondaryChance, megaTree, secondaryMegaTree, tree, secondaryTree, flowers, secondaryFlowers)
  }
  *//*?} else {*/
  fun getTreeGrower(): net.minecraft.world.level.block.grower.AbstractMegaTreeGrower {
    return object : net.minecraft.world.level.block.grower.AbstractMegaTreeGrower() {
      override fun getConfiguredFeature(randomSource: RandomSource, hasFlowers: Boolean): ResourceKey<ConfiguredFeature<*, *>>? {
        return getConfiguredFeatures(randomSource, hasFlowers)
      }

      override fun getConfiguredMegaFeature(randomSource: RandomSource): ResourceKey<ConfiguredFeature<*, *>>? {
        return getConfiguredMegaFeatures(randomSource)
      }
    }
  }
  /*?}*/

  companion object {
    val SAMPLE: DeltaboxTreeGrower = DeltaboxTreeGrower("sample", Optional.of(TreeFeatures.MEGA_SPRUCE), Optional.of(TreeFeatures.ACACIA), Optional.of(TreeFeatures.CHERRY))
  }
}