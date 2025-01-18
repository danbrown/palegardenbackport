package com.dannbrown.deltaboxlib.platform.registrate.generators.worldgen

import com.dannbrown.deltaboxlib.common.content.block.CropLeavesBlock
import com.dannbrown.deltaboxlib.common.content.worldgen.configuration.WildCropConfiguration
import com.dannbrown.deltaboxlib.common.content.worldgen.placerType.PalmFoliagePlacer
import com.dannbrown.deltaboxlib.common.content.worldgen.placerType.CrookedTrunkPlacer
import com.dannbrown.deltaboxlib.platform.util.DeltaboxUtil
import net.minecraft.core.BlockPos
import net.minecraft.core.Holder
import net.minecraft.core.registries.Registries
/*? if >=1.21 {*/
/*import net.minecraft.data.worldgen.BootstrapContext
*//*?} else {*/
import net.minecraft.data.worldgen.BootstapContext as BootstrapContext
/*?}*/
import net.minecraft.data.worldgen.placement.PlacementUtils
import net.minecraft.resources.ResourceKey
import net.minecraft.tags.BlockTags
import net.minecraft.util.random.SimpleWeightedRandomList
import net.minecraft.util.valueproviders.ConstantInt
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature
import net.minecraft.world.level.levelgen.feature.Feature
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration
import net.minecraft.world.level.levelgen.feature.configurations.SimpleBlockConfiguration
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration.TreeConfigurationBuilder
import net.minecraft.world.level.levelgen.feature.featuresize.TwoLayersFeatureSize
import net.minecraft.world.level.levelgen.feature.foliageplacers.BlobFoliagePlacer
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider
import net.minecraft.world.level.levelgen.feature.stateproviders.WeightedStateProvider
import net.minecraft.world.level.levelgen.feature.trunkplacers.StraightTrunkPlacer
import net.minecraft.world.level.levelgen.placement.BlockPredicateFilter
import net.minecraft.world.level.levelgen.placement.PlacedFeature
import net.minecraft.world.level.levelgen.placement.PlacementModifier
import net.minecraft.world.level.levelgen.structure.templatesystem.BlockMatchTest
import net.minecraft.world.level.levelgen.structure.templatesystem.RuleTest
import net.minecraft.world.level.levelgen.structure.templatesystem.TagMatchTest
import net.minecraft.world.level.material.Fluids

object ConfiguredFeaturesUtil: AbstractWorldgenUtil() {
  fun registerKey(name: String, modId: String): ResourceKey<ConfiguredFeature<*, *>> {
    return ResourceKey.create(
      Registries.CONFIGURED_FEATURE,
      DeltaboxUtil.resourceLocation(modId, name)
    )
  }

  fun <FC : FeatureConfiguration?, F : Feature<FC>?> register(
    context: BootstrapContext<ConfiguredFeature<*, *>>,
    key: ResourceKey<ConfiguredFeature<*, *>>, feature: F, configuration: FC
  ) {
    context.register(key, ConfiguredFeature(feature, configuration))
  }

  // Replaceables
  val stoneReplaceable: RuleTest = TagMatchTest(BlockTags.STONE_ORE_REPLACEABLES)
  val deepslateReplaceables: RuleTest = TagMatchTest(BlockTags.DEEPSLATE_ORE_REPLACEABLES)
  val netherrackReplacables: RuleTest = BlockMatchTest(Blocks.NETHERRACK)
  val endReplaceables: RuleTest = BlockMatchTest(Blocks.END_STONE)
  val sandReplaceables: RuleTest = TagMatchTest(BlockTags.SAND)
  val basaltReplaceables: RuleTest = BlockMatchTest(Blocks.BASALT)

  // Positioning
  val BLOCK_BELOW: BlockPos = BlockPos(0, -1, 0)
  val BLOCK_ABOVE: BlockPos = BlockPos(0, 1, 0)

  // FILTER KEYS
  val ON_SAND_FILTER: BlockPredicateFilter =
    BlockPredicateFilter.forPredicate(BlockPredicate.matchesTag(BlockPos.ZERO.below(), BlockTags.SAND))
  val ON_DIRT_FILTER: BlockPredicateFilter =
    BlockPredicateFilter.forPredicate(BlockPredicate.matchesTag(BlockPos.ZERO.below(), BlockTags.DIRT))
  val ON_WATER_FILTER =
    BlockPredicateFilter.forPredicate(BlockPredicate.matchesFluids(BlockPos.ZERO.below(), Fluids.WATER))

  // Utils
  // facilitate the creation of a WeightedStateProvider
  fun weightedStateProvider(blocks: Map<BlockState, Int>): WeightedStateProvider {
    val builder = SimpleWeightedRandomList.builder<BlockState>()
    blocks.forEach { (state, weight) -> builder.add(state, weight) }
    return WeightedStateProvider(builder.build())
  }

  fun directPlacedFeature(
    configuredFeature: Holder<ConfiguredFeature<*, *>>,
    vararg modifiers: PlacementModifier
  ): Holder<PlacedFeature> {
    return Holder.direct(PlacedFeature(configuredFeature, listOf(*modifiers)))
  }

  // Special features
  fun createStraightFruitBlobTree(logBlock: Block, leavesBlock: Block, fruitBlock: Block, leavesChance: Int, fruitChance: Int, fruitReadyChance: Int,  baseHeight: Int, heightRandA: Int, heightRandB: Int, radius: Int): TreeConfigurationBuilder {
    return TreeConfigurationBuilder(
      BlockStateProvider.simple(logBlock),
      StraightTrunkPlacer(baseHeight, heightRandA, heightRandB),
      weightedStateProvider(mapOf(
        leavesBlock.defaultBlockState() to leavesChance,
        fruitBlock.defaultBlockState() to fruitChance,
        fruitBlock.defaultBlockState().setValue(CropLeavesBlock.AGE, 3) to fruitReadyChance,
        fruitBlock.defaultBlockState().setValue(CropLeavesBlock.AGE, 2) to fruitReadyChance,
        fruitBlock.defaultBlockState().setValue(CropLeavesBlock.AGE, 1) to fruitReadyChance,
      )),
      BlobFoliagePlacer(ConstantInt.of(radius), ConstantInt.of(0), 3),
      TwoLayersFeatureSize(1, 0, 1)
    )
  }

  fun createPalmTree(logBlock: Block, leavesBlock: Block, baseHeight: Int, heightRandA: Int, heightRandB: Int, radius: Int): TreeConfigurationBuilder {
    return TreeConfigurationBuilder(
      BlockStateProvider.simple(logBlock),
      CrookedTrunkPlacer(baseHeight, heightRandA, heightRandB),
      BlockStateProvider.simple(leavesBlock),
      PalmFoliagePlacer(ConstantInt.of(radius), ConstantInt.of(0)),
      TwoLayersFeatureSize(1, 0, 1)
    )
  }

  fun wildCropWithFloorConfig(primaryBlock: Block, secondaryBlock: Block, plantedOn: BlockPredicate, floorBlock: Block, replaces: BlockPredicate): WildCropConfiguration {
    return WildCropConfiguration(64, 6, 3, plantBlockConfig(primaryBlock, plantedOn), plantBlockConfig(secondaryBlock, plantedOn), floorBlockConfig(floorBlock, replaces))
  }
  fun plantBlockConfig(block: Block, plantedOn: BlockPredicate): Holder<PlacedFeature> {
    return PlacementUtils.filtered(
      Feature.SIMPLE_BLOCK, SimpleBlockConfiguration(BlockStateProvider.simple(block)),
      BlockPredicate.allOf(BlockPredicate.ONLY_IN_AIR_PREDICATE, plantedOn))
  }
  fun floorBlockConfig(block: Block, replaces: BlockPredicate): Holder<PlacedFeature> {
    return PlacementUtils.filtered<SimpleBlockConfiguration, Feature<SimpleBlockConfiguration>>(
      Feature.SIMPLE_BLOCK, SimpleBlockConfiguration(BlockStateProvider.simple(block)),
      BlockPredicate.allOf(BlockPredicate.replaceable(BLOCK_ABOVE), replaces))
  }
}