package com.dannbrown.palegardenbackport.content.entity.creaking

import com.mojang.blaze3d.vertex.PoseStack
import com.mojang.blaze3d.vertex.VertexConsumer
import net.minecraft.client.model.ArmedModel
import net.minecraft.client.model.HeadedModel
import net.minecraft.client.model.HierarchicalModel
import net.minecraft.client.model.geom.ModelPart
import net.minecraft.client.model.geom.PartPose
import net.minecraft.client.model.geom.builders.CubeListBuilder
import net.minecraft.client.model.geom.builders.LayerDefinition
import net.minecraft.client.model.geom.builders.MeshDefinition
import net.minecraft.world.entity.HumanoidArm

class CreakingModel(private val modelPart: ModelPart) : HierarchicalModel<CreakingEntity>(), ArmedModel, HeadedModel {
  override fun root(): ModelPart {
    return modelPart
  }

  private val root: ModelPart = modelPart.getChild("root") ?: throw IllegalArgumentException("Model part not found: root")
  private val upperBody: ModelPart = root.getChild("upper_body") ?: throw IllegalArgumentException("Model part not found: upper_body")
  private val head: ModelPart = upperBody.getChild("head") ?: throw IllegalArgumentException("Model part not found: head")
  private val body: ModelPart = upperBody.getChild("body") ?: throw IllegalArgumentException("Model part not found: body")
  private val rightArm: ModelPart = upperBody.getChild("right_arm") ?: throw IllegalArgumentException("Model part not found: right_arm")
  private val leftArm: ModelPart = upperBody.getChild("left_arm") ?: throw IllegalArgumentException("Model part not found: left_arm")
  private val rightLeg: ModelPart = root.getChild("right_leg") ?: throw IllegalArgumentException("Model part not found: right_leg")
  private val leftLeg: ModelPart = root.getChild("left_leg") ?: throw IllegalArgumentException("Model part not found: left_leg")


  override fun setupAnim(entity: CreakingEntity, limbSwing: Float, limbSwingAmount: Float, ageInTicks: Float, netHeadYaw: Float, headPitch: Float) {
    val var1: Iterator<*> = this.modelPart.allParts.iterator()
    while (var1.hasNext()) {
      val modelPart1 = var1.next() as ModelPart
      modelPart1.resetPose()
    }

    val flag: Boolean = entity.fallFlyingTicks > 4
    head.yRot = netHeadYaw * 0.017453292f
    if (flag) {
      head.xRot = -0.7853982f
    }
    else {
      head.xRot = headPitch * 0.017453292f
    }

    this.animateWalk(CreakingAnimation.CREAKING_WALK, limbSwing, limbSwingAmount, 1.0f, 1.0f)
    this.animate(entity.attackAnimationState, CreakingAnimation.CREAKING_ATTACK, ageInTicks)
    this.animate(entity.invulnerabilityAnimationState, CreakingAnimation.CREAKING_INVULNERABLE, ageInTicks)
    this.animate(entity.deathAnimationState, CreakingAnimation.CREAKING_DEATH, ageInTicks)
  }


  override fun translateToHand(arm: HumanoidArm, stack: PoseStack) {
    (if (arm == HumanoidArm.LEFT) this.leftArm else this.rightArm).translateAndRotate(stack)
  }

  override fun getHead(): ModelPart {
    return this.head
  }

  override fun renderToBuffer(poseStack: PoseStack, vertexConsumer: VertexConsumer, packedLight: Int, packedOverlay: Int, red: Float, green: Float, blue: Float, alpha: Float) {
    head.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha)
    body.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha)
    rightArm.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha)
    leftArm.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha)
    rightLeg.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha)
    leftLeg.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha)
  }

  companion object {
    fun create(): LayerDefinition {
      val meshDefinition = MeshDefinition()
      val partdefinition = meshDefinition.root
      val `$$2` = partdefinition.addOrReplaceChild("root", CubeListBuilder.create(), PartPose.offset(0.0f, 29.0f, 0.0f))
      val `$$3` = `$$2`.addOrReplaceChild("upper_body", CubeListBuilder.create(), PartPose.offset(-1.0f, -14.0f, 0.0f))
      `$$3`.addOrReplaceChild("head",
        CubeListBuilder.create()
          .texOffs(0, 0)
          .addBox(-3.0f, -10.0f, -3.0f, 6.0f, 10.0f, 6.0f)
          .texOffs(28, 31)
          .addBox(-3.0f, -13.0f, -3.0f, 6.0f, 3.0f, 6.0f)
          .texOffs(12, 40)
          .addBox(3.0f, -13.0f, 0.0f, 9.0f, 14.0f, 0.0f)
          .texOffs(34, 12)
          .addBox(-12.0f, -14.0f, 0.0f, 9.0f, 14.0f, 0.0f),
        PartPose.offset(-3.0f, -6.0f, 0.0f))
      `$$3`.addOrReplaceChild("body",
        CubeListBuilder.create()
          .texOffs(0, 16)
          .addBox(0.0f, -3.0f, -3.0f, 6.0f, 13.0f, 5.0f)
          .texOffs(24, 0)
          .addBox(-6.0f, -4.0f, -3.0f, 6.0f, 7.0f, 5.0f),
        PartPose.offset(0.0f, -2.0f, 1.0f))
      `$$3`.addOrReplaceChild("right_arm",
        CubeListBuilder.create()
          .texOffs(22, 13)
          .addBox(-2.0f, -1.5f, -1.5f, 3.0f, 21.0f, 3.0f)
          .texOffs(46, 0)
          .addBox(-2.0f, 19.5f, -1.5f, 3.0f, 4.0f, 3.0f),
        PartPose.offset(-7.0f, -4.5f, 1.5f))
      `$$3`.addOrReplaceChild("left_arm",
        CubeListBuilder.create()
          .texOffs(30, 40)
          .addBox(0.0f, -1.0f, -1.5f, 3.0f, 16.0f, 3.0f)
          .texOffs(52, 12)
          .addBox(0.0f, -5.0f, -1.5f, 3.0f, 4.0f, 3.0f)
          .texOffs(52, 19)
          .addBox(0.0f, 15.0f, -1.5f, 3.0f, 4.0f, 3.0f),
        PartPose.offset(6.0f, -4.0f, 0.5f))
      `$$2`.addOrReplaceChild("left_leg",
        CubeListBuilder.create()
          .texOffs(42, 40)
          .addBox(-1.5f, 0.0f, -1.5f, 3.0f, 16.0f, 3.0f)
          .texOffs(45, 55)
          .addBox(-1.5f, 15.7f, -4.5f, 5.0f, 0.0f, 9.0f),
        PartPose.offset(2.5f, 8.0f, 0.5f))
      `$$2`.addOrReplaceChild("right_leg",
        CubeListBuilder.create()
          .texOffs(0, 34)
          .addBox(-3.0f, -1.5f, -1.5f, 3.0f, 19.0f, 3.0f)
          .texOffs(45, 46)
          .addBox(-5.0f, 17.2f, -4.5f, 5.0f, 0.0f, 9.0f)
          .texOffs(12, 34)
          .addBox(-3.0f, -4.5f, -1.5f, 3.0f, 3.0f, 3.0f),
        PartPose.offset(0.0f, 6.5f, 0.5f))

      return LayerDefinition.create(meshDefinition, 64, 64)
    }
  }
}