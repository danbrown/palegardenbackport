package com.dannbrown.deltaboxlib.common.init

import com.dannbrown.deltaboxlib.common.DeltaboxLibCommon
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.levelgen.feature.Feature

object DeltaboxConfiguredFeatures {
  val TEST_FEATURE = DeltaboxLibCommon.REGISTRATE.configuredFeature("test_feature", { k, c, u ->
    u.register(c, k, Feature.TREE,
      u.createStraightFruitBlobTree(Blocks.OAK_LOG, DeltaboxBlocks.ACAI_LEAVES.get(), DeltaboxBlocks.CROP_LEAVES.get(), 12, 4, 1,5, 4, 0, 2)
        .ignoreVines()
        .build()
    )
  })

  fun register() {
    // init class
  }
}