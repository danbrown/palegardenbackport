package com.dannbrown.palegardenbackport.content.entity.creaking

import com.mojang.serialization.Dynamic
import net.minecraft.core.BlockPos
import net.minecraft.network.syncher.EntityDataAccessor
import net.minecraft.network.syncher.EntityDataSerializers
import net.minecraft.network.syncher.SynchedEntityData
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.entity.AnimationState
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.ai.Brain
import net.minecraft.world.entity.ai.control.JumpControl
import net.minecraft.world.entity.ai.control.LookControl
import net.minecraft.world.entity.ai.control.MoveControl
import net.minecraft.world.entity.ai.goal.FloatGoal
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal
import net.minecraft.world.entity.ai.memory.MemoryModuleType
import net.minecraft.world.entity.ai.navigation.GroundPathNavigation
import net.minecraft.world.entity.player.Player
import net.minecraft.world.level.Level
import net.minecraft.world.phys.Vec3
import java.util.*

class CreakingEntity(type: EntityType<out AbstractCreaking>, level: Level) : AbstractCreaking(type, level) {
  val idleAnimationState: AnimationState = AnimationState()
  private var idleAnimationTimeout = 0

  val attackAnimationState: AnimationState = AnimationState()
  var attackAnimationTimeout: Int = 0

  val invulnerabilityAnimationState: AnimationState = AnimationState()
  private var invulnerabilityAnimationTimeout: Int = 0

  val deathAnimationState: AnimationState = AnimationState()
  private var deathAnimationTimeout: Int = 0

  init {
    lookControl = CreakingLookControl(this)
    moveControl = CreakingMoveControl(this)
    jumpControl = CreakingJumpControl(this)

    val groundPathNavigation: GroundPathNavigation = getNavigation() as GroundPathNavigation
    groundPathNavigation.setCanFloat(true)

    xpReward = 0
  }

  override fun brainProvider(): Brain.Provider<CreakingEntity> {
    return CreakingAi.brainProvider()
  }

  override fun makeBrain(dynamic: Dynamic<*>?): Brain<*> {
    return CreakingAi.makeBrain(brainProvider().makeBrain(dynamic))
  }

  fun canMove(): Boolean {
    return entityData.get(CAN_MOVE) as Boolean
  }

  class CreakingLookControl(private val creakingEntity: CreakingEntity) : LookControl(creakingEntity) {
    override fun tick() {
      if (creakingEntity.canMove()) {
        super.tick()
      }
    }
  }

  class CreakingMoveControl(private val creakingEntity: CreakingEntity) : MoveControl(creakingEntity) {
    override fun tick() {
      if (creakingEntity.canMove()) {
        super.tick()
      }
    }
  }

  class CreakingJumpControl(private val creakingEntity: CreakingEntity) : JumpControl(creakingEntity) {
    override fun tick() {
      if (creakingEntity.canMove()) {
        super.tick()
      } else {
        creakingEntity.setJumping(false)
      }
    }
  }

  override fun tick() {
    if (!level().isClientSide) {
      val homePos = getHomePos()
      if (homePos != null) {
        var shouldDie = false
        // TODO: Check for interaction with CreakingHeartBlockEntity (unfinished)
        if (!shouldDie) {
          health = 0.0f
        }
      }
    }
    super.tick()

    if (level().isClientSide()) {
      setupAnimationStates()
    }
  }

  private fun setupAnimationStates() {
    if (idleAnimationTimeout <= 0) {
      idleAnimationTimeout = random.nextInt(40) + 80
      idleAnimationState.start(tickCount)
    } else {
      --idleAnimationTimeout
    }

    if (isAttacking() && attackAnimationTimeout <= 0) {
      attackAnimationTimeout = 80 // Length in ticks of your animation
      attackAnimationState.start(tickCount)
    } else {
      --attackAnimationTimeout
    }

    if (!isAttacking()) {
      attackAnimationState.stop()
    }
  }

  fun setAttacking(attacking: Boolean) {
    entityData.set(ATTACKING, attacking)
  }

  fun isAttacking(): Boolean {
    return entityData.get(ATTACKING)
  }

  override fun defineSynchedData() {
    super.defineSynchedData()
    entityData.define(ATTACKING, false)
    entityData.define(CAN_MOVE, true)
    entityData.define(IS_ACTIVE, false)
    entityData.define(IS_TEARING_DOWN, false)
    entityData.define(HOME_POS, Optional.empty())
  }

  fun activate(player: Player?) {
    getBrain().setMemory(MemoryModuleType.ATTACK_TARGET, player)
    // TODO: Add activation sound (unfinished)
    setIsActive(true)
  }

  fun deactivate() {
    getBrain().eraseMemory(MemoryModuleType.ATTACK_TARGET)
    // TODO: Add deactivation sound (unfinished)
    setIsActive(false)
  }

  fun setIsActive(isActive: Boolean) {
    entityData.set(IS_ACTIVE, isActive)
  }

  fun isActive(): Boolean {
    return entityData.get(IS_ACTIVE) as Boolean
  }

  fun checkCanMove(): Boolean {
    val players = brain.getMemory(MemoryModuleType.NEAREST_PLAYERS).orElse(listOf()) as List<*>
    val currentActiveState = isActive()

    if (players.isEmpty()) {
      if (currentActiveState) {
        deactivate()
      }
      return true
    } else {
      var canMove = false
      val iterator = players.iterator()

      while (iterator.hasNext()) {
        val player = iterator.next() as Player
        // TODO: Check attack and alliance logic (unfinished)
        if (isLookingAtMe(player, 0.5, false, true, eyeY, y + 0.5 * scale, (eyeY + y) / 2.0)) {
          if (currentActiveState) {
            return false
          }

          if (player.distanceToSqr(this) < 144.0) {
            activate(player)
            return false
          }
        }
      }
    }
    return true
  }

  fun isLookingAtMe(target: LivingEntity, angleThreshold: Double, checkDistance: Boolean, checkLineOfSight: Boolean, vararg additionalYPositions: Double): Boolean {
    val viewVector = target.getViewVector(1.0f).normalize()
    additionalYPositions.forEach { yPos ->
      val directionVec = Vec3(x - target.x, yPos - target.eyeY, z - target.z)
      val distance = directionVec.length()
      val normalizedVec = directionVec.normalize()
      val dotProduct = viewVector.dot(normalizedVec)

      if (dotProduct > 1.0 - angleThreshold / (if (checkDistance) distance else 1.0) && target.hasLineOfSight(this)) {
        return true
      }
    }
    return false
  }

  fun stopInPlace() {
    getNavigation().stop()
    setXxa(0.0f)
    setYya(0.0f)
    speed = 0.0f
  }

  override fun aiStep() {
    if (!level().isClientSide) {
      val canMoveFlag = entityData.get(CAN_MOVE) as Boolean
      val canMoveNow = checkCanMove()

      if (canMoveNow != canMoveFlag) {
        if (canMoveNow) {
          // TODO: Add unfreeze sound (unfinished)
        } else {
          stopInPlace()
          // TODO: Add freeze sound (unfinished)
        }
      }

      entityData.set(CAN_MOVE, canMoveNow)
    }

    super.aiStep()
  }

  override fun registerGoals() {
    goalSelector.addGoal(0, FloatGoal(this))
    goalSelector.addGoal(1, CreakingFreezeWhenLookedAt(this))
    goalSelector.addGoal(2, AnimattedAttackGoal(this, 1.0, true))
    goalSelector.addGoal(5, RandomLookAroundGoal(this))
    targetSelector.addGoal(2, NearestAttackableTargetGoal(this, Player::class.java, true))
  }

  override fun tickDeath() {
    if (isHeartBound() && isTearingDown()) {
      ++deathTime
      if (!level().isClientSide() && deathTime > 45 && !isRemoved) {
        tearDown()
      }
    } else {
      super.tickDeath()
    }
  }

  fun isHeartBound(): Boolean {
    return this.getHomePos() != null
  }

  fun getHomePos(): BlockPos? {
    return (entityData.get(HOME_POS)).orElse(null)
  }

  fun setTearingDown() {
    entityData.set(IS_TEARING_DOWN, true)
  }

  fun isTearingDown(): Boolean {
    return entityData.get(IS_TEARING_DOWN) as Boolean
  }

  fun tearDown() {
    val serverLevel = level() as ServerLevel
    val pos = blockPosition()

    // TODO: Add a specific tear down logic for creaking entities (unfinished)
  }

  companion object {
    val ATTACKING: EntityDataAccessor<Boolean> = SynchedEntityData.defineId(CreakingEntity::class.java, EntityDataSerializers.BOOLEAN)
    val CAN_MOVE = SynchedEntityData.defineId(CreakingEntity::class.java, EntityDataSerializers.BOOLEAN)
    val IS_ACTIVE = SynchedEntityData.defineId(CreakingEntity::class.java, EntityDataSerializers.BOOLEAN)
    val IS_TEARING_DOWN = SynchedEntityData.defineId(CreakingEntity::class.java, EntityDataSerializers.BOOLEAN)
    val HOME_POS = SynchedEntityData.defineId(CreakingEntity::class.java, EntityDataSerializers.OPTIONAL_BLOCK_POS)
  }
}
