package com.dannbrown.palegardenbackport.content.placerTypes

import com.dannbrown.palegardenbackport.ModContent
import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.world.level.levelgen.feature.trunkplacers.DarkOakTrunkPlacer
import net.minecraft.world.level.levelgen.feature.trunkplacers.TrunkPlacerType

class PaleOakTrunkPlacer(p_70077_: Int, p_70078_: Int, p_70079_: Int): DarkOakTrunkPlacer(p_70077_, p_70078_, p_70079_) {
  companion object{
    val CODEC: Codec<PaleOakHeartTrunkPlacer> = RecordCodecBuilder.create { p_70090_: RecordCodecBuilder.Instance<PaleOakHeartTrunkPlacer> ->
      trunkPlacerParts(p_70090_)
        .apply(p_70090_) { p_70077_: Int?, p_70078_: Int?, p_70079_: Int? -> PaleOakHeartTrunkPlacer(p_70077_!!, p_70078_!!, p_70079_!!) }
    }
  }

  override fun type(): TrunkPlacerType<*> {
    return ModContent.PALE_OAK_TRUNK_PLACER.get()
  }
}