package com.dannbrown.palegardenbackport.init

import com.dannbrown.palegardenbackport.content.blocks.eyeblossom.EyeBlossomRenderer
import net.minecraft.client.model.geom.ModelLayerLocation
import com.dannbrown.palegardenbackport.init.ModContent.REGISTRATE

object ModModelLayers {
  val EYE_BLOSSOM: ModelLayerLocation = REGISTRATE.modelLayer("eyeblossom/eye", EyeBlossomRenderer::createEyeLayer)

  fun register() {
    // init
  }
}