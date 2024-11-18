package com.dannbrown.deltaboxlib.platform.registrate.generators.block

import com.dannbrown.deltaboxlib.platform.registrate.generators.BlockGenerator
import com.dannbrown.deltaboxlib.platform.registrate.generators.BlockGeneratorBuilder
import com.dannbrown.deltaboxlib.platform.registrate.transformers.RecipePresets
import com.dannbrown.deltaboxlib.registry.transformers.BlockTagPresets
import com.tterrag.registrate.util.DataIngredient
import net.minecraft.world.item.crafting.Ingredient
import net.minecraft.world.level.ItemLike
import net.minecraft.world.level.block.Block
import java.util.function.Supplier

class StorageBlockPreset(val name: String, val ingotItem: Supplier<ItemLike>, val ingredient: Supplier<Ingredient>, val addSuffix: Boolean = true): IBlockBuilderPreset() {
  override fun <T : Block> create(generator: BlockGenerator): BlockGeneratorBuilder<T> {
    return generator.create<T>(name)
      .suffix(if (addSuffix) { "_block" } else { "" })
      .blockTags(BlockTagPresets.storageBlockTags(name).first.toList())
      .recipe { c, p -> RecipePresets.storageBlockRecipe(c, p, ingotItem, ingredient) }
      .itemTags(BlockTagPresets.storageBlockTags(name).second.toList())
  }
}