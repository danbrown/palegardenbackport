package com.dannbrown.deltaboxlib.platform.registrate.generators.worldgen

import com.dannbrown.deltaboxlib.platform.util.DeltaboxUtil
import net.minecraft.core.Holder
import net.minecraft.core.HolderSet
import net.minecraft.core.registries.Registries
import net.minecraft.resources.ResourceKey
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.level.biome.Biome
import net.minecraft.world.level.biome.MobSpawnSettings
import net.minecraft.world.level.levelgen.GenerationStep
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature
import net.minecraft.world.level.levelgen.placement.PlacedFeature

/*? if >=1.21 {*/
/*import net.minecraft.data.worldgen.BootstrapContext
*//*?} else {*/
import net.minecraft.data.worldgen.BootstapContext as BootstrapContext
/*?}*/

object BiomeModifiersUtil: AbstractWorldgenUtil() {
  /*? if forge {*/
  fun registerKey(name: String, modId: String): ResourceKey<net.minecraftforge.common.world.BiomeModifier> {
    return ResourceKey.create(net.minecraftforge.registries.ForgeRegistries.Keys.BIOME_MODIFIERS, DeltaboxUtil.resourceLocation(modId, name))
  }

  // Utils Functions
  fun addOre(biomes: HolderSet<Biome>, feature: Holder<PlacedFeature>): net.minecraftforge.common.world.ForgeBiomeModifiers.AddFeaturesBiomeModifier {
    return net.minecraftforge.common.world.ForgeBiomeModifiers.AddFeaturesBiomeModifier(biomes, HolderSet.direct(feature), GenerationStep.Decoration.UNDERGROUND_ORES)
  }

  fun addVegetation(biomes: HolderSet<Biome>, feature: Holder<PlacedFeature>): net.minecraftforge.common.world.ForgeBiomeModifiers.AddFeaturesBiomeModifier {
    return net.minecraftforge.common.world.ForgeBiomeModifiers.AddFeaturesBiomeModifier(biomes, HolderSet.direct(feature), GenerationStep.Decoration.VEGETAL_DECORATION)
  }

  fun addRawGeneration(biomes: HolderSet<Biome>, feature: Holder<PlacedFeature>): net.minecraftforge.common.world.ForgeBiomeModifiers.AddFeaturesBiomeModifier {
    return net.minecraftforge.common.world.ForgeBiomeModifiers.AddFeaturesBiomeModifier(biomes, HolderSet.direct(feature), GenerationStep.Decoration.RAW_GENERATION)
  }

  fun addMob(biomes: HolderSet<Biome>, spawnerData: List<MobSpawnSettings.SpawnerData>): net.minecraftforge.common.world.ForgeBiomeModifiers.AddSpawnsBiomeModifier {
    return net.minecraftforge.common.world.ForgeBiomeModifiers.AddSpawnsBiomeModifier(biomes, spawnerData)
  }
  /*?} elif neoforge {*/
  /*fun registerKey(name: String, modId: String): ResourceKey<net.neoforged.neoforge.common.world.BiomeModifier> {
    return ResourceKey.create(net.neoforged.neoforge.registries.NeoForgeRegistries.Keys.BIOME_MODIFIERS, DeltaboxUtil.resourceLocation(modId, name))
  }

  // Utils Functions
  fun addOre(biomes: HolderSet<Biome>, feature: Holder<PlacedFeature>): net.neoforged.neoforge.common.world.BiomeModifiers.AddFeaturesBiomeModifier {
    return net.neoforged.neoforge.common.world.BiomeModifiers.AddFeaturesBiomeModifier(biomes, HolderSet.direct(feature), GenerationStep.Decoration.UNDERGROUND_ORES)
  }

  fun addVegetation(biomes: HolderSet<Biome>, feature: Holder<PlacedFeature>): net.neoforged.neoforge.common.world.BiomeModifiers.AddFeaturesBiomeModifier {
    return net.neoforged.neoforge.common.world.BiomeModifiers.AddFeaturesBiomeModifier(biomes, HolderSet.direct(feature), GenerationStep.Decoration.VEGETAL_DECORATION)
  }

  fun addRawGeneration(biomes: HolderSet<Biome>, feature: Holder<PlacedFeature>): net.neoforged.neoforge.common.world.BiomeModifiers.AddFeaturesBiomeModifier {
    return net.neoforged.neoforge.common.world.BiomeModifiers.AddFeaturesBiomeModifier(biomes, HolderSet.direct(feature), GenerationStep.Decoration.RAW_GENERATION)
  }

  fun addMob(biomes: HolderSet<Biome>, spawnerData: List<MobSpawnSettings.SpawnerData>): net.neoforged.neoforge.common.world.BiomeModifiers.AddSpawnsBiomeModifier {
    return net.neoforged.neoforge.common.world.BiomeModifiers.AddSpawnsBiomeModifier(biomes, spawnerData)
  }
  *//*?}*/
}