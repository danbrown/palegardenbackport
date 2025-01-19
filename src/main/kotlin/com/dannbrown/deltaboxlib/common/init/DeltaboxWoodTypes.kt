package com.dannbrown.deltaboxlib.common.init

import com.dannbrown.deltaboxlib.common.DeltaboxLibCommon
import net.minecraft.world.level.block.state.properties.BlockSetType
import net.minecraft.world.level.block.state.properties.WoodType

object DeltaboxWoodTypes {
  val EBONY_SET: BlockSetType = BlockSetType.register(BlockSetType(DeltaboxLibCommon.MOD_ID + ":ebony"))
  val EBONY: WoodType = WoodType.register(WoodType(DeltaboxLibCommon.MOD_ID + ":ebony", EBONY_SET))

  fun register() {
    // init class
  }
}