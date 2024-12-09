package com.dannbrown.palegardenbackport.content.particle

import com.dannbrown.palegardenbackport.init.ModParticles
import com.mojang.brigadier.StringReader
import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.core.particles.ParticleOptions
import net.minecraft.core.particles.ParticleType
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.network.FriendlyByteBuf
import net.minecraft.util.ExtraCodecs
import net.minecraft.world.phys.Vec3

@JvmRecord
data class TrailParticleOption(val _target: Vec3, val _color: Int, val _duration: Int) : ParticleOptions {

  override fun getType(): ParticleType<TrailParticleOption> {
    return ModParticles.TRAIL_PARTICLE.get()
  }

  override fun writeToNetwork(p0: FriendlyByteBuf) {
    p0.writeDouble(this._target.x())
    p0.writeDouble(this._target.y())
    p0.writeDouble(this._target.z())
    p0.writeInt(this._color)
    p0.writeInt(this._duration)
  }

  override fun writeToString(): String {
    return "${BuiltInRegistries.PARTICLE_TYPE.getKey(ModParticles.TRAIL_PARTICLE.get())}(${_target.x()}, ${_target.y()}, ${_target.z()}, $_color, $_duration)"
  }

  fun target(): Vec3 = this._target
  fun color(): Int = this._color
  fun duration(): Int = this._duration

  companion object {
    val DESERIALIZER: ParticleOptions.Deserializer<TrailParticleOption> = object : ParticleOptions.Deserializer<TrailParticleOption> {
      override fun fromCommand(particleType: ParticleType<TrailParticleOption>, stringReader: StringReader): TrailParticleOption {
        val target = Vec3(stringReader.readDouble(), stringReader.readDouble(), stringReader.readDouble())
        val color = stringReader.readInt()
        val duration = stringReader.readInt()
        return TrailParticleOption(target, color, duration)
      }

      override fun fromNetwork(particleType: ParticleType<TrailParticleOption>, friendlyByteBuf: FriendlyByteBuf): TrailParticleOption {
        val target = Vec3(friendlyByteBuf.readDouble(), friendlyByteBuf.readDouble(), friendlyByteBuf.readDouble())
        val color = friendlyByteBuf.readInt()
        val duration = friendlyByteBuf.readInt()
        return TrailParticleOption(target, color, duration)
      }
    }

    val CODEC: Codec<TrailParticleOption> = RecordCodecBuilder.create { optionInstance: RecordCodecBuilder.Instance<TrailParticleOption> ->
      optionInstance.group(Vec3.CODEC.fieldOf("target")
        .forGetter { obj: TrailParticleOption -> obj.target() },
        Codec.INT.fieldOf("color")
          .forGetter { obj: TrailParticleOption -> obj.color() },
        ExtraCodecs.POSITIVE_INT.fieldOf("duration")
          .forGetter { obj: TrailParticleOption -> obj.duration() })
        .apply(optionInstance) { target, color, duration -> TrailParticleOption(target, color, duration) }
    }
  }
}