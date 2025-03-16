package com.dannbrown.palegardenbackport.init

import com.dannbrown.palegardenbackport.content.blocks.eyeblossom.EyeBlossomRenderer
import com.dannbrown.palegardenbackport.content.entity.creaking.CreakingModel
import net.minecraft.client.model.geom.ModelLayerLocation
import com.dannbrown.palegardenbackport.init.ModContent.REGISTRATE

object ModModelLayers {
  val EYE_BLOSSOM: ModelLayerLocation = REGISTRATE.modelLayer("eyeblossom/eye", EyeBlossomRenderer::createEyeLayer)
  val CREAKING: ModelLayerLocation = REGISTRATE.modelLayer("creaking", CreakingModel::create)


  fun register() {
    // init
  }
}