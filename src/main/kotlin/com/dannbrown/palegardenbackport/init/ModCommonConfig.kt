package com.dannbrown.palegardenbackport.init

import net.minecraftforge.common.ForgeConfigSpec

object ModCommonConfig {
  val BUILDER: ForgeConfigSpec.Builder = ForgeConfigSpec.Builder()
  var SPEC: ForgeConfigSpec? = null

  var CREAKING_HEART_CHANCE: ForgeConfigSpec.ConfigValue<Double>? = null
  var CREAKING_FREEZE_DISTANCE: ForgeConfigSpec.ConfigValue<Double>? = null
  var CREAKING_DISTANCE_TO_HEART: ForgeConfigSpec.ConfigValue<Double>? = null
  var PLAYER_FOV_ANGLE: ForgeConfigSpec.ConfigValue<Double>? = null

  init {
    CREAKING_HEART_CHANCE = BUILDER.comment("The chance of a Pale Oak tree containing a Creaking Heart, 1 is 100%")
      .define("CreakingHeartChance", 0.10)
    CREAKING_FREEZE_DISTANCE = BUILDER.comment("The distance at which Creaking entities freeze when looked at")
      .define("CreakingFreezeDistance", 128.0)
    CREAKING_DISTANCE_TO_HEART = BUILDER.comment("The distance at which Creaking entities freeze when looked at")
      .define("CreakingDistanceToHeart", 32.0)
    PLAYER_FOV_ANGLE = BUILDER.comment("The field of view angle of the player to detect Creaking entities")
      .define("PlayerFovAngle", 45.0)

    SPEC = BUILDER.build();
  }
}