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
//    if(!creakingEntity.level().isClientSide){
//      if(target is Player) {
//        val looking = creakingEntity.isLookingAtMe(target as Player, creakingEntity)
//        if(looking && creakingEntity.isActive()) {
//          creakingEntity.deactivate()
//        } else {
//          creakingEntity.activate(target as Player)
//        }
////        val canMove = checkCanMove(target as Player)
////        val canMoveFlag = creakingEntity.canMove()
////        if (canMove != canMoveFlag) {
////          if (canMove) {
////            creakingEntity.playSound(ModSounds.CREAKING_UNFREEZE.get())
////          } else {
////            creakingEntity.playSound(ModSounds.CREAKING_FREEZE.get())
////          }
////        }
////        creakingEntity.setCanMove(canMove)
//      }
//    }
  }

//  fun checkCanMove(player: Player?): Boolean {
//    val currentActiveState = creakingEntity.isActive()
//
//    if (player === null) {
//      if (currentActiveState) {
////        creakingEntity.deactivate()
//      }
//      return true
//    } else {
//      // TODO: Check attack and alliance logic (unfinished)
//      if (creakingEntity.isLookingAtMe(player, creakingEntity)) {
//        if (currentActiveState) {
//          return false
//        }
//
//        if (player.distanceToSqr(creakingEntity) < 144.0) {
////          creakingEntity.activate(player)
//          return false
//        }
//      }
//    }
//    return true
//  }
}