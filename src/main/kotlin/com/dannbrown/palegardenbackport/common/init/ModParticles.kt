package com.dannbrown.palegardenbackport.common.init

import com.dannbrown.palegardenbackport.common.ModCommon
import com.dannbrown.palegardenbackport.common.content.particle.PaleOakParticle
import net.minecraft.core.particles.SimpleParticleType

object ModParticles {
  val PALE_OAK_LEAVES = ModCommon.REGISTRATE.particleType("pale_oak_leaves", { SimpleParticleType(true) }, { PaleOakParticle.Provider(it) })

  fun register(){
    // init class
  }
}