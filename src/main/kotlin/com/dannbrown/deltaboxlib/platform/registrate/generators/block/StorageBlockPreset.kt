package com.dannbrown.deltaboxlib.platform.registrate.generators.block

import com.dannbrown.deltaboxlib.platform.registrate.generators.recipe.RecipeBuilder
import com.dannbrown.deltaboxlib.registry.transformers.BlockTagPresets
import net.minecraft.world.item.crafting.Ingredient
import net.minecraft.world.level.ItemLike
import net.minecraft.world.level.block.Block
import java.util.function.Supplier

class StorageBlockPreset<T : Block>(val _name: String, val ingotItem: Supplier<ItemLike>, val ingredient: Supplier<Ingredient>, val addSuffix: Boolean = true): IBlockBuilderPreset<T>() {
  override fun create(generator: BlockGenerator): BlockGeneratorBuilder<T> {
    return generator.create<T>(_name)
      .suffix(if (addSuffix) { "_block" } else { "" })
      .blockTags(BlockTagPresets.storageBlockTags(_name).first.toList())
      .recipe { c, p, b -> b.storageBlockRecipe({ c.get() }, ingotItem, ingredient) }
      .itemTags(BlockTagPresets.storageBlockTags(_name).second.toList())
  }
}