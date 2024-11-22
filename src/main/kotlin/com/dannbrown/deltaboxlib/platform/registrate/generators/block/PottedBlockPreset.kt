package com.dannbrown.deltaboxlib.platform.registrate.generators.block

import com.dannbrown.deltaboxlib.platform.registrate.generators.BlockGenerator
import com.dannbrown.deltaboxlib.platform.registrate.generators.BlockGeneratorBuilder
import com.dannbrown.deltaboxlib.platform.registrate.transformers.BlockLootPresets
import com.dannbrown.deltaboxlib.platform.registrate.transformers.BlockstatePresets
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.FlowerPotBlock
import java.util.function.Supplier

class PottedBlockPreset (
  private val _name: String,
  private val block: Supplier<Block>,
  private val suffix: String = "_sapling"
): IBlockBuilderPreset<FlowerPotBlock>() {
  override fun create(generator: BlockGenerator): BlockGeneratorBuilder<FlowerPotBlock> {
    return generator.create<FlowerPotBlock>("potted_$_name" + suffix)
      /*? if forge || neoforge {*/
      .blockFactory { p ->
        FlowerPotBlock({ Blocks.FLOWER_POT as FlowerPotBlock }, { block.get() }, p)
      }
      /*?} else {*/
      /*.blockFactory { p ->
        FlowerPotBlock(block.get(), p)
      }
      *//*?}*/
      .copyFrom { Blocks.POTTED_POPPY }
      .noItem()
      .properties { p -> p.noOcclusion() }
      .loot(BlockLootPresets.pottedPlantLoot { block.get() })
      .blockstate(BlockstatePresets.pottedPlantBlock(_name + suffix))
      .cutoutRender()
  }
}