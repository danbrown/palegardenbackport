package com.dannbrown.deltaboxlib.platform.registrate.generators.worldgen

import com.dannbrown.deltaboxlib.platform.util.DeltaboxUtil
import net.minecraft.core.registries.Registries
import net.minecraft.data.worldgen.BootstapContext
import net.minecraft.resources.ResourceKey
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature
import net.minecraft.world.level.levelgen.feature.Feature
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration

abstract class AbstractConfiguredFeaturesGen {
  abstract val modId: String

  abstract fun bootstrap(context: BootstapContext<ConfiguredFeature<*, *>>)
}