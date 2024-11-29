package com.dannbrown.deltaboxlib.platform.registrate.generators.block

import com.dannbrown.deltaboxlib.common.content.block.GenericDoublePlantBlock
import com.dannbrown.deltaboxlib.common.content.block.GenericGrassBlock
import com.dannbrown.deltaboxlib.common.content.block.GenericTallGrassBlock
import com.dannbrown.deltaboxlib.platform.registrate.transformers.BlockLootPresets
import com.dannbrown.deltaboxlib.platform.registrate.transformers.BlockstatePresets
import com.dannbrown.deltaboxlib.platform.registrate.transformers.ItemModelPresets
import net.minecraft.core.BlockPos
import net.minecraft.world.level.BlockGetter
import net.minecraft.world.level.ItemLike
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.SoundType
import net.minecraft.world.level.block.state.BlockState
import java.util.function.Supplier

class GrassBlockPreset(
  val _name: String,
  val dropItem: Supplier<ItemLike>? = null,
  val isSticky: Boolean = false,
  val isHarmful: Boolean = false,
  val isBonemealable: Boolean = false,
  val chance: Float = 0.6f,
  val multiplier: Int = 2,
  val placeOn: ((blockState: BlockState, blockGetter: BlockGetter, blockPos: BlockPos) -> Boolean)? = null
): IBlockBuilderPreset<GenericGrassBlock>() {
  override fun create(generator: BlockGenerator): BlockGeneratorBuilder<GenericGrassBlock> {
    return generator
      .create<GenericGrassBlock>(_name)
      .blockFactory { p -> GenericGrassBlock(p, placeOn, isSticky, isHarmful, isBonemealable) }
      .copyFrom { Blocks.FERN }
      .properties { p ->
        p.sound(SoundType.GRASS)
          .strength(0.0f)
          .noCollission()
          .noOcclusion()
      }
      .blockstate(BlockstatePresets.simpleCrossBlock(_name))
      .loot(BlockLootPresets.dropSelfSilkShearsOtherLoot(dropItem!!, chance, multiplier))
      .transform { t ->
        t
          .item()
          .model(ItemModelPresets.simpleLayerItem(_name))
          .build()
      }
      .cutoutRender()
  }

  fun createFlower(generator: BlockGenerator): BlockGeneratorBuilder<GenericGrassBlock> {
    return generator
      .create<GenericGrassBlock>(_name)
      .blockFactory { p -> GenericGrassBlock(p, placeOn, isSticky, isHarmful, isBonemealable) }
      .copyFrom { Blocks.POPPY }
      .properties { p ->
        p.sound(SoundType.GRASS)
          .strength(0.0f)
          .noCollission()
          .noOcclusion()
      }
      .blockstate(BlockstatePresets.simpleCrossBlock(_name))
      .loot(BlockLootPresets.dropItselfLoot())
      .transform { t ->
        t
          .item()
          .model(ItemModelPresets.simpleLayerItem(_name))
          .build()
      }
      .cutoutRender()
  }

  fun createSmallTallGrassBlock(
    generator: BlockGenerator,
    doubleBlock: Supplier<GenericDoublePlantBlock>,
    needBonemeal: Boolean = false
  ): BlockGeneratorBuilder<GenericTallGrassBlock> {
    return generator
      .create<GenericTallGrassBlock>(_name)
      .blockFactory { p -> GenericTallGrassBlock(doubleBlock, p, needBonemeal, placeOn) }
      .copyFrom { Blocks.TALL_GRASS }
      .properties { p -> p.strength(0.0f).randomTicks().noCollission().noOcclusion() }
      .blockstate(BlockstatePresets.simpleCrossBlock(_name))
      .loot(BlockLootPresets.dropSelfSilkShearsOtherLoot(dropItem!!, chance, multiplier))
      .transform { t ->
        t
          .item()
          .model(ItemModelPresets.simpleLayerItem(_name))
          .build()
      }
      .cutoutRender()
  }

  fun createDoubleTallGrassBlock(
    generator: BlockGenerator,
    seedItem: Supplier<ItemLike>? = null,
    prefix: String = "tall_"
  ): BlockGeneratorBuilder<GenericDoublePlantBlock> {
    return generator
      .create<GenericDoublePlantBlock>("${prefix}${_name}")
      .blockFactory { p -> GenericDoublePlantBlock(p, placeOn) }
      .copyFrom { Blocks.TALL_GRASS }
      .properties { p -> p.strength(0.0f).randomTicks().noCollission().noOcclusion() }
      .loot(BlockLootPresets.dropDoubleCropLoot(dropItem!!, seedItem ?: dropItem, chance, multiplier.toFloat()))
      .blockstate(BlockstatePresets.simpleDoubleCrossBlock(_name))
      .transform { t ->
        t
          .item()
          .model(ItemModelPresets.simpleLayerItem(_name + "_top"))
          .build()
      }
      .cutoutRender()
  }



//  fun createDoubleTallGrassBlock(
//    _name: String,
//    color: MapColor,
//    dropItem: Supplier<Item>,
//    seedItem: Supplier<Item>? = null,
//    chance: Float = 0.25f,
//    multiplier: Int = 2,
//    placeOn:
//    ((blockState: BlockState, blockGetter: BlockGetter, blockPos: BlockPos) -> Boolean)? =
//      null,
//    prefix: String = "tall_"
//  ): BlockEntry<GenericDoublePlantBlock> {
//    return BLOCKS
//      .create<GenericDoublePlantBlock>("${prefix}${_name}")
//      .blockFactory { p -> GenericDoublePlantBlock(p, placeOn) }
//      .copyFrom { Blocks.TALL_GRASS }
//      .color(color)
//      .properties { p -> p.strength(0.0f).randomTicks().noCollission().noOcclusion() }
//      .loot(
//        BlockLootPresets.dropDoubleCropLoot(
//          dropItem,
//          seedItem ?: dropItem,
//          chance,
//          multiplier.toFloat()
//        )
//      )
//      .transform { t ->
//        t
//          .blockstate { c, p ->
//            p.getVariantBuilder(c.get())
//              .partialState()
//              .with(DoublePlantBlock.HALF, DoubleBlockHalf.UPPER)
//              .setModels(
//                *ConfiguredModel.builder()
//                  .modelFile(
//                    p.models()
//                      .withExistingParent(c.name + "_top", p.mcLoc("block/cross"))
//                      .texture("cross", p.modLoc("block/${_name}_top"))
//                      .texture("particle", p.modLoc("block/${_name}_top"))
//                      .renderType("cutout_mipped")
//                  )
//                  .build()
//              )
//              .partialState()
//              .with(DoublePlantBlock.HALF, DoubleBlockHalf.LOWER)
//              .setModels(
//                *ConfiguredModel.builder()
//                  .modelFile(
//                    p.models()
//                      .withExistingParent(c.name + "_bottom", p.mcLoc("block/cross"))
//                      .texture("cross", p.modLoc("block/${_name}_bottom"))
//                      .texture("particle", p.modLoc("block/${_name}_bottom"))
//                      .renderType("cutout_mipped")
//                  )
//                  .build()
//              )
//          }
//          .item()
//          .model { c, p ->
//            p.withExistingParent(c.name, p.mcLoc("item/generated"))
//              .texture("layer0", p.modLoc("block/${_name}_top"))
//          }
//          .build()
//      }
//      .register()
//  }
}