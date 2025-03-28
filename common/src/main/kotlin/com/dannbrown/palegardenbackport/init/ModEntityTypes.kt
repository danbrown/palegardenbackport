package com.dannbrown.palegardenbackport.init

import com.dannbrown.palegardenbackport.content.entity.creaking.CreakingEntity
import com.dannbrown.palegardenbackport.content.entity.creaking.CreakingRenderer
import com.dannbrown.palegardenbackport.init.ModContent.REGISTRATE
import net.minecraft.world.entity.ai.attributes.Attributes
import java.util.function.Function

object ModEntityTypes {
  val CREAKING = REGISTRATE.entityType<CreakingEntity>("creaking")
    .factory { type, level -> CreakingEntity(type, level) }
    .properties { p ->
      p.fireImmune()
        .fireImmune()
        .sized(0.9F, 2.7F)
        .clientTrackingRange(8)
    }
    .renderer { Function { ctx -> CreakingRenderer(ctx) } }
    .attributes { u ->
      u.createMonsterAttributes()
        .add(Attributes.MAX_HEALTH, 1.0)
        .add(Attributes.MOVEMENT_SPEED, 0.4000000059604645)
        .add(Attributes.ATTACK_DAMAGE, 3.0)
        .add(Attributes.FOLLOW_RANGE, 32.0)
    }
    .register()

  fun register() {
    REGISTRATE.buildEntityTypes()
  }
}