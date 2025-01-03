package com.dannbrown.palegardenbackport.content.entity.creaking

import com.dannbrown.palegardenbackport.init.ModCommonConfig
import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.damagesource.DamageSource
import net.minecraft.world.effect.MobEffectInstance
import net.minecraft.world.entity.AgeableMob
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.ai.attributes.AttributeSupplier
import net.minecraft.world.entity.ai.attributes.Attributes
import net.minecraft.world.entity.animal.Animal
import net.minecraft.world.entity.monster.Monster
import net.minecraft.world.entity.player.Player
import net.minecraft.world.level.Level
import net.minecraft.world.level.pathfinder.BlockPathTypes

open class AbstractCreaking protected constructor(type: EntityType<out AbstractCreaking>, level: Level) : Animal(type, level) {
  var soundCooldown: Int = 15

  init {
    this.setPathfindingMalus(BlockPathTypes.LEAVES, -1.0f)
    this.setPathfindingMalus(BlockPathTypes.FENCE, -1.0f)
    this.setPathfindingMalus(BlockPathTypes.COCOA, -1.0f)
    this.setPathfindingMalus(BlockPathTypes.WATER, 0.0f)
    this.setPathfindingMalus(BlockPathTypes.UNPASSABLE_RAIL, 0.0f)
  }


  override fun aiStep() {
    super.aiStep()
    if (this.soundCooldown > 0) {
      soundCooldown--
    }
  }

  override fun getBreedOffspring(level: ServerLevel, mob: AgeableMob): AgeableMob? {
    return null
  }

  override fun getStepHeight(): Float {
    return 1.0f
  }

  override fun removeWhenFarAway(dist: Double): Boolean {
    return false
  }


  override fun onClimbable(): Boolean {
    return false
  }

  override fun addEffect(instance: MobEffectInstance, entity: Entity?): Boolean {
    return false
  }

  override fun causeFallDamage(dist: Float, mult: Float, source: DamageSource): Boolean {
    return false
  }

  override fun checkDespawn() {
  }

  override fun canChangeDimensions(): Boolean {
    return false
  }

  override fun attackable(): Boolean {
    return true
  }

  override fun isAffectedByPotions(): Boolean {
    return false
  }

  override fun getWaterSlowDown(): Float {
    return 0.9f
  }

  override fun getFluidJumpThreshold(): Double {
    return 0.2
  }

  override fun canBeLeashed(player: Player): Boolean {
    return false
  }

  override fun isIgnoringBlockTriggers(): Boolean {
    return true
  }

  override fun isSteppingCarefully(): Boolean {
    return true
  }

  override fun canBeHitByProjectile(): Boolean {
    return false
  }

  override fun isPushable(): Boolean {
    return false
  }

  companion object {
    fun registerAttributes(): AttributeSupplier.Builder {
      return Monster.createMonsterAttributes()
        .add(Attributes.MAX_HEALTH, 1.0)
        .add(Attributes.MOVEMENT_SPEED, 0.4000000059604645)
        .add(Attributes.ATTACK_DAMAGE,3.0)
        .add(Attributes.FOLLOW_RANGE,32.0)
    }
  }
}
