package com.dannbrown.palegardenbackport.init

import com.dannbrown.palegardenbackport.ModContent
import net.minecraft.resources.ResourceLocation
import net.minecraft.sounds.SoundEvent
import net.minecraftforge.common.util.ForgeSoundType
import net.minecraftforge.eventbus.api.IEventBus
import net.minecraftforge.registries.DeferredRegister
import net.minecraftforge.registries.ForgeRegistries
import net.minecraftforge.registries.RegistryObject

object ModSounds {
  val SOUND_EVENTS = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, ModContent.MOD_ID)

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

  fun register(bus: IEventBus){
    SOUND_EVENTS.register(bus)
  }

  fun registerSoundEvents(name:String): RegistryObject<SoundEvent>{
    return SOUND_EVENTS.register(name) { SoundEvent.createVariableRangeEvent(ResourceLocation(ModContent.MOD_ID, name))}
  }
}