package com.dannbrown.palegardenbackport.common.init

import com.dannbrown.palegardenbackport.common.ModCommon
import net.minecraft.data.worldgen.placement.PlacementUtils
import net.minecraft.tags.BlockTags
import net.minecraft.util.random.SimpleWeightedRandomList
import net.minecraft.util.valueproviders.ConstantInt
import net.minecraft.util.valueproviders.UniformInt
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.levelgen.feature.Feature
import net.minecraft.world.level.levelgen.feature.configurations.SimpleBlockConfiguration
import net.minecraft.world.level.levelgen.feature.configurations.VegetationPatchConfiguration
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider
import net.minecraft.world.level.levelgen.feature.stateproviders.WeightedStateProvider
import net.minecraft.world.level.levelgen.placement.CaveSurface


object ModConfiguredFeatures {
  val PALE_MOSS_VEGETATION = ModCommon.REGISTRATE.configuredFeature("pale_moss_vegetation", { k, c, u ->
    u.register(c, k, Feature.SIMPLE_BLOCK,
     SimpleBlockConfiguration(WeightedStateProvider(SimpleWeightedRandomList.builder<BlockState>()
       .add(Blocks.MOSS_CARPET.defaultBlockState(), 25)
       .add(Blocks.GRASS.defaultBlockState(), 50)
       .add(Blocks.TALL_GRASS.defaultBlockState(), 10))
     ))
  })

  val PALE_MOSS_PATCH_BONEMEAL = ModCommon.REGISTRATE.configuredFeature("pale_moss_patch_bonemeal", { k, c, u ->
    u.register(c, k, Feature.VEGETATION_PATCH,
     VegetationPatchConfiguration(BlockTags.MOSS_REPLACEABLE, BlockStateProvider.simple(Blocks.MOSS_BLOCK), PlacementUtils.inlinePlaced(u.lookupConfiguredFeature(c, PALE_MOSS_VEGETATION)), CaveSurface.FLOOR, ConstantInt.of(1), 0.0f, 5, 0.6f, UniformInt.of(1, 2), 0.75f))
  })

//  val PALE_OAK_TREE = ModCommon.REGISTRATE.configuredFeature("pale_moss_patch_bonemeal", { k, c, u ->
//    u.register(c, k, Feature.TREE,
//     TreeConfigurationBuilder(
//      BlockStateProvider.simple(ModContent.WOOD_FAMILY.blocks[BlockFamily.Type.LOG]!!.get()),
//      PaleOakTrunkPlacer(6, 2, 1),
//      BlockStateProvider.simple(ModContent.PALE_OAK_LEAVES.get()),
//      PaleOakFoliagePlacer(ConstantInt.of(0), ConstantInt.of(0)),
//      ThreeLayersFeatureSize(1, 1, 0, 1, 2, OptionalInt.empty()))
//        .ignoreVines()
//        .decorators(
//          listOf(
//            PaleOakGroundDecorator(BlockStateProvider.simple(ModContent.PALE_MOSS_BLOCK.get()), BlockStateProvider.simple(ModContent.PALE_MOSS_CARPET_BLOCK.get())),
//            PaleOakVineDecorator(0.14F, 1, 0, BlockStateProvider.simple(ModContent.PALE_HANGING_MOSS_PLANT.get().defaultBlockState()), 2, mutableListOf(Direction.DOWN), BlockStateProvider.simple(ModContent.PALE_HANGING_MOSS.get().defaultBlockState())),
//        )
//    )
//    .build()
//  })

//  register<TreeConfiguration, Feature<TreeConfiguration>>(
//  context, PALE_OAK_TREE, Feature.TREE,
//  TreeConfigurationBuilder(
//    BlockStateProvider.simple(ModContent.WOOD_FAMILY.blocks[BlockFamily.Type.LOG]!!.get()),
//    PaleOakTrunkPlacer(6, 2, 1),
//    BlockStateProvider.simple(ModContent.PALE_OAK_LEAVES.get()),
//    PaleOakFoliagePlacer(ConstantInt.of(0), ConstantInt.of(0)),
//    ThreeLayersFeatureSize(1, 1, 0, 1, 2, OptionalInt.empty()))
//      .ignoreVines()
//      .decorators(
//        listOf(
//          PaleOakGroundDecorator(BlockStateProvider.simple(ModContent.PALE_MOSS_BLOCK.get()), BlockStateProvider.simple(ModContent.PALE_MOSS_CARPET_BLOCK.get())),
//          PaleOakVineDecorator(0.14F, 1, 0, BlockStateProvider.simple(ModContent.PALE_HANGING_MOSS_PLANT.get().defaultBlockState()), 2, mutableListOf(Direction.DOWN), BlockStateProvider.simple(ModContent.PALE_HANGING_MOSS.get().defaultBlockState())),
//      )
//  )
//  .build()
//)
//  register<VegetationPatchConfiguration, Feature<VegetationPatchConfiguration>>(context,
//  PALE_MOSS_PATCH_BONEMEAL,
//  Feature.VEGETATION_PATCH,
//  VegetationPatchConfiguration(BlockTags.MOSS_REPLACEABLE, BlockStateProvider.simple(ModContent.PALE_MOSS_BLOCK.get()), PlacementUtils.inlinePlaced(configuredFeatures.getOrThrow(PALE_MOSS_VEGETATION)), CaveSurface.FLOOR, ConstantInt.of(1), 0.0f, 5, 0.6f, UniformInt.of(1, 2), 0.75f)
//)

//  register<SimpleBlockConfiguration, Feature<SimpleBlockConfiguration>>(context,
//    PALE_MOSS_VEGETATION,
//    Feature.SIMPLE_BLOCK,
//    SimpleBlockConfiguration(WeightedStateProvider(SimpleWeightedRandomList.builder<BlockState>()
//      .add(ModContent.PALE_MOSS_CARPET_BLOCK.get().defaultBlockState(), 25)
//      .add(Blocks.GRASS.defaultBlockState(), 50)
//      .add(Blocks.TALL_GRASS.defaultBlockState(), 10)))
//  )

  fun register() {
    // init class
  }
}