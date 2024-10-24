package com.dannbrown.palegardenbackport.init

import com.dannbrown.palegardenbackport.ModContent.Companion.MOD_ID
import com.dannbrown.palegardenbackport.ModContent.Companion.WOOD_NAME
import net.minecraft.client.model.geom.ModelLayerLocation
import net.minecraft.resources.ResourceLocation

object ModModelLayers {
  // ----- Model Layers -----
  val BOAT_LAYER: ModelLayerLocation =
    ModelLayerLocation(ResourceLocation(MOD_ID, "boat/$WOOD_NAME"), "main")
  val CHEST_BOAT_LAYER: ModelLayerLocation =
    ModelLayerLocation(ResourceLocation(MOD_ID, "chest_boat/$WOOD_NAME"), "main")
  // ----- End Model Layers -----
}