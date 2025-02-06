package com.dannbrown.palegardenbackport.common.init

import com.dannbrown.palegardenbackport.common.ModCommon
import com.dannbrown.deltaboxlib.platform.registrate.generators.item.ItemGenerator
import com.dannbrown.deltaboxlib.platform.util.DeltaboxUtil
import net.minecraft.world.item.Item

object ModItems {
  val ITEMS = ItemGenerator(ModCommon.REGISTRATE)

  val MOD_ICON = ITEMS.create<Item>("mod_icon").register()

  fun register() {
    DeltaboxUtil.logInfo("Registering items...")
  }
}