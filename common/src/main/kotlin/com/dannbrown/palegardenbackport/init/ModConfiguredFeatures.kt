package com.dannbrown.palegardenbackport.init

import com.dannbrown.palegardenbackport.init.ModContent.REGISTRATE
import net.minecraft.core.registries.Registries
import net.minecraft.data.worldgen.placement.PlacementUtils
import net.minecraft.tags.BlockTags
import net.minecraft.util.valueproviders.ConstantInt
import net.minecraft.util.valueproviders.UniformInt
import net.minecraft.world.level.levelgen.feature.Feature
import net.minecraft.world.level.levelgen.feature.configurations.VegetationPatchConfiguration
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider
import net.minecraft.world.level.levelgen.placement.CaveSurface

object ModConfiguredFeatures {
  val PALE_MOSS_PATCH_BONEMEAL = REGISTRATE.configuredFeature("pale_moss_patch_bonemeal", { k, c, u ->
    val configuredFeatures = c.lookup(Registries.CONFIGURED_FEATURE)
    u.register(
      c, k, Feature.VEGETATION_PATCH,
      VegetationPatchConfiguration(
        BlockTags.MOSS_REPLACEABLE,
        BlockStateProvider.simple(ModBlocks.PALE_MOSS_BLOCK.get()),
        PlacementUtils.inlinePlaced(configuredFeatures.getOrThrow(k)),
        CaveSurface.FLOOR,
        ConstantInt.of(1),
        0.0f,
        5,
        0.6f,
        UniformInt.of(1, 2),
        0.75f
      )
    )
  })

  fun register() {
    // init
  }
}