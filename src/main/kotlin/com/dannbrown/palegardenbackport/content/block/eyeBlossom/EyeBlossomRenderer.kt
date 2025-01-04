package com.dannbrown.palegardenbackport.content.block.eyeBlossom

import com.dannbrown.palegardenbackport.ModContent
import com.dannbrown.palegardenbackport.init.ModModelLayers
import com.mojang.blaze3d.vertex.PoseStack
import com.mojang.blaze3d.vertex.VertexConsumer
import net.minecraft.client.model.geom.ModelPart
import net.minecraft.client.model.geom.PartPose
import net.minecraft.client.model.geom.builders.CubeDeformation
import net.minecraft.client.model.geom.builders.CubeListBuilder
import net.minecraft.client.model.geom.builders.LayerDefinition
import net.minecraft.client.model.geom.builders.MeshDefinition
import net.minecraft.client.renderer.MultiBufferSource
import net.minecraft.client.renderer.RenderType
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider
import net.minecraft.client.renderer.texture.TextureAtlas
import net.minecraft.client.resources.model.Material
import net.minecraft.core.BlockPos
import net.minecraft.resources.ResourceLocation
import net.minecraft.util.Mth
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.phys.Vec3
import net.minecraftforge.api.distmarker.Dist
import net.minecraftforge.api.distmarker.OnlyIn
import org.joml.Quaternionf

@OnlyIn(Dist.CLIENT)
class EyeBlossomRenderer(pContext: BlockEntityRendererProvider.Context) : BlockEntityRenderer<EyeBlossomBlockEntity> {
  private val shell: ModelPart = pContext.bakeLayer(ModModelLayers.FLIGHT_CONDUIT_EYE)
  private val renderer: BlockEntityRenderDispatcher = pContext.blockEntityRenderDispatcher

  override fun render(
    pBlockEntity: EyeBlossomBlockEntity,
    pPartialTick: Float,
    pPoseStack: PoseStack,
    pBuffer: MultiBufferSource,
    pPackedLight: Int,
    pPackedOverlay: Int
  ) {
    val vertexConsumer = TEXTURE.buffer(pBuffer) { pLocation ->
      RenderType.entityTranslucentEmissive(pLocation)
    }
    val blockPos = pBlockEntity.blockPos
    val blockState = pBlockEntity.blockState
    val blockOffset = getBlockOffset(blockState, blockPos)

    if(pBlockEntity.blockState.`is`(ModContent.EYE_BLOSSOM.get())){
      pPoseStack.pushPose()
      pPoseStack.translate(0.5, 0.0, 0.5)

      pPoseStack.translate(blockOffset.x, blockOffset.y, blockOffset.z)
      val quaternion = Quaternionf().apply {
        rotateX(0.0f)
        rotateY(0.0f)
        rotateZ(0.0f)
      }
      pPoseStack.mulPose(quaternion)
      shell.render(pPoseStack, vertexConsumer, pPackedLight, pPackedOverlay)
      pPoseStack.popPose()
    }
  }

  companion object {
    val TEXTURE: Material = Material(
      TextureAtlas.LOCATION_BLOCKS,
      ResourceLocation(ModContent.MOD_ID, "block/open_eyeblossom_emissive")
    )

    fun getBlockOffset(blockState: BlockState, blockPos: BlockPos): Vec3 {
        val block = blockState.block
        val i = Mth.getSeed(blockPos.x, 0, blockPos.z)
        val f = block.maxHorizontalOffset
        val d0 = Mth.clamp((((i and 15L).toFloat() / 15.0f).toDouble() - 0.5) * 0.5, (-f).toDouble(), f.toDouble())
        val d1 = Mth.clamp((((i shr 8 and 15L).toFloat() / 15.0f).toDouble() - 0.5) * 0.5, (-f).toDouble(), f.toDouble())
        return Vec3(d0, 0.0, d1)
    }

    fun createEyeLayer(): LayerDefinition {
      val meshDefinition = MeshDefinition()
      val partDefinition = meshDefinition.root

      // First emissive plane (north-south)
      partDefinition.addOrReplaceChild(
        "plane_ns",
        CubeListBuilder.create()
          .texOffs(0, 0)
          .addBox(-8f, 0.0f, -0f, 16f, 16.0f, 0.0f, CubeDeformation.NONE), // Flat plane
        PartPose.offsetAndRotation(0.0f, 0.0f, 0.0f, 0.0f, Math.toRadians(45.0).toFloat(), 0.0f)
      )

      // Second emissive plane (east-west)
      partDefinition.addOrReplaceChild(
        "plane_ew",
        CubeListBuilder.create()
          .texOffs(0, 0)
          .addBox(-0f, 0.0f, -8f, 0.0f, 16.0f, 16f, CubeDeformation.NONE), // Flat plane
        PartPose.offsetAndRotation(0.0f, 0.0f, 0.0f, 0.0f, Math.toRadians(45.0).toFloat(), 0.0f)
      )

      return LayerDefinition.create(meshDefinition, 32, 32)
    }
  }
}
