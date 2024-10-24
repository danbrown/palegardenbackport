package com.dannbrown.palegardenbackport.datagen.recipe

import com.dannbrown.palegardenbackport.ModContent
import com.dannbrown.deltaboxlib.registry.datagen.recipe.DeltaboxRecipeSlice
import com.tterrag.registrate.util.DataIngredient
import java.util.function.Consumer
import net.minecraft.data.recipes.FinishedRecipe

class MainRecipeGen : DeltaboxRecipeSlice(ModContent.MOD_ID) {
  override fun name(): String {
    return "Mod Recipes"
  }
  override fun addRecipes(recipeConsumer: Consumer<FinishedRecipe>) {
    val RESIN_BRICK = cooking(recipeConsumer,
      { DataIngredient.items(ModContent.RESIN_CLUMP.get()) },
      { ModContent.RESIN_BRICK.get() }
    ) { b ->
      b
        .comboFoodCooking(200, 2f)
    }
  }
}
