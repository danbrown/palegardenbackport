package com.dannbrown.palegardenbackport.init

import com.dannbrown.deltaboxlib.registrate.presets.family.BlockFamily
import com.dannbrown.palegardenbackport.init.ModContent.REGISTRATE
import net.minecraft.core.BlockPos
import net.minecraft.data.worldgen.placement.PlacementUtils
import net.minecraft.data.worldgen.placement.VegetationPlacements
import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate
import net.minecraft.world.level.levelgen.placement.BiomeFilter
import net.minecraft.world.level.levelgen.placement.BlockPredicateFilter
import net.minecraft.world.level.levelgen.placement.CountPlacement
import net.minecraft.world.level.levelgen.placement.InSquarePlacement
import net.minecraft.world.level.levelgen.placement.RarityFilter

object ModPlacedFeatures {
  val PALE_OAK_CHECKED = REGISTRATE.placedFeature("pale_oak_checked") { k, c, u ->
    u.register(
      c, k, u.lookupConfiguredFeature(c, ModConfiguredFeatures.PALE_OAK_TREE), listOf(
        BlockPredicateFilter.forPredicate(
          BlockPredicate.wouldSurvive(
            ModBlocks.PALE_OAK.blockFamily.blocks[BlockFamily.Type.SAPLING]!!.get().defaultBlockState(), BlockPos.ZERO
          )
        ),
        BlockPredicateFilter.forPredicate(BlockPredicate.noFluid())
      )
    )
  }

  val PALE_OAK_PLACED = REGISTRATE.placedFeature("pale_oak_placed") { k, c, u ->
    u.register(
      c, k, u.lookupConfiguredFeature(c, ModConfiguredFeatures.PALE_OAK_TREE), VegetationPlacements.treePlacement(
        RarityFilter.onAverageOnceEvery(5),
        ModBlocks.PALE_OAK.blockFamily.blocks[BlockFamily.Type.SAPLING]!!.get()
      )
    )
  }
  val PALE_OAK_HEART_CHECKED = REGISTRATE.placedFeature("pale_oak_heart_checked") { k, c, u ->
    u.register(
      c, k, u.lookupConfiguredFeature(c, ModConfiguredFeatures.PALE_OAK_TREE_HEART), listOf(
        BlockPredicateFilter.forPredicate(
          BlockPredicate.wouldSurvive(
            ModBlocks.PALE_OAK.blockFamily.blocks[BlockFamily.Type.SAPLING]!!.get().defaultBlockState(), BlockPos.ZERO
          )
        ),
        BlockPredicateFilter.forPredicate(BlockPredicate.noFluid())
      )
    )
  }

  val PALE_OAK_HEART_PLACED = REGISTRATE.placedFeature("pale_oak_heart_placed") { k, c, u ->
    u.register(
      c, k, u.lookupConfiguredFeature(c, ModConfiguredFeatures.PALE_OAK_TREE_HEART), VegetationPlacements.treePlacement(
        RarityFilter.onAverageOnceEvery(5),
        ModBlocks.PALE_OAK.blockFamily.blocks[BlockFamily.Type.SAPLING]!!.get()
      )
    )
  }

  val PALE_GARDEN_VEGETATION = REGISTRATE.placedFeature("pale_garden_vegetation") { k, c, u ->
    u.register(
      c, k, u.lookupConfiguredFeature(c, ModConfiguredFeatures.PALE_MOSS_VEGETATION), listOf(
        CountPlacement.of(14),
        InSquarePlacement.spread(),
        PlacementUtils.HEIGHTMAP_OCEAN_FLOOR,
        BiomeFilter.biome()
      )
    )
  }

  val PALE_GARDEN_FLOWERS = REGISTRATE.placedFeature("pale_garden_flowers") { k, c, u ->
    u.register(
      c, k, u.lookupConfiguredFeature(c, ModConfiguredFeatures.PALE_GARDEN_PATCH), listOf(
        RarityFilter.onAverageOnceEvery(2),
        InSquarePlacement.spread(),
        PlacementUtils.HEIGHTMAP,
        BiomeFilter.biome()
      )
    )
  }

  fun register() {
    // init
  }
}