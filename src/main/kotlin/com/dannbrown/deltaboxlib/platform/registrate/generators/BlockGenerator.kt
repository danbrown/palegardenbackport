package com.dannbrown.deltaboxlib.platform.registrate.generators

import com.dannbrown.deltaboxlib.platform.registrate.DeltaboxRegistrate
import com.dannbrown.deltaboxlib.platform.registrate.generators.block.StorageBlockPreset
import com.tterrag.registrate.util.DataIngredient
import net.minecraft.world.item.crafting.Ingredient
import net.minecraft.world.level.ItemLike
import net.minecraft.world.level.block.Block
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
  fun <T: Block> storageBlock(name: String, ingotItem: Supplier<ItemLike>, ingredient: Supplier<Ingredient>, addSuffix: Boolean = true): BlockGeneratorBuilder<T> {
    return StorageBlockPreset(name, ingotItem, ingredient, addSuffix).create(this)
  }
}