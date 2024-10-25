package com.dannbrown.palegardenbackport.datagen

import com.dannbrown.palegardenbackport.ModContent
import com.dannbrown.palegardenbackport.datagen.lang.ModLangGen
import com.dannbrown.palegardenbackport.datagen.recipe.MainRecipeGen
import com.dannbrown.palegardenbackport.datagen.worldgen.ModConfiguredFeatures
import com.dannbrown.deltaboxlib.registry.datagen.DatagenRootInterface
import com.dannbrown.deltaboxlib.registry.datagen.recipe.DeltaboxRecipeProvider
import com.dannbrown.palegardenbackport.datagen.tags.ModBiomeTags
import com.dannbrown.palegardenbackport.datagen.worldgen.ModBiomeModifiers
import com.dannbrown.palegardenbackport.datagen.worldgen.ModBiomes
import com.dannbrown.palegardenbackport.datagen.worldgen.ModPlacedFeatures
import java.util.concurrent.CompletableFuture
import net.minecraft.core.HolderLookup
import net.minecraft.core.RegistrySetBuilder
import net.minecraft.core.registries.Registries
import net.minecraft.data.PackOutput
import net.minecraftforge.common.data.DatapackBuiltinEntriesProvider
import net.minecraftforge.data.event.GatherDataEvent
import net.minecraftforge.registries.ForgeRegistries

class ModDatagen(output: PackOutput, future: CompletableFuture<HolderLookup.Provider>) : DatapackBuiltinEntriesProvider(output, future, BUILDER, modIds) {
  companion object : DatagenRootInterface {
    override val modIds: MutableSet<String> = mutableSetOf(ModContent.MOD_ID)
    override val BUILDER: RegistrySetBuilder = RegistrySetBuilder()
      .add(Registries.CONFIGURED_FEATURE, ModConfiguredFeatures::bootstrap)
      .add(Registries.PLACED_FEATURE, ModPlacedFeatures::bootstrap)
      .add(Registries.BIOME, ModBiomes::bootstrap)
      .add(ForgeRegistries.Keys.BIOME_MODIFIERS, ModBiomeModifiers::bootstrap)

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
      // Biome Tags
      generator.addProvider(
        event.includeServer(),
        ModBiomeTags(packOutput, lookupProvider.thenApply { r -> append(r, BUILDER) }, existingFileHelper)
      )
    }
  }
}
