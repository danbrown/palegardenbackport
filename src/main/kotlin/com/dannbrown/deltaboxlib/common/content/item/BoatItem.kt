package com.dannbrown.deltaboxlib.common.content.item

import com.dannbrown.deltaboxlib.common.content.entity.boat.BaseBoatEntity
import com.dannbrown.deltaboxlib.common.content.entity.boat.BaseChestBoatEntity
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import java.util.function.Predicate;
import java.util.function.Supplier

class BoatItem(
  private val _name: String,
  private val entityType: Supplier<EntityType<out Boat>>,
  private val hasChest: Boolean,
  pProperties: Properties
) : Item(pProperties) {
  override fun use(pLevel: Level, pPlayer: Player, pHand: InteractionHand): InteractionResultHolder<ItemStack> {
    val itemstack: ItemStack = pPlayer.getItemInHand(pHand)
    val hitresult: HitResult = getPlayerPOVHitResult(pLevel, pPlayer, ClipContext.Fluid.ANY)
    if (hitresult.type == HitResult.Type.MISS) {
      return InteractionResultHolder.pass(itemstack)
    } else {
      val vec3: Vec3 = pPlayer.getViewVector(1.0f)
      val list: MutableList<Entity> = pLevel.getEntities(
        pPlayer,
        pPlayer.getBoundingBox()
          .expandTowards(vec3.scale(5.0))
          .inflate(1.0),
        ENTITY_PREDICATE
      )
      if (!list.isEmpty()) {
        val vec31: Vec3 = pPlayer.getEyePosition()

        for (entity in list) {
          val aabb: AABB = entity.boundingBox
            .inflate(entity.pickRadius.toDouble())
          if (aabb.contains(vec31)) {
            return InteractionResultHolder.pass(itemstack)
          }
        }
      }

      if (hitresult.type == HitResult.Type.BLOCK) {
        val boat = this.getBoat(pLevel, hitresult)
        if (boat is BaseChestBoatEntity) {
          boat.setVariant(_name)
        } else if (boat is BaseBoatEntity) {
          boat.setVariant(_name)
        }
        boat.yRot = pPlayer.getYRot()
        if (!pLevel.noCollision(boat, boat.boundingBox)) {
          return InteractionResultHolder.fail(itemstack)
        } else {
          if (!pLevel.isClientSide) {
            pLevel.addFreshEntity(boat)
            pLevel.gameEvent(pPlayer, GameEvent.ENTITY_PLACE, hitresult.location)
            if (!pPlayer.getAbilities().instabuild) {
              itemstack.shrink(1)
            }
          }

          pPlayer.awardStat(Stats.ITEM_USED.get(this))
          return InteractionResultHolder.sidedSuccess(itemstack, pLevel.isClientSide())
        }
      } else {
        return InteractionResultHolder.pass(itemstack)
      }
    }
  }

  private fun getBoat(level: Level, hitResult: HitResult): Boat {
    return (if (this.hasChest) BaseChestBoatEntity(
      { this },
      entityType,
      level,
      hitResult.location.x,
      hitResult.location.y,
      hitResult.location.z
    ) else BaseBoatEntity(
      { this },
      entityType,
      level,
      hitResult.location.x,
      hitResult.location.y,
      hitResult.location.z
    ))
  }

  companion object {
    private val ENTITY_PREDICATE: Predicate<Entity> = EntitySelector.NO_SPECTATORS.and(Entity::isPickable)
  }
}
