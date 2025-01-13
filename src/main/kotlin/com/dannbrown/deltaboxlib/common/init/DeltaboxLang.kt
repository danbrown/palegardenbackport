package com.dannbrown.deltaboxlib.common.init

import com.dannbrown.deltaboxlib.common.DeltaboxLibCommon

object DeltaboxLang {
  init {
    DeltaboxLibCommon.REGISTRATE.addGenericTooltipLang("flint", "It's a Delta!")
    DeltaboxLibCommon.REGISTRATE.addCreativeTabLang("deltaboxlib", "Deltabox Lib")
  }

  fun register(){
    // init class
  }
}