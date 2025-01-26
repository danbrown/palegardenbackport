package com.dannbrown.deltaboxlib.common.content.entity.boat

import com.dannbrown.deltaboxlib.platform.util.DeltaboxUtil
import com.mojang.blaze3d.vertex.PoseStack
import com.mojang.datafixers.util.Pair
import com.mojang.math.Axis
import net.minecraft.client.model.BoatModel
import net.minecraft.client.model.ChestBoatModel
import net.minecraft.client.model.ListModel
import net.minecraft.client.model.WaterPatchModel
import net.minecraft.client.model.geom.ModelLayerLocation
import net.minecraft.client.renderer.MultiBufferSource
import net.minecraft.client.renderer.RenderType
import net.minecraft.client.renderer.entity.EntityRenderer
import net.minecraft.client.renderer.entity.EntityRendererProvider
import net.minecraft.client.renderer.texture.OverlayTexture
import net.minecraft.resources.ResourceLocation
import net.minecraft.util.Mth
import net.minecraft.world.entity.vehicle.Boat
import org.joml.Quaternionf

class BaseBoatRenderer(
  private val modId: String,
  private val variant: String,
  private val context: EntityRendererProvider.Context,
  private val isChestBoat: Boolean
) : EntityRenderer<Boat>(context) {
  override fun getTextureLocation(boat: Boat): ResourceLocation {
    if (boat is BaseBoatEntity) {
      val variant: String = boat.variant
      return DeltaboxUtil.resourceLocation(modId, "textures/entity/boat/$variant.png")
    } else if (boat is BaseChestBoatEntity) {
      val variant: String = boat.variant
      return DeltaboxUtil.resourceLocation(modId, "textures/entity/chest_boat/$variant.png")
    }
    return DeltaboxUtil.resourceLocation(
      modId,
      if (isChestBoat) "textures/entity/chest_boat/$variant.png" else "textures/entity/boat/$variant.png"
    )
  }

  fun getTextureLocation(): ResourceLocation {
    return DeltaboxUtil.resourceLocation(
      modId,
      if (isChestBoat) "textures/entity/chest_boat/$variant.png" else "textures/entity/boat/$variant.png"
    )
  }

  fun getModelWithLocation(boat: Boat): Pair<ResourceLocation, ListModel<Boat>>? {
    if (boat is BaseBoatEntity) {
      val variant: String = boat.variant // Retrieve the variant (hashed string name)
      return createModelWithLocation(variant, this.context, this.isChestBoat)
    } else if (boat is BaseChestBoatEntity) {
      val variant: String = boat.variant // Retrieve the variant (hashed string name)
      return createModelWithLocation(variant, this.context, true)
    }
    return null
  }

  // Dynamically create the model and texture location using the variant string
  private fun createModelWithLocation(
    variant: String,
    pContext: EntityRendererProvider.Context,
    pChestBoat: Boolean
  ): Pair<ResourceLocation, ListModel<Boat>> {
    val texture = getTextureLocation()
    val model = createBoatModel(pContext, variant, pChestBoat)
    return Pair.of(texture, model)
  }

  private fun createBoatModel(
    pContext: EntityRendererProvider.Context,
    variant: String,
    pChestBoat: Boolean
  ): ListModel<Boat> {
    val modelLayerLocation =
      if (pChestBoat) createChestBoatModelName(modId, variant) else createBoatModelName(modId, variant)
    val modelPart = pContext.bakeLayer(modelLayerLocation)
    return if (pChestBoat) ChestBoatModel(modelPart) else BoatModel(modelPart)
  }


  override fun render(
    boat: Boat,
    yaw: Float,
    partialTicks: Float,
    poseStack: PoseStack,
    bufferSource: MultiBufferSource,
    packedLight: Int
  ) {
    poseStack.pushPose()
    poseStack.translate(0.0f, 0.375f, 0.0f)
    poseStack.mulPose(Axis.YP.rotationDegrees(180.0f - yaw))
    val damageTime = boat.hurtTime - partialTicks
    var damage = boat.damage - partialTicks
    if (damage < 0.0f) damage = 0.0f

    if (damageTime > 0.0f) {
      poseStack.mulPose(Axis.XP.rotationDegrees(Mth.sin(damageTime) * damageTime * damage / 10.0f * boat.hurtDir))
    }

    val bubbleAngle = boat.getBubbleAngle(partialTicks)
    if (!Mth.equal(bubbleAngle, 0.0f)) {
      poseStack.mulPose(Quaternionf().setAngleAxis(bubbleAngle * (Math.PI.toFloat() / 180f), 1.0f, 0.0f, 1.0f))
    }

    val alt = getModelWithLocation(boat)?: return
    poseStack.scale(-1.0f, -1.0f, 1.0f)
    poseStack.mulPose(Axis.YP.rotationDegrees(90.0f))
    alt.second.setupAnim(boat, partialTicks, 0.0f, -0.1f, 0.0f, 0.0f)
    val vertexConsumer = bufferSource.getBuffer(alt.second.renderType(alt.first))
    /*? if >1.21 {*/
    /*alt.second.renderToBuffer(poseStack, vertexConsumer, packedLight, OverlayTexture.NO_OVERLAY);
    *//*?} else {*/
    alt.second.renderToBuffer(poseStack, vertexConsumer, packedLight, OverlayTexture.NO_OVERLAY, 1.0f, 1.0f, 1.0f, 1.0f)
    /*?}*/
    if (!boat.isUnderWater) {
      val waterBuffer = bufferSource.getBuffer(RenderType.waterMask())
      if (alt.second is WaterPatchModel) {
        (alt.second as WaterPatchModel).waterPatch().render(poseStack, waterBuffer, packedLight, OverlayTexture.NO_OVERLAY)
      }
    }

    poseStack.popPose()
    super.render(boat, yaw, partialTicks, poseStack, bufferSource, packedLight)
  }


  companion object {
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
