package com.dannbrown.deltaboxlib.common.init

import com.dannbrown.deltaboxlib.common.DeltaboxLibCommon
import com.dannbrown.deltaboxlib.common.content.worldgen.placerType.PalmFoliagePlacer
import com.dannbrown.deltaboxlib.common.content.worldgen.placerType.CrookedTrunkPlacer

object DeltaboxPlacerTypes {
  val CROOKED_TRUNK_PLACER = DeltaboxLibCommon.REGISTRATE.trunkPlacer("crooked_trunk_placer") { CrookedTrunkPlacer.CODEC }
  val PALM_FOLIAGE_PLACER = DeltaboxLibCommon.REGISTRATE.foliagePlacer("palm_foliage_placer") { PalmFoliagePlacer.CODEC }

  fun register(){
    // init class
  }
}