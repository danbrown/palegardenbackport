package com.dannbrown.palegardenbackport.init

import com.dannbrown.palegardenbackport.content.worldgen.biome.PaleGardenBiome
import com.dannbrown.palegardenbackport.init.ModContent.REGISTRATE

object ModBiomes {
  val PALE_GARDEN = REGISTRATE.biome(PaleGardenBiome)
  fun register() {
    // init
  }
}