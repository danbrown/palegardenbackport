package com.dannbrown.palegardenbackport.content.particle

import net.minecraft.client.multiplayer.ClientLevel
import net.minecraft.client.particle.Particle
import net.minecraft.client.particle.ParticleProvider
import net.minecraft.client.particle.ParticleRenderType
import net.minecraft.client.particle.SpriteSet
import net.minecraft.client.particle.TextureSheetParticle
import net.minecraft.core.particles.SimpleParticleType
import kotlin.math.cos
import kotlin.math.min
import kotlin.math.sin

class PaleOakParticle(pLevel: ClientLevel, pX: Double, pY: Double, pZ: Double, pSpriteSet: SpriteSet) : TextureSheetParticle(pLevel, pX, pY, pZ) {
  private var rotSpeed: Float
  private val particleRandom: Float
  private val spinAcceleration: Float

  init {
    this.setSprite(pSpriteSet[random.nextInt(12), 12])
    this.rotSpeed = Math.toRadians(if (random.nextBoolean()) -30.0 else 30.0)
      .toFloat()
    this.particleRandom = random.nextFloat()
    this.spinAcceleration = Math.toRadians(if (random.nextBoolean()) -5.0 else 5.0).toFloat()
    this.lifetime = 300
    this.gravity = 7.5E-4f
    val f = if (random.nextBoolean()) 0.075f else 0.095f
    this.quadSize = f
    this.setSize(f, f)
    this.friction = 1.0f
  }

  override fun getRenderType(): ParticleRenderType {
    return ParticleRenderType.PARTICLE_SHEET_OPAQUE
  }

  override fun tick() {
    this.xo = this.x
    this.yo = this.y
    this.zo = this.z
    if (lifetime-- <= 0) {
      this.remove()
    }

    if (!this.removed) {
      val f = (300 - this.lifetime).toFloat()
      val f1 = min((f / 300.0f).toDouble(), 1.0)
        .toFloat()
      val d0: Double = cos(Math.toRadians((this.particleRandom * 60.0f).toDouble())) * 2.0 * Math.pow(f1.toDouble(), 1.25)
      val d1: Double = sin(Math.toRadians((this.particleRandom * 60.0f).toDouble())) * 2.0 * Math.pow(f1.toDouble(), 1.25)
      this.xd += d0 * 0.0024999999441206455
      this.zd += d1 * 0.0024999999441206455
      this.yd -= gravity.toDouble()
      this.rotSpeed += this.spinAcceleration / 20.0f
      this.oRoll = this.roll
      this.roll += this.rotSpeed / 20.0f
      this.move(this.xd, this.yd, this.zd)
      if (this.onGround || this.lifetime < 299 && (this.xd == 0.0 || this.zd == 0.0)) {
        this.remove()
      }

      if (!this.removed) {
        this.xd *= friction.toDouble()
        this.yd *= friction.toDouble()
        this.zd *= friction.toDouble()
      }
    }
  }

  companion object {
    private const val ACCELERATION_SCALE = 0.0025f
    private const val INITIAL_LIFETIME = 300
    private const val CURVE_ENDPOINT_TIME = 300
    private const val FALL_ACC = 0.25f
    private const val WIND_BIG = 2.0f
  }

  class Provider(private val spriteSet: SpriteSet): ParticleProvider<SimpleParticleType>{
    override fun createParticle(particleType: SimpleParticleType, level: ClientLevel, px: Double, py: Double, pz: Double, xSpeed: Double, ySpeed: Double, zSpeed: Double): Particle {
      return PaleOakParticle(level, px, py, pz, spriteSet)
    }
  }
}
