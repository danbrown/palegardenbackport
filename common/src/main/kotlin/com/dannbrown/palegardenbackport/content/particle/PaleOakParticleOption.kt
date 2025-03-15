package com.dannbrown.palegardenbackport.content.particle

import com.dannbrown.palegardenbackport.init.ModParticles
import net.minecraft.core.particles.SimpleParticleType

class PaleOakParticleOption : SimpleParticleType(false) {
  override fun getType(): SimpleParticleType {
    return ModParticles.PALE_OAK_LEAVES.get() as SimpleParticleType
  }
}
