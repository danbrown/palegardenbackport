package com.dannbrown.deltaboxlib.common.init

import com.dannbrown.deltaboxlib.common.DeltaboxLibCommon
import net.minecraft.data.worldgen.placement.VegetationPlacements
import net.minecraft.world.level.levelgen.placement.RarityFilter

object DeltaboxPlacedFeatures {
  val TEST_FEATURE = DeltaboxLibCommon.REGISTRATE.placedFeature("test_placed") { k, c, u ->
    u.register(c, k, u.lookup(c, DeltaboxConfiguredFeatures.TEST_FEATURE), VegetationPlacements.treePlacement(
      RarityFilter.onAverageOnceEvery(32),
      DeltaboxBlocks.LEMON_SAPLING.get(),
    ))
  }

  fun register() {
    // init class
  }
}