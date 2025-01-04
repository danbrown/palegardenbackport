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

  val ADD_PALE_OAK_TREE = registerKey("add_pale_oak_tree")

  override fun bootstrap(context: BootstapContext<BiomeModifier>) {
    val biomeLookup: HolderGetter<Biome> = context.lookup(Registries.BIOME)
    val featureLookup: HolderGetter<PlacedFeature> = context.lookup(Registries.PLACED_FEATURE)

    val paleTreePlaced: Holder<PlacedFeature> = featureLookup.getOrThrow(ModPlacedFeatures.PALE_OAK_HEART_PLACED)
    context.register(ADD_PALE_OAK_TREE, addVegetation(biomeLookup.getOrThrow(ModContent.HAS_PALE_OAK), paleTreePlaced))
  }
}