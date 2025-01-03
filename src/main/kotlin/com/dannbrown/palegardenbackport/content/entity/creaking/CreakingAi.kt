package com.dannbrown.palegardenbackport.content.entity.creaking

import com.google.common.collect.ImmutableList
import com.google.common.collect.ImmutableSet
import com.mojang.datafixers.util.Pair
import net.minecraft.server.level.ServerLevel
import net.minecraft.util.valueproviders.UniformInt
import net.minecraft.world.entity.Mob
import net.minecraft.world.entity.ai.Brain
import net.minecraft.world.entity.ai.behavior.Behavior
import net.minecraft.world.entity.ai.behavior.BehaviorControl
import net.minecraft.world.entity.ai.behavior.DoNothing
import net.minecraft.world.entity.ai.behavior.LookAtTargetSink
import net.minecraft.world.entity.ai.behavior.MeleeAttack
import net.minecraft.world.entity.ai.behavior.MoveToTargetSink
import net.minecraft.world.entity.ai.behavior.RandomStroll
import net.minecraft.world.entity.ai.behavior.RunOne
import net.minecraft.world.entity.ai.behavior.SetEntityLookTargetSometimes
import net.minecraft.world.entity.ai.behavior.SetWalkTargetFromAttackTargetIfTargetOutOfReach
import net.minecraft.world.entity.ai.behavior.SetWalkTargetFromLookTarget
import net.minecraft.world.entity.ai.behavior.StartAttacking
import net.minecraft.world.entity.ai.behavior.StopAttackingIfTargetInvalid
import net.minecraft.world.entity.ai.behavior.Swim
import net.minecraft.world.entity.ai.memory.MemoryModuleType
import net.minecraft.world.entity.ai.sensing.SensorType
import net.minecraft.world.entity.schedule.Activity

object CreakingAi {
  val SENSOR_TYPES = ImmutableList.of(SensorType.NEAREST_PLAYERS)
  val MEMORY_TYPES: ImmutableList<out MemoryModuleType<*>> = ImmutableList.of(
        MemoryModuleType.NEAREST_PLAYERS,
        MemoryModuleType.NEAREST_LIVING_ENTITIES,
        MemoryModuleType.NEAREST_VISIBLE_LIVING_ENTITIES,
        MemoryModuleType.NEAREST_VISIBLE_PLAYER,
        MemoryModuleType.NEAREST_VISIBLE_ATTACKABLE_PLAYER,
        MemoryModuleType.LOOK_TARGET,
        MemoryModuleType.WALK_TARGET,
        MemoryModuleType.CANT_REACH_WALK_TARGET_SINCE,
        MemoryModuleType.PATH,
        MemoryModuleType.ATTACK_TARGET,
        MemoryModuleType.ATTACK_COOLING_DOWN)
  fun brainProvider(): Brain.Provider<CreakingEntity> {
    return Brain.provider(MEMORY_TYPES, SENSOR_TYPES)
  }

  fun initCoreActivity(brain: Brain<CreakingEntity>) {
    brain.addActivity(Activity.CORE, 0, ImmutableList.of<Behavior<Mob>>(
      object : Swim(0.8f) {
        override fun checkExtraStartConditions(pLevel: ServerLevel, pOwner: Mob): Boolean {
          return (pOwner as CreakingEntity).canMove() ?: false
        }
      },
      LookAtTargetSink(45, 90),
      MoveToTargetSink()
    ))
  }

  fun initIdleActivity(brain: Brain<CreakingEntity>) {
    brain.addActivity(Activity.IDLE, 10, ImmutableList.of(
      StartAttacking.create({ mob: CreakingEntity -> mob.isActive() },
        { mob: CreakingEntity -> mob.brain.getMemory(MemoryModuleType.NEAREST_VISIBLE_ATTACKABLE_PLAYER) }),
      SetEntityLookTargetSometimes.create(8.0f, UniformInt.of(30, 60)),
      RunOne(ImmutableList.of(
        Pair.of(RandomStroll.stroll(0.3f), 2),
        Pair.of(SetWalkTargetFromLookTarget.create(0.3f, 3), 2),
        Pair.of(DoNothing(30, 60), 1)
      ))
    ))
  }

  fun initFightActivity(brain: Brain<CreakingEntity>) {
    brain.addActivityAndRemoveMemoryWhenStopped(
      Activity.FIGHT, 10, ImmutableList.of<BehaviorControl<Mob>>(
        SetWalkTargetFromAttackTargetIfTargetOutOfReach.create(1.0f),
        MeleeAttack.create(40),
        StopAttackingIfTargetInvalid.create<Mob>()
      ), MemoryModuleType.ATTACK_TARGET
    )
  }

  fun makeBrain(brain: Brain<CreakingEntity>): Brain<CreakingEntity> {
    initCoreActivity(brain)
    initIdleActivity(brain)
    initFightActivity(brain)
    brain.setCoreActivities(ImmutableSet.of(Activity.CORE))
    brain.setDefaultActivity(Activity.IDLE)
    brain.useDefaultActivity()
    return brain
  }

  fun updateActivity(creakingEntity: CreakingEntity) {
    if (creakingEntity.canMove()) {
      creakingEntity.brain.setActiveActivityToFirstValid(ImmutableList.of(Activity.FIGHT, Activity.IDLE))
    } else {
      creakingEntity.brain.setActiveActivityToFirstValid(ImmutableList.of(Activity.CORE))
    }
  }
}
