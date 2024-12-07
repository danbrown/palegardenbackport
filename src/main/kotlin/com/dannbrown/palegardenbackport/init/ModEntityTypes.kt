package com.dannbrown.palegardenbackport.init

import com.dannbrown.palegardenbackport.ModContent
import com.dannbrown.palegardenbackport.content.entity.creaking.AbstractCreaking
import com.dannbrown.palegardenbackport.content.entity.creaking.CreakingEntity
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.MobCategory
import net.minecraftforge.event.entity.EntityAttributeCreationEvent
import net.minecraftforge.eventbus.api.IEventBus
import net.minecraftforge.registries.DeferredRegister
import net.minecraftforge.registries.ForgeRegistries
import net.minecraftforge.registries.RegistryObject
import java.util.function.Supplier

object ModEntityTypes {
  val ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, ModContent.MOD_ID)

  val CreakingEntity: RegistryObject<EntityType<CreakingEntity>> = ENTITY_TYPES.register("creaking",
    Supplier {
      EntityType.Builder.of(::CreakingEntity, MobCategory.CREATURE)
        .fireImmune()
        .sized(0.9F, 2.7F).clientTrackingRange(8)
        .build("${ModContent.MOD_ID}:creaking")
    })

    fun register(bus: IEventBus) {
      ENTITY_TYPES.register(bus)
    }

  fun registerEntityAttributes(event: EntityAttributeCreationEvent) {
    event.put(CreakingEntity.get(), AbstractCreaking.registerAttributes().build())
  }
}