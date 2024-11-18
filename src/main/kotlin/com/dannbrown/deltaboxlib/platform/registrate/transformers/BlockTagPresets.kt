package com.dannbrown.deltaboxlib.registry.transformers

import com.dannbrown.deltaboxlib.platform.util.DeltaboxUtil
import net.minecraft.tags.BlockTags
import net.minecraft.tags.ItemTags
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item
import net.minecraft.world.level.block.Block

object BlockTagPresets {
  fun oreBlockTags(name: String, replace: String = "stone"): Pair<List<TagKey<Block>>, List<TagKey<Item>>> {
    val oreBlockTag = DeltaboxUtil.TAGS.modloaderBlockTag("ores/$name")
    val oreItemTag = DeltaboxUtil.TAGS.modloaderItemTag("ores/$name")
    val oreGroundBlockTag = DeltaboxUtil.TAGS.modloaderBlockTag("ores_in_ground/$replace")
    val oreGroundItemTag = DeltaboxUtil.TAGS.modloaderItemTag("ores_in_ground/$replace")
    val oresBlockTag = DeltaboxUtil.TAGS.modloaderBlockTag("ores")
    val oresItemTag = DeltaboxUtil.TAGS.modloaderItemTag("ores")

    return Pair(
      listOf(*oreBlockTag.toTypedArray(), *oresBlockTag.toTypedArray(), *oreGroundBlockTag.toTypedArray()),
      listOf(*oreItemTag.toTypedArray(), *oresItemTag.toTypedArray(), *oreGroundItemTag.toTypedArray())
    )
  }

  fun storageBlockTags(name: String): Pair<List<TagKey<Block>>, List<TagKey<Item>>> {
    val namedStorageBlockTag = DeltaboxUtil.TAGS.modloaderBlockTag("storage_blocks/$name")
    val namedStorageItemTag = DeltaboxUtil.TAGS.modloaderItemTag("storage_blocks/$name")
    val storageBlockTag = DeltaboxUtil.TAGS.modloaderBlockTag("storage_blocks")
    val storageItemTag = DeltaboxUtil.TAGS.modloaderItemTag("storage_blocks")

    return Pair(
      listOf(*namedStorageBlockTag.toTypedArray(), *storageBlockTag.toTypedArray()),
      listOf(*namedStorageItemTag.toTypedArray(), *storageItemTag.toTypedArray())
    )
  }

  fun woodenStairsTags(): Pair<List<TagKey<Block>>, List<TagKey<Item>>> {
    return Pair(listOf(BlockTags.STAIRS, BlockTags.WOODEN_STAIRS), listOf(ItemTags.STAIRS, ItemTags.WOODEN_STAIRS))
  }

  fun stairsTags(): Pair<List<TagKey<Block>>, List<TagKey<Item>>> {
    return Pair(listOf(BlockTags.STAIRS), listOf(ItemTags.STAIRS))
  }

  fun woodenSlabTags(): Pair<List<TagKey<Block>>, List<TagKey<Item>>> {
    return Pair(listOf(BlockTags.SLABS, BlockTags.WOODEN_SLABS), listOf(ItemTags.SLABS, ItemTags.WOODEN_SLABS))
  }

  fun slabTags(): Pair<List<TagKey<Block>>, List<TagKey<Item>>> {
    return Pair(listOf(BlockTags.SLABS), listOf(ItemTags.SLABS))
  }

  fun wallTags(): Pair<List<TagKey<Block>>, List<TagKey<Item>>> {
    return Pair(listOf(BlockTags.WALLS), listOf(ItemTags.WALLS))
  }


  fun ladderBlockTags(): Pair<List<TagKey<Block>>, List<TagKey<Item>>> {
    return Pair(listOf(BlockTags.CLIMBABLE), listOf())
  }

  fun woodenTrapdoorTags(): Pair<List<TagKey<Block>>, List<TagKey<Item>>> {
    return Pair(listOf(BlockTags.WOODEN_TRAPDOORS, BlockTags.TRAPDOORS), listOf(ItemTags.WOODEN_TRAPDOORS, ItemTags.TRAPDOORS))
  }

  fun trapdoorTags(): Pair<List<TagKey<Block>>, List<TagKey<Item>>> {
    return Pair(listOf(BlockTags.TRAPDOORS), listOf(ItemTags.TRAPDOORS))
  }


  fun fenceTags(isWooden: Boolean): Pair<List<TagKey<Block>>, List<TagKey<Item>>> {
    return if (isWooden) Pair(listOf(BlockTags.FENCES, BlockTags.WOODEN_FENCES), listOf(ItemTags.FENCES, ItemTags.WOODEN_FENCES))
    else Pair(listOf(BlockTags.FENCES), listOf(ItemTags.FENCES))
  }

  fun pressurePlateTags(isWooden: Boolean): Pair<List<TagKey<Block>>, List<TagKey<Item>>> {
    return if (isWooden) Pair(listOf(BlockTags.PRESSURE_PLATES, BlockTags.WOODEN_PRESSURE_PLATES), listOf(ItemTags.WOODEN_PRESSURE_PLATES))
    else Pair(listOf(BlockTags.PRESSURE_PLATES), listOf())
  }

  fun buttonTags(isWooden: Boolean): Pair<List<TagKey<Block>>, List<TagKey<Item>>> {
    return if (isWooden) Pair(listOf(BlockTags.BUTTONS, BlockTags.WOODEN_BUTTONS), listOf(ItemTags.WOODEN_BUTTONS, ItemTags.BUTTONS))
    else Pair(listOf(BlockTags.BUTTONS), listOf(ItemTags.BUTTONS))
  }

  fun doorTags(isWooden: Boolean): Pair<List<TagKey<Block>>, List<TagKey<Item>>> {
    return if (isWooden) Pair(listOf(BlockTags.DOORS, BlockTags.WOODEN_DOORS), listOf(ItemTags.DOORS, ItemTags.WOODEN_DOORS))
    else Pair(listOf(BlockTags.DOORS), listOf(ItemTags.DOORS))
  }

  fun caveReplaceableTags(): Pair<List<TagKey<Block>>, List<TagKey<Item>>> {
    return Pair(listOf(BlockTags.DRIPSTONE_REPLACEABLE, BlockTags.AZALEA_ROOT_REPLACEABLE, BlockTags.MOSS_REPLACEABLE, BlockTags.LUSH_GROUND_REPLACEABLE), listOf())
  }
}
