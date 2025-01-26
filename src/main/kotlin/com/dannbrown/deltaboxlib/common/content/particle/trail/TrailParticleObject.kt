package com.dannbrown.deltaboxlib.common.content.particle.trail

import com.mojang.serialization.Codec
import com.mojang.serialization.MapCodec
import net.minecraft.core.particles.ParticleOptions
import net.minecraft.core.particles.ParticleType
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.network.FriendlyByteBuf
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.StreamCodec

/*? if <1.21 {*/
class TrailParticleObject(pOverrideLimiter: Boolean) : ParticleType<TrailParticleOption>(pOverrideLimiter, TrailParticleOption.DESERIALIZER), ParticleOptions {
  /*?} elif {*/
/*class TrailParticleObject(pOverrideLimiter: Boolean) : ParticleType<TrailParticleOption>(pOverrideLimiter), ParticleOptions {
  *//*?}*/
  override fun getType(): TrailParticleObject {
    return this
  }

  /*? if <1.21 {*/
    override fun codec(): Codec<TrailParticleOption> {
    return TrailParticleOption.CODEC
  }

   override fun writeToNetwork(pBuffer: FriendlyByteBuf) {}

  override fun writeToString(): String {
    return BuiltInRegistries.PARTICLE_TYPE.getKey(this).toString()
  }

  /*?} elif {*/
  /*override fun codec(): MapCodec<TrailParticleOption> {
    return TrailParticleOption.CODEC
  }

  override fun streamCodec(): StreamCodec<in RegistryFriendlyByteBuf, TrailParticleOption> {
    return StreamCodec.ofMember(TrailParticleOption::encode, ::TrailParticleOption)
  }
  *//*?}*/
}
