package com.dannbrown.palegardenbackport.datagen.worldgen

import com.dannbrown.deltaboxlib.registry.generators.BlockFamily
import com.dannbrown.deltaboxlib.registry.worldgen.AbstractPlacedFeaturesGen
import com.dannbrown.palegardenbackport.ModContent
import com.dannbrown.palegardenbackport.datagen.worldgen.biome.PaleGardenBiome
import net.minecraft.core.BlockPos
import net.minecraft.core.registries.Registries
import net.minecraft.data.worldgen.BootstapContext
import net.minecraft.data.worldgen.placement.PlacementUtils
import net.minecraft.data.worldgen.placement.VegetationPlacements
import net.minecraft.resources.ResourceKey
import net.minecraft.util.valueproviders.ClampedInt
import net.minecraft.util.valueproviders.UniformInt
import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate
import net.minecraft.world.level.levelgen.placement.BiomeFilter
import net.minecraft.world.level.levelgen.placement.BlockPredicateFilter
import net.minecraft.world.level.levelgen.placement.CountPlacement
import net.minecraft.world.level.levelgen.placement.InSquarePlacement
import net.minecraft.world.level.levelgen.placement.PlacedFeature
import net.minecraft.world.level.levelgen.placement.RarityFilter

object ModPlacedFeatures : AbstractPlacedFeaturesGen() {
  override val modId: String = ModContent.MOD_ID

  val PALE_OAK_CHECKED: ResourceKey<PlacedFeature> = registerKey("pale_oak_checked")
  val PALE_GARDEN_FLOWERS: ResourceKey<PlacedFeature> = registerKey("pale_garden_flowers")
  val PALE_GARDEN_VEGETATION: ResourceKey<PlacedFeature> = registerKey("pale_garden_vegetation")
  val PALE_OAK_PLACED: ResourceKey<PlacedFeature> = registerKey("pale_oak_placed")
  val PALE_OAK_HEART_CHECKED = registerKey("pale_oak_heart_checked")
  val PALE_OAK_HEART_PLACED = registerKey("pale_oak_heart_placed")

  override fun bootstrap(context: BootstapContext<PlacedFeature>) {
    val configuredFeatures = context.lookup(Registries.CONFIGURED_FEATURE)

    // PALE OAK TREE
    register(
      context, PALE_OAK_CHECKED, configuredFeatures.getOrThrow(ModConfiguredFeatures.PALE_OAK_TREE),
      listOf(
        BlockPredicateFilter.forPredicate(BlockPredicate.wouldSurvive(ModContent.WOOD_FAMILY.blocks[BlockFamily.Type.SAPLING]!!.get().defaultBlockState(), BlockPos.ZERO)),
        BlockPredicateFilter.forPredicate(BlockPredicate.noFluid())
      )
    )

    register(
      context, PALE_OAK_PLACED, configuredFeatures.getOrThrow(ModConfiguredFeatures.PALE_OAK_TREE),
      VegetationPlacements.treePlacement(
        RarityFilter.onAverageOnceEvery(5),
        ModContent.WOOD_FAMILY.blocks[BlockFamily.Type.SAPLING]!!.get()
      )
    )

    register(
      context, PALE_OAK_HEART_CHECKED, configuredFeatures.getOrThrow(ModConfiguredFeatures.PALE_OAK_TREE_HEART),
      listOf(
        BlockPredicateFilter.forPredicate(BlockPredicate.wouldSurvive(ModContent.WOOD_FAMILY.blocks[BlockFamily.Type.SAPLING]!!.get().defaultBlockState(), BlockPos.ZERO)),
        BlockPredicateFilter.forPredicate(BlockPredicate.noFluid())
      )
    )

    register(
      context, PALE_OAK_HEART_PLACED, configuredFeatures.getOrThrow(ModConfiguredFeatures.PALE_OAK_TREE_HEART),
      VegetationPlacements.treePlacement(
        RarityFilter.onAverageOnceEvery(5),
        ModContent.WOOD_FAMILY.blocks[BlockFamily.Type.SAPLING]!!.get()
      )
    )

    register(context, PALE_GARDEN_VEGETATION, configuredFeatures.getOrThrow(ModConfiguredFeatures.PALE_GARDEN_VEGETATION), listOf(CountPlacement.of(14), InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP_OCEAN_FLOOR, BiomeFilter.biome()))

    register(context, PALE_GARDEN_FLOWERS, configuredFeatures.getOrThrow(ModConfiguredFeatures.PALE_GARDEN_PATCH), listOf(RarityFilter.onAverageOnceEvery(2), InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP, BiomeFilter.biome()))
  }
}