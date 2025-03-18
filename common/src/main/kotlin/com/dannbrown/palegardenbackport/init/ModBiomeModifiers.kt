package com.dannbrown.palegardenbackport.init

import com.dannbrown.palegardenbackport.init.ModContent.REGISTRATE
import net.minecraft.world.level.levelgen.GenerationStep

object ModBiomeModifiers {
  val ADD_PALE_OAK =
    REGISTRATE.biomeModifier(
      "add_pale_oak_tree",
      ModTags.HAS_PALE_OAK,
      ModPlacedFeatures.PALE_OAK_HEART_PLACED,
      GenerationStep.Decoration.VEGETAL_DECORATION
    )

  fun register() {
    // init
  }
}