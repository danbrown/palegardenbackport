package com.dannbrown.deltaboxlib.platform.registrate.generators

import com.dannbrown.deltaboxlib.common.content.tree.DeltaboxTreeGrower
import com.dannbrown.deltaboxlib.platform.registrate.DeltaboxRegistrate
import com.dannbrown.deltaboxlib.platform.registrate.generators.block.PottedBlockPreset
import com.dannbrown.deltaboxlib.platform.registrate.generators.block.SaplingBlockPreset
import com.dannbrown.deltaboxlib.platform.registrate.generators.block.StorageBlockPreset
import net.minecraft.core.BlockPos
import net.minecraft.tags.TagKey
import net.minecraft.world.item.crafting.Ingredient
import net.minecraft.world.level.BlockGetter
import net.minecraft.world.level.ItemLike
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.FlowerPotBlock
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.material.MapColor
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
   * Add Storage Block properties, tags and block-items, items-block recipes
   * @param ingotItem The ingot item to use in the block-items recipe
   * @param ingredient The ingredient to use in the items-block recipe
   * @param addSuffix Whether to add the "_block" suffix to the block name
   */
  fun <T: Block> storageBlock(_name: String, ingotItem: Supplier<ItemLike>, ingredient: Supplier<Ingredient>, addSuffix: Boolean = true): BlockGeneratorBuilder<T> {
    return StorageBlockPreset<T>(_name, ingotItem, ingredient, addSuffix).create(this)
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
  ): BlockGeneratorBuilder<com.dannbrown.deltaboxlib.common.content.block.GenericSaplingBlock> {
    return SaplingBlockPreset(_name, treeGrower, placeOn).create(this)
  }

  /**
   * Add Potted Block properties
   * @param block The block to put in the flower pot
   * @param suffix The suffix to add to the block name
   */
  fun pottedBlock(_name: String, block: Supplier<Block>, suffix: String = "_sapling"): BlockGeneratorBuilder<FlowerPotBlock> {
    return PottedBlockPreset(_name, block, suffix).create(this)
  }
}