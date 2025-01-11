package com.dannbrown.deltaboxlib.platform.registrate.generators.recipe.builders

import com.dannbrown.deltaboxlib.platform.util.DeltaboxUtil
import net.minecraft.data.recipes.RecipeBuilder
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.level.ItemLike
import java.util.function.Supplier

abstract class AbstractDeltaboxRecipeBuilder {
  abstract fun getRecipes(): MutableMap<ResourceLocation, RecipeBuilder>

  companion object {
    fun createSimpleLocation(
      modId: String,
      recipeType: String,
      result: Supplier<ItemLike>,
      prefix: String,
      suffix: String
    ): ResourceLocation {
      return DeltaboxUtil.resourceLocation(modId, recipeType + "/" + prefix + DeltaboxUtil.itemId { result.get().asItem() } + suffix)
    }
  }
}