package com.dannbrown.palegardenbackport.datagen.worldgen.biome

import com.dannbrown.deltaboxlib.registry.worldgen.AbstractBiome
import com.dannbrown.palegardenbackport.ModContent
import com.dannbrown.palegardenbackport.datagen.worldgen.ModPlacedFeatures
import net.minecraft.core.registries.Registries
import net.minecraft.data.worldgen.BiomeDefaultFeatures
import net.minecraft.data.worldgen.BootstapContext
import net.minecraft.data.worldgen.placement.VegetationPlacements
import net.minecraft.resources.ResourceKey
import net.minecraft.resources.ResourceLocation
import net.minecraft.sounds.Musics
import net.minecraft.sounds.SoundEvents
import net.minecraft.util.Mth
import net.minecraft.world.level.biome.AmbientMoodSettings
import net.minecraft.world.level.biome.Biome
import net.minecraft.world.level.biome.BiomeGenerationSettings
import net.minecraft.world.level.biome.BiomeSpecialEffects
import net.minecraft.world.level.biome.MobSpawnSettings
import net.minecraft.world.level.levelgen.GenerationStep

object PaleGardenBiome : AbstractBiome() {
  override val biomeId: String = "pale_garden"
  override val BIOME_KEY: ResourceKey<Biome> = ResourceKey.create(Registries.BIOME, ResourceLocation(ModContent.MOD_ID, biomeId))
  override fun createBiome(context: BootstapContext<Biome>): Biome {
    val placedFeatures = context.lookup(Registries.PLACED_FEATURE)
    val caveGetter = context.lookup(Registries.CONFIGURED_CARVER)

    val mobSpawnSettingsBuilder = MobSpawnSettings.Builder()
    BiomeDefaultFeatures.commonSpawns(mobSpawnSettingsBuilder)
    val biomeGenerationSettingsBuilder = BiomeGenerationSettings.Builder(placedFeatures, caveGetter)
    BiomeDefaultFeatures.addDefaultCarversAndLakes(biomeGenerationSettingsBuilder)
    BiomeDefaultFeatures.addDefaultCrystalFormations(biomeGenerationSettingsBuilder)
    BiomeDefaultFeatures.addDefaultMonsterRoom(biomeGenerationSettingsBuilder)
    BiomeDefaultFeatures.addDefaultUndergroundVariety(biomeGenerationSettingsBuilder)
    BiomeDefaultFeatures.addDefaultSprings(biomeGenerationSettingsBuilder)
    BiomeDefaultFeatures.addSurfaceFreezing(biomeGenerationSettingsBuilder)
    biomeGenerationSettingsBuilder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, ModPlacedFeatures.PALE_GARDEN_VEGETATION)
    biomeGenerationSettingsBuilder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, ModPlacedFeatures.PALE_GARDEN_FLOWERS);
    BiomeDefaultFeatures.addDefaultOres(biomeGenerationSettingsBuilder)
    BiomeDefaultFeatures.addDefaultSoftDisks(biomeGenerationSettingsBuilder)
    BiomeDefaultFeatures.addForestGrass(biomeGenerationSettingsBuilder)
    BiomeDefaultFeatures.addDefaultMushrooms(biomeGenerationSettingsBuilder)
    BiomeDefaultFeatures.addDefaultExtraVegetation(biomeGenerationSettingsBuilder)

    return Biome.BiomeBuilder()
      .hasPrecipitation(true)
      .temperature(0.7f)
      .downfall(0.8f)
      .specialEffects(
        BiomeSpecialEffects.Builder()
          .waterColor(0x76889D)
          .waterFogColor(0x556980)
          .fogColor(0x817770)
          .skyColor(0xB9B9B9)
          .grassColorOverride(0x778272)
          .foliageColorOverride(0x878D76)
          .ambientMoodSound(AmbientMoodSettings.LEGACY_CAVE_SETTINGS)
          .build()
      )
      .mobSpawnSettings(mobSpawnSettingsBuilder.build())
      .generationSettings(biomeGenerationSettingsBuilder.build())
      .build()
  }


}