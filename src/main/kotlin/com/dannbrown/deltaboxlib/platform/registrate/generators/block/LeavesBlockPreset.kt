package com.dannbrown.deltaboxlib.platform.registrate.generators.block

import com.dannbrown.deltaboxlib.common.content.block.BuddingLeavesBlock
import com.dannbrown.deltaboxlib.common.content.block.CropLeavesBlock
import com.dannbrown.deltaboxlib.common.content.block.FlammableLeavesBlock
import com.dannbrown.deltaboxlib.common.content.block.GenericSaplingBlock
import com.dannbrown.deltaboxlib.common.content.block.PalmLeavesBlock
import com.dannbrown.deltaboxlib.platform.registrate.transformers.BlockLootPresets
import com.dannbrown.deltaboxlib.platform.registrate.transformers.BlockstatePresets
import com.dannbrown.deltaboxlib.platform.util.DeltaboxUtil
import net.minecraft.tags.BlockTags
import net.minecraft.tags.ItemTags
import net.minecraft.world.level.ItemLike
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.FaceAttachedHorizontalDirectionalBlock
import java.util.function.Supplier

class LeavesBlockPreset(
  private val _name: String,
  private val sapling: Supplier<GenericSaplingBlock>,
  private val suffix: String = "_leaves"
) : IBlockBuilderPreset<FlammableLeavesBlock>() {
  override fun create(generator: BlockGenerator): BlockGeneratorBuilder<FlammableLeavesBlock> {
    return generator
      .create<FlammableLeavesBlock>(_name + suffix)
      .blockFactory { p, c -> FlammableLeavesBlock(p, c.flammability!!.first, c.flammability.second) }
      .flammable(30, 60)
      .cutoutRender()
      .copyFrom { Blocks.OAK_LEAVES }
      .properties { p ->
        p.randomTicks()
          .noOcclusion()
          .isSuffocating { s, b, p -> false }
          .isViewBlocking { s, b, p -> false }
          .isRedstoneConductor { s, b, p -> false }
          .ignitedByLava()
      }
      .blockTags(
        listOf(BlockTags.LEAVES, BlockTags.MINEABLE_WITH_HOE, *DeltaboxUtil.TAGS.modloaderBlockTag("leaves").toTypedArray())
      )
      .itemTags(listOf(ItemTags.LEAVES, *DeltaboxUtil.TAGS.modloaderItemTag("leaves").toTypedArray()))
      .blockstate(BlockstatePresets.leavesBlock(_name + suffix))
      .loot(BlockLootPresets.leavesLoot { sapling.get() })
  }

  fun createPalmLeaves(generator: BlockGenerator): BlockGeneratorBuilder<FlammableLeavesBlock> {
    return generator
      .create<FlammableLeavesBlock>(_name + suffix)
      .blockFactory { p, c -> PalmLeavesBlock(p, c.flammability!!.first, c.flammability.second) }
      .flammable(30, 60)
      .cutoutRender()
      .copyFrom { Blocks.OAK_LEAVES }
      .properties { p ->
        p.randomTicks()
          .noOcclusion()
          .isSuffocating { s, b, p -> false }
          .isViewBlocking { s, b, p -> false }
          .isRedstoneConductor { s, b, p -> false }
          .ignitedByLava()
      }
      .blockTags(
        listOf(BlockTags.LEAVES, BlockTags.MINEABLE_WITH_HOE, *DeltaboxUtil.TAGS.modloaderBlockTag("leaves").toTypedArray())
      )
      .itemTags(listOf(ItemTags.LEAVES, *DeltaboxUtil.TAGS.modloaderItemTag("leaves").toTypedArray()))
      .blockstate(BlockstatePresets.leavesBlock(_name + suffix))
      .loot(BlockLootPresets.leavesLoot { sapling.get() })
  }

  fun createBuddingLeaves(generator: BlockGenerator, fruitBlock: Supplier<Block>): BlockGeneratorBuilder<BuddingLeavesBlock> {
    return generator
      .create<BuddingLeavesBlock>(_name + suffix)
      .blockFactory { p, c -> BuddingLeavesBlock(p, fruitBlock, c.flammability!!.first, c.flammability.second) }
      .flammable(30, 60)
      .cutoutRender()
      .copyFrom { Blocks.OAK_LEAVES }
      .properties { p ->
        p.randomTicks()
          .noOcclusion()
          .isSuffocating { s, b, p -> false }
          .isViewBlocking { s, b, p -> false }
          .isRedstoneConductor { s, b, p -> false }
          .ignitedByLava()
      }
      .blockTags(
        listOf(BlockTags.LEAVES, BlockTags.MINEABLE_WITH_HOE, *DeltaboxUtil.TAGS.modloaderBlockTag("leaves").toTypedArray())
      )
      .blockstate(BlockstatePresets.leavesBlock(_name + suffix))
      .loot(BlockLootPresets.leavesLoot { sapling.get() })
      .noItem()
  }

  fun createCropLeaves(generator: BlockGenerator, itemToDrop: Supplier<ItemLike>): BlockGeneratorBuilder<CropLeavesBlock> {
    return generator
      .create<CropLeavesBlock>(_name + suffix)
      .blockFactory { p, c -> CropLeavesBlock(p, itemToDrop) }
      .flammable(30, 60)
      .cutoutRender()
      .copyFrom { Blocks.OAK_LEAVES }
      .properties { p ->
        p.randomTicks()
          .noOcclusion()
          .isSuffocating { s, b, p -> false }
          .isViewBlocking { s, b, p -> false }
          .isRedstoneConductor { s, b, p -> false }
          .ignitedByLava()
      }
      .blockstate(BlockstatePresets.cropLeavesBlock(_name + suffix))
      .blockTags(
        listOf(BlockTags.LEAVES, BlockTags.MINEABLE_WITH_HOE, *DeltaboxUtil.TAGS.modloaderBlockTag("leaves").toTypedArray())
      )
      .loot(BlockLootPresets.dropLeafCropLoot({ itemToDrop.get() }, { sapling.get().asItem() }))
      .noItem()
  }
}
