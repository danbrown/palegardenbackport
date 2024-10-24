package com.dannbrown.palegardenbackport.content.treeGrower

import com.dannbrown.palegardenbackport.datagen.worldgen.ModConfiguredFeatures
import net.minecraft.resources.ResourceKey
import net.minecraft.util.RandomSource
import net.minecraft.world.level.block.grower.AbstractMegaTreeGrower
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature

class PaleOakTreeGrower: AbstractMegaTreeGrower() {
  override fun getConfiguredFeature(pRandom: RandomSource, pHasFlowers: Boolean): ResourceKey<ConfiguredFeature<*, *>>? {
    return null
  }

  override fun getConfiguredMegaFeature(randomSource: RandomSource): ResourceKey<ConfiguredFeature<*, *>>? {
    return ModConfiguredFeatures.PALE_OAK_TREE
  }
}