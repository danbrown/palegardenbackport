package com.dannbrown.deltaboxlib.platform.registrate.generators.block

import com.dannbrown.deltaboxlib.common.content.block.GenericGrassBlock
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
}