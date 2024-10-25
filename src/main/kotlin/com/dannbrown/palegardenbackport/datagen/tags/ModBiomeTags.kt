package com.dannbrown.palegardenbackport.datagen.tags

import com.dannbrown.deltaboxlib.lib.LibTags
import com.dannbrown.palegardenbackport.ModContent
import com.dannbrown.palegardenbackport.datagen.worldgen.biome.PaleGardenBiome
import net.minecraft.core.HolderLookup
import net.minecraft.data.PackOutput
import net.minecraft.data.tags.BiomeTagsProvider
import net.minecraft.tags.BiomeTags
import net.minecraft.world.level.biome.Biomes
import net.minecraftforge.common.data.ExistingFileHelper
import java.util.concurrent.CompletableFuture

class ModBiomeTags(
  val output: PackOutput,
  val future: CompletableFuture<HolderLookup.Provider>,
  val existingFileHelper: ExistingFileHelper?
) : BiomeTagsProvider(output, future, ModContent.MOD_ID, existingFileHelper) {
  override fun getName(): String {
    return "${ModContent.NAME} Biome Tags"
  }

  override fun addTags(provider: HolderLookup.Provider) {
    tag(BiomeTags.IS_OVERWORLD)
      .add(PaleGardenBiome.BIOME_KEY)

    tag(LibTags.modBiomeTag(ModContent.MOD_ID, "has_pale_oak"))
      .add(Biomes.DARK_FOREST)
  }
}
