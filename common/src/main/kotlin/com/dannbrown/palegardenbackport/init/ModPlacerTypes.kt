package com.dannbrown.palegardenbackport.init

import com.dannbrown.palegardenbackport.content.worldgen.placerTypes.PaleOakFoliagePlacer
import com.dannbrown.palegardenbackport.content.worldgen.placerTypes.PaleOakHeartTrunkPlacer
import com.dannbrown.palegardenbackport.content.worldgen.placerTypes.PaleOakTrunkPlacer
import com.dannbrown.palegardenbackport.content.worldgen.treeDecorator.PaleOakGroundDecorator
import com.dannbrown.palegardenbackport.content.worldgen.treeDecorator.PaleOakVineDecorator
import com.dannbrown.palegardenbackport.content.worldgen.treeDecorator.ResinTreeDecorator
import com.dannbrown.palegardenbackport.init.ModContent.REGISTRATE

object ModPlacerTypes {
  val PALE_OAK_FOLIAGE_PLACER = REGISTRATE.foliagePlacer("pale_oak") { PaleOakFoliagePlacer.CODEC }
  val PALE_OAK_TRUNK_PLACER = REGISTRATE.trunkPlacer("pale_oak") { PaleOakTrunkPlacer.CODEC }
  val PALE_OAK_HEART_TRUNK_PLACER = REGISTRATE.trunkPlacer("pale_oak_heart") { PaleOakHeartTrunkPlacer.CODEC }
  val GROUND_DECORATOR = REGISTRATE.treeDecorator("pale_oak_ground_decorator") { PaleOakGroundDecorator.CODEC }
  val VINE_DECORATOR = REGISTRATE.treeDecorator("pale_oak_vine_decorator") { PaleOakVineDecorator.CODEC }
  val RESIN_DECORATOR = REGISTRATE.treeDecorator("resin_clump_decorator") { ResinTreeDecorator.CODEC }

  fun register() {
    // init
  }
}