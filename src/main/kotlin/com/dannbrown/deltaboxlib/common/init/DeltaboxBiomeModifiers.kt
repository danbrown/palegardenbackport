package com.dannbrown.deltaboxlib.common.init

import com.dannbrown.deltaboxlib.common.DeltaboxLibCommon
import net.minecraft.tags.BiomeTags
import net.minecraft.world.level.levelgen.GenerationStep

object DeltaboxBiomeModifiers {
  init {
    DeltaboxLibCommon.REGISTRATE.biomeModifier("add_test_to_plains", BiomeTags.IS_OVERWORLD, DeltaboxPlacedFeatures.TEST_FEATURE, GenerationStep.Decoration.VEGETAL_DECORATION)
  }

  fun register() {
    // init class
  }
}