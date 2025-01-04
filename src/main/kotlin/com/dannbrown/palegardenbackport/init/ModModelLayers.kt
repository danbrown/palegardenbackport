package com.dannbrown.palegardenbackport.init

import com.dannbrown.palegardenbackport.ModContent.Companion.MOD_ID
import com.dannbrown.palegardenbackport.ModContent.Companion.WOOD_NAME
import com.dannbrown.palegardenbackport.content.block.eyeBlossom.EyeBlossomRenderer
import com.dannbrown.palegardenbackport.content.entity.creaking.CreakingModel
import net.minecraft.client.model.BoatModel
import net.minecraft.client.model.ChestBoatModel
import net.minecraft.client.model.geom.ModelLayerLocation
import net.minecraft.resources.ResourceLocation
import net.minecraftforge.client.event.EntityRenderersEvent

object ModModelLayers {
  // ----- Model Layers -----
  val BOAT_LAYER: ModelLayerLocation =
    ModelLayerLocation(ResourceLocation(MOD_ID, "boat/$WOOD_NAME"), "main")
  val CHEST_BOAT_LAYER: ModelLayerLocation =
    ModelLayerLocation(ResourceLocation(MOD_ID, "chest_boat/$WOOD_NAME"), "main")
  val FLIGHT_CONDUIT_EYE: ModelLayerLocation = ModelLayerLocation(
    ResourceLocation(MOD_ID, "eyeblossom/eye"), "main")


  val CREAKING: ModelLayerLocation = ModelLayerLocation(ResourceLocation(MOD_ID, "creaking"), "main")
  // ----- End Model Layers -----

  fun register(event: EntityRenderersEvent.RegisterLayerDefinitions) {
    event.registerLayerDefinition(BOAT_LAYER, BoatModel::createBodyModel)
    event.registerLayerDefinition(CHEST_BOAT_LAYER, ChestBoatModel::createBodyModel)
    event.registerLayerDefinition(CREAKING) { CreakingModel.create() }
    event.registerLayerDefinition(FLIGHT_CONDUIT_EYE, EyeBlossomRenderer::createEyeLayer);
  }
}