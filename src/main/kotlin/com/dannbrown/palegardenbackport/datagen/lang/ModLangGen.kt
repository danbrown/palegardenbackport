package com.dannbrown.palegardenbackport.datagen.lang

import com.dannbrown.palegardenbackport.ModContent

object ModLangGen {
  fun addStaticLangs(doRun: Boolean) {
    if (!doRun) return // avoid running in the server-side

    ModContent.REGISTRATE.addEntityLang(
            "${ModContent.WOOD_NAME}_boat",
            "Pale Oak Boat"
    )
    ModContent.REGISTRATE.addEntityLang(
            "${ModContent.WOOD_NAME}_chest_boat",
            "Pale Oak Chest Boat"
    )
    ModContent.REGISTRATE.addCreativeTabLang(
            "${ModContent.MOD_ID}_tab",
            "Pale Oak Backport"
    )
    ModContent.REGISTRATE.addBiomeLang(
            "pale_garden",
            "Pale Garden"
    )
    ModContent.REGISTRATE.addRawLang("trim_material.${ModContent.MOD_ID}.resin_clump", "Resin Clump Material")
  }
}
