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

    // Sounds
    ModContent.REGISTRATE.addSoundLang("resin_brick_break", "Resin Brick Break")
    ModContent.REGISTRATE.addSoundLang("resin_brick_step", "Resin Brick Step")
    ModContent.REGISTRATE.addSoundLang("resin_brick_fall", "Resin Brick Fall")
    ModContent.REGISTRATE.addSoundLang("resin_brick_place", "Resin Brick Place")
    ModContent.REGISTRATE.addSoundLang("resin_brick_hit", "Resin Brick Hit")
    ModContent.REGISTRATE.addSoundLang("block_of_resin_break", "Resin Break")
    ModContent.REGISTRATE.addSoundLang("block_of_resin_step", "Resin Step")
    ModContent.REGISTRATE.addSoundLang("block_of_resin_fall", "Resin Fall")
    ModContent.REGISTRATE.addSoundLang("block_of_resin_place", "Resin Place")
    ModContent.REGISTRATE.addSoundLang("block_of_resin_hit", "Resin Hit")
  }
}
