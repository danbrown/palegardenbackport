package com.dannbrown.deltaboxlib.platform.registrate.generators.recipe

import com.dannbrown.deltaboxlib.platform.registrate.DeltaboxRegistrate
import com.dannbrown.deltaboxlib.platform.util.DeltaboxUtil
import net.minecraft.core.HolderLookup
import net.minecraft.data.DataGenerator
import net.minecraft.data.recipes.RecipeProvider
import java.util.concurrent.CompletableFuture

/*? if forge || neoforge {*/
class DeltaboxRecipeProvider(private val registrate: DeltaboxRegistrate, private val generator: DataGenerator, completableFuture: CompletableFuture<HolderLookup.Provider>)
/*? if >=1.21 {*/
/*: RecipeProvider(generator.packOutput, completableFuture)
*//*?} else {*/
  : RecipeProvider(generator.packOutput)
/*?}*/
{
  override fun buildRecipes(
    /*? if >=1.21 {*/
    /*consumer: net.minecraft.data.recipes.RecipeOutput
    *//*?} else {*/
    consumer: java.util.function.Consumer<net.minecraft.data.recipes.FinishedRecipe>
  /*?}*/
  ) {
    val slicesRegistries = registrate.RECIPES
    for(registryEntry in slicesRegistries) {
      val slice = registryEntry.get()
      slice.registerRecipes(consumer)
      DeltaboxUtil.logInfo("DeltaBox Lib registered " + slice.all.size + " recipe" + (if (slice.all.size == 1) "" else "s" + " for " + slice.name()))
    }
  }
}
/*?}*/