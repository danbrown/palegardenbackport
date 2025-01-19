package com.dannbrown.deltaboxlib.common.init

import com.dannbrown.deltaboxlib.common.DeltaboxLibCommon
import com.dannbrown.deltaboxlib.platform.util.DeltaboxUtil
import net.minecraft.tags.BlockTags

object DeltaboxTags {
  init {
    DeltaboxLibCommon.REGISTRATE.blockTags(BlockTags.DIRT, {DeltaboxBlocks.ADAMANTIUM_BLOCK.get()}, {DeltaboxBlocks.FLAMMABLE_SAND.get()})
    DeltaboxLibCommon.REGISTRATE.blockTags(BlockTags.DIRT, BlockTags.SAND)
  }
  object ITEM {
    val EXCLUDE_FROM_CREATIVE = DeltaboxUtil.TAGS.deltaboxItemTag("exclude_from_creative")
  }

  fun register() {
    // init class
  }
}