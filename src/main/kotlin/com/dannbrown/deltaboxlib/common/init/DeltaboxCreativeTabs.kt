package com.dannbrown.deltaboxlib.common.init

import com.dannbrown.deltaboxlib.common.DeltaboxLibCommon
import com.dannbrown.deltaboxlib.platform.registrate.generators.creativeTabs.CreativeTabsUtil
import net.minecraft.world.item.ItemStack

object DeltaboxCreativeTabs {
  init {
    DeltaboxLibCommon.REGISTRATE.creativeTab("deltaboxlib", { ItemStack(DeltaboxItems.BEAN_POD.get()) }, { p, o -> CreativeTabsUtil.displayAll(DeltaboxLibCommon.REGISTRATE, p, o)})
  }

  fun register() {
    // init
  }
}