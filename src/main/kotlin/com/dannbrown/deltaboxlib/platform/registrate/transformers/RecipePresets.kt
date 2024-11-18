package com.dannbrown.deltaboxlib.platform.registrate.transformers

import com.dannbrown.deltaboxlib.platform.util.DeltaboxUtil
import com.tterrag.registrate.providers.DataGenContext
import com.tterrag.registrate.providers.RegistrateRecipeProvider
import com.tterrag.registrate.util.DataIngredient
import net.minecraft.advancements.critereon.InventoryChangeTrigger
import net.minecraft.data.recipes.RecipeCategory
import net.minecraft.data.recipes.ShapedRecipeBuilder
import net.minecraft.data.recipes.ShapelessRecipeBuilder
import net.minecraft.world.item.crafting.Ingredient
import net.minecraft.world.level.ItemLike
import net.minecraft.world.level.block.Block
import java.util.function.Supplier

/*? if <1.21 {*/
import net.minecraft.data.recipes.FinishedRecipe
/*?}*/

object RecipePresets {
  fun <B : Block> simpleStonecuttingRecipe(c: DataGenContext<Block, B>, p: RegistrateRecipeProvider, ingredient: Supplier<DataIngredient>, amount: Int = 1
  ) {
    p.stonecutting(ingredient.get(), RecipeCategory.BUILDING_BLOCKS, { c.get() }, amount)
  }

  fun <B : Block> storageBlockRecipe(c: DataGenContext<Block, B>, p: RegistrateRecipeProvider, ingotItem: Supplier<ItemLike>, ingredient: Supplier<Ingredient>) {
    val recipePrefix =  c.name  // ProjectContent.MOD_ID + ":" + c.name

    ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, c.get(), 1)
      .define('I', ingredient.get())
      .pattern("III")
      .pattern("III")
      .pattern("III")
      .unlockedBy("has_ingredient", InventoryChangeTrigger.TriggerInstance.hasItems(*ingredient.get().items.map { it.item }.toTypedArray()))
      /*? if >=1.21 {*/
      /*.save(p, p.safeId(DeltaboxUtil.resourceLocation(recipePrefix + "_from_materials")))
    *//*?} else {*/
      .save({ t: FinishedRecipe -> p.accept(t) }, p.safeId(DeltaboxUtil.resourceLocation(recipePrefix + "_from_materials")))
    /*?}*/

    ShapelessRecipeBuilder.shapeless(RecipeCategory.BUILDING_BLOCKS, ingotItem.get(), 9)
      .requires(c.get())
      .unlockedBy("has_ingredient", InventoryChangeTrigger.TriggerInstance.hasItems(c.get()))
      /*? if >=1.21 {*/
      /*.save(p, p.safeId(DeltaboxUtil.resourceLocation(recipePrefix + "_to_materials")))
      *//*?} else {*/
       .save({ t: FinishedRecipe -> p.accept(t) }, p.safeId(DeltaboxUtil.resourceLocation(recipePrefix + "_to_materials")))
      /*?}*/
  }
}