package com.dannbrown.deltaboxlib.common.init

import com.dannbrown.deltaboxlib.common.DeltaboxLibCommon
import com.dannbrown.deltaboxlib.common.content.particle.trail.TrailParticle
import com.dannbrown.deltaboxlib.common.content.particle.trail.TrailParticleObject

object DeltaboxParticles {
  val TRAIL = DeltaboxLibCommon.REGISTRATE.particleType("trail", { TrailParticleObject(false) }, { TrailParticle.Provider(it) })

  fun register(){
    // init class
  }
}