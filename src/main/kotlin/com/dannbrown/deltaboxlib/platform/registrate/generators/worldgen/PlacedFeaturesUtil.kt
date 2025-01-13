package com.dannbrown.deltaboxlib.platform.registrate.generators.worldgen

import com.dannbrown.deltaboxlib.platform.util.DeltaboxUtil
import net.minecraft.core.Holder
import net.minecraft.core.registries.Registries
import net.minecraft.data.worldgen.BootstapContext
import net.minecraft.data.worldgen.placement.PlacementUtils
import net.minecraft.resources.ResourceKey
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature
import net.minecraft.world.level.levelgen.placement.BiomeFilter
import net.minecraft.world.level.levelgen.placement.CountPlacement
import net.minecraft.world.level.levelgen.placement.InSquarePlacement
import net.minecraft.world.level.levelgen.placement.PlacedFeature
import net.minecraft.world.level.levelgen.placement.PlacementModifier
import net.minecraft.world.level.levelgen.placement.RarityFilter

object PlacedFeaturesUtil {
  fun registerKey(name: String, modId: String): ResourceKey<PlacedFeature> {
    return ResourceKey.create(Registries.PLACED_FEATURE, DeltaboxUtil.resourceLocation(modId, name))
  }
  fun register(
    context: BootstapContext<PlacedFeature>,
    key: ResourceKey<PlacedFeature>,
    configuration: Holder<ConfiguredFeature<*, *>>,
    modifiers: List<PlacementModifier>
  ) {
    context.register(key, PlacedFeature(configuration, modifiers))
  }

  // Lookup Functions
  fun lookup(context: BootstapContext<PlacedFeature>, configuredFeature: ResourceKey<ConfiguredFeature<*, *>>): Holder<ConfiguredFeature<*, *>> {
    return context.lookup(Registries.CONFIGURED_FEATURE).getOrThrow(configuredFeature)
  }


  // Utility Functions
  fun orePlacement(placementModifier: PlacementModifier, modifier: PlacementModifier): List<PlacementModifier> {
    return listOf(placementModifier, InSquarePlacement.spread(), modifier, BiomeFilter.biome())
  }

  fun commonOrePlacement(pCount: Int, pHeightRange: PlacementModifier): List<PlacementModifier> {
    return orePlacement(CountPlacement.of(pCount), pHeightRange)
  }

  fun rareOrePlacement(pChance: Int, pHeightRange: PlacementModifier): List<PlacementModifier> {
    return orePlacement(RarityFilter.onAverageOnceEvery(pChance), pHeightRange)
  }

  private fun wildCropPlaced(chance: Int): List<PlacementModifier> {
    return listOf(RarityFilter.onAverageOnceEvery(chance), InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP, BiomeFilter.biome())
  }
}