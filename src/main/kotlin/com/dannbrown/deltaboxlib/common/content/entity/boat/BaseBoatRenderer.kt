package com.dannbrown.deltaboxlib.common.content.entity.boat

import com.dannbrown.deltaboxlib.platform.util.DeltaboxUtil
import com.mojang.datafixers.util.Pair
import net.minecraft.client.model.BoatModel
import net.minecraft.client.model.ChestBoatModel
import net.minecraft.client.model.ListModel
import net.minecraft.client.model.geom.ModelLayerLocation
import net.minecraft.client.renderer.entity.BoatRenderer
import net.minecraft.client.renderer.entity.EntityRendererProvider
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.entity.vehicle.Boat

class BaseBoatRenderer(private val modId: String, private val context: EntityRendererProvider.Context, private val isChestBoat: Boolean) : BoatRenderer(context, isChestBoat) {
  override fun getModelWithLocation(boat: Boat): Pair<ResourceLocation, ListModel<Boat>> {
    if (boat is BaseBoatEntity) {
      val variant: String = boat.modVariant // Retrieve the variant (hashed string name)
      return createModelWithLocation(variant, this.context, this.isChestBoat)
    }
    else if (boat is BaseChestBoatEntity) {
      val variant: String = boat.modVariant // Retrieve the variant (hashed string name)
      return createModelWithLocation(variant, this.context, true)
    }
    return super.getModelWithLocation(boat)
  }

  // Dynamically create the model and texture location using the variant string
  private fun createModelWithLocation(variant: String, pContext: EntityRendererProvider.Context, pChestBoat: Boolean): Pair<ResourceLocation, ListModel<Boat>> {
    val texture = DeltaboxUtil.resourceLocation(modId, getTextureLocation(variant, pChestBoat))
    val model = createBoatModel(pContext, variant, pChestBoat)
    return Pair.of(texture, model)
  }

  private fun createBoatModel(pContext: EntityRendererProvider.Context, variant: String, pChestBoat: Boolean): ListModel<Boat> {
    val modelLayerLocation = if (pChestBoat) createChestBoatModelName(modId, variant) else createBoatModelName(modId, variant)
    val modelPart = pContext.bakeLayer(modelLayerLocation)
    return if (pChestBoat) ChestBoatModel(modelPart) else BoatModel(modelPart)
  }

  companion object {
    private fun getTextureLocation(variant: String, pChestBoat: Boolean): String {
      return if (pChestBoat) "textures/entity/chest_boat/$variant.png" else "textures/entity/boat/$variant.png"
    }

    fun createBoatModelName(modId: String, variant: String): ModelLayerLocation {
      return createLocation(modId, "boat/$variant", "main")
    }

    fun createChestBoatModelName(modId: String, variant: String): ModelLayerLocation {
      return createLocation(modId, "chest_boat/$variant", "main")
    }

    private fun createLocation(modId: String, path: String, model: String): ModelLayerLocation {
      return ModelLayerLocation(DeltaboxUtil.resourceLocation(modId, path), model)
    }
  }
}
