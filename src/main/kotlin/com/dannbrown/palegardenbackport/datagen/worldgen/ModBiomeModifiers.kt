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

  override fun bootstrap(context: BootstapContext<BiomeModifier>) {
    val biomeLookup: HolderGetter<Biome> = context.lookup(Registries.BIOME)
    val featureLookup: HolderGetter<PlacedFeature> = context.lookup(Registries.PLACED_FEATURE)

  }
}