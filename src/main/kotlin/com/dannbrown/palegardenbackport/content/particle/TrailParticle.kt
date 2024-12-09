package com.dannbrown.palegardenbackport.content.particle

import net.minecraft.client.multiplayer.ClientLevel
import net.minecraft.client.particle.Particle
import net.minecraft.client.particle.ParticleProvider
import net.minecraft.client.particle.ParticleRenderType
import net.minecraft.client.particle.SpriteSet
import net.minecraft.client.particle.TextureSheetParticle
import net.minecraft.util.Mth
import net.minecraft.world.phys.Vec3
import net.minecraftforge.api.distmarker.Dist
import net.minecraftforge.api.distmarker.OnlyIn

@OnlyIn(Dist.CLIENT)
class TrailParticle internal constructor(
  level: ClientLevel?,
  x: Double,
  y: Double,
  z: Double,
  xd: Double,
  yd: Double,
  zd: Double,
  target: Vec3,
  color: Int
) : TextureSheetParticle(level, x, y, z, xd, yd, zd) {
  private val target: Vec3

  init {
    val scaledColor = LongColor.scaleRGB(color, 0.875f + random.nextFloat() * 0.25f, 0.875f + random.nextFloat() * 0.25f, 0.875f + random.nextFloat() * 0.25f)
    this.rCol = LongColor.red(scaledColor) / 255.0f
    this.gCol = LongColor.green(scaledColor) / 255.0f
    this.bCol = LongColor.blue(scaledColor) / 255.0f
    this.quadSize = 0.26f
    this.target = target
  }

  override fun getRenderType(): ParticleRenderType {
    return ParticleRenderType.PARTICLE_SHEET_OPAQUE
  }

  override fun tick() {
    this.xo = this.x
    this.yo = this.y
    this.zo = this.z
    if (age++ >= this.lifetime) {
      this.remove()
    } else {
      val remainingTime = this.lifetime - this.age
      val lerpFactor = 1.0 / remainingTime.toDouble()
      this.x = Mth.lerp(lerpFactor, this.x, target.x())
      this.y = Mth.lerp(lerpFactor, this.y, target.y())
      this.z = Mth.lerp(lerpFactor, this.z, target.z())
    }
  }

  override fun getLightColor(pTickDelta: Float): Int {
    return 15728880
  }

  @OnlyIn(Dist.CLIENT)
  class Provider(private val sprite: SpriteSet) : ParticleProvider<TrailParticleOption> {
    override fun createParticle(
      option: TrailParticleOption,
      level: ClientLevel,
      x: Double,
      y: Double,
      z: Double,
      xd: Double,
      yd: Double,
      zd: Double
    ): Particle {
      val particle = TrailParticle(level, x, y, z, xd, yd, zd, option.target(), option.color())
      particle.pickSprite(this.sprite)
      particle.setLifetime(option.duration())
      return particle
    }
  }

  object LongColor {
    fun red(color: Int): Int = (color shr 16 and 0xFF)
    fun green(color: Int): Int = (color shr 8 and 0xFF)
    fun blue(color: Int): Int = (color and 0xFF)

    fun scaleRGB(color: Int, scaleR: Float, scaleG: Float, scaleB: Float): Int {
      val r = (red(color) * scaleR).coerceIn(0f, 255f).toInt()
      val g = (green(color) * scaleG).coerceIn(0f, 255f).toInt()
      val b = (blue(color) * scaleB).coerceIn(0f, 255f).toInt()
      return (r shl 16) or (g shl 8) or b
    }
  }
}
