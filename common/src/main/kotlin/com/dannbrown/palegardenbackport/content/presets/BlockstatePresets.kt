package com.dannbrown.palegardenbackport.content.presets

import com.dannbrown.deltaboxlib.registrate.datagen.model.RegistrateModelTemplates
import com.dannbrown.deltaboxlib.registrate.types.BlockstateFactory
import com.dannbrown.palegardenbackport.content.blocks.creakingHeart.CreakingHeartBlock
import net.minecraft.core.Direction.Axis
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.data.models.blockstates.MultiVariantGenerator
import net.minecraft.data.models.blockstates.PropertyDispatch
import net.minecraft.data.models.blockstates.Variant
import net.minecraft.data.models.blockstates.VariantProperties
import net.minecraft.data.models.model.TextureMapping
import net.minecraft.data.models.model.TextureSlot
import net.minecraft.world.level.block.state.properties.BlockStateProperties

object BlockstatePresets {
  fun creakingHeart(): BlockstateFactory {
    return { g, b ->
      val location = RegistrateModelTemplates.ROTATED_PILLAR.create(
        BuiltInRegistries.BLOCK.getKey(b.get()).withPrefix("block/").withSuffix(""),
        TextureMapping()
          .put(TextureSlot.END, g.optionalTexture(b.get(), "", "_top", "block/"))
          .put(TextureSlot.SIDE, g.optionalTexture(b.get(), "", "", "block/")),
        g.modelOutput
      )

      val location2 = RegistrateModelTemplates.ROTATED_PILLAR.create(
        BuiltInRegistries.BLOCK.getKey(b.get()).withPrefix("block/").withSuffix("_active"),
        TextureMapping()
          .put(TextureSlot.END, g.optionalTexture(b.get(), "", "_top_active", "block/"))
          .put(TextureSlot.SIDE, g.optionalTexture(b.get(), "", "_active", "block/")),
        g.modelOutput
      )

      val rotatedPillarState = PropertyDispatch.properties(BlockStateProperties.AXIS, CreakingHeartBlock.ACTIVE)
        .select(Axis.Y, false, Variant.variant().with(VariantProperties.MODEL, location))
        .select(
          Axis.Z,
          false,
          Variant.variant().with(VariantProperties.MODEL, location)
            .with(VariantProperties.X_ROT, VariantProperties.Rotation.R90)
        )
        .select(
          Axis.X, false,
          Variant.variant().with(VariantProperties.MODEL, location)
            .with(VariantProperties.X_ROT, VariantProperties.Rotation.R90)
            .with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90)
        )
        .select(Axis.Y, true, Variant.variant().with(VariantProperties.MODEL, location2))
        .select(
          Axis.Z,
          true,
          Variant.variant().with(VariantProperties.MODEL, location2)
            .with(VariantProperties.X_ROT, VariantProperties.Rotation.R90)
        )
        .select(
          Axis.X, true,
          Variant.variant().with(VariantProperties.MODEL, location2)
            .with(VariantProperties.X_ROT, VariantProperties.Rotation.R90)
            .with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90)
        )

      g.blockStateOutput.accept(
        MultiVariantGenerator.multiVariant(b.get()).with(
          rotatedPillarState
        )
      )
    }
  }
}