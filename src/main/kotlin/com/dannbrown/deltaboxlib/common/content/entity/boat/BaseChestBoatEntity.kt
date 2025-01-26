package com.dannbrown.deltaboxlib.common.content.entity.boat

import net.minecraft.nbt.CompoundTag
import net.minecraft.network.syncher.EntityDataAccessor
import net.minecraft.network.syncher.EntityDataSerializers
import net.minecraft.network.syncher.SynchedEntityData
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.vehicle.Boat
import net.minecraft.world.entity.vehicle.ChestBoat
import net.minecraft.world.item.Item
import net.minecraft.world.level.Level
import java.util.function.Supplier

class BaseChestBoatEntity(
  private val boatItem: Supplier<Item>,
  val variant: String,
  private val pEntityType: Supplier<EntityType<out Boat>>,
  pLevel: Level
) : ChestBoat(pEntityType.get(), pLevel) {
  constructor(
    boatItem: Supplier<Item>,
    variant: String,
    pEntityType: Supplier<EntityType<out Boat>>,
    level: Level,
    pX: Double,
    pY: Double,
    pZ: Double
  ) : this(boatItem, variant, pEntityType, level) {
    this.setPos(pX, pY, pZ)
    this.xo = pX
    this.yo = pY
    this.zo = pZ
    setVariant(variant)
  }

  init {
    setVariant(variant)
  }

  override fun getDropItem(): Item {
    return boatItem.get()
  }

  fun setVariant(name: String) {
    try {
      entityData.set(BaseBoatEntity.DATA_ID_TYPE, name)
    } catch (err: IllegalArgumentException) {
      throw IllegalArgumentException("Invalid boat variant: $name")
    }
  }

  /*? if >1.21 {*/
  /*override fun defineSynchedData(arg: SynchedEntityData.Builder) {
    super.defineSynchedData(arg)
    arg.define(BaseBoatEntity.DATA_ID_TYPE, "oak") // Default variant
  }
  *//*?} else {*/
  override fun defineSynchedData() {
  super.defineSynchedData()
  entityData.define(BaseBoatEntity.DATA_ID_TYPE, "oak") // Default variant
}
  /*?}*/

  override fun addAdditionalSaveData(pCompound: CompoundTag) {
    /*? if >1.21 {*/
    /*this.writeLeashData(pCompound, this.leashData)
    this.addChestVehicleSaveData(pCompound, this.registryAccess())
    *//*?} else {*/
    this.addChestVehicleSaveData(pCompound)
    /*?}*/
    pCompound.putString("Type", this.entityData.get(BaseBoatEntity.DATA_ID_TYPE))
  }

  override fun readAdditionalSaveData(pCompound: CompoundTag) {
    /*? if >1.21 {*/
    /*this.leashData = this.readLeashData(pCompound)
    this.readChestVehicleSaveData(pCompound, this.registryAccess())
    *//*?} else {*/
    this.readChestVehicleSaveData(pCompound)
    /*?}*/
    if (pCompound.contains("Type", 8)) {
      this.setVariant(pCompound.getString("Type"))
    }
  }
}
