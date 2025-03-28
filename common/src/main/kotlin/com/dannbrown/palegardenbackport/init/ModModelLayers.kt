package com.dannbrown.palegardenbackport.init

import com.dannbrown.palegardenbackport.content.blocks.eyeblossom.EyeBlossomRenderer
import com.dannbrown.palegardenbackport.content.entity.creaking.CreakingModel
import com.dannbrown.palegardenbackport.init.ModContent.REGISTRATE

object ModModelLayers {
  val EYE_BLOSSOM = REGISTRATE.modelLayer("eyeblossom/eye", { EyeBlossomRenderer.createEyeLayer() })
  val CREAKING = REGISTRATE.modelLayer("creaking", CreakingModel::create)


  fun register() {
    // init
  }
}