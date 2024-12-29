package com.dannbrown.deltaboxlib.platform.registrate.generators.block

import com.dannbrown.deltaboxlib.common.content.block.*
import com.dannbrown.deltaboxlib.common.content.tree.DeltaboxTreeGrower
import com.dannbrown.deltaboxlib.platform.registrate.DeltaboxRegistrate
import com.dannbrown.deltaboxlib.platform.registrate.generators.family.BlockFamilyGeneratorBuilder
import com.tterrag.registrate.util.entry.BlockEntry
import net.minecraft.core.BlockPos
import net.minecraft.world.item.crafting.Ingredient
import net.minecraft.world.level.BlockGetter
import net.minecraft.world.level.ItemLike
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.FaceAttachedHorizontalDirectionalBlock
import net.minecraft.world.level.block.FlowerPotBlock
import net.minecraft.world.level.block.state.BlockState
import java.util.function.Supplier

class BlockGenerator(val registrate: DeltaboxRegistrate) {
  fun  <T: Block> create(name: String): BlockGeneratorBuilder<T> {
    return BlockGeneratorBuilder(name, registrate)
  }

  fun createFamily(name: String): BlockFamilyGeneratorBuilder {
    return BlockFamilyGeneratorBuilder(name, this)
  }

  // @ Block Presets
  // functions to make it easier to create common block types

  /**
   * Add Storage Block (3x3) properties, tags and block-items, items-block recipes
   * @param ingotItem The ingot item to use in the block-items recipe
   * @param ingredient The ingredient to use in the items-block recipe
   * @param addSuffix Whether to add the "_block" suffix to the block name
   */
  fun <T: Block> storageBlock(_name: String, ingotItem: Supplier<ItemLike>, ingredient: Supplier<Ingredient>, addSuffix: Boolean = true): BlockGeneratorBuilder<T> {
    return StorageBlockPreset<T>(_name, ingotItem, ingredient, addSuffix).create(this)
  }

  /**
   * Add Small Storage Block (2x2) properties, tags and block-items, items-block recipes
   * @param ingotItem The ingot item to use in the block-items recipe
   * @param ingredient The ingredient to use in the items-block recipe
   * @param addSuffix Whether to add the "_block" suffix to the block name
   */
  fun <T: Block> smallStorageBlock(_name: String, ingotItem: Supplier<ItemLike>, ingredient: Supplier<Ingredient>, addSuffix: Boolean = true): BlockGeneratorBuilder<T> {
    return StorageBlockPreset<T>(_name, ingotItem, ingredient, addSuffix).createSmall(this)
  }

  /**
   * Add Sapling Block properties, tags and block-items, items-block recipes
   * @param treeGrower The tree grower to use for the sapling
   * @param placeOn The function to use to determine if the sapling can be placed on a block
   */
  fun saplingBlock(
    _name: String,
    treeGrower: DeltaboxTreeGrower,
    placeOn: ((blockState: BlockState, blockGetter: BlockGetter, blockPos: BlockPos) -> Boolean)? = null
  ): BlockGeneratorBuilder<GenericSaplingBlock> {
    return SaplingBlockPreset(_name, treeGrower, placeOn).create(this)
  }

  /**
   * Add Potted Block properties
   * @param block The block to put in the flower pot
   * @param suffix The suffix to add to the block name
   */
  fun pottedBlock(_name: String, block: BlockEntry<out Block>, suffix: String = ""): BlockGeneratorBuilder<FlowerPotBlock> {
    return PottedBlockPreset(_name, block, suffix).create(this)
  }

  /**
   * Add Grass Block properties
   * @param dropItem The item to drop when the block is broken
   * @param isSticky Whether the block is sticky
   * @param isHarmful Whether the block is harmful
   * @param isBonemealable Whether the block can be bonemealed
   * @param chance The chance to drop the other item
   * @param multiplier The multiplier for the other item drop
   * @param placeOn The function to use to determine if the grass block can be placed on a block
   */
  fun grassBlock(
    _name: String,
    dropItem: Supplier<ItemLike>,
    isSticky: Boolean = false,
    isHarmful: Boolean = false,
    isBonemealable: Boolean = false,
    chance: Float = 0.6f,
    multiplier: Int = 2,
    placeOn: ((blockState: BlockState, blockGetter: BlockGetter, blockPos: BlockPos) -> Boolean)? = null
  ): BlockGeneratorBuilder<GenericGrassBlock> {
    return GrassBlockPreset(_name, dropItem, isSticky, isHarmful, isBonemealable, chance, multiplier, placeOn).create(this)
  }

  /**
   * Add Flower Block properties
   * @param isSticky Whether the block is sticky
   * @param isHarmful Whether the block is harmful
   * @param isBonemealable Whether the block can be bonemealed
   * @param placeOn The function to use to determine if the flower block can be placed on a block
   */
  fun flowerBlock(
    _name: String,
    isSticky: Boolean = false,
    isHarmful: Boolean = false,
    isBonemealable: Boolean = false,
    placeOn: ((blockState: BlockState, blockGetter: BlockGetter, blockPos: BlockPos) -> Boolean)? = null
  ): BlockGeneratorBuilder<GenericGrassBlock> {
    return GrassBlockPreset(_name, null, isSticky, isHarmful, isBonemealable, 0f, 0, placeOn).createFlower(this)
  }

  /**
   * Add Tall Grass Block properties
   * @param doubleBlock The double plant block to use for the tall grass block
   * @param dropItem The item to drop when the block is broken
   * @param needBonemeal Whether the block needs bonemeal to grow
   * @param placeOn The function to use to determine if the tall grass block can be placed on a block
   * @param chance The chance to drop the other item
   * @param multiplier The multiplier for the other item drop
   */
  fun createSmallTallGrassBlock(
    _name: String,
    doubleBlock: Supplier<GenericDoublePlantBlock>,
    dropItem: Supplier<ItemLike>,
    needBonemeal: Boolean = false,
    placeOn: ((blockState: BlockState, blockGetter: BlockGetter, blockPos: BlockPos) -> Boolean)? = null,
    chance: Float = 0.25f,
    multiplier: Int = 2,
  ): BlockGeneratorBuilder<GenericTallGrassBlock> {
    return GrassBlockPreset(_name, dropItem, false, false, false, chance, multiplier, placeOn).createSmallTallGrassBlock(this, doubleBlock, needBonemeal)
  }

  /**
   * Add Double Tall Grass Block properties
   * @param dropItem The item to drop when the block is broken
   * @param seedItem The item to drop when the block is bonemealed
   * @param placeOn The function to use to determine if the double tall grass block can be placed on a block
   * @param prefix The prefix to add to the block name
   * @param chance The chance to drop the other item
   * @param multiplier The multiplier for the other item drop
   */
  fun createDoubleTallGrassBlock(
    _name: String,
    dropItem: Supplier<ItemLike>,
    seedItem: Supplier<ItemLike>? = null,
    placeOn: ((blockState: BlockState, blockGetter: BlockGetter, blockPos: BlockPos) -> Boolean)? = null,
    prefix: String = "tall_",
    chance: Float = 0.25f,
    multiplier: Int = 2,
  ): BlockGeneratorBuilder<GenericDoublePlantBlock> {
    return GrassBlockPreset(_name, dropItem, false, false, false, chance, multiplier, placeOn).createDoubleTallGrassBlock(this, seedItem, prefix)
  }

  /**
   * Add Flammable Leaves Block properties
   * @param saplingBlock The sapling block to use for the leaves block
   */
  fun createLeavesBlock(
    _name: String,
    saplingBlock: Supplier<GenericSaplingBlock>,
    suffix: String = "_leaves"
  ): BlockGeneratorBuilder<FlammableLeavesBlock> {
    return LeavesBlockPreset(_name, saplingBlock, suffix).create(this)
  }

  /**
   * Add Palm Leaves Block properties
   * @param saplingBlock The sapling block to use for the leaves block
   */
  fun createPalmLeavesBlock(
    _name: String,
    saplingBlock: Supplier<GenericSaplingBlock>,
    suffix: String = "_leaves"
  ): BlockGeneratorBuilder<FlammableLeavesBlock> {
    return LeavesBlockPreset(_name, saplingBlock, suffix).createPalmLeaves(this)
  }

  /**
   * Add Budding Leaves Block properties
   * @param saplingBlock The sapling block to use for the leaves block
   * @param fruitBlock The fruit block to use for the budding leaves block
   */
  fun createBuddingLeavesBlock(
    _name: String,
    saplingBlock: Supplier<GenericSaplingBlock>,
    fruitBlock: Supplier<Block>,
    suffix: String = "_leaves"
  ): BlockGeneratorBuilder<BuddingLeavesBlock> {
    return LeavesBlockPreset(_name, saplingBlock, suffix).createBuddingLeaves(this, fruitBlock)
  }

  /**
   * Add Crop Leaves Block properties
   * @param saplingBlock The sapling block to use for the leaves block
   * @param itemToDrop The item to drop when the block is broken
   */
  fun createCropLeavesBlock(
    _name: String,
    saplingBlock: Supplier<GenericSaplingBlock>,
    itemToDrop: Supplier<ItemLike>,
    suffix: String = "_leaves"
  ): BlockGeneratorBuilder<CropLeavesBlock> {
    return LeavesBlockPreset(_name, saplingBlock, suffix).createCropLeaves(this, itemToDrop)
  }

  fun createCropBlock(
    _name: String,
    seedName: String,
    cropLang: String,
    seedLang: String,
    dropItem: Supplier<ItemLike>?,
    isBush: Boolean = true,
    includeSeedOnDrop: Boolean = true,
    chance: Float = 1f,
    multiplier: Int = 1,
  ): BlockGeneratorBuilder<GenericCropBlock>{
    return CropBlockPreset(_name, seedName, cropLang, seedLang, dropItem, isBush, includeSeedOnDrop, chance, multiplier).create(this)
  }

  fun createDoubleCropBlock(
    _name: String,
    seedName: String,
    cropLang: String,
    seedLang: String,
    dropItem: Supplier<ItemLike>?,
    isBush: Boolean = true,
    includeSeedOnDrop: Boolean = true,
    chance: Float = 1f,
    multiplier: Int = 1,
  ): BlockGeneratorBuilder<DoubleCropBlock>{
    return CropBlockPreset(_name, seedName, cropLang, seedLang, dropItem, isBush, includeSeedOnDrop, chance, multiplier).createDouble(this)
  }

}