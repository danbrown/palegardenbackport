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
    ModContent.REGISTRATE.addEntityLang(
            "creaking",
            "Creaking"
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
    ModContent.REGISTRATE.addRawLang("sounds.${ModContent.MOD_ID}.resin_bricks_break", "Resin Bricks Break")
    ModContent.REGISTRATE.addRawLang("sounds.${ModContent.MOD_ID}.resin_bricks_step", "Resin Bricks Step")
    ModContent.REGISTRATE.addRawLang("sounds.${ModContent.MOD_ID}.resin_bricks_fall", "Resin Bricks Fall")
    ModContent.REGISTRATE.addRawLang("sounds.${ModContent.MOD_ID}.resin_bricks_place", "Resin Bricks Place")
    ModContent.REGISTRATE.addRawLang("sounds.${ModContent.MOD_ID}.resin_bricks_hit", "Resin Brick Hit")
    ModContent.REGISTRATE.addRawLang("sounds.${ModContent.MOD_ID}.block_of_resin_break", "Resin Break")
    ModContent.REGISTRATE.addRawLang("sounds.${ModContent.MOD_ID}.block_of_resin_step", "Resin Step")
    ModContent.REGISTRATE.addRawLang("sounds.${ModContent.MOD_ID}.block_of_resin_fall", "Resin Fall")
    ModContent.REGISTRATE.addRawLang("sounds.${ModContent.MOD_ID}.block_of_resin_place", "Resin Place")
    ModContent.REGISTRATE.addRawLang("sounds.${ModContent.MOD_ID}.block_of_resin_hit", "Resin Hit")

    ModContent.REGISTRATE.addRawLang("sounds.${ModContent.MOD_ID}.creaking_ambient", "Creaking Ambient")
    ModContent.REGISTRATE.addRawLang("sounds.${ModContent.MOD_ID}.creaking_activate", "Creaking Activate")
    ModContent.REGISTRATE.addRawLang("sounds.${ModContent.MOD_ID}.creaking_deactivate", "Creaking Deactivate")
    ModContent.REGISTRATE.addRawLang("sounds.${ModContent.MOD_ID}.creaking_attack", "Creaking Attack")
    ModContent.REGISTRATE.addRawLang("sounds.${ModContent.MOD_ID}.creaking_death", "Creaking Death")
    ModContent.REGISTRATE.addRawLang("sounds.${ModContent.MOD_ID}.creaking_step", "Creaking Step")
    ModContent.REGISTRATE.addRawLang("sounds.${ModContent.MOD_ID}.creaking_freeze", "Creaking Freeze")
    ModContent.REGISTRATE.addRawLang("sounds.${ModContent.MOD_ID}.creaking_unfreeze", "Creaking Unfreeze")
    ModContent.REGISTRATE.addRawLang("sounds.${ModContent.MOD_ID}.creaking_spawn", "Creaking Spawn")
    ModContent.REGISTRATE.addRawLang("sounds.${ModContent.MOD_ID}.creaking_hit", "Creaking Hit")
    ModContent.REGISTRATE.addRawLang("sounds.${ModContent.MOD_ID}.creaking_twitch", "Creaking Twitch")

    ModContent.REGISTRATE.addRawLang("sounds.${ModContent.MOD_ID}.creaking_heart_break", "Creaking Heart Break")
    ModContent.REGISTRATE.addRawLang("sounds.${ModContent.MOD_ID}.creaking_heart_fall", "Creaking Heart Fall")
    ModContent.REGISTRATE.addRawLang("sounds.${ModContent.MOD_ID}.creaking_heart_hit", "Creaking Heart Hit")
    ModContent.REGISTRATE.addRawLang("sounds.${ModContent.MOD_ID}.creaking_heart_hurt", "Creaking Heart Hurt")
    ModContent.REGISTRATE.addRawLang("sounds.${ModContent.MOD_ID}.creaking_heart_place", "Creaking Heart Place")
    ModContent.REGISTRATE.addRawLang("sounds.${ModContent.MOD_ID}.creaking_heart_step", "Creaking Heart Step")
    ModContent.REGISTRATE.addRawLang("sounds.${ModContent.MOD_ID}.creaking_heart_idle", "Creaking Heart Idle")

    ModContent.REGISTRATE.addRawLang("sounds.${ModContent.MOD_ID}.eyeblossom_idle", "Eyeblossom Idle")
    ModContent.REGISTRATE.addRawLang("sounds.${ModContent.MOD_ID}.eyeblossom_open", "Eyeblossom Open")
    ModContent.REGISTRATE.addRawLang("sounds.${ModContent.MOD_ID}.eyeblossom_close", "Eyeblossom Close")
    ModContent.REGISTRATE.addRawLang("sounds.${ModContent.MOD_ID}.eyeblossom_open_long", "Eyeblossom Open Long")
    ModContent.REGISTRATE.addRawLang("sounds.${ModContent.MOD_ID}.eyeblossom_close_long", "Eyeblossom Close Long")
  }
}
