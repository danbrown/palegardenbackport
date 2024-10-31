package com.dannbrown.palegardenbackport.terrablender

import com.dannbrown.palegardenbackport.ModContent
import net.minecraft.resources.ResourceLocation
import terrablender.api.Regions

object ModTerraBlenderAPI {
  fun registerRegions () {
    Regions.register(ModOverworldRegion(ResourceLocation(ModContent.MOD_ID, "overworld"), 1))
  }
}