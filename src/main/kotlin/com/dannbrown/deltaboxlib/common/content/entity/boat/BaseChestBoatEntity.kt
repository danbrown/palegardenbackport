package com.dannbrown.deltaboxlib.common.content.entity.boat

import com.dannbrown.deltaboxlib.common.init.DeltaboxBoatVariants
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

class BaseChestBoatEntity(private val boatItem: Supplier<Item>, pEntityType: Supplier<EntityType<out Boat>>, pLevel: Level) : ChestBoat(pEntityType.get(), pLevel) {
  constructor(boatItem: Supplier<Item>, pEntityType: Supplier<EntityType<out Boat>>, pLevel: Level, pX: Double, pY: Double, pZ: Double) : this(boatItem, pEntityType, pLevel) {
    this.setPos(pX, pY, pZ)
    this.xo = pX
    this.yo = pY
    this.zo = pZ
  }

  override fun getDropItem(): Item {
    val variantName = modVariant
    return boatItem.get()
  }

  fun setVariant(name: String) {
    if (DeltaboxBoatVariants.VARIANT_MAP.containsKey(name)) {
      entityData.set(DATA_ID_TYPE, name)
    }
    else {
      throw IllegalArgumentException("Invalid chest boat variant: $name")
    }
  }

  override fun defineSynchedData() {
    super.defineSynchedData()
    entityData.define(DATA_ID_TYPE, "pine") // Default variant
  }

  override fun addAdditionalSaveData(pCompound: CompoundTag) {
    pCompound.putString("Type", this.modVariant)
  }

  override fun readAdditionalSaveData(pCompound: CompoundTag) {
    if (pCompound.contains("Type", 8)) {
      this.setVariant(pCompound.getString("Type"))
    }
  }

  val modVariant: String
    get() = entityData.get(DATA_ID_TYPE)

  companion object {
    private val DATA_ID_TYPE: EntityDataAccessor<String> = SynchedEntityData.defineId(ChestBoat::class.java, EntityDataSerializers.STRING)
  }
}
