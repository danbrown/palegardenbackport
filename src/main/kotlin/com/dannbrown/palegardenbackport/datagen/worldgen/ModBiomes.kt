package com.dannbrown.palegardenbackport.datagen.worldgen

import com.dannbrown.deltaboxlib.registry.worldgen.AbstractBiome
import com.dannbrown.palegardenbackport.datagen.worldgen.biome.PaleGardenBiome
import net.minecraft.data.worldgen.BootstapContext
import net.minecraft.world.level.biome.Biome


object ModBiomes {
  val BIOMES: MutableList<AbstractBiome> = ArrayList()

  init {
    // Planet Zero
    BIOMES.add(PaleGardenBiome)
  }

  fun bootstrap(context: BootstapContext<Biome>) {
    for (biome in BIOMES) {
      biome.bootstrapBiome(context)
    }
  }
}