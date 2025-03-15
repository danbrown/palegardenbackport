package com.dannbrown.palegardenbackport.init

import com.dannbrown.deltaboxlib.init.DeltaboxTags
import com.dannbrown.palegardenbackport.init.ModContent.REGISTRATE
import net.minecraft.world.item.Item

object ModItems {
  val MOD_ICON = REGISTRATE.item<Item>("mod_icon").itemTags(DeltaboxTags.ITEM.EXCLUDE_FROM_CREATIVE).register()
  val RESIN_BRICK = REGISTRATE.item<Item>("resin_brick").register()

  fun register() {
    // init
  }
}