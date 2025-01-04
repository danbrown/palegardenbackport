package com.dannbrown.palegardenbackport.terrablender

import com.dannbrown.palegardenbackport.datagen.worldgen.biome.PaleGardenBiome
import com.dannbrown.palegardenbackport.init.ModCommonConfig
import com.mojang.datafixers.util.Pair
import net.minecraft.core.Registry
import net.minecraft.resources.ResourceKey
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.level.biome.Biome
import net.minecraft.world.level.biome.Biomes
import net.minecraft.world.level.biome.Climate
import terrablender.api.Region
import terrablender.api.RegionType
import java.util.function.Consumer

class ModOverworldRegion(name: ResourceLocation, weight: Int): Region(name, RegionType.OVERWORLD, weight) {
  override fun addBiomes(registry: Registry<Biome>?, mapper: Consumer<Pair<Climate.ParameterPoint, ResourceKey<Biome>>>?) {
    if(ModCommonConfig.PALE_GARDEN_ENABLED?.get()!!){
      this.addModifiedVanillaOverworldBiomes(mapper) { builder -> builder.replaceBiome(Biomes.DARK_FOREST, PaleGardenBiome.BIOME_KEY) }
    }
  }
}