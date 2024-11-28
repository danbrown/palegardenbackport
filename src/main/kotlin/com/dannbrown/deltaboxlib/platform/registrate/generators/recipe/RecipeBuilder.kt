package com.dannbrown.deltaboxlib.platform.registrate.generators.recipe

import com.dannbrown.deltaboxlib.platform.registrate.DeltaboxRegistrate
import com.dannbrown.deltaboxlib.platform.util.DeltaboxUtil
import net.minecraft.advancements.critereon.InventoryChangeTrigger
import net.minecraft.data.recipes.RecipeCategory
import net.minecraft.data.recipes.ShapedRecipeBuilder
import net.minecraft.data.recipes.ShapelessRecipeBuilder
import net.minecraft.data.recipes.SingleItemRecipeBuilder
import net.minecraft.world.item.crafting.Ingredient
import net.minecraft.world.level.ItemLike
import java.util.function.Supplier

/*? if >=1.21 {*/
/*import net.minecraft.data.recipes.RecipeOutput
*//*?} else {*/
import net.minecraft.data.recipes.FinishedRecipe
import java.util.function.Consumer
/*?}*/

class RecipeBuilder(
  val registrate: DeltaboxRegistrate,
  /*? if >=1.21 {*/
  /*val p: RecipeOutput
*//*?} else {*/
  val p: Consumer<FinishedRecipe>
/*?}*/
) {
  // Shaped
  fun simpleShapedRecipe(result: Supplier<ItemLike>, pattern: Array<String>, key: Map<Char, Supplier<Ingredient>>, amount: Int = 1, name: String, suffix: String = "") {
    val builder = ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, result.get(), amount)

    for (line in pattern) builder.pattern(line)
    for ((k, v) in key) { builder.define(k, v.get()) }


    builder.unlockedBy("has_ingredients", InventoryChangeTrigger.TriggerInstance.hasItems(*key.values.map { it.get().items[0].item }.toTypedArray()))
    builder.save(p, DeltaboxUtil.resourceLocation(registrate.modid, name + suffix))
  }

  fun simpleShapedRecipe(result: Supplier<ItemLike>, pattern: Array<String>, key: Map<Char, Supplier<Ingredient>>, amount: Int = 1, suffix: String = "") {
    simpleShapedRecipe(result, pattern, key, amount, DeltaboxUtil.itemId(result), suffix)
  }

  // End Shaped

  // Shapeless
  fun simpleShapelessRecipe(result: Supplier<ItemLike>, ingredients: List<Supplier<Ingredient>>, amount: Int = 1, name: String, suffix: String = "") {
    val builder = ShapelessRecipeBuilder.shapeless(RecipeCategory.BUILDING_BLOCKS, result.get(), amount)

    for (ingredient in ingredients) builder.requires(ingredient.get())

    builder.unlockedBy("has_ingredients", InventoryChangeTrigger.TriggerInstance.hasItems(*ingredients.map { it.get().items[0].item }.toTypedArray()))
    builder.save(p, DeltaboxUtil.resourceLocation(registrate.modid, name + suffix))
  }

  fun simpleShapelessRecipe(result: Supplier<ItemLike>, ingredients: List<Supplier<Ingredient>>, amount: Int = 1, suffix: String = "") {
    simpleShapelessRecipe(result, ingredients, amount, DeltaboxUtil.itemId(result), suffix)
  }
  // End Shapeless

  // Storage Blocks
  fun storageBlockRecipe(result: Supplier<ItemLike>, ingotItem: Supplier<ItemLike>, ingredient: Supplier<Ingredient>) {
    simpleShapedRecipe(result,
      arrayOf("III", "III", "III"),
      mapOf('I' to ingredient),
      1,
      "_from_materials"
    )

    simpleShapelessRecipe(ingotItem, listOf( Supplier { Ingredient.of(result.get()) } ), 9, DeltaboxUtil.itemId(result), "_to_materials")
  }
  // End Storage Blocks

  // Stonecutting
  fun simpleStonecuttingRecipe(result: Supplier<ItemLike>, ingredient: Supplier<Ingredient>, amount: Int = 1) {
    SingleItemRecipeBuilder.stonecutting(ingredient.get(), RecipeCategory.BUILDING_BLOCKS, result.get(), amount)
      .unlockedBy("has_ingredients", InventoryChangeTrigger.TriggerInstance.hasItems(ingredient.get().items[0].item))
      .save(p, DeltaboxUtil.resourceLocation(registrate.modid, DeltaboxUtil.itemId(result) + "_stonecutting"))
  }
  // End Stonecutting
}