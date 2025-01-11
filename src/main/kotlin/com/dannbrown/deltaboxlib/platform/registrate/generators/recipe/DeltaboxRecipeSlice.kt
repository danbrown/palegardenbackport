package com.dannbrown.deltaboxlib.platform.registrate.generators.recipe

import com.dannbrown.deltaboxlib.platform.registrate.generators.recipe.builders.CookingRecipeBuilder
import com.dannbrown.deltaboxlib.platform.registrate.generators.recipe.builders.StandardRecipeBuilder
import com.dannbrown.deltaboxlib.platform.util.DeltaboxUtil
import net.minecraft.data.recipes.RecipeBuilder
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.crafting.Ingredient
import net.minecraft.world.level.ItemLike
import java.util.function.Supplier
import java.util.function.UnaryOperator

abstract class DeltaboxRecipeSlice(private val modId: String) {
  val all: MutableMap<ResourceLocation, RecipeBuilder> = HashMap()

  abstract fun addRecipes()
  abstract fun name(): String
  fun registerRecipes(
    /*? if >=1.21 {*/
    /*consumer: net.minecraft.data.recipes.RecipeOutput
    *//*?} else {*/
    consumer: java.util.function.Consumer<net.minecraft.data.recipes.FinishedRecipe>
  /*?}*/
  ) {
    this.addRecipes()
    for ((key, value) in all) {
      value.save(consumer, key)
    }
  }

  // generator
  fun cooking(
    ingredient: Supplier<Ingredient>,
    result: Supplier<ItemLike>,
    builder: UnaryOperator<CookingRecipeBuilder> = UnaryOperator.identity()
  ): MutableMap<ResourceLocation, RecipeBuilder> {
    val allRecipes = CookingRecipeBuilder(modId, ingredient, result).apply(builder).getRecipes()
    all.putAll(allRecipes)
    return allRecipes
  }

  fun crafting(
    result: Supplier<ItemLike>,
    amount: Int = 1,
    builder: UnaryOperator<StandardRecipeBuilder> = UnaryOperator.identity()
  ): MutableMap<ResourceLocation, RecipeBuilder> {
    val allRecipes = StandardRecipeBuilder(modId, result, amount).apply(builder).getRecipes()
    all.putAll(allRecipes)
    return allRecipes
  }


  fun oreRecipes(
    name: String,
    ingot: ItemLike?,
    block: ItemLike?,
    nugget: ItemLike?,
    dust: ItemLike?,
    plate: ItemLike?,
    rawIngot: ItemLike?,
    crushedRaw: ItemLike?,
    rawBlock: ItemLike?,
    ores: List<OreBlockProcessor>?,
  ) {
    val hasOres = ores != null && ores.isNotEmpty()
    // smelt the raw ore into ingots
    if (ingot != null && rawIngot != null) {
      val SMELT_RAW = cooking({
        Ingredient.of(DeltaboxUtil.TAGS.deltaboxItemTag("raw_materials/$name"))
      },
        { ingot }) { b ->
        b
          .suffix("_from_raw")
          .comboOreSmelting(200, 0.5f)
      }
    }
    // smelt the raw block into a block
    if (block != null && rawBlock != null) {
      val SMELT_RAW_BLOCK = cooking({
        Ingredient.of(DeltaboxUtil.TAGS.deltaboxItemTag("storage_blocks/raw_$name"))
      },
        { block }) { b ->
        b
          .suffix("_from_raw_block")
          .comboOreSmelting(400, 4.5f)
      }
    }
    // smelt the ores into ingots
    if (ingot != null && hasOres) {
      val SMELT_ORE = cooking({
        Ingredient.of(DeltaboxUtil.TAGS.deltaboxItemTag("ores/$name"))
      },
        { ingot }) { b ->
        b
          .suffix("_from_ore")
          .comboOreSmelting(200, 1f)
      }
    }
    // smelt the crushed raw ore into ingots
    if (ingot != null && crushedRaw != null) {
      val SMELT_CRUSHED = cooking({
        Ingredient.of(crushedRaw)
      },
        { ingot }) { b ->
        b
          .suffix("_from_crushed_raw")
          .comboOreSmelting(200, 1f)
      }
    }

    // smelt the dust into ingots
    if (ingot != null && dust != null) {
      val SMELT_DUST = cooking({
        Ingredient.of(DeltaboxUtil.TAGS.deltaboxItemTag("dusts/$name"))
      },
        { ingot }) { b ->
        b
          .suffix("_from_dust")
          .comboOreSmelting(200, 0.5f)
      }
    }


    // smelt the plate into nuggets
    if (nugget != null && plate != null) {
      val SMELT_PLATE = cooking({
        Ingredient.of(DeltaboxUtil.TAGS.deltaboxItemTag("plates/$name"))
      },
        { nugget }) { b ->
        b
          .suffix("_from_plate")
          .comboOreSmelting(200, 0.5f)
      }
    }
  }

  class OreBlockProcessor(oreBlock: ItemLike, stoneBlock: ItemLike, multiplier: Int) {
    val oreBlock = oreBlock
    val stoneBlock = stoneBlock
    val multiplier = multiplier
  }
}
