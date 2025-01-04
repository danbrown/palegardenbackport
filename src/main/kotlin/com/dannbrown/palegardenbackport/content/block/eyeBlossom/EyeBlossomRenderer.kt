package com.dannbrown.palegardenbackport.content.block.eyeBlossom

import com.mojang.blaze3d.vertex.PoseStack
import net.minecraft.client.renderer.MultiBufferSource
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider
import net.minecraftforge.api.distmarker.Dist
import net.minecraftforge.api.distmarker.OnlyIn

@OnlyIn(Dist.CLIENT)
class EyeBlossomRenderer(pContext: BlockEntityRendererProvider.Context) : BlockEntityRenderer<EyeBlossomBlockEntity> {
  init {
  }

  override fun render(pBlockEntity: EyeBlossomBlockEntity, pPartialTick: Float, pPoseStack: PoseStack, pBuffer: MultiBufferSource, pPackedLight: Int, pPackedOverlay: Int) {
  }

  companion object {
  }
}
