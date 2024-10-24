package com.dannbrown.palegardenbackport.content.placerTypes

import com.dannbrown.palegardenbackport.ModContent
import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.util.valueproviders.IntProvider
import net.minecraft.world.level.levelgen.feature.foliageplacers.DarkOakFoliagePlacer
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacerType

class PaleOakFoliagePlacer(radius: IntProvider, offset: IntProvider): DarkOakFoliagePlacer( radius,  offset) {
  companion object{
    val CODEC: Codec<DarkOakFoliagePlacer> = RecordCodecBuilder.create { p_68473_: RecordCodecBuilder.Instance<DarkOakFoliagePlacer> ->
      foliagePlacerParts(p_68473_).apply(p_68473_) { p_161384_: IntProvider?, p_161385_: IntProvider? -> DarkOakFoliagePlacer(p_161384_, p_161385_) }
    }
  }

  override fun type(): FoliagePlacerType<*> {
    return ModContent.PALE_OAK_FOLIAGE_PLACER.get()
  }
}