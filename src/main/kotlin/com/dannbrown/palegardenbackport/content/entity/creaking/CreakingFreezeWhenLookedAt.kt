package com.dannbrown.palegardenbackport.content.entity.creaking

import com.dannbrown.palegardenbackport.init.ModSounds
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.ai.goal.Goal
import net.minecraft.world.entity.player.Player
import java.util.*

class CreakingFreezeWhenLookedAt(private val creakingEntity: CreakingEntity) : Goal() {
  private var target: LivingEntity? = null

  init {
    this.flags = EnumSet.of(Flag.JUMP, Flag.MOVE)
  }

  override fun canUse(): Boolean {
    this.target = creakingEntity.target
    return if (target !is Player) false
    else creakingEntity.isLookingAtMe(target as Player, creakingEntity)
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