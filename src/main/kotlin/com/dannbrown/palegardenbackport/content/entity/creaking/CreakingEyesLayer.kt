package com.dannbrown.palegardenbackport.content.entity.creaking

import com.dannbrown.palegardenbackport.ModContent
import net.minecraft.client.renderer.RenderType
import net.minecraft.client.renderer.entity.RenderLayerParent
import net.minecraft.client.renderer.entity.layers.EyesLayer
import net.minecraft.resources.ResourceLocation
import net.minecraftforge.api.distmarker.Dist
import net.minecraftforge.api.distmarker.OnlyIn

@OnlyIn(Dist.CLIENT)
class CreakingEyesLayer(renderLayerParent: RenderLayerParent<CreakingEntity, CreakingModel>) : EyesLayer<CreakingEntity, CreakingModel>(renderLayerParent) {
  override fun renderType(): RenderType {
    return CREAKING_EYES
  }

  companion object {
    private val CREAKING_EYES: RenderType = RenderType.eyes(ResourceLocation(ModContent.MOD_ID, "textures/entity/creaking/creaking_eyes.png"))
  }
}