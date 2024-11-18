package com.dannbrown.deltaboxlib.platform.registrate.transformers

import com.tterrag.registrate.providers.DataGenContext
import com.tterrag.registrate.providers.RegistrateRecipeProvider
import com.tterrag.registrate.util.DataIngredient
import net.minecraft.data.recipes.RecipeCategory
import net.minecraft.world.level.block.Block
import java.util.function.Supplier

object RecipePresets {
  fun <B : Block> simpleStonecuttingRecipe(c: DataGenContext<Block, B>, p: RegistrateRecipeProvider, ingredient: Supplier<DataIngredient>, amount: Int = 1
  ) {
    p.stonecutting(ingredient.get(), RecipeCategory.BUILDING_BLOCKS, { c.get() }, amount)
  }
}