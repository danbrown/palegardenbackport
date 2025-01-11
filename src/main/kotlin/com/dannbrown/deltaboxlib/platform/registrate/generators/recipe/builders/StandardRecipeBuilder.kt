package com.dannbrown.deltaboxlib.platform.registrate.generators.recipe.builders

import com.tterrag.registrate.providers.RegistrateRecipeProvider
import net.minecraft.advancements.critereon.ItemPredicate
import net.minecraft.data.recipes.RecipeBuilder
import net.minecraft.data.recipes.RecipeCategory
import net.minecraft.data.recipes.ShapedRecipeBuilder
import net.minecraft.data.recipes.ShapelessRecipeBuilder
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.Items
import net.minecraft.world.item.crafting.Ingredient
import net.minecraft.world.level.ItemLike
import java.util.function.Supplier
import java.util.function.UnaryOperator

// STANDARD RECIPE CLASS
class StandardRecipeBuilder(private val modId: String, private val result: Supplier<ItemLike>, private val amount: Int = 1): AbstractDeltaboxRecipeBuilder() {
  private var unlockedBy: Supplier<ItemPredicate>? = null
  private var prefix: String = ""
  private var suffix: String = ""
  private val allRecipes: MutableMap<ResourceLocation, RecipeBuilder> = HashMap()

  fun prefix(prefix: String): StandardRecipeBuilder {
    this.prefix = prefix
    return this
  }

  fun suffix(suffix: String): StandardRecipeBuilder {
    this.suffix = suffix
    return this
  }

  fun apply(builder: UnaryOperator<StandardRecipeBuilder>): StandardRecipeBuilder {
    return builder.apply(this)
  }

  fun unlockedBy(unlockedBy: Supplier<ItemPredicate>): StandardRecipeBuilder {
    this.unlockedBy = unlockedBy
    return this
  }

  // SHAPED
  fun shaped(builder: UnaryOperator<ShapedRecipeBuilder> = UnaryOperator.identity()): StandardRecipeBuilder {
    return shaped(amount, prefix, suffix, builder)
  }

  fun shaped(
    amount: Int,
    builder: UnaryOperator<ShapedRecipeBuilder> = UnaryOperator.identity(),
  ): StandardRecipeBuilder {
    return shaped(amount, prefix, suffix, builder)
  }

  fun shaped(
    amount: Int,
    prefix: String,
    suffix: String,
    builder: UnaryOperator<ShapedRecipeBuilder> = UnaryOperator.identity(),
  ): StandardRecipeBuilder {

    val _builder = builder.apply( ShapedRecipeBuilder.shaped(RecipeCategory.MISC, result.get(), amount))

    if (unlockedBy != null) {
      _builder.unlockedBy("has_item", RegistrateRecipeProvider.inventoryTrigger(unlockedBy!!.get()))
    }

    allRecipes[createSimpleLocation(modId, "crafting_shaped", result, prefix, suffix)] = _builder
    return this
  }

  // SHAPELESS
  fun shapeless(requiresList: List<Ingredient> = ArrayList()): StandardRecipeBuilder {
    return shapeless(amount, prefix, suffix, requiresList)
  }

  fun shapeless(amount: Int, requiresList: List<Ingredient> = ArrayList()): StandardRecipeBuilder {
    return shapeless(amount, prefix, suffix, requiresList)
  }

  fun shapeless(
    amount: Int,
    prefix: String,
    suffix: String,
    requiresList: List<Ingredient> = ArrayList(),
    builder: UnaryOperator<ShapelessRecipeBuilder> = UnaryOperator.identity(),
  ): StandardRecipeBuilder {
    val _builder = builder.apply(ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, result.get(), amount))

    for (ingredient in requiresList) {
      _builder.requires(ingredient)
    }

    if (unlockedBy != null) {
      _builder.unlockedBy("has_item", RegistrateRecipeProvider.inventoryTrigger(unlockedBy!!.get()))
    } else {
      _builder.unlockedBy("has_item",
        RegistrateRecipeProvider.inventoryTrigger(ItemPredicate.Builder.item()
          .of(Items.AIR)
          .build()))
    }

    allRecipes[createSimpleLocation(modId, "crafting_shapeless", result, prefix, suffix)] = _builder
    return this
  }

  override fun getRecipes(): MutableMap<ResourceLocation, RecipeBuilder> {
    return allRecipes
  }
}

