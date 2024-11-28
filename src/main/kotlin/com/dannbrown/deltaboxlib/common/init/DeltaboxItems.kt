package com.dannbrown.deltaboxlib.common.init

import com.dannbrown.deltaboxlib.common.DeltaboxLib
import com.dannbrown.deltaboxlib.platform.registrate.generators.item.ItemGenerator
import com.dannbrown.deltaboxlib.platform.registrate.generators.recipe.RecipeBuilder
import com.dannbrown.deltaboxlib.platform.util.DeltaboxUtil
import net.minecraft.world.item.Item
import net.minecraft.world.item.Items
import net.minecraft.world.item.crafting.Ingredient
import java.util.function.Supplier

object DeltaboxItems {
  val ITEMS = ItemGenerator(DeltaboxLib.REGISTRATE)

  val WARP_CRYSTAL = ITEMS.create<Item>("warp_crystal")
    .recipe { c, p ->
      RecipeBuilder(ITEMS.registrate, p).simpleShapedRecipe(
        { c.get() },
        arrayOf("GSG", "SBS", "GSG"),
        mapOf(
          'G' to Supplier { Ingredient.of(Items.GLASS) },
          'S' to Supplier { Ingredient.of(Items.STICK) },
          'B' to Supplier { Ingredient.of(Items.BLAZE_POWDER) }
        )
      )
    }
    .register()

  fun register() {
    DeltaboxUtil.logInfo("Registering items...")
  }
}