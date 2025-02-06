package com.dannbrown.palegardenbackport.common.init

import com.dannbrown.deltaboxlib.common.content.worldgen.tree.DeltaboxTreeGrower
import com.dannbrown.palegardenbackport.common.ModCommon
import com.dannbrown.deltaboxlib.mixin.woodType.WoodTypeMixin
import net.minecraft.world.level.block.state.properties.BlockSetType
import net.minecraft.world.level.block.state.properties.WoodType
import java.util.*

object ModWoodTypes {
  val PALE_OAK_SET: BlockSetType = BlockSetType(ModCommon.MOD_ID + ":pale_oak")
  val PALE_OAK: WoodType = WoodTypeMixin.invokeRegister(WoodType(ModCommon.MOD_ID + ":pale_oak", PALE_OAK_SET))

//  val PALE_OAK_GROWER = DeltaboxTreeGrower("sample", Optional.of(DeltaboxConfiguredFeatures.TEST_FEATURE), Optional.of(DeltaboxConfiguredFeatures.TEST_FEATURE), Optional.of(DeltaboxConfiguredFeatures.TEST_FEATURE))

  fun register() {
    // init class
  }
}