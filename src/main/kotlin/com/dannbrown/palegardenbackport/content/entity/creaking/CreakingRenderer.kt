package com.dannbrown.palegardenbackport.content.entity.creaking

import com.dannbrown.palegardenbackport.ModContent
import com.dannbrown.palegardenbackport.init.ModModelLayers
import net.minecraft.client.renderer.entity.EntityRendererProvider
import net.minecraft.client.renderer.entity.MobRenderer
import net.minecraft.resources.ResourceLocation

class CreakingRenderer(ctx: EntityRendererProvider.Context) : MobRenderer<CreakingEntity, CreakingModel>(ctx, CreakingModel(ctx.bakeLayer(ModModelLayers.CREAKING)), 0.5f) {
  override fun getTextureLocation(creakingEntity: CreakingEntity): ResourceLocation {
    return ResourceLocation(ModContent.MOD_ID, "textures/entity/creaking.png"
    )
  }


}
