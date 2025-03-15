package com.dannbrown.palegardenbackport.init

import com.dannbrown.deltaboxlib.content.particle.leaves.LeavesParticle
import com.dannbrown.palegardenbackport.content.particle.PaleOakParticleOption
import com.dannbrown.palegardenbackport.init.ModContent.REGISTRATE

object ModParticles {
  val PALE_OAK_LEAVES =
    REGISTRATE.particleType("pale_oak_leaves", { PaleOakParticleOption() }, { LeavesParticle.Provider(it) })


  fun register() {
    // init
  }
}