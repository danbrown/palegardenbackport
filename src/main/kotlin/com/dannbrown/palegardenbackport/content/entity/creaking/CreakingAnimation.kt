package com.dannbrown.palegardenbackport.content.entity.creaking

import net.minecraft.client.animation.AnimationChannel
import net.minecraft.client.animation.AnimationDefinition
import net.minecraft.client.animation.Keyframe
import net.minecraft.client.animation.KeyframeAnimations
import net.minecraftforge.api.distmarker.Dist
import net.minecraftforge.api.distmarker.OnlyIn

@OnlyIn(Dist.CLIENT)
object CreakingAnimation {
  val CREAKING_WALK: AnimationDefinition = AnimationDefinition.Builder.withLength(1.125f)
    .looping()
    .addAnimation("upper_body",
      AnimationChannel(AnimationChannel.Targets.ROTATION,
        *arrayOf(Keyframe(0.0f, KeyframeAnimations.degreeVec(26.8802f, -23.399f, -9.0616f), AnimationChannel.Interpolations.LINEAR),
          Keyframe(0.125f, KeyframeAnimations.degreeVec(-2.2093f, 5.9119f, 0.0675f), AnimationChannel.Interpolations.LINEAR),
          Keyframe(0.5417f, KeyframeAnimations.degreeVec(23.0778f, 14.2906f, 4.6066f), AnimationChannel.Interpolations.LINEAR),
          Keyframe(0.7083f, KeyframeAnimations.degreeVec(-10.0f, 0.0f, 0.0f), AnimationChannel.Interpolations.LINEAR),
          Keyframe(0.875f, KeyframeAnimations.degreeVec(7.5f, 0.0f, 0.0f), AnimationChannel.Interpolations.LINEAR),
          Keyframe(1.125f, KeyframeAnimations.degreeVec(26.8802f, -23.399f, -9.0616f), AnimationChannel.Interpolations.LINEAR))))
    .addAnimation("head", AnimationChannel(
      AnimationChannel.Targets.ROTATION,
      *arrayOf(Keyframe(0.0f, KeyframeAnimations.degreeVec(0.0f, 0.0f, 0.0f), AnimationChannel.Interpolations.LINEAR),
        Keyframe(0.0417f, KeyframeAnimations.degreeVec(-17.5f, -62.5f, 0.0f), AnimationChannel.Interpolations.LINEAR),
        Keyframe(0.0833f, KeyframeAnimations.degreeVec(0.0f, 0.0f, 0.0f), AnimationChannel.Interpolations.LINEAR),
        Keyframe(0.4167f, KeyframeAnimations.degreeVec(0.0f, 0.0f, 0.0f), AnimationChannel.Interpolations.LINEAR),
        Keyframe(0.4583f, KeyframeAnimations.degreeVec(0.0f, 15.0f, 0.0f), AnimationChannel.Interpolations.LINEAR),
        Keyframe(0.5f, KeyframeAnimations.degreeVec(0.0f, 0.0f, 0.0f), AnimationChannel.Interpolations.LINEAR),
        Keyframe(1.0417f, KeyframeAnimations.degreeVec(0.0f, 0.0f, 0.0f), AnimationChannel.Interpolations.LINEAR),
        Keyframe(1.0833f, KeyframeAnimations.degreeVec(-37.1532f, 81.1131f, -28.3621f), AnimationChannel.Interpolations.LINEAR),
        Keyframe(1.125f, KeyframeAnimations.degreeVec(0.0f, 0.0f, 0.0f), AnimationChannel.Interpolations.LINEAR))))
    .addAnimation("right_arm", AnimationChannel(
      AnimationChannel.Targets.ROTATION,
      *arrayOf(Keyframe(0.0f, KeyframeAnimations.degreeVec(12.5f, 0.0f, 0.0f), AnimationChannel.Interpolations.LINEAR),
        Keyframe(0.25f, KeyframeAnimations.degreeVec(-32.0f, 0.0f, 0.0f), AnimationChannel.Interpolations.LINEAR),
        Keyframe(0.875f, KeyframeAnimations.degreeVec(12.0f, 0.0f, 0.0f), AnimationChannel.Interpolations.LINEAR),
        Keyframe(1.125f, KeyframeAnimations.degreeVec(-15.0f, 0.0f, 0.0f), AnimationChannel.Interpolations.LINEAR))))
    .addAnimation("left_arm", AnimationChannel(
      AnimationChannel.Targets.ROTATION,
      *arrayOf(Keyframe(0.0f, KeyframeAnimations.degreeVec(-15.0f, 0.0f, 0.0f), AnimationChannel.Interpolations.LINEAR),
        Keyframe(0.125f, KeyframeAnimations.degreeVec(10.0f, 0.0f, 0.0f), AnimationChannel.Interpolations.LINEAR),
        Keyframe(0.5417f, KeyframeAnimations.degreeVec(-25.0f, 0.0f, 0.0f), AnimationChannel.Interpolations.LINEAR),
        Keyframe(0.75f, KeyframeAnimations.degreeVec(-9.0923f, 0.0f, 0.0f), AnimationChannel.Interpolations.LINEAR),
        Keyframe(0.7917f, KeyframeAnimations.degreeVec(-15.137f, -66.7758f, 13.9603f), AnimationChannel.Interpolations.LINEAR),
        Keyframe(0.8333f, KeyframeAnimations.degreeVec(-9.0923f, 0.0f, 0.0f), AnimationChannel.Interpolations.LINEAR),
        Keyframe(1.0f, KeyframeAnimations.degreeVec(10.0f, 0.0f, 0.0f), AnimationChannel.Interpolations.LINEAR),
        Keyframe(1.125f, KeyframeAnimations.degreeVec(-15.0f, 0.0f, 0.0f), AnimationChannel.Interpolations.LINEAR))))
    .addAnimation("left_leg", AnimationChannel(
      AnimationChannel.Targets.ROTATION,
      *arrayOf(Keyframe(0.0f, KeyframeAnimations.degreeVec(0.0f, 0.0f, 0.0f), AnimationChannel.Interpolations.LINEAR),
        Keyframe(0.25f, KeyframeAnimations.degreeVec(30.0f, 0.0f, 0.0f), AnimationChannel.Interpolations.LINEAR),
        Keyframe(0.375f, KeyframeAnimations.degreeVec(49.8924f, -3.8282f, 3.2187f), AnimationChannel.Interpolations.LINEAR),
        Keyframe(0.5f, KeyframeAnimations.degreeVec(17.5f, 0.0f, 0.0f), AnimationChannel.Interpolations.LINEAR),
        Keyframe(0.625f, KeyframeAnimations.degreeVec(-56.5613f, -12.2403f, -8.7374f), AnimationChannel.Interpolations.LINEAR),
        Keyframe(0.9167f, KeyframeAnimations.degreeVec(0.0f, 0.0f, 0.0f), AnimationChannel.Interpolations.LINEAR),
        Keyframe(1.125f, KeyframeAnimations.degreeVec(0.0f, 0.0f, 0.0f), AnimationChannel.Interpolations.LINEAR))))
    .addAnimation("left_leg", AnimationChannel(
      AnimationChannel.Targets.POSITION,
      *arrayOf(Keyframe(0.0f, KeyframeAnimations.posVec(0.0f, 0.0f, 2.0f), AnimationChannel.Interpolations.LINEAR),
        Keyframe(0.25f, KeyframeAnimations.posVec(0.0f, 0.1846f, 0.5979f), AnimationChannel.Interpolations.LINEAR),
        Keyframe(0.375f, KeyframeAnimations.posVec(0.0f, -0.0665f, -2.2177f), AnimationChannel.Interpolations.LINEAR),
        Keyframe(0.5f, KeyframeAnimations.posVec(0.0f, 1.3563f, -4.3474f), AnimationChannel.Interpolations.LINEAR),
        Keyframe(0.625f, KeyframeAnimations.posVec(0.0f, 0.1047f, -1.6556f), AnimationChannel.Interpolations.LINEAR),
        Keyframe(0.9167f, KeyframeAnimations.posVec(0.0f, 0.0f, -1.0f), AnimationChannel.Interpolations.LINEAR),
        Keyframe(1.125f, KeyframeAnimations.posVec(0.0f, 0.0f, 2.0f), AnimationChannel.Interpolations.LINEAR))))
    .addAnimation("right_leg", AnimationChannel(
      AnimationChannel.Targets.ROTATION,
      *arrayOf(Keyframe(0.0f, KeyframeAnimations.degreeVec(25.5305f, 11.3125f, 5.3525f), AnimationChannel.Interpolations.LINEAR),
        Keyframe(0.125f, KeyframeAnimations.degreeVec(-49.5628f, 7.3556f, 6.7933f), AnimationChannel.Interpolations.LINEAR),
        Keyframe(0.25f, KeyframeAnimations.degreeVec(0.0f, 0.0f, 0.0f), AnimationChannel.Interpolations.LINEAR),
        Keyframe(0.4583f, KeyframeAnimations.degreeVec(0.0f, 0.0f, 0.0f), AnimationChannel.Interpolations.LINEAR),
        Keyframe(0.9167f, KeyframeAnimations.degreeVec(30.0f, 0.0f, 0.0f), AnimationChannel.Interpolations.LINEAR),
        Keyframe(1.0417f, KeyframeAnimations.degreeVec(55.0f, 0.0f, 0.0f), AnimationChannel.Interpolations.LINEAR),
        Keyframe(1.125f, KeyframeAnimations.degreeVec(25.5305f, 11.3125f, 5.3525f), AnimationChannel.Interpolations.LINEAR))))
    .addAnimation("right_leg", AnimationChannel(
      AnimationChannel.Targets.POSITION,
      *arrayOf(Keyframe(0.0f, KeyframeAnimations.posVec(0.0f, 0.9674f, -3.6578f), AnimationChannel.Interpolations.LINEAR),
        Keyframe(0.125f, KeyframeAnimations.posVec(0.0f, -0.2979f, -0.9411f), AnimationChannel.Interpolations.LINEAR),
        Keyframe(0.25f, KeyframeAnimations.posVec(0.0f, -0.3f, -0.94f), AnimationChannel.Interpolations.LINEAR),
        Keyframe(0.4583f, KeyframeAnimations.posVec(0.0f, -0.3f, 1.06f), AnimationChannel.Interpolations.LINEAR),
        Keyframe(1.125f, KeyframeAnimations.posVec(0.0f, 0.9674f, -3.6578f), AnimationChannel.Interpolations.LINEAR))))
    .build()
  val CREAKING_ATTACK: AnimationDefinition = AnimationDefinition.Builder.withLength(0.7083f)
    .looping()
    .addAnimation("upper_body",
      AnimationChannel(AnimationChannel.Targets.ROTATION,
        *arrayOf(Keyframe(0.0f, KeyframeAnimations.degreeVec(0.0f, 0.0f, 0.0f), AnimationChannel.Interpolations.LINEAR),
          Keyframe(0.0833f, KeyframeAnimations.degreeVec(0.0f, 45.0f, 0.0f), AnimationChannel.Interpolations.LINEAR),
          Keyframe(0.1667f, KeyframeAnimations.degreeVec(-115.0f, 67.5f, -90.0f), AnimationChannel.Interpolations.LINEAR),
          Keyframe(0.375f, KeyframeAnimations.degreeVec(67.5f, 0.0f, 0.0f), AnimationChannel.Interpolations.LINEAR),
          Keyframe(0.5417f, KeyframeAnimations.degreeVec(0.0f, 45.0f, 0.0f), AnimationChannel.Interpolations.LINEAR),
          Keyframe(0.7083f, KeyframeAnimations.degreeVec(0.0f, 0.0f, 0.0f), AnimationChannel.Interpolations.LINEAR))))
    .addAnimation("upper_body", AnimationChannel(
      AnimationChannel.Targets.POSITION,
      *arrayOf(Keyframe(0.0f, KeyframeAnimations.posVec(0.0f, 0.0f, 0.0f), AnimationChannel.Interpolations.LINEAR),
        Keyframe(0.0833f, KeyframeAnimations.posVec(0.0f, 0.0f, 0.0f), AnimationChannel.Interpolations.LINEAR),
        Keyframe(0.2917f, KeyframeAnimations.posVec(0.0f, -2.7716f, -1.1481f), AnimationChannel.Interpolations.LINEAR),
        Keyframe(0.375f, KeyframeAnimations.posVec(0.0f, 0.0f, 0.0f), AnimationChannel.Interpolations.LINEAR),
        Keyframe(0.5417f, KeyframeAnimations.posVec(0.0f, 0.0f, 0.0f), AnimationChannel.Interpolations.LINEAR),
        Keyframe(0.7083f, KeyframeAnimations.posVec(0.0f, 0.0f, 0.0f), AnimationChannel.Interpolations.LINEAR))))
    .addAnimation("upper_body", AnimationChannel(
      AnimationChannel.Targets.SCALE, *arrayOf(Keyframe(0.0f, KeyframeAnimations.scaleVec(1.0, 1.0, 1.0), AnimationChannel.Interpolations.LINEAR), Keyframe(0.7083f, KeyframeAnimations.scaleVec(1.0, 1.0, 1.0), AnimationChannel.Interpolations.LINEAR))))
    .addAnimation("head",
      AnimationChannel(AnimationChannel.Targets.ROTATION,
        *arrayOf(Keyframe(0.0f, KeyframeAnimations.degreeVec(0.0f, 0.0f, 0.0f), AnimationChannel.Interpolations.LINEAR),
          Keyframe(0.1667f, KeyframeAnimations.degreeVec(0.0f, -45.0f, 0.0f), AnimationChannel.Interpolations.LINEAR),
          Keyframe(0.25f, KeyframeAnimations.degreeVec(-11.25f, -45.0f, 0.0f), AnimationChannel.Interpolations.LINEAR),
          Keyframe(0.2917f, KeyframeAnimations.degreeVec(-117.3939f, 76.6331f, -130.1483f), AnimationChannel.Interpolations.LINEAR),
          Keyframe(0.4167f, KeyframeAnimations.degreeVec(-45.0f, -45.0f, 0.0f), AnimationChannel.Interpolations.LINEAR),
          Keyframe(0.5f, KeyframeAnimations.degreeVec(60.0f, -45.0f, 0.0f), AnimationChannel.Interpolations.LINEAR),
          Keyframe(0.5833f, KeyframeAnimations.degreeVec(60.0f, -45.0f, 0.0f), AnimationChannel.Interpolations.LINEAR),
          Keyframe(0.625f, KeyframeAnimations.degreeVec(0.0f, -45.0f, 0.0f), AnimationChannel.Interpolations.LINEAR),
          Keyframe(0.7083f, KeyframeAnimations.degreeVec(0.0f, 0.0f, 0.0f), AnimationChannel.Interpolations.LINEAR))))
    .addAnimation("head", AnimationChannel(
      AnimationChannel.Targets.POSITION,
      *arrayOf(Keyframe(0.0f, KeyframeAnimations.posVec(0.0f, 0.0f, 0.0f), AnimationChannel.Interpolations.LINEAR),
        Keyframe(0.1667f, KeyframeAnimations.posVec(0.0f, 0.0f, 0.0f), AnimationChannel.Interpolations.LINEAR),
        Keyframe(0.4167f, KeyframeAnimations.posVec(0.0f, 0.0f, 0.0f), AnimationChannel.Interpolations.LINEAR),
        Keyframe(0.5f, KeyframeAnimations.posVec(0.3827f, 0.5133f, -0.7682f), AnimationChannel.Interpolations.LINEAR),
        Keyframe(0.5833f, KeyframeAnimations.posVec(0.3827f, 0.5133f, -0.7682f), AnimationChannel.Interpolations.LINEAR),
        Keyframe(0.625f, KeyframeAnimations.posVec(0.0f, 0.0f, 0.0f), AnimationChannel.Interpolations.LINEAR),
        Keyframe(0.7083f, KeyframeAnimations.posVec(0.0f, 0.0f, 0.0f), AnimationChannel.Interpolations.LINEAR))))
    .addAnimation("head", AnimationChannel(
      AnimationChannel.Targets.SCALE,
      *arrayOf(Keyframe(0.1667f, KeyframeAnimations.scaleVec(1.0, 1.0, 1.0), AnimationChannel.Interpolations.LINEAR),
        Keyframe(0.4167f, KeyframeAnimations.scaleVec(1.0, 1.0, 1.0), AnimationChannel.Interpolations.LINEAR),
        Keyframe(0.5f, KeyframeAnimations.scaleVec(1.0, 1.2999999523162842, 1.0), AnimationChannel.Interpolations.LINEAR),
        Keyframe(0.625f, KeyframeAnimations.scaleVec(1.0, 1.0, 1.0), AnimationChannel.Interpolations.LINEAR))))
    .addAnimation("right_arm", AnimationChannel(
      AnimationChannel.Targets.ROTATION,
      *arrayOf(
        Keyframe(0.0f, KeyframeAnimations.degreeVec(0.0f, 0.0f, 0.0f), AnimationChannel.Interpolations.LINEAR),
        Keyframe(0.1667f, KeyframeAnimations.degreeVec(0.0f, 0.0f, 0.0f), AnimationChannel.Interpolations.LINEAR),
        Keyframe(0.25f, KeyframeAnimations.degreeVec(-7.5f, 0.0f, 0.0f), AnimationChannel.Interpolations.LINEAR),
        Keyframe(0.4583f, KeyframeAnimations.degreeVec(-55.0f, 0.0f, 0.0f), AnimationChannel.Interpolations.LINEAR),
        Keyframe(0.625f, KeyframeAnimations.degreeVec(0.0f, 0.0f, 0.0f), AnimationChannel.Interpolations.LINEAR),
        Keyframe(0.7083f, KeyframeAnimations.degreeVec(0.0f, 0.0f, 0.0f), AnimationChannel.Interpolations.LINEAR)
      )
    ))
    .addAnimation("right_arm", AnimationChannel(
      AnimationChannel.Targets.POSITION,
      *arrayOf(
        Keyframe(0.0f, KeyframeAnimations.posVec(0.0f, 0.0f, 0.0f), AnimationChannel.Interpolations.LINEAR),
        Keyframe(0.1667f, KeyframeAnimations.posVec(0.0f, 0.0f, 0.0f), AnimationChannel.Interpolations.LINEAR),
        Keyframe(0.625f, KeyframeAnimations.posVec(0.0f, 0.0f, 2.0f), AnimationChannel.Interpolations.LINEAR),
        Keyframe(0.7083f, KeyframeAnimations.posVec(0.0f, 0.0f, 0.0f), AnimationChannel.Interpolations.LINEAR)
      )
    ))
    .addAnimation("left_leg", AnimationChannel(
      AnimationChannel.Targets.ROTATION,
      *arrayOf(Keyframe(0.0f, KeyframeAnimations.degreeVec(0.0f, 0.0f, 0.0f), AnimationChannel.Interpolations.LINEAR),
        Keyframe(0.1667f, KeyframeAnimations.degreeVec(0.0f, 0.0f, 0.0f), AnimationChannel.Interpolations.LINEAR),
        Keyframe(0.625f, KeyframeAnimations.degreeVec(0.0f, 0.0f, 0.0f), AnimationChannel.Interpolations.LINEAR),
        Keyframe(0.7083f, KeyframeAnimations.degreeVec(0.0f, 0.0f, 0.0f), AnimationChannel.Interpolations.LINEAR))))
    .addAnimation("left_leg", AnimationChannel(
      AnimationChannel.Targets.POSITION,
      *arrayOf(Keyframe(0.0f, KeyframeAnimations.posVec(0.0f, 0.0f, 0.0f), AnimationChannel.Interpolations.LINEAR),
        Keyframe(0.1667f, KeyframeAnimations.posVec(0.0f, 0.0f, -2.0f), AnimationChannel.Interpolations.LINEAR),
        Keyframe(0.625f, KeyframeAnimations.posVec(0.0f, 0.0f, -2.0f), AnimationChannel.Interpolations.LINEAR),
        Keyframe(0.7083f, KeyframeAnimations.posVec(0.0f, 0.0f, 0.0f), AnimationChannel.Interpolations.LINEAR))))
    .addAnimation("right_leg", AnimationChannel(
      AnimationChannel.Targets.ROTATION,
      *arrayOf(Keyframe(0.0f, KeyframeAnimations.degreeVec(0.0f, 0.0f, 0.0f), AnimationChannel.Interpolations.LINEAR),
        Keyframe(0.1667f, KeyframeAnimations.degreeVec(0.0f, 45.0f, 0.0f), AnimationChannel.Interpolations.LINEAR),
        Keyframe(0.625f, KeyframeAnimations.degreeVec(0.0f, 45.0f, 0.0f), AnimationChannel.Interpolations.LINEAR),
        Keyframe(0.7083f, KeyframeAnimations.degreeVec(0.0f, 0.0f, 0.0f), AnimationChannel.Interpolations.LINEAR))))
    .addAnimation("right_leg", AnimationChannel(
      AnimationChannel.Targets.POSITION,
      *arrayOf(Keyframe(0.0f, KeyframeAnimations.posVec(0.0f, 0.0f, 0.0f), AnimationChannel.Interpolations.LINEAR),
        Keyframe(0.1667f, KeyframeAnimations.posVec(0.7071f, 0.0f, 0.0f), AnimationChannel.Interpolations.LINEAR),
        Keyframe(0.625f, KeyframeAnimations.posVec(0.7071f, 0.0f, 0.0f), AnimationChannel.Interpolations.LINEAR),
        Keyframe(0.7083f, KeyframeAnimations.posVec(0.0f, 0.0f, 0.0f), AnimationChannel.Interpolations.LINEAR))))
    .addAnimation("left_arm", AnimationChannel(
      AnimationChannel.Targets.ROTATION,
      *arrayOf(Keyframe(0.0f, KeyframeAnimations.degreeVec(0.0f, 0.0f, 0.0f), AnimationChannel.Interpolations.LINEAR),
        Keyframe(0.1667f, KeyframeAnimations.degreeVec(0.0f, 0.0f, 0.0f), AnimationChannel.Interpolations.LINEAR),
        Keyframe(0.25f, KeyframeAnimations.degreeVec(-10.3453f, 14.7669f, 2.664f), AnimationChannel.Interpolations.LINEAR),
        Keyframe(0.4583f, KeyframeAnimations.degreeVec(-57.5f, 0.0f, 0.0f), AnimationChannel.Interpolations.LINEAR),
        Keyframe(0.625f, KeyframeAnimations.degreeVec(0.0f, 0.0f, 0.0f), AnimationChannel.Interpolations.LINEAR),
        Keyframe(0.7083f, KeyframeAnimations.degreeVec(0.0f, 0.0f, 0.0f), AnimationChannel.Interpolations.LINEAR))))
    .addAnimation("left_arm", AnimationChannel(
      AnimationChannel.Targets.POSITION, *arrayOf(Keyframe(0.0f, KeyframeAnimations.posVec(0.0f, 0.0f, 0.0f), AnimationChannel.Interpolations.LINEAR), Keyframe(0.7083f, KeyframeAnimations.posVec(0.0f, 0.0f, 0.0f), AnimationChannel.Interpolations.LINEAR))))
    .build()
  val CREAKING_INVULNERABLE: AnimationDefinition = AnimationDefinition.Builder.withLength(0.2917f)
    .addAnimation("upper_body",
      AnimationChannel(AnimationChannel.Targets.ROTATION,
        *arrayOf(Keyframe(0.0f, KeyframeAnimations.degreeVec(0.0f, 0.0f, 0.0f), AnimationChannel.Interpolations.LINEAR),
          Keyframe(0.0833f, KeyframeAnimations.degreeVec(-5.0f, 0.0f, 0.0f), AnimationChannel.Interpolations.LINEAR),
          Keyframe(0.1667f, KeyframeAnimations.degreeVec(5.0f, 0.0f, 0.0f), AnimationChannel.Interpolations.LINEAR),
          Keyframe(0.25f, KeyframeAnimations.degreeVec(0.0f, 0.0f, 0.0f), AnimationChannel.Interpolations.LINEAR))))
    .addAnimation("upper_body", AnimationChannel(
      AnimationChannel.Targets.POSITION, *arrayOf(Keyframe(0.0f, KeyframeAnimations.posVec(0.0f, 0.0f, 0.0f), AnimationChannel.Interpolations.LINEAR), Keyframe(0.0833f, KeyframeAnimations.posVec(0.0f, 0.0f, 0.0f), AnimationChannel.Interpolations.LINEAR), Keyframe(0.25f, KeyframeAnimations.posVec(0.0f, 0.0f, 0.0f), AnimationChannel.Interpolations.LINEAR))))
    .addAnimation("right_arm", AnimationChannel(
      AnimationChannel.Targets.ROTATION,
      *arrayOf(Keyframe(0.0f, KeyframeAnimations.degreeVec(0.0f, 0.0f, 0.0f), AnimationChannel.Interpolations.LINEAR),
        Keyframe(0.0833f, KeyframeAnimations.degreeVec(17.5f, 0.0f, 0.0f), AnimationChannel.Interpolations.LINEAR),
        Keyframe(0.1667f, KeyframeAnimations.degreeVec(-15.0f, 0.0f, 0.0f), AnimationChannel.Interpolations.LINEAR),
        Keyframe(0.25f, KeyframeAnimations.degreeVec(0.0f, 0.0f, 0.0f), AnimationChannel.Interpolations.LINEAR))))
    .addAnimation("right_arm", AnimationChannel(
      AnimationChannel.Targets.POSITION, *arrayOf(Keyframe(0.0f, KeyframeAnimations.posVec(0.0f, 0.0f, 0.0f), AnimationChannel.Interpolations.LINEAR), Keyframe(0.25f, KeyframeAnimations.posVec(0.0f, 0.0f, 0.0f), AnimationChannel.Interpolations.LINEAR))))
    .addAnimation("left_arm",
      AnimationChannel(AnimationChannel.Targets.ROTATION,
        *arrayOf(Keyframe(0.0f, KeyframeAnimations.degreeVec(0.0f, 0.0f, 0.0f), AnimationChannel.Interpolations.LINEAR),
          Keyframe(0.0833f, KeyframeAnimations.degreeVec(20.0f, 0.0f, 0.0f), AnimationChannel.Interpolations.LINEAR),
          Keyframe(0.1667f, KeyframeAnimations.degreeVec(-15.0f, 0.0f, 0.0f), AnimationChannel.Interpolations.LINEAR),
          Keyframe(0.25f, KeyframeAnimations.degreeVec(0.0f, 0.0f, 0.0f), AnimationChannel.Interpolations.LINEAR))))
    .addAnimation("left_arm", AnimationChannel(
      AnimationChannel.Targets.POSITION, *arrayOf(Keyframe(0.0f, KeyframeAnimations.posVec(0.0f, 0.0f, 0.0f), AnimationChannel.Interpolations.LINEAR), Keyframe(0.25f, KeyframeAnimations.posVec(0.0f, 0.0f, 0.0f), AnimationChannel.Interpolations.LINEAR))))
    .build()
  val CREAKING_DEATH: AnimationDefinition = AnimationDefinition.Builder.withLength(2.25f)
    .addAnimation("upper_body",
      AnimationChannel(AnimationChannel.Targets.ROTATION,
        *arrayOf(Keyframe(0.0f, KeyframeAnimations.degreeVec(0.0f, 0.0f, 0.0f), AnimationChannel.Interpolations.LINEAR),
          Keyframe(0.0833f, KeyframeAnimations.degreeVec(-40.0f, 0.0f, 0.0f), AnimationChannel.Interpolations.LINEAR),
          Keyframe(0.1667f, KeyframeAnimations.degreeVec(-5.0f, 0.0f, 0.0f), AnimationChannel.Interpolations.LINEAR),
          Keyframe(0.2917f, KeyframeAnimations.degreeVec(7.5f, 0.0f, 0.0f), AnimationChannel.Interpolations.LINEAR),
          Keyframe(0.5833f, KeyframeAnimations.degreeVec(16.25f, 0.0f, 0.0f), AnimationChannel.Interpolations.LINEAR),
          Keyframe(0.6667f, KeyframeAnimations.degreeVec(29.0814f, 62.5516f, 26.5771f), AnimationChannel.Interpolations.LINEAR),
          Keyframe(0.75f, KeyframeAnimations.degreeVec(12.2115f, 0.0f, 0.0f), AnimationChannel.Interpolations.LINEAR),
          Keyframe(1.0f, KeyframeAnimations.degreeVec(10.25f, 0.0f, 0.0f), AnimationChannel.Interpolations.LINEAR),
          Keyframe(1.0417f, KeyframeAnimations.degreeVec(-47.64f, 0.0f, 0.0f), AnimationChannel.Interpolations.LINEAR),
          Keyframe(1.125f, KeyframeAnimations.degreeVec(21.96f, 0.0f, 0.0f), AnimationChannel.Interpolations.LINEAR),
          Keyframe(1.25f, KeyframeAnimations.degreeVec(12.5f, 0.0f, 0.0f), AnimationChannel.Interpolations.LINEAR),
          Keyframe(2.25f, KeyframeAnimations.degreeVec(17.3266f, 7.9022f, -0.1381f), AnimationChannel.Interpolations.LINEAR))))
    .addAnimation("upper_body", AnimationChannel(
      AnimationChannel.Targets.POSITION,
      *arrayOf(Keyframe(0.0f, KeyframeAnimations.posVec(0.0f, 0.0f, 0.0f), AnimationChannel.Interpolations.LINEAR),
        Keyframe(0.0833f, KeyframeAnimations.posVec(0.0f, 0.557f, 1.2659f), AnimationChannel.Interpolations.LINEAR),
        Keyframe(0.1667f, KeyframeAnimations.posVec(0.0f, -2.0889f, -0.3493f), AnimationChannel.Interpolations.LINEAR),
        Keyframe(0.2917f, KeyframeAnimations.posVec(0.0f, 0.0f, 0.0f), AnimationChannel.Interpolations.LINEAR))))
    .addAnimation("upper_body", AnimationChannel(
      AnimationChannel.Targets.SCALE,
      *arrayOf(Keyframe(0.0f, KeyframeAnimations.scaleVec(1.0, 1.0, 1.0), AnimationChannel.Interpolations.LINEAR),
        Keyframe(0.0833f, KeyframeAnimations.scaleVec(1.0, 1.100000023841858, 1.0), AnimationChannel.Interpolations.LINEAR),
        Keyframe(0.1667f, KeyframeAnimations.scaleVec(1.0, 0.8999999761581421, 1.0), AnimationChannel.Interpolations.LINEAR),
        Keyframe(0.2917f, KeyframeAnimations.scaleVec(1.0, 1.0, 1.0), AnimationChannel.Interpolations.LINEAR))))
    .addAnimation("right_arm", AnimationChannel(
      AnimationChannel.Targets.ROTATION,
      *arrayOf(Keyframe(0.0f, KeyframeAnimations.degreeVec(0.0f, 0.0f, 0.0f), AnimationChannel.Interpolations.LINEAR),
        Keyframe(0.2917f, KeyframeAnimations.degreeVec(-10.0f, 0.0f, 0.0f), AnimationChannel.Interpolations.LINEAR),
        Keyframe(0.5f, KeyframeAnimations.degreeVec(0.0f, 0.0f, 0.0f), AnimationChannel.Interpolations.LINEAR),
        Keyframe(1.25f, KeyframeAnimations.degreeVec(-10.0f, 0.0f, 0.0f), AnimationChannel.Interpolations.LINEAR),
        Keyframe(1.5417f, KeyframeAnimations.degreeVec(-10.0f, 0.0f, 0.0f), AnimationChannel.Interpolations.LINEAR),
        Keyframe(1.5833f, KeyframeAnimations.degreeVec(-12.1479f, -34.3927f, 6.9326f), AnimationChannel.Interpolations.LINEAR),
        Keyframe(1.6667f, KeyframeAnimations.degreeVec(-10.0f, 0.0f, 0.0f), AnimationChannel.Interpolations.LINEAR))))
    .addAnimation("right_arm", AnimationChannel(
      AnimationChannel.Targets.POSITION, *arrayOf(Keyframe(0.0f, KeyframeAnimations.posVec(0.0f, 0.0f, 0.0f), AnimationChannel.Interpolations.LINEAR), Keyframe(0.2917f, KeyframeAnimations.posVec(0.0f, 0.0f, 0.0f), AnimationChannel.Interpolations.LINEAR))))
    .addAnimation("left_arm",
      AnimationChannel(AnimationChannel.Targets.ROTATION,
        *arrayOf(Keyframe(0.0f, KeyframeAnimations.degreeVec(0.0f, 0.0f, 0.0f), AnimationChannel.Interpolations.LINEAR),
          Keyframe(0.2917f, KeyframeAnimations.degreeVec(-10.0f, 0.0f, 0.0f), AnimationChannel.Interpolations.LINEAR),
          Keyframe(0.5f, KeyframeAnimations.degreeVec(0.0f, 0.0f, 0.0f), AnimationChannel.Interpolations.LINEAR),
          Keyframe(0.8333f, KeyframeAnimations.degreeVec(-4.4444f, 0.0f, 0.0f), AnimationChannel.Interpolations.LINEAR),
          Keyframe(0.875f, KeyframeAnimations.degreeVec(-26.7402f, -78.831f, 26.3025f), AnimationChannel.Interpolations.LINEAR),
          Keyframe(0.9583f, KeyframeAnimations.degreeVec(-5.5556f, 0.0f, 0.0f), AnimationChannel.Interpolations.LINEAR),
          Keyframe(1.25f, KeyframeAnimations.degreeVec(-10.0f, 0.0f, 0.0f), AnimationChannel.Interpolations.LINEAR))))
    .addAnimation("left_arm", AnimationChannel(
      AnimationChannel.Targets.POSITION, *arrayOf(Keyframe(0.0f, KeyframeAnimations.posVec(0.0f, 0.0f, 0.0f), AnimationChannel.Interpolations.LINEAR), Keyframe(0.2917f, KeyframeAnimations.posVec(0.0f, 0.0f, 0.0f), AnimationChannel.Interpolations.LINEAR))))
    .addAnimation("head",
      AnimationChannel(AnimationChannel.Targets.ROTATION,
        *arrayOf(Keyframe(0.0f, KeyframeAnimations.degreeVec(0.0f, 0.0f, 0.0f), AnimationChannel.Interpolations.LINEAR),
          Keyframe(0.0833f, KeyframeAnimations.degreeVec(-5.0f, 0.0f, 0.0f), AnimationChannel.Interpolations.LINEAR),
          Keyframe(0.2917f, KeyframeAnimations.degreeVec(10.0f, 0.0f, 0.0f), AnimationChannel.Interpolations.LINEAR),
          Keyframe(0.5f, KeyframeAnimations.degreeVec(2.5f, 0.0f, 0.0f), AnimationChannel.Interpolations.LINEAR),
          Keyframe(0.5417f, KeyframeAnimations.degreeVec(5.5f, 0.0f, 0.0f), AnimationChannel.Interpolations.LINEAR),
          Keyframe(0.5833f, KeyframeAnimations.degreeVec(-67.4168f, -12.9552f, -8.0231f), AnimationChannel.Interpolations.LINEAR),
          Keyframe(0.6667f, KeyframeAnimations.degreeVec(8.5f, 0.0f, 0.0f), AnimationChannel.Interpolations.LINEAR),
          Keyframe(1.0f, KeyframeAnimations.degreeVec(10.773f, -29.5608f, -5.3627f), AnimationChannel.Interpolations.LINEAR),
          Keyframe(1.25f, KeyframeAnimations.degreeVec(10.0f, 0.0f, 0.0f), AnimationChannel.Interpolations.LINEAR),
          Keyframe(1.7917f, KeyframeAnimations.degreeVec(10.0f, 0.0f, 0.0f), AnimationChannel.Interpolations.LINEAR),
          Keyframe(1.8333f, KeyframeAnimations.degreeVec(12.9625f, 39.2735f, 8.2901f), AnimationChannel.Interpolations.LINEAR),
          Keyframe(1.9167f, KeyframeAnimations.degreeVec(10.0f, 0.0f, 0.0f), AnimationChannel.Interpolations.LINEAR))))
    .addAnimation("head", AnimationChannel(
      AnimationChannel.Targets.POSITION, *arrayOf(Keyframe(0.0f, KeyframeAnimations.posVec(0.0f, 0.0f, 0.0f), AnimationChannel.Interpolations.LINEAR), Keyframe(0.2917f, KeyframeAnimations.posVec(0.0f, 0.0f, 0.0f), AnimationChannel.Interpolations.LINEAR))))
    .build()
}
