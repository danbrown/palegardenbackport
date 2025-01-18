package com.dannbrown.deltaboxlib.platform.registrate.generators.worldgen

import net.minecraft.core.Holder
import net.minecraft.core.HolderSet
import net.minecraft.core.registries.Registries
import net.minecraft.resources.ResourceKey
import net.minecraft.tags.TagKey
import net.minecraft.world.level.biome.Biome
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature
import net.minecraft.world.level.levelgen.placement.PlacedFeature
/*? if >=1.21 {*/
/*import net.minecraft.data.worldgen.BootstrapContext
*//*?} else {*/
import net.minecraft.data.worldgen.BootstapContext as BootstrapContext
/*?}*/

abstract class AbstractWorldgenUtil {
  fun lookupConfiguredFeature(context: BootstrapContext<*>, configuredFeature: ResourceKey<ConfiguredFeature<*, *>>): Holder<ConfiguredFeature<*, *>> {
    return context.lookup(Registries.CONFIGURED_FEATURE).getOrThrow(configuredFeature)
  }
  fun lookupPlacedFeature(context: BootstrapContext<*>, placedFeature: ResourceKey<PlacedFeature>): Holder<PlacedFeature> {
    return context.lookup(Registries.PLACED_FEATURE).getOrThrow(placedFeature)
  }
  fun lookupBiome(context: BootstrapContext<*>, biome: ResourceKey<Biome>): Holder<Biome> {
    return context.lookup(Registries.BIOME).getOrThrow(biome)
  }
  fun lookupBiomeNamed(context: BootstrapContext<*>, biome: TagKey<Biome>): HolderSet.Named<Biome> {
    return context.lookup(Registries.BIOME).getOrThrow(biome)
  }
  fun lookupPlacedFeatureDirect(context: BootstrapContext<*>, placedFeature: ResourceKey<PlacedFeature>): HolderSet.Direct<PlacedFeature> {
    return HolderSet.direct(context.lookup(Registries.PLACED_FEATURE).getOrThrow(placedFeature))
  }
}