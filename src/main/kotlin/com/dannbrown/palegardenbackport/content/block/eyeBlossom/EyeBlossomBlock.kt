package com.dannbrown.palegardenbackport.content.block.eyeBlossom

import com.dannbrown.palegardenbackport.ModContent
import com.dannbrown.palegardenbackport.content.particle.TrailParticleOption
import com.dannbrown.palegardenbackport.init.ModSounds
import net.minecraft.core.BlockPos
import net.minecraft.core.particles.ParticleOptions
import net.minecraft.server.level.ServerLevel
import net.minecraft.sounds.SoundEvent
import net.minecraft.sounds.SoundSource
import net.minecraft.util.RandomSource
import net.minecraft.world.effect.MobEffect
import net.minecraft.world.effect.MobEffectInstance
import net.minecraft.world.effect.MobEffects
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.animal.Bee
import net.minecraft.world.entity.player.Player
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.EntityBlock
import net.minecraft.world.level.block.FlowerBlock
import net.minecraft.world.level.block.RenderShape
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.gameevent.GameEvent
import net.minecraft.world.phys.Vec3
import java.util.function.Consumer
import kotlin.math.sqrt

class EyeBlossomBlock(open: Boolean, properties: Properties) : FlowerBlock({ Type.fromBoolean(open).effect }, Type.fromBoolean(open).effectDuration.toInt(), properties), EntityBlock {
  private val type: Type = Type.fromBoolean(open)

  override fun animateTick(blockState: BlockState, level: Level, blockPos: BlockPos, randomSource: RandomSource) {
    if (type.emitSounds() && randomSource.nextInt(700) == 0) {
      val blockstate = level.getBlockState(blockPos.below())
      if (blockstate.`is`(ModContent.PALE_MOSS_BLOCK.get())) {
        level.playLocalSound(blockPos.x.toDouble(), blockPos.y.toDouble(), blockPos.z.toDouble(), ModSounds.EYEBLOSSOM_IDLE.get(), SoundSource.BLOCKS, 1.0f, 1.0f, false)
      }
    }
  }

  override fun randomTick(blockState: BlockState, serverLevel: ServerLevel, blockPos: BlockPos, randomSource: RandomSource) {
    if (this.tryChangingState(blockState, serverLevel, blockPos, randomSource)) {
      serverLevel.playSound(null as Player?, blockPos, type.transform().longSwitchSound, SoundSource.BLOCKS, 1.0f, 1.0f)
    }

    super.randomTick(blockState, serverLevel, blockPos, randomSource)
  }

  override fun tick(blockState: BlockState, serverLevel: ServerLevel, blockPos: BlockPos, randomSource: RandomSource) {
    if (this.tryChangingState(blockState, serverLevel, blockPos, randomSource)) {
      serverLevel.playSound(null as Player?, blockPos, type.transform().shortSwitchSound, SoundSource.BLOCKS, 1.0f, 1.0f)
    }

    super.tick(blockState, serverLevel, blockPos, randomSource)
  }

  private fun tryChangingState(state: BlockState, level: ServerLevel, pos: BlockPos, random: RandomSource): Boolean {
    if (!level.dimensionType()
        .natural()) {
      return false
    }
    else if (level.isDay != type.open) {
      return false
    }
    else {
      val `eyeblossomblock$type` = type.transform()
      level.setBlock(pos, `eyeblossomblock$type`.state(), 3)
      level.gameEvent(GameEvent.BLOCK_CHANGE, pos, GameEvent.Context.of(state))
      `eyeblossomblock$type`.spawnTransformParticle(level, pos, random)
      BlockPos.betweenClosed(pos.offset(-3, -2, -3), pos.offset(3, 2, 3))
        .forEach(Consumer { blockPos: BlockPos ->
          val _blockstate = level.getBlockState(blockPos)
          if (_blockstate === state) {
            val d0 = sqrt(pos.distSqr(blockPos))
            val i = random.nextIntBetweenInclusive((d0 * 5.0).toInt(), (d0 * 10.0).toInt())
            level.scheduleTick(blockPos, state.block, i)
          }
        })
      return true
    }
  }

  override fun entityInside(pState: BlockState, pLevel: Level, pPos: BlockPos, pEntity: Entity) {
    super.entityInside(pState, pLevel, pPos, pEntity)
    if (pEntity is Bee) {
      pEntity.addEffect(beeInteractionEffect)
    }
  }

  val beeInteractionEffect: MobEffectInstance
    get() = MobEffectInstance(MobEffects.POISON, 25, 0)

  enum class Type(val open: Boolean, val effect: MobEffect, val effectDuration: Float, val longSwitchSound: SoundEvent, val shortSwitchSound: SoundEvent, private val particleColor: Int) {
    OPEN(true, MobEffects.BLINDNESS, 11.0f, ModSounds.EYEBLOSSOM_OPEN_LONG.get(), ModSounds.EYEBLOSSOM_OPEN.get(), 16545810),
    CLOSED(false, MobEffects.CONFUSION, 7.0f, ModSounds.EYEBLOSSOM_CLOSE_LONG.get(), ModSounds.EYEBLOSSOM_CLOSE.get(), 6250335);

    fun block(): Block {
      return if (this.open) ModContent.EYE_BLOSSOM.get() else ModContent.CLOSED_EYE_BLOSSOM.get()
    }

    fun state(): BlockState {
      return block().defaultBlockState()
    }

    fun transform(): Type {
      return fromBoolean(!this.open)
    }

    fun emitSounds(): Boolean {
      return this.open
    }

    fun spawnTransformParticle(level: ServerLevel, pos: BlockPos, random: RandomSource) {
      val vec3 = pos.center
      val d0 = 0.5 + random.nextDouble()
      val vec31 = Vec3(random.nextDouble() - 0.5, random.nextDouble() + 1.0, random.nextDouble() - 0.5)
      val vec32 = vec3.add(vec31.scale(d0))
      val trailparticleoption = TrailParticleOption(vec32, this.particleColor, (20.0 * d0).toInt())
      level.sendParticles<ParticleOptions>(trailparticleoption, vec3.x, vec3.y, vec3.z, 1, 0.0, 0.0, 0.0, 0.0)
    }

    companion object {
      fun fromBoolean(open: Boolean): Type {
        return if (open) OPEN else CLOSED
      }
    }
  }

  override fun newBlockEntity(p0: BlockPos, p1: BlockState): BlockEntity? {
    return ModContent.EYEBLOSSOM_BLOCK_ENTITY.get().create(p0, p1)
  }

  override fun getRenderShape(pState: BlockState): RenderShape {
    return RenderShape.MODEL
  }
}