package com.dannbrown.palegardenbackport.init

import com.dannbrown.deltaboxlib.registrate.util.DeltaboxUtil
import com.dannbrown.palegardenbackport.content.worldgen.biome.PaleGardenBiome
import com.dannbrown.palegardenbackport.init.ModContent.REGISTRATE

object ModTags {
  val HAS_PALE_OAK = DeltaboxUtil.TAGS.deltaboxBiomeTag("has_pale_oak")
  val TAGS = REGISTRATE
    .biomeTags(HAS_PALE_OAK)
    .add(PaleGardenBiome.BIOME_KEY)

  fun register() {
    // init
  }
}