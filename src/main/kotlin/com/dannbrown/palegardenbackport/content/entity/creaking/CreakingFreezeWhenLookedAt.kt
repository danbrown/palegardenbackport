package com.dannbrown.palegardenbackport.content.entity.creaking

import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.ai.goal.Goal
import net.minecraft.world.entity.player.Player
import net.minecraft.world.phys.Vec3
import java.util.*

class CreakingFreezeWhenLookedAt(private val creakingEntity: CreakingEntity) : Goal() {
  private var target: LivingEntity? = null

  init {
    this.flags = EnumSet.of(Flag.JUMP, Flag.MOVE)
  }

  override fun canUse(): Boolean {
    this.target = creakingEntity.target
    if (target !is Player) {
      return false
    }
    else {
      val d0 = (target as Player).distanceToSqr(this.creakingEntity)
      return if (d0 > 256.0) false else isLookingAtMe(target as Player, creakingEntity)
    }
  }

  fun isLookingAtMe(player: Player, mob: CreakingEntity): Boolean {
    val vec3 = player.getViewVector(1.0f).normalize()
    var vec31 = Vec3(mob.x - player.x, mob.eyeY - player.eyeY, mob.z - player.z)
    val d0 = vec31.length()
    vec31 = vec31.normalize()

    val d1 = vec3.dot(vec31)
    val fovThreshold = Math.cos(Math.toRadians(45.0)) // 45 degrees field of view
    return if (d1 > fovThreshold / d0) {
      player.hasLineOfSight(mob)
    } else {
      false
    }
  }

  override fun start() {
    creakingEntity.navigation.stop()
  }

  override fun stop() {
    creakingEntity.canFreeze()
  }

  override fun tick() {
  }
}