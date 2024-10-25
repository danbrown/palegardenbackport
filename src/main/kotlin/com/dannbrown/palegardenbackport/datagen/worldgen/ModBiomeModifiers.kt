package com.dannbrown.palegardenbackport.datagen.worldgen

import com.dannbrown.deltaboxlib.lib.LibTags
import com.dannbrown.deltaboxlib.registry.worldgen.AbstractBiomeModifiersGen
import com.dannbrown.palegardenbackport.ModContent
import net.minecraft.core.Holder
import net.minecraft.core.HolderGetter
import net.minecraft.core.registries.Registries
import net.minecraft.data.worldgen.BootstapContext
import net.minecraft.world.level.biome.Biome
import net.minecraft.world.level.levelgen.placement.PlacedFeature
import net.minecraftforge.common.world.BiomeModifier

object ModBiomeModifiers: AbstractBiomeModifiersGen() {
  override val modId: String = ModContent.MOD_ID
  // Content
  val ADD_PALE_OAK = registerKey("add_pale_oak")

  override fun bootstrap(context: BootstapContext<BiomeModifier>) {
    val biomeLookup: HolderGetter<Biome> = context.lookup(Registries.BIOME)
    val featureLookup: HolderGetter<PlacedFeature> = context.lookup(Registries.PLACED_FEATURE)

    val paleOakPlaced: Holder<PlacedFeature> = featureLookup.getOrThrow(ModPlacedFeatures.PALE_OAK_PLACED)
    context.register(ADD_PALE_OAK, addVegetation(biomeLookup.getOrThrow(LibTags.modBiomeTag(ModContent.MOD_ID, "has_pale_oak")), paleOakPlaced))
  }
}