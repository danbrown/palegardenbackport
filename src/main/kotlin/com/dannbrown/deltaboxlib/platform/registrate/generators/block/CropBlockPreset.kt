package com.dannbrown.deltaboxlib.platform.registrate.generators.block

import com.dannbrown.deltaboxlib.common.content.block.GenericCropBlock
import com.dannbrown.deltaboxlib.platform.registrate.transformers.BlockLootPresets
import com.dannbrown.deltaboxlib.platform.registrate.transformers.BlockstatePresets
import com.dannbrown.deltaboxlib.platform.registrate.transformers.ItemModelPresets
import net.minecraft.world.item.ItemNameBlockItem
import net.minecraft.world.level.ItemLike
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.SoundType
import net.minecraft.world.level.material.PushReaction
import java.util.function.Supplier

class CropBlockPreset(
  private val _name: String,
  private val seedName: String,
  private val cropLang: String,
  private val seedLang: String,
  private val dropItem: Supplier<ItemLike>,
  private val isBush: Boolean = true,
  private val includeSeedOnDrop: Boolean = true,
  private val chance: Float = 1f,
  private val multiplier: Int = 1,
): IBlockBuilderPreset<GenericCropBlock>() {
  override fun create(generator: BlockGenerator): BlockGeneratorBuilder<GenericCropBlock> {
    return generator.create<GenericCropBlock>(seedName)
      .blockFactory { p -> GenericCropBlock(p, isBush, includeSeedOnDrop, dropItem, chance, multiplier) }
      .copyFrom { Blocks.WHEAT }
      .properties { p ->
        p
          .noCollission()
          .randomTicks()
          .instabreak()
          .sound(SoundType.CROP)
          .pushReaction(PushReaction.DESTROY)
      }
      .cutoutRender()
      .blockstate(BlockstatePresets.cropBlock(_name))
      .loot(BlockLootPresets.dropCropLoot(dropItem, null, includeSeedOnDrop, chance, multiplier))
      .transform { t ->
        t
          .lang(cropLang)
          .item { b, p -> ItemNameBlockItem(b, p) }
          .model(ItemModelPresets.simpleItem(seedName))
          .lang(seedLang)
          .build()
      }
  }

}
