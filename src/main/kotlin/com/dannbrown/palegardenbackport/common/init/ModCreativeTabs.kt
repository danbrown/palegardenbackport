package com.dannbrown.palegardenbackport.common.init

import com.dannbrown.palegardenbackport.common.ModCommon
import com.dannbrown.deltaboxlib.platform.registrate.generators.creativeTabs.CreativeTabsUtil
import net.minecraft.world.item.ItemStack

object ModCreativeTabs {
  init {
    ModCommon.REGISTRATE.creativeTab(ModCommon.MOD_ID, { ItemStack(ModItems.MOD_ICON.get()) }, { p, o -> CreativeTabsUtil.displayAll(ModCommon.REGISTRATE, p, o)})
  }

  fun register() {
    // init
  }
}