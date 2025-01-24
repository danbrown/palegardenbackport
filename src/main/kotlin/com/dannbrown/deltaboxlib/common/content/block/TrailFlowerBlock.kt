package com.dannbrown.deltaboxlib.common.content.block

import com.dannbrown.deltaboxlib.common.content.particle.trail.TrailParticleOption
import net.minecraft.core.BlockPos
import net.minecraft.core.particles.ParticleOptions
import net.minecraft.server.level.ServerLevel
import net.minecraft.util.RandomSource
import net.minecraft.world.level.block.FlowerBlock
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.effect.MobEffects
import net.minecraft.world.phys.Vec3
import java.util.function.Supplier

class TrailFlowerBlock(properties: Properties) :
    FlowerBlock(Supplier { MobEffects.REGENERATION }, 10, properties) {

    override fun randomTick(blockState: BlockState, serverLevel: ServerLevel, blockPos: BlockPos, randomSource: RandomSource) {
        for (i in 1..10){
            spawnTransformParticle(serverLevel, blockPos, randomSource)
        }
    }

    private fun spawnTransformParticle(level: ServerLevel, pos: BlockPos, random: RandomSource) {
        val vec3 = pos.center
        val d0 = 0.5 + random.nextDouble()
        val vec31 = Vec3(random.nextDouble() - 0.5, random.nextDouble() + 1.0, random.nextDouble() - 0.5)
        val vec32 = vec3.add(vec31.scale(d0))
        val trailparticleoption = TrailParticleOption(vec32, 16545810, (20.0 * d0).toInt())
        level.sendParticles<ParticleOptions>(trailparticleoption, vec3.x, vec3.y, vec3.z, 1, 0.0, 0.0, 0.0, 0.0)
      }
}
