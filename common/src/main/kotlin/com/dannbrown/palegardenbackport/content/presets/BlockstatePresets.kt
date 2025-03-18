package com.dannbrown.palegardenbackport.content.presets

import com.dannbrown.deltaboxlib.registrate.datagen.model.RegistrateModelTemplates
import com.dannbrown.deltaboxlib.registrate.datagen.model.RegistrateTextureSlots
import com.dannbrown.deltaboxlib.registrate.types.BlockstateFactory
import com.dannbrown.deltaboxlib.registrate.util.DeltaboxUtil
import com.dannbrown.palegardenbackport.content.blocks.PaleMossBlock
import com.dannbrown.palegardenbackport.content.blocks.PaleMossCarpetBlock
import com.dannbrown.palegardenbackport.content.blocks.creakingHeart.CreakingHeartBlock
import net.minecraft.core.Direction
import net.minecraft.core.Direction.Axis
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.data.models.blockstates.Condition
import net.minecraft.data.models.blockstates.MultiPartGenerator
import net.minecraft.data.models.blockstates.MultiVariantGenerator
import net.minecraft.data.models.blockstates.PropertyDispatch
import net.minecraft.data.models.blockstates.Variant
import net.minecraft.data.models.blockstates.VariantProperties
import net.minecraft.data.models.blockstates.VariantProperties.Rotation
import net.minecraft.data.models.model.ModelTemplates
import net.minecraft.data.models.model.TextureMapping
import net.minecraft.data.models.model.TextureSlot
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.level.block.state.properties.BlockStateProperties
import net.minecraft.world.level.block.state.properties.WallSide

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

  val FACE = TextureSlot.create("face")
  val RESIN_CLUMP =
    RegistrateModelTemplates.create(
      DeltaboxUtil.resourceLocation(
        "minecraft", "block/resin_clump"
      ),
      FACE
    )

  fun resinClump(): BlockstateFactory {
    return { g, b ->
      val model = RESIN_CLUMP.create(
        BuiltInRegistries.BLOCK.getKey(b.get()).withPrefix("block/").withSuffix(""),
        TextureMapping()
          .put(FACE, g.optionalTexture(b.get(), "", "", "block/")),
        g.modelOutput
      )

      g.blockStateOutput.accept(
        MultiPartGenerator.multiPart(b.get())
          .with(
            Condition.condition().term(BlockStateProperties.NORTH, true),
            Variant.variant().with(VariantProperties.MODEL, model)
          )
          .with(
            Condition.condition()
              .term(BlockStateProperties.DOWN, false)
              .term(BlockStateProperties.EAST, false)
              .term(BlockStateProperties.NORTH, false)
              .term(BlockStateProperties.SOUTH, false)
              .term(BlockStateProperties.UP, false)
              .term(BlockStateProperties.WEST, false),
            Variant.variant().with(VariantProperties.MODEL, model)
          )
          .with(
            Condition.condition().term(BlockStateProperties.EAST, true),
            Variant.variant().with(VariantProperties.MODEL, model)
              .with(VariantProperties.Y_ROT, Rotation.R90)
              .with(VariantProperties.UV_LOCK, true)
          )
          .with(
            Condition.condition().term(BlockStateProperties.SOUTH, true),
            Variant.variant().with(VariantProperties.MODEL, model)
              .with(VariantProperties.Y_ROT, Rotation.R180)
              .with(VariantProperties.UV_LOCK, true)
          )
          .with(
            Condition.condition().term(BlockStateProperties.WEST, true),
            Variant.variant().with(VariantProperties.MODEL, model)
              .with(VariantProperties.Y_ROT, Rotation.R270)
              .with(VariantProperties.UV_LOCK, true)
          )
          .with(
            Condition.condition().term(BlockStateProperties.UP, true),
            Variant.variant().with(VariantProperties.MODEL, model)
              .with(VariantProperties.X_ROT, Rotation.R270)
              .with(VariantProperties.UV_LOCK, true)
          )
          .with(
            Condition.condition().term(BlockStateProperties.DOWN, true),
            Variant.variant().with(VariantProperties.MODEL, model)
              .with(VariantProperties.X_ROT, Rotation.R90)
              .with(VariantProperties.UV_LOCK, true)
          )
      )
    }
  }

  val CARPET_SIDE_SMALL =
    RegistrateModelTemplates.create(
      DeltaboxUtil.resourceLocation(
        "minecraft", "block/pale_moss_carpet_side_small"
      ),
      TextureSlot.SIDE
    )
  val CARPET_SIDE_TALL =
    RegistrateModelTemplates.create(
      DeltaboxUtil.resourceLocation(
        "minecraft", "block/pale_moss_carpet_side_tall"
      ),
      TextureSlot.SIDE
    )
//
//  fun paleMossCarpetBlock(): BlockstateFactory {
//    return { g, b ->
//      val baseModel = ModelTemplates.CARPET.create(
//        BuiltInRegistries.BLOCK.getKey(b.get()).withPrefix("block/"),
//        TextureMapping().put(TextureSlot.WOOL, g.optionalTexture(b.get(), "", "", "block/")),
//        g.modelOutput
//      )
//
//      val tallSideModel = CARPET_SIDE_TALL.create(
//        BuiltInRegistries.BLOCK.getKey(b.get()).withPrefix("block/").withSuffix("_side_tall"),
//        TextureMapping().put(TextureSlot.SIDE, g.optionalTexture(b.get(), "", "_side_tall", "block/")),
//        g.modelOutput
//      )
//
//      val smallSideModel = CARPET_SIDE_SMALL.create(
//        BuiltInRegistries.BLOCK.getKey(b.get()).withPrefix("block/").withSuffix("_side_small"),
//        TextureMapping().put(TextureSlot.SIDE, g.optionalTexture(b.get(), "", "_side_small", "block/")),
//        g.modelOutput
//      )
//
//      val directions = arrayOf(
//        PaleMossCarpetBlock.NORTH,
//        PaleMossCarpetBlock.EAST,
//        PaleMossCarpetBlock.SOUTH,
//        PaleMossCarpetBlock.WEST
//      )
//
//      g.blockStateOutput.accept(
//        MultiVariantGenerator.multiVariant(b.get()).with(
//          PropertyDispatch.properties(
//            PaleMossCarpetBlock.BASE,
//            PaleMossCarpetBlock.NORTH,
//            PaleMossCarpetBlock.EAST,
//            PaleMossCarpetBlock.SOUTH,
//            PaleMossCarpetBlock.WEST
//          ).apply {
//            for (dir in directions) {
//              select(
//                true, WallSide.NONE, WallSide.NONE, WallSide.NONE, WallSide.NONE,
//                Variant.variant().with(VariantProperties.MODEL, baseModel)
//              )
//            }
//
//            fun addSideConditions(
//              direction: String,
//              wallState: WallSide,
//              model: ResourceLocation,
//              rotation: Rotation
//            ) {
//              val directionState = when (direction) {
//                "north" -> PaleMossCarpetBlock.NORTH
//                "east" -> PaleMossCarpetBlock.EAST
//                "south" -> PaleMossCarpetBlock.SOUTH
//                "west" -> PaleMossCarpetBlock.WEST
//                else -> throw Error("Invalid direction")
//              }
//              select(
//                false,
//                if (PaleMossCarpetBlock.NORTH == directionState) wallState else WallSide.NONE,
//                if (PaleMossCarpetBlock.EAST == directionState) wallState else WallSide.NONE,
//                if (PaleMossCarpetBlock.SOUTH == directionState) wallState else WallSide.NONE,
//                if (PaleMossCarpetBlock.WEST == directionState) wallState else WallSide.NONE,
//                Variant.variant().with(VariantProperties.MODEL, model)
//                  .with(VariantProperties.Y_ROT, rotation).with(VariantProperties.UV_LOCK, true)
//              )
//            }
//
//            addSideConditions("north", WallSide.TALL, tallSideModel, Rotation.R0)
//            addSideConditions("north", WallSide.LOW, smallSideModel, Rotation.R0)
//            addSideConditions("east", WallSide.TALL, tallSideModel, Rotation.R90)
//            addSideConditions("east", WallSide.LOW, smallSideModel, Rotation.R90)
//            addSideConditions("south", WallSide.TALL, tallSideModel, Rotation.R180)
//            addSideConditions("south", WallSide.LOW, smallSideModel, Rotation.R180)
//            addSideConditions("west", WallSide.TALL, tallSideModel, Rotation.R270)
//            addSideConditions("west", WallSide.LOW, smallSideModel, Rotation.R270)
//          }
//        )
//      )
//    }
//  }

  fun paleMossCarpetBlock(): BlockstateFactory {
    return { g, b ->
      val basePath = BuiltInRegistries.BLOCK.getKey(b.get()).withPrefix("block/")

      val baseModel = ModelTemplates.CARPET.create(
        basePath,
        TextureMapping().put(TextureSlot.WOOL, g.optionalTexture(b.get(), "", "", "block/")),
        g.modelOutput
      )

      val tallSideModel = CARPET_SIDE_TALL.create(
        basePath.withSuffix("_side_tall"),
        TextureMapping().put(TextureSlot.SIDE, g.optionalTexture(b.get(), "", "_side_tall", "block/")),
        g.modelOutput
      )

      val smallSideModel = CARPET_SIDE_SMALL.create(
        basePath.withSuffix("_side_small"),
        TextureMapping().put(TextureSlot.SIDE, g.optionalTexture(b.get(), "", "_side_small", "block/")),
        g.modelOutput
      )

      g.blockStateOutput.accept(
        MultiPartGenerator.multiPart(b.get())
          .with(
            Condition.condition().term(PaleMossCarpetBlock.BASE, true),
            Variant.variant().with(VariantProperties.MODEL, baseModel)
          )
          .with(
            Condition.condition()
              .term(PaleMossCarpetBlock.BASE, false)
              .term(PaleMossCarpetBlock.NORTH, WallSide.NONE)
              .term(PaleMossCarpetBlock.EAST, WallSide.NONE)
              .term(PaleMossCarpetBlock.SOUTH, WallSide.NONE)
              .term(PaleMossCarpetBlock.WEST, WallSide.NONE),
            Variant.variant().with(VariantProperties.MODEL, baseModel)
          )
          .with(
            Condition.condition().term(PaleMossCarpetBlock.NORTH, WallSide.TALL),
            Variant.variant().with(VariantProperties.MODEL, tallSideModel)
          )
          .with(
            Condition.condition().term(PaleMossCarpetBlock.NORTH, WallSide.LOW),
            Variant.variant().with(VariantProperties.MODEL, smallSideModel)
          )
          .with(
            Condition.condition().term(PaleMossCarpetBlock.EAST, WallSide.TALL),
            Variant.variant().with(VariantProperties.MODEL, tallSideModel)
              .with(VariantProperties.Y_ROT, Rotation.R90)
              .with(VariantProperties.UV_LOCK, true)
          )
          .with(
            Condition.condition().term(PaleMossCarpetBlock.EAST, WallSide.LOW),
            Variant.variant().with(VariantProperties.MODEL, smallSideModel)
              .with(VariantProperties.Y_ROT, Rotation.R90)
              .with(VariantProperties.UV_LOCK, true)
          )
          .with(
            Condition.condition().term(PaleMossCarpetBlock.SOUTH, WallSide.TALL),
            Variant.variant().with(VariantProperties.MODEL, tallSideModel)
              .with(VariantProperties.Y_ROT, Rotation.R180)
              .with(VariantProperties.UV_LOCK, true)
          )
          .with(
            Condition.condition().term(PaleMossCarpetBlock.SOUTH, WallSide.LOW),
            Variant.variant().with(VariantProperties.MODEL, smallSideModel)
              .with(VariantProperties.Y_ROT, Rotation.R180)
              .with(VariantProperties.UV_LOCK, true)
          )
          .with(
            Condition.condition().term(PaleMossCarpetBlock.WEST, WallSide.TALL),
            Variant.variant().with(VariantProperties.MODEL, tallSideModel)
              .with(VariantProperties.Y_ROT, Rotation.R270)
              .with(VariantProperties.UV_LOCK, true)
          )
          .with(
            Condition.condition().term(PaleMossCarpetBlock.WEST, WallSide.LOW),
            Variant.variant().with(VariantProperties.MODEL, smallSideModel)
              .with(VariantProperties.Y_ROT, Rotation.R270)
              .with(VariantProperties.UV_LOCK, true)
          )
          .with(
            Condition.condition()
              .term(PaleMossCarpetBlock.BASE, false)
              .term(PaleMossCarpetBlock.NORTH, WallSide.NONE)
              .term(PaleMossCarpetBlock.EAST, WallSide.NONE)
              .term(PaleMossCarpetBlock.SOUTH, WallSide.NONE)
              .term(PaleMossCarpetBlock.WEST, WallSide.NONE),
            Variant.variant().with(VariantProperties.MODEL, tallSideModel)
              .with(VariantProperties.UV_LOCK, true)
          )
      )
    }
  }
}