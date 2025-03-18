package com.dannbrown.palegardenbackport.forge.compat

import com.dannbrown.palegardenbackport.init.ModContent
import net.minecraft.resources.ResourceLocation
import terrablender.api.Regions

object ModTerraBlenderAPIForge {
  fun registerRegions() {
    Regions.register(ModOverworldRegionForge(ResourceLocation(ModContent.MOD_ID, "overworld"), 1))
  }
}