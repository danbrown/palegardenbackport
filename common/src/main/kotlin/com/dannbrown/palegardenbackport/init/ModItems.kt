package com.dannbrown.palegardenbackport.init

import com.dannbrown.deltaboxlib.content.item.DeltaboxSpawnEggItem
import com.dannbrown.deltaboxlib.init.DeltaboxTags
import com.dannbrown.deltaboxlib.registrate.datagen.RegistrateRecipes
import com.dannbrown.deltaboxlib.registrate.util.DataIngredient
import com.dannbrown.palegardenbackport.init.ModContent.REGISTRATE
import net.minecraft.data.recipes.RecipeCategory
import net.minecraft.world.item.Item
import net.minecraft.world.item.SpawnEggItem

object ModItems {
  val MOD_ICON = REGISTRATE.item<Item>("mod_icon").itemTags(DeltaboxTags.ITEM.EXCLUDE_FROM_CREATIVE).register()
  val RESIN_BRICK = REGISTRATE.item<Item>("resin_brick")
    .recipe { g, i ->
      g.simpleCookingRecipe(
        { i.get() },
        { DataIngredient(ModBlocks.RESIN_CLUMP.get()) },
        RecipeCategory.DECORATIONS,
        RegistrateRecipes.CookingRecipeType.SMELTING,
        0.1f, 200
      )
    }
    .register()
  val CREAKING_SPAWN_EGG =
    REGISTRATE.item<SpawnEggItem>("creaking_spawn_egg")
      .factory { p -> DeltaboxSpawnEggItem({ ModEntityTypes.CREAKING.get() }, -10526881, -231406, p) }
      .model { g, i ->
        g.spawnEgg(i.get())
      }
      .register()

  fun register() {
    // init
  }
}