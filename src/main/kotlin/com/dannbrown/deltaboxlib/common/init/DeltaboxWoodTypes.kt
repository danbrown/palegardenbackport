package com.dannbrown.deltaboxlib.common.init

import com.dannbrown.deltaboxlib.common.DeltaboxLibCommon
import com.dannbrown.deltaboxlib.mixin.woodType.WoodTypeMixin
import net.minecraft.world.level.block.state.properties.BlockSetType
import net.minecraft.world.level.block.state.properties.WoodType

object DeltaboxWoodTypes {
  val EBONY_SET: BlockSetType = BlockSetType(DeltaboxLibCommon.MOD_ID + ":ebony")
  val EBONY: WoodType = WoodTypeMixin.invokeRegister(WoodType(DeltaboxLibCommon.MOD_ID + ":ebony", EBONY_SET))

  fun register() {
    // init class
  }
}