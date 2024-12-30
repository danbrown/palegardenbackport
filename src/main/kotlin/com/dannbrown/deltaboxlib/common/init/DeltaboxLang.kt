package com.dannbrown.deltaboxlib.common.init

import com.dannbrown.deltaboxlib.common.DeltaboxLibCommon
import com.dannbrown.deltaboxlib.platform.registrate.types.IDeltaboxLang
import com.tterrag.registrate.providers.RegistrateLangProvider

object DeltaboxLang: IDeltaboxLang(DeltaboxLibCommon.MOD_ID) {
  override fun addLang(provider: RegistrateLangProvider) {
    addGenericTooltipLang(provider, "flint", "It's a Delta!")
  }
}