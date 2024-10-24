package com.dannbrown.palegardenbackport.content.block

import com.dannbrown.palegardenbackport.datagen.worldgen.ModConfiguredFeatures
import net.minecraft.core.BlockPos
import net.minecraft.core.Holder
import net.minecraft.core.Registry
import net.minecraft.core.registries.Registries
import net.minecraft.data.worldgen.features.CaveFeatures
import net.minecraft.server.level.ServerLevel
import net.minecraft.util.RandomSource
import net.minecraft.world.level.block.MossBlock
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature

class PaleMossBlock(props: Properties): MossBlock(props) {

  override fun performBonemeal(pLevel: ServerLevel, pRandom: RandomSource, pPos: BlockPos, pState: BlockState) {
    pLevel.registryAccess()
      .registry(Registries.CONFIGURED_FEATURE)
      .flatMap { configuredFeatures: Registry<ConfiguredFeature<*, *>> ->
        configuredFeatures.getHolder(ModConfiguredFeatures.PALE_MOSS_PATCH_BONEMEAL)
      }
      .ifPresent { reference: Holder.Reference<ConfiguredFeature<*, *>> ->
        reference.value().place(pLevel, pLevel.chunkSource.generator, pRandom, pPos.above())
      }
  }
}