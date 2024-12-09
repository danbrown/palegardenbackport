package com.dannbrown.palegardenbackport.init

import com.dannbrown.palegardenbackport.ModContent
import net.minecraft.resources.ResourceLocation
import net.minecraft.sounds.SoundEvent
import net.minecraft.sounds.SoundEvents
import net.minecraftforge.common.util.ForgeSoundType
import net.minecraftforge.eventbus.api.IEventBus
import net.minecraftforge.registries.DeferredRegister
import net.minecraftforge.registries.ForgeRegistries
import net.minecraftforge.registries.RegistryObject

object ModSounds {
  val SOUND_EVENTS = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, ModContent.MOD_ID)

  // Resin
  val RESIN_BRICK_BREAK = registerSoundEvents("resin_bricks_break")
  val RESIN_BRICK_STEP = registerSoundEvents("resin_bricks_step")
  val RESIN_BRICK_FALL = registerSoundEvents("resin_bricks_fall")
  val RESIN_BRICK_PLACE = registerSoundEvents("resin_bricks_place")
  val RESIN_BRICK_HIT = registerSoundEvents("resin_bricks_hit")

  val BLOCK_OF_RESIN_BREAK = registerSoundEvents("block_of_resin_break")
  val BLOCK_OF_RESIN_STEP = registerSoundEvents("block_of_resin_step")
  val BLOCK_OF_RESIN_FALL = registerSoundEvents("block_of_resin_fall")
  val BLOCK_OF_RESIN_PLACE = registerSoundEvents("block_of_resin_place")
  val BLOCK_OF_RESIN_HIT = registerSoundEvents("block_of_resin_hit")

  val RESIN_BRICK_SOUNDS = ForgeSoundType(1f, 1f, RESIN_BRICK_BREAK, RESIN_BRICK_STEP, RESIN_BRICK_PLACE, RESIN_BRICK_HIT, RESIN_BRICK_FALL)
  val BLOCK_OF_RESIN_SOUNDS = ForgeSoundType(1f, 1f, BLOCK_OF_RESIN_BREAK, BLOCK_OF_RESIN_STEP, BLOCK_OF_RESIN_PLACE, BLOCK_OF_RESIN_HIT, BLOCK_OF_RESIN_FALL)

  // Creaking
  val CREAKING_AMBIENT = registerSoundEvents("creaking_ambient")
  val CREAKING_ACTIVATE = registerSoundEvents("creaking_activate")
  val CREAKING_DEACTIVATE = registerSoundEvents("creaking_deactivate")
  val CREAKING_ATTACK = registerSoundEvents("creaking_attack")
  val CREAKING_DEATH = registerSoundEvents("creaking_death")
  val CREAKING_STEP = registerSoundEvents("creaking_step")
  val CREAKING_FREEZE = registerSoundEvents("creaking_freeze")
  val CREAKING_UNFREEZE = registerSoundEvents("creaking_unfreeze")
  val CREAKING_SPAWN = registerSoundEvents("creaking_spawn")
  val CREAKING_HIT = registerSoundEvents("creaking_hit")
  val CREAKING_TWITCH = registerSoundEvents("creaking_twitch")

  // Creaking Heart
  val CREAKING_HEART_BREAK = registerSoundEvents("creaking_heart_break")
  val CREAKING_HEART_FALL = registerSoundEvents("creaking_heart_fall")
  val CREAKING_HEART_HIT = registerSoundEvents("creaking_heart_hit")
  val CREAKING_HEART_HURT = registerSoundEvents("creaking_heart_hurt")
  val CREAKING_HEART_PLACE = registerSoundEvents("creaking_heart_place")
  val CREAKING_HEART_STEP = registerSoundEvents("creaking_heart_step")
  val CREAKING_HEART_IDLE = registerSoundEvents("creaking_heart_idle")

  val CREAKING_HEART_SOUNDS = ForgeSoundType(1f, 1f, CREAKING_HEART_BREAK, CREAKING_HEART_STEP, CREAKING_HEART_PLACE, CREAKING_HEART_HIT, CREAKING_HEART_FALL)


  fun register(bus: IEventBus){
    SOUND_EVENTS.register(bus)
  }

  fun registerSoundEvents(name:String): RegistryObject<SoundEvent>{
    return SOUND_EVENTS.register(name) { SoundEvent.createVariableRangeEvent(ResourceLocation(ModContent.MOD_ID, name))}
  }

  fun registerSoundEvents2(name:String): RegistryObject<SoundEvent>{
    return SOUND_EVENTS.register(name) { SoundEvent.createVariableRangeEvent(ResourceLocation(ModContent.MOD_ID, name))}
  }
}