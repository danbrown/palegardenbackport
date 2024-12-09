package com.dannbrown.palegardenbackport.content.particle

import com.mojang.serialization.Codec
import net.minecraft.core.particles.ParticleOptions
import net.minecraft.core.particles.ParticleType
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.network.FriendlyByteBuf

class TrailParticleObject(pOverrideLimiter: Boolean) : ParticleType<TrailParticleOption>(pOverrideLimiter, TrailParticleOption.DESERIALIZER), ParticleOptions {
  override fun getType(): TrailParticleObject {
    return this
  }

  override fun codec(): Codec<TrailParticleOption> {
    return TrailParticleOption.CODEC
  }

  override fun writeToNetwork(pBuffer: FriendlyByteBuf) {
  }

  override fun writeToString(): String {
    return BuiltInRegistries.PARTICLE_TYPE.getKey(this)
      .toString()
  }
}
