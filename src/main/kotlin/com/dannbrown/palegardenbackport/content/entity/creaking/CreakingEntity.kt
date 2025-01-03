package com.dannbrown.palegardenbackport.content.entity.creaking

import com.dannbrown.deltaboxlib.registry.generators.BlockFamily
import com.dannbrown.palegardenbackport.ModContent
import com.dannbrown.palegardenbackport.content.block.creakingHeart.CreakingHeartBlock
import com.dannbrown.palegardenbackport.content.block.creakingHeart.CreakingHeartBlockEntity
import com.dannbrown.palegardenbackport.init.ModCommonConfig
import com.dannbrown.palegardenbackport.init.ModSounds
import com.mojang.serialization.Dynamic
import net.minecraft.core.BlockPos
import net.minecraft.core.particles.BlockParticleOption
import net.minecraft.core.particles.ParticleTypes
import net.minecraft.nbt.CompoundTag
import net.minecraft.nbt.NbtUtils
import net.minecraft.network.syncher.EntityDataAccessor
import net.minecraft.network.syncher.EntityDataSerializers
import net.minecraft.network.syncher.SynchedEntityData
import net.minecraft.server.level.ServerLevel
import net.minecraft.sounds.SoundEvent
import net.minecraft.tags.DamageTypeTags
import net.minecraft.world.damagesource.DamageSource
import net.minecraft.world.entity.AnimationState
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.TamableAnimal
import net.minecraft.world.entity.ai.Brain
import net.minecraft.world.entity.ai.behavior.Swim
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
import net.minecraft.world.entity.projectile.Projectile
import net.minecraft.world.item.Items
import net.minecraft.world.level.ClipContext
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.material.Fluids
import net.minecraft.world.level.pathfinder.BlockPathTypes
import net.minecraft.world.phys.AABB
import net.minecraft.world.phys.HitResult
import net.minecraft.world.phys.Vec3
import net.minecraftforge.event.entity.living.LivingKnockBackEvent
import net.minecraftforge.fluids.FluidType
import java.util.*

class CreakingEntity(type: EntityType<out AbstractCreaking>, level: Level) : AbstractCreaking(type, level) {

  val idleAnimationState: AnimationState = AnimationState()
  private var idleAnimationTimeout = 0

  val attackAnimationState: AnimationState = AnimationState()
  var attackAnimationTimeout: Int = 0
  var attackAnimationRemainingTicks: Int = 0

  val invulnerabilityAnimationState: AnimationState = AnimationState()
  private var invulnerabilityAnimationTimeout: Int = 0
  private var invulnerabilityAnimationRemainingTicks: Int = 0

  val deathAnimationState: AnimationState = AnimationState()
  private var deathAnimationTimeout: Int = 0
  var tearingTicks = 0

  private var playerStuckCounter: Int = 0

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

  override fun makeBrain(dynamic: Dynamic<*>): Brain<*> {
    return CreakingAi.makeBrain(brainProvider().makeBrain(dynamic))
  }

  fun canMove(): Boolean {
    return entityData.get(CAN_MOVE)
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

  override fun readAdditionalSaveData(compoundTag: CompoundTag) {
    super.readAdditionalSaveData(compoundTag)
    if (compoundTag.contains("home_pos")) {
      this.setTransient(NbtUtils.readBlockPos(compoundTag.getCompound("home_pos")))
    }
  }

  override fun addAdditionalSaveData(compoundTag: CompoundTag) {
    super.addAdditionalSaveData(compoundTag)
    val blockPos = this.getHomePos()
    if (blockPos != null) {
      compoundTag.put("home_pos", NbtUtils.writeBlockPos(blockPos))
    }
  }

  override fun tick() {
    if (!level().isClientSide) {
      val blockPos = this.getHomePos()
      if (blockPos != null) {
        var isProtector = false
        val blockEntity = level().getBlockEntity(blockPos)
        if (blockEntity is CreakingHeartBlockEntity) {
          isProtector = blockEntity.isProtector(this)
        }
        if (!isProtector) {
          if(isTearingDown()) {
            if(tearingTicks > 50 && !isRemoved) this.tearDown()
          } else {
            this.health = 0.0f
          }
        }
      }

      if(isTearingDown()) tearingTicks++
    }

    super.tick()
    if (level().isClientSide) {
      this.setupAnimationStates()
//      this.checkEyeBlink() // TODO: Implement eye blink
    }
  }

  private fun setupAnimationStates() {
    attackAnimationState.animateWhen(this.attackAnimationRemainingTicks > 0, this.tickCount)
    invulnerabilityAnimationState.animateWhen(this.invulnerabilityAnimationRemainingTicks > 0, this.tickCount)
    deathAnimationState.animateWhen(this.isTearingDown(), this.tickCount)
    idleAnimationState.animateWhen(idleAnimationTimeout > 0, this.tickCount)

    if (isAttacking() && attackAnimationTimeout <= 0) {
      attackAnimationTimeout = 20 // Length in ticks of your animation
      attackAnimationRemainingTicks = 20
      attackAnimationState.start(tickCount)
    } else if(attackAnimationTimeout > 0) {
      --attackAnimationTimeout
    }

    if (!isAttacking()) {
      attackAnimationState.stop()
    }


    if (idleAnimationTimeout <= 0) {
      idleAnimationTimeout = random.nextInt(40) + 80
      if(!isAttacking()) idleAnimationState.start(tickCount)
    } else {
      --idleAnimationTimeout
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
    this.playSound(ModSounds.CREAKING_ACTIVATE.get())
    setIsActive(true)
  }

  fun deactivate() {
    getBrain().eraseMemory(MemoryModuleType.ATTACK_TARGET)
    this.playSound(ModSounds.CREAKING_DEACTIVATE.get())
    setIsActive(false)
  }

  fun setIsActive(isActive: Boolean) {
    entityData.set(IS_ACTIVE, isActive)
  }

  fun isActive(): Boolean {
    return entityData.get(IS_ACTIVE) as Boolean
  }

//  fun checkCanMove(): Boolean {
//    val players = brain.getMemory(MemoryModuleType.NEAREST_PLAYERS).orElse(listOf()) as List<*>
//    val currentActiveState = isActive()
//
//    if (players.isEmpty()) {
//      if (currentActiveState) {
//        deactivate()
//      }
//      return true
//    } else {
//      var canMove = false
//      val iterator = players.iterator()
//
//      while (iterator.hasNext()) {
//        val player = iterator.next() as Player
//        // TODO: Check attack and alliance logic (unfinished)
//        if (isLookingAtMe(player, 0.5, false, true, eyeY, y + 0.5 * scale, (eyeY + y) / 2.0)) {
//          if (currentActiveState) {
//            return false
//          }
//
//          if (player.distanceToSqr(this) < 144.0) {
//            activate(player)
//            return false
//          }
//        }
//      }
//    }
//    return true
//  }

//  fun isLookingAtMe(target: LivingEntity, angleThreshold: Double, checkDistance: Boolean, checkLineOfSight: Boolean, vararg additionalYPositions: Double): Boolean {
//    val viewVector = target.getViewVector(1.0f).normalize()
//    additionalYPositions.forEach { yPos ->
//      val directionVec = Vec3(x - target.x, yPos - target.eyeY, z - target.z)
//      val distance = directionVec.length()
//      val normalizedVec = directionVec.normalize()
//      val dotProduct = viewVector.dot(normalizedVec)
//
//      if (dotProduct > 1.0 - angleThreshold / (if (checkDistance) distance else 1.0) && target.hasLineOfSight(this)) {
//        return true
//      }
//    }
//    return false
//  }

  fun isLookingAtMe(player: Player, mob: CreakingEntity): Boolean {
    val vec3 = player.getViewVector(1.0f).normalize()
    var vec31 = Vec3(mob.x - player.x, mob.eyeY - player.eyeY, mob.z - player.z)
    val d0 = vec31.length()
    vec31 = vec31.normalize()

    val d1 = vec3.dot(vec31)
    val fovThreshold = Math.cos(Math.toRadians(ModCommonConfig.PLAYER_FOV_ANGLE?.get() ?: 45.0)) // 45 degrees field of view
    return if (d1 > fovThreshold / d0) {
      hasLineOfSight(player, mob) && !player.armorSlots.any { it.`is`(Items.CARVED_PUMPKIN) }
    } else false
  }

  fun hasLineOfSight(player: Player, mob: CreakingEntity): Boolean{
    if (mob.level() !== player.level()) {
      return false
    }
    else {
      val vec3 = Vec3(player.x, player.eyeY, player.z)
      val vec31 = Vec3(mob.x, mob.eyeY, mob.z)
      return if (vec31.distanceTo(vec3) > (ModCommonConfig.CREAKING_FREEZE_DISTANCE?.get() ?: 128.0)) {
        false
      }
      else {
        player.level()
          .clip(ClipContext(vec3, vec31, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, player))
          .type == HitResult.Type.MISS
      }
    }
  }

  fun stopInPlace() {
    getNavigation().stop()
    setXxa(0.0f)
    setYya(0.0f)
    speed = 0.0f
  }

  fun setCanMove(value: Boolean) {
    entityData.set(CAN_MOVE, value)
  }

  override fun aiStep() {
    if (this.invulnerabilityAnimationRemainingTicks > 0) {
      --this.invulnerabilityAnimationRemainingTicks
    }

    if (this.attackAnimationRemainingTicks > 0) {
      --this.attackAnimationRemainingTicks
    }

    if (!level().isClientSide) {
      CreakingAi.updateActivity(this)
      val canMoveFlag = entityData.get(CAN_MOVE) as Boolean
      val canMoveNow = checkCanMove()

      if (canMoveNow != canMoveFlag) {
        if (canMoveNow) {
          this.playSound(ModSounds.CREAKING_UNFREEZE.get())
        } else {
          stopInPlace()
          this.playSound(ModSounds.CREAKING_FREEZE.get())
        }
      }

      setCanMove(canMoveNow)
    }

    super.aiStep()
  }

  fun getNearestPlayers(): List<Player> {
    val fromBrain = brain.getMemory(MemoryModuleType.NEAREST_PLAYERS).orElse(listOf())
    if(fromBrain.isEmpty()){
      val AABB = this.boundingBox.inflate(16.0)
      val entities = level().getEntities(this, AABB)
      val players = entities.filterIsInstance<Player>()
      brain.setMemory(MemoryModuleType.NEAREST_PLAYERS, players)
      return players
    }
    return fromBrain
  }

  fun checkCanMove(): Boolean{
    val nearestPlayers: List<Player> = getNearestPlayers()
    if (nearestPlayers.isEmpty()) {
      return true
    }
    else {
      nearestPlayers.forEach {
        val player = it
        if (isLookingAtMe(player, this)) {
          if (isActive()) {
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
    val level = this.level()
    if (level is ServerLevel) {
      val aabb = this.boundingBox
      val vec3 = aabb.center
      val d0 = aabb.xsize * 0.3
      val d1 = aabb.ysize * 0.3
      val d2 = aabb.zsize * 0.3
      level.sendParticles(BlockParticleOption(ParticleTypes.BLOCK, ModContent.WOOD_FAMILY.blocks[BlockFamily.Type.WOOD]!!.get().defaultBlockState()), vec3.x, vec3.y, vec3.z, 100, d0, d1, d2, 0.0)
      level.sendParticles(BlockParticleOption(ParticleTypes.BLOCK, ModContent.CREAKING_HEART.get().defaultBlockState().setValue(CreakingHeartBlock.ACTIVE, true)), vec3.x, vec3.y, vec3.z, 10, d0, d1, d2, 0.0)
    }

    this.playSound(this.deathSound)
    this.remove(RemovalReason.DISCARDED)
  }

  fun playAttackSound() {
    this.playSound(ModSounds.CREAKING_ATTACK.get())
  }

  override fun playStepSound(pPos: BlockPos, pState: BlockState) {
    this.playSound(ModSounds.CREAKING_STEP.get())
  }

  override fun getAmbientSound(): SoundEvent? {
    return if (this.isActive()) null else ModSounds.CREAKING_AMBIENT.get()
  }

  override fun getHurtSound(damageSource: DamageSource): SoundEvent? {
    return if (this.isHeartBound()) ModSounds.CREAKING_HIT.get() else super.getHurtSound(damageSource)
  }

  override fun getDeathSound(): SoundEvent {
    return ModSounds.CREAKING_DEATH.get()
  }

  fun creakingDeathEffects(damageSource: DamageSource) {
    this.blameSourceForDamage(damageSource)
    this.playSound(ModSounds.CREAKING_TWITCH.get())
  }

  fun blameSourceForDamage(damageSource: DamageSource): Player? {
    this.resolveMobResponsibleForDamage(damageSource)
    return this.resolvePlayerResponsibleForDamage(damageSource)
  }

  private fun resolveMobResponsibleForDamage(damageSource: DamageSource) {
    val entity = damageSource.entity
    if (entity is LivingEntity) {
      if (!damageSource.`is`(DamageTypeTags.NO_ANGER)) {
        this.lastHurtByMob = entity
      }
    }
  }

  private fun resolvePlayerResponsibleForDamage(damageSource: DamageSource): Player? {
    val entity = damageSource.entity
    if (entity is Player) {
      this.lastHurtByPlayerTime = 100
      this.lastHurtByPlayer = entity
      return entity
    }
    else {
      if (entity is TamableAnimal) {
        if (entity.isTame) {
          this.lastHurtByPlayerTime = 100
          val var6: LivingEntity? = entity.owner
          if (var6 is Player) {
            this.lastHurtByPlayer = var6
          }
          else {
            this.lastHurtByPlayer = null
          }

          return this.lastHurtByPlayer
        }
      }

      return null
    }
  }

  fun setTransient(blockPos: BlockPos) {
    this.setHomePos(blockPos)
    this.setPathfindingMalus(BlockPathTypes.DAMAGE_OTHER, 8.0f)
    this.setPathfindingMalus(BlockPathTypes.POWDER_SNOW, 8.0f)
    this.setPathfindingMalus(BlockPathTypes.LAVA, 8.0f)
    this.setPathfindingMalus(BlockPathTypes.DAMAGE_FIRE, 0.0f)
    this.setPathfindingMalus(BlockPathTypes.DANGER_FIRE, 0.0f)
  }

  fun setHomePos(blockPos: BlockPos) {
    entityData.set<Optional<BlockPos>>(HOME_POS, Optional.of(blockPos))
  }

  fun playerIsStuckInYou(): Boolean {
    val list: List<Player> = brain.getMemory(MemoryModuleType.NEAREST_PLAYERS).orElse(listOf())
    if (list.isEmpty()) {
      this.playerStuckCounter = 0
      return false
    }
    else {
      val aabb = this.boundingBox
      val iterator: Iterator<*> = list.iterator()
      var player: Player
      do {
        if (!iterator.hasNext()) {
          this.playerStuckCounter = 0
          return false
        }
        player = iterator.next() as Player
      } while (!aabb.contains(player.eyePosition))

      ++this.playerStuckCounter
      return this.playerStuckCounter > 4
    }
  }

  override fun hurt(source: DamageSource, amount: Float): Boolean {
    val blockPos = this.getHomePos()
    if (blockPos != null && !source.`is`(DamageTypeTags.BYPASSES_INVULNERABILITY)) {
      if (!this.isInvulnerableTo(source) && this.invulnerabilityAnimationRemainingTicks <= 0 && !this.isDeadOrDying) {
        val player = this.blameSourceForDamage(source)
        val entity = source.directEntity
        if (entity !is LivingEntity && entity !is Projectile && player == null) {
          return false
        }
        else {
          this.invulnerabilityAnimationRemainingTicks = 8
          level().broadcastEntityEvent(this, 66.toByte())
          val blockEntity = level().getBlockEntity(blockPos)
          if (blockEntity is CreakingHeartBlockEntity) {
            if (blockEntity.isProtector(this)) {
              if (player != null) {
                blockEntity.creakingHurt()
              }
              this.playHurtSound(source)
            }
          }

          return true
        }
      }
      else {
        return false
      }
    }
    else {
      return super.hurt(source, amount)
    }
  }



  companion object {
    val ATTACKING: EntityDataAccessor<Boolean> = SynchedEntityData.defineId(CreakingEntity::class.java, EntityDataSerializers.BOOLEAN)
    val CAN_MOVE = SynchedEntityData.defineId(CreakingEntity::class.java, EntityDataSerializers.BOOLEAN)
    val IS_ACTIVE = SynchedEntityData.defineId(CreakingEntity::class.java, EntityDataSerializers.BOOLEAN)
    val IS_TEARING_DOWN = SynchedEntityData.defineId(CreakingEntity::class.java, EntityDataSerializers.BOOLEAN)
    val HOME_POS = SynchedEntityData.defineId(CreakingEntity::class.java, EntityDataSerializers.OPTIONAL_BLOCK_POS)

    fun cancelKnockback(event: LivingKnockBackEvent){
      if(event.entity is CreakingEntity){
        event.isCanceled = true
      }
    }
  }
}
