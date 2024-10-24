package com.dannbrown.palegardenbackport.datagen

import com.dannbrown.palegardenbackport.ModContent
import com.dannbrown.palegardenbackport.datagen.lang.ModLangGen
import com.dannbrown.palegardenbackport.datagen.recipe.MainRecipeGen
import com.dannbrown.palegardenbackport.datagen.worldgen.ModConfiguredFeatures
import com.dannbrown.deltaboxlib.registry.datagen.DatagenRootInterface
import com.dannbrown.deltaboxlib.registry.datagen.recipe.DeltaboxRecipeProvider
import java.util.concurrent.CompletableFuture
import net.minecraft.core.HolderLookup
import net.minecraft.core.RegistrySetBuilder
import net.minecraft.core.registries.Registries
import net.minecraft.data.PackOutput
import net.minecraftforge.common.data.DatapackBuiltinEntriesProvider
import net.minecraftforge.data.event.GatherDataEvent

class ModDatagen(output: PackOutput, future: CompletableFuture<HolderLookup.Provider>) :
        DatapackBuiltinEntriesProvider(output, future, BUILDER, modIds) {
  companion object : DatagenRootInterface {
    override val modIds: MutableSet<String> = mutableSetOf(ModContent.MOD_ID)
    override val BUILDER: RegistrySetBuilder = RegistrySetBuilder()
      .add(Registries.CONFIGURED_FEATURE, ModConfiguredFeatures::bootstrap)

    override fun gatherData(event: GatherDataEvent) {
      val generator = event.generator
      val packOutput = generator.packOutput
      val lookupProvider = event.lookupProvider
      val existingFileHelper = event.existingFileHelper
      // Builder generators above
      generator.addProvider(event.includeServer(), ModDatagen(packOutput, lookupProvider))
      // Langs
      ModLangGen.addStaticLangs(event.includeClient())
      // Recipes
      generator.addProvider(
        event.includeServer(),
        DeltaboxRecipeProvider(packOutput, listOf(MainRecipeGen()))
      )
    }
  }
}
