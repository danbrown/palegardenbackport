package com.dannbrown.deltaboxlib.platform.registrate.generators.worldgen

import net.minecraft.data.worldgen.BootstapContext
import net.minecraft.world.level.levelgen.placement.PlacedFeature

abstract class AbstractPlacedFeaturesGen {
  abstract val modId: String


  abstract fun bootstrap(context: BootstapContext<PlacedFeature>)

}