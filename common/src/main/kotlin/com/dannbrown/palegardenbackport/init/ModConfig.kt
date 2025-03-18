package com.dannbrown.palegardenbackport.init

import com.dannbrown.palegardenbackport.init.ModContent.REGISTRATE

object ModConfig {
  val CREAKING_HEART_CHANCE = REGISTRATE.configFloat(
    "creaking_heart_chance", 0.10f, "The chance of a Pale Oak tree containing a Creaking Heart, 1 is 100%"
  )
  val REQUIRE_NATURAL = REGISTRATE.configBoolean(
    "require_natural", true, "Whether the Creaking Heart requires a natural dimensionType to spawn"
  )
  val PALE_GARDEN_ENABLED =
    REGISTRATE.configBoolean("pale_garden_enabled", true, "Whether the Pale Garden biome is enabled")
  val CREAKING_FREEZE_DISTANCE =
    REGISTRATE.configFloat(
      "creaking_freeze_distance",
      128.0f,
      "The distance at which Creaking entities freeze when looked at"
    )
  val CREAKING_DISTANCE_TO_HEART =
    REGISTRATE.configFloat(
      "creaking_distance_to_heart",
      32.0f,
      "The distance at which Creaking despawn if too far from heart"
    )
  val PLAYER_FOV_ANGLE =
    REGISTRATE.configFloat(
      "player_fov_angle",
      45.0f,
      "The field of view angle of the player to detect Creaking entities"
    )

  fun register() {
    REGISTRATE.freezeConfig()
  }
}