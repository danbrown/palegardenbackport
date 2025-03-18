package com.dannbrown.palegardenbackport.init

import com.dannbrown.palegardenbackport.init.ModContent.REGISTRATE

object ModLang {
  val LANGS = REGISTRATE.langs()
    .addRawLang("trim_material.${ModContent.MOD_ID}.resin_clump", "Resin Clump Material")
    .biome("pale_garden", "Pale Garden")

  fun register() {
    // init
  }
}