package com.dannbrown.palegardenbackport.fabric.compat

import com.dannbrown.palegardenbackport.init.ModContent
import net.minecraft.resources.ResourceLocation
import terrablender.api.Regions

object ModTerraBlenderAPIFabric {
  fun registerRegions() {
    Regions.register(ModOverworldRegionFabric(ResourceLocation(ModContent.MOD_ID, "overworld"), 1))
  }
}