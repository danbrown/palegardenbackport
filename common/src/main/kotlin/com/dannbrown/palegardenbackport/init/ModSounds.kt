package com.dannbrown.palegardenbackport.init

import com.dannbrown.deltaboxlib.registrate.util.SoundTypeSupplier
import com.dannbrown.palegardenbackport.init.ModContent.REGISTRATE

object ModSounds {
  // Resin
  val RESIN_BRICK_BREAK = REGISTRATE.soundEvent("resin_bricks_break", 1)
  val RESIN_BRICK_STEP = REGISTRATE.soundEvent("resin_bricks_step", 5)
  val RESIN_BRICK_FALL = REGISTRATE.soundEvent("resin_bricks_fall", 1)
  val RESIN_BRICK_PLACE = REGISTRATE.soundEvent("resin_bricks_place", 4)
  val RESIN_BRICK_HIT = REGISTRATE.soundEvent("resin_bricks_hit", 5)

  val BLOCK_OF_RESIN_BREAK = REGISTRATE.soundEvent("block_of_resin_break", 5)
  val BLOCK_OF_RESIN_STEP = REGISTRATE.soundEvent("block_of_resin_step", 5)
  val BLOCK_OF_RESIN_FALL = REGISTRATE.soundEvent("block_of_resin_fall", 1)
  val BLOCK_OF_RESIN_PLACE = REGISTRATE.soundEvent("block_of_resin_place", 4)
  val BLOCK_OF_RESIN_HIT = REGISTRATE.soundEvent("block_of_resin_hit", 5)

  val RESIN_BRICK_SOUNDS = SoundTypeSupplier(
    1f,
    1f,
    RESIN_BRICK_BREAK,
    RESIN_BRICK_STEP,
    RESIN_BRICK_PLACE,
    RESIN_BRICK_HIT,
    RESIN_BRICK_FALL
  )
  val BLOCK_OF_RESIN_SOUNDS = SoundTypeSupplier(
    1f,
    1f,
    BLOCK_OF_RESIN_BREAK,
    BLOCK_OF_RESIN_STEP,
    BLOCK_OF_RESIN_PLACE,
    BLOCK_OF_RESIN_HIT,
    BLOCK_OF_RESIN_FALL
  )

  // Creaking
  val CREAKING_AMBIENT = REGISTRATE.soundEvent("creaking_ambient", 6)
  val CREAKING_ACTIVATE = REGISTRATE.soundEvent("creaking_activate", 1)
  val CREAKING_DEACTIVATE = REGISTRATE.soundEvent("creaking_deactivate", 1)
  val CREAKING_ATTACK = REGISTRATE.soundEvent("creaking_attack", 4)
  val CREAKING_DEATH = REGISTRATE.soundEvent("creaking_death", 1)
  val CREAKING_STEP = REGISTRATE.soundEvent("creaking_step", 5)
  val CREAKING_FREEZE = REGISTRATE.soundEvent("creaking_freeze", 4)
  val CREAKING_UNFREEZE = REGISTRATE.soundEvent("creaking_unfreeze", 3)
  val CREAKING_SPAWN = REGISTRATE.soundEvent("creaking_spawn", 1)
  val CREAKING_HIT = REGISTRATE.soundEvent("creaking_hit", 4)
  val CREAKING_TWITCH = REGISTRATE.soundEvent("creaking_twitch", 1)

  // Creaking Heart
  val CREAKING_HEART_BREAK = REGISTRATE.soundEvent("creaking_heart_break", 1)
  val CREAKING_HEART_FALL = REGISTRATE.soundEvent("creaking_heart_fall", 1)
  val CREAKING_HEART_HIT = REGISTRATE.soundEvent("creaking_heart_hit", 5)
  val CREAKING_HEART_HURT = REGISTRATE.soundEvent("creaking_heart_hurt", 7)
  val CREAKING_HEART_PLACE = REGISTRATE.soundEvent("creaking_heart_place", 4)
  val CREAKING_HEART_STEP = REGISTRATE.soundEvent("creaking_heart_step", 6)
  val CREAKING_HEART_IDLE = REGISTRATE.soundEvent("creaking_heart_idle", 4)

  val CREAKING_HEART_SOUNDS = SoundTypeSupplier(
    1f,
    1f,
    CREAKING_HEART_BREAK,
    CREAKING_HEART_STEP,
    CREAKING_HEART_PLACE,
    CREAKING_HEART_HIT,
    CREAKING_HEART_FALL
  )

  // Eyeblossom
  val EYEBLOSSOM_IDLE = REGISTRATE.soundEvent("eyeblossom_idle", 6)
  val EYEBLOSSOM_OPEN = REGISTRATE.soundEvent("eyeblossom_open", 4)
  val EYEBLOSSOM_CLOSE = REGISTRATE.soundEvent("eyeblossom_close", 3)
  val EYEBLOSSOM_OPEN_LONG = REGISTRATE.soundEvent("eyeblossom_open_long", 1)
  val EYEBLOSSOM_CLOSE_LONG = REGISTRATE.soundEvent("eyeblossom_close_long", 1)

  fun register() {
    REGISTRATE.buildSounds()
  }
}