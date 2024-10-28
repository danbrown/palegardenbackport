package com.dannbrown.palegardenbackport.datagen.recipe

import com.dannbrown.palegardenbackport.ModContent
import com.dannbrown.deltaboxlib.registry.datagen.recipe.DeltaboxRecipeSlice
import com.tterrag.registrate.util.DataIngredient
import java.util.function.Consumer
import net.minecraft.data.recipes.FinishedRecipe
import net.minecraft.world.item.Items

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
        .comboOreSmelting(200, 2f)
    }

    val ORANGE_DYE_FROM_EYEBLOSSOM = crafting(recipeConsumer, { Items.ORANGE_DYE }) { b ->
      b.shapeless(1, "", "", listOf(DataIngredient.items(ModContent.EYE_BLOSSOM.get())))
    }

    val GRAY_DYE_FROM_EYEBLOSSOM = crafting(recipeConsumer, { Items.GRAY_DYE }) { b ->
      b.shapeless(1, "", "", listOf(DataIngredient.items(ModContent.CLOSED_EYE_BLOSSOM.get())))
    }

    val RESIN_DUPLICATION = crafting(recipeConsumer, { ModContent.RESIN_CLUMP.get() }) { b ->
      b.shapeless(1, "", "", listOf(DataIngredient.items(ModContent.RESIN_CLUMP.get()), DataIngredient.items(Items.HONEY_BOTTLE)))
    }
  }
}
