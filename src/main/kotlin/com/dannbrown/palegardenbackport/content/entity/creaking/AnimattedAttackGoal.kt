package com.dannbrown.palegardenbackport.content.entity.creaking

import net.minecraft.world.InteractionHand
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.PathfinderMob
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal
import kotlin.math.max

class AnimattedAttackGoal(pMob: PathfinderMob, pSpeedModifier: Double, pFollowingTargetEvenIfNotSeen: Boolean) : MeleeAttackGoal(pMob, pSpeedModifier, pFollowingTargetEvenIfNotSeen) {
  private val entity: CreakingEntity = pMob as CreakingEntity
  private var attackDelay = 10
  private var ticksUntilNextAttack = 20
  private var shouldCountTillNextAttack = false
  override fun start() {
    super.start()
    attackDelay = 10
    ticksUntilNextAttack = 20
  }

  override fun checkAndPerformAttack(pEnemy: LivingEntity, pDistToEnemySqr: Double) {
    if (isEnemyWithinAttackDistance(pEnemy, pDistToEnemySqr)) {
      shouldCountTillNextAttack = true

      if (isTimeToStartAttackAnimation) {
        entity.setAttacking(true)
      }

      if (isTimeToAttack) {
        entity.lookControl.setLookAt(pEnemy.x, pEnemy.eyeY, pEnemy.z)
        performAttack(pEnemy)
      }
    }
    else {
      resetAttackCooldown()
      shouldCountTillNextAttack = false
      entity.setAttacking(false)
      entity.attackAnimationTimeout = 0
    }
  }

  private fun isEnemyWithinAttackDistance(pEnemy: LivingEntity, pDistToEnemySqr: Double): Boolean {
    return pDistToEnemySqr <= this.getAttackReachSqr(pEnemy)
  }

  override fun resetAttackCooldown() {
    this.ticksUntilNextAttack = this.adjustedTickDelay(attackDelay * 2)
  }

  override fun isTimeToAttack(): Boolean {
    return this.ticksUntilNextAttack <= 0
  }

  protected val isTimeToStartAttackAnimation: Boolean
    get() = this.ticksUntilNextAttack <= attackDelay

  override fun getTicksUntilNextAttack(): Int {
    return this.ticksUntilNextAttack
  }

  protected fun performAttack(pEnemy: LivingEntity?) {
    this.resetAttackCooldown()
    entity.swing(InteractionHand.MAIN_HAND)
    entity.doHurtTarget(pEnemy)
    entity.playAttackSound()
  }

  override fun tick() {
    super.tick()
    if (shouldCountTillNextAttack) {
      this.ticksUntilNextAttack = max((this.ticksUntilNextAttack - 1).toDouble(), 0.0)
        .toInt()
    }
  }

  override fun stop() {
    entity.setAttacking(false)
    super.stop()
  }
}