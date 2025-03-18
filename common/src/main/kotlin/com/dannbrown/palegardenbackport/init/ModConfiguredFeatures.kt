package com.dannbrown.palegardenbackport.init

import com.dannbrown.deltaboxlib.registrate.presets.family.BlockFamily
import com.dannbrown.palegardenbackport.content.worldgen.placerTypes.PaleOakFoliagePlacer
import com.dannbrown.palegardenbackport.content.worldgen.placerTypes.PaleOakHeartTrunkPlacer
import com.dannbrown.palegardenbackport.content.worldgen.placerTypes.PaleOakTrunkPlacer
import com.dannbrown.palegardenbackport.content.worldgen.treeDecorator.PaleOakGroundDecorator
import com.dannbrown.palegardenbackport.content.worldgen.treeDecorator.PaleOakVineDecorator
import com.dannbrown.palegardenbackport.init.ModContent.REGISTRATE
import net.minecraft.core.Direction
import net.minecraft.core.registries.Registries
import net.minecraft.data.worldgen.features.FeatureUtils
import net.minecraft.data.worldgen.placement.PlacementUtils
import net.minecraft.tags.BlockTags
import net.minecraft.util.random.SimpleWeightedRandomList
import net.minecraft.util.valueproviders.ConstantInt
import net.minecraft.util.valueproviders.UniformInt
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.levelgen.feature.Feature
import net.minecraft.world.level.levelgen.feature.WeightedPlacedFeature
import net.minecraft.world.level.levelgen.feature.configurations.RandomFeatureConfiguration
import net.minecraft.world.level.levelgen.feature.configurations.SimpleBlockConfiguration
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration.TreeConfigurationBuilder
import net.minecraft.world.level.levelgen.feature.configurations.VegetationPatchConfiguration
import net.minecraft.world.level.levelgen.feature.featuresize.ThreeLayersFeatureSize
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider
import net.minecraft.world.level.levelgen.feature.stateproviders.WeightedStateProvider
import net.minecraft.world.level.levelgen.placement.CaveSurface
import java.util.OptionalInt

object ModConfiguredFeatures {
  val PALE_MOSS_PATCH_BONEMEAL = REGISTRATE.configuredFeature("pale_moss_patch_bonemeal") { k, c, u ->
    val configuredFeatures = c.lookup(Registries.CONFIGURED_FEATURE)
    u.register(
      c, k, Feature.VEGETATION_PATCH,
      VegetationPatchConfiguration(
        BlockTags.MOSS_REPLACEABLE,
        BlockStateProvider.simple(ModBlocks.PALE_MOSS_BLOCK.get()),
        PlacementUtils.inlinePlaced(configuredFeatures.getOrThrow(k)),
        CaveSurface.FLOOR,
        ConstantInt.of(1),
        0.0f,
        5,
        0.6f,
        UniformInt.of(1, 2),
        0.75f
      )
    )
  }

  val PALE_MOSS_PATCH = REGISTRATE.configuredFeature("pale_moss_patch") { k, c, u ->
    u.register(
      c, k, Feature.SIMPLE_BLOCK,
      SimpleBlockConfiguration(
        WeightedStateProvider(
          SimpleWeightedRandomList.builder<BlockState>()
            .add(ModBlocks.PALE_MOSS_CARPET_BLOCK.get().defaultBlockState(), 25)
            .add(Blocks.GRASS.defaultBlockState(), 50)
            .add(Blocks.TALL_GRASS.defaultBlockState(), 10)
        )
      )
    )
  }

  val PALE_OAK_TREE = REGISTRATE.configuredFeature("pale_oak_tree") { k, c, u ->
    u.register(
      c, k, Feature.TREE,
      TreeConfigurationBuilder(
        BlockStateProvider.simple(ModBlocks.PALE_OAK.blockFamily.blocks[BlockFamily.Type.LOG]!!.get()),
        PaleOakTrunkPlacer(6, 2, 1),
        BlockStateProvider.simple(ModBlocks.PALE_OAK_LEAVES.get()),
        PaleOakFoliagePlacer(ConstantInt.of(0), ConstantInt.of(0)),
        ThreeLayersFeatureSize(1, 1, 0, 1, 2, OptionalInt.empty())
      )
        .ignoreVines()
        .decorators(
          listOf(
            PaleOakGroundDecorator(
              BlockStateProvider.simple(ModBlocks.PALE_MOSS_BLOCK.get()),
              BlockStateProvider.simple(ModBlocks.PALE_MOSS_CARPET_BLOCK.get())
            ),
            PaleOakVineDecorator(
              0.14F,
              1,
              0,
              BlockStateProvider.simple(ModBlocks.PALE_HANGING_MOSS_PLANT.get().defaultBlockState()),
              2,
              mutableListOf(Direction.DOWN),
              BlockStateProvider.simple(ModBlocks.PALE_HANGING_MOSS.get().defaultBlockState())
            ),
          )
        )
        .build()
    )
  }

  val PALE_OAK_TREE_HEART = REGISTRATE.configuredFeature("pale_oak_tree_heart") { k, c, u ->
    u.register(
      c, k, Feature.TREE,
      TreeConfigurationBuilder(
        BlockStateProvider.simple(ModBlocks.PALE_OAK.blockFamily.blocks[BlockFamily.Type.LOG]!!.get()),
        PaleOakHeartTrunkPlacer(6, 2, 1),
        BlockStateProvider.simple(ModBlocks.PALE_OAK_LEAVES.get()),
        PaleOakFoliagePlacer(ConstantInt.of(0), ConstantInt.of(0)),
        ThreeLayersFeatureSize(1, 1, 0, 1, 2, OptionalInt.empty())
      )
        .ignoreVines()
        .decorators(
          listOf(
            PaleOakGroundDecorator(
              BlockStateProvider.simple(ModBlocks.PALE_MOSS_BLOCK.get()),
              BlockStateProvider.simple(ModBlocks.PALE_MOSS_CARPET_BLOCK.get())
            ),
            PaleOakVineDecorator(
              0.14F,
              1,
              0,
              BlockStateProvider.simple(ModBlocks.PALE_HANGING_MOSS_PLANT.get().defaultBlockState()),
              2,
              mutableListOf(Direction.DOWN),
              BlockStateProvider.simple(ModBlocks.PALE_HANGING_MOSS.get().defaultBlockState())
            ),
          )
        )
        .build()
    )
  }

  val PALE_GARDEN_PATCH = REGISTRATE.configuredFeature("pale_garden_patch") { k, c, u ->
    u.register(
      c, k, Feature.RANDOM_PATCH,
      FeatureUtils.simplePatchConfiguration<SimpleBlockConfiguration, Feature<SimpleBlockConfiguration>>(
        Feature.SIMPLE_BLOCK,
        SimpleBlockConfiguration(BlockStateProvider.simple(ModBlocks.CLOSED_EYE_BLOSSOM.get()))
      )
    )
  }

  val PALE_GARDEN_VEGETATION = REGISTRATE.configuredFeature("pale_garden_vegetation") { k, c, u ->
    val placed = c.lookup(Registries.PLACED_FEATURE)
    u.register(
      c, k, Feature.RANDOM_SELECTOR,
      RandomFeatureConfiguration(
        listOf(
          WeightedPlacedFeature(placed.getOrThrow(ModPlacedFeatures.PALE_OAK_HEART_CHECKED), 1F),
        ), placed.getOrThrow(ModPlacedFeatures.PALE_OAK_HEART_CHECKED)
      )
    )
  }

  fun register() {
    // init
  }
}