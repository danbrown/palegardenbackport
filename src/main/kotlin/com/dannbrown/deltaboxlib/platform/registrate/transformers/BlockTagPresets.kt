package com.dannbrown.deltaboxlib.registry.transformers

import com.dannbrown.deltaboxlib.platform.util.Util
import net.minecraft.tags.BlockTags
import net.minecraft.tags.ItemTags
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item
import net.minecraft.world.level.block.Block

object BlockTagPresets {
  fun oreBlockTags(name: String, replace: String = "stone"): Pair<Array<TagKey<Block>>, Array<TagKey<Item>>> {
    val forgeOreBlockTag = Util.TAGS.forgeBlockTag("ores/$name")
    val forgeOreItemTag = Util.TAGS.forgeItemTag("ores/$name")
    val forgeOreGroundBlockTag = Util.TAGS.forgeBlockTag("ores_in_ground/$replace")
    val forgeOreGroundItemTag = Util.TAGS.forgeItemTag("ores_in_ground/$replace")

    // net.minecraftforge.common.Tags, Tags.Blocks.ORES, Tags.Items.ORES

    return Pair(arrayOf(forgeOreBlockTag, forgeOreGroundBlockTag), arrayOf(forgeOreItemTag, forgeOreGroundItemTag))
  }

  fun storageBlockTags(name: String): Pair<Array<TagKey<Block>>, Array<TagKey<Item>>> {
    val forgeStorageBlockTag = Util.TAGS.forgeBlockTag("storage_blocks/$name")
    val forgeStorageItemTag = Util.TAGS.forgeItemTag("storage_blocks/$name")

    // net.minecraftforge.common.Tags, Tags.Blocks.STORAGE_BLOCKS, Tags.Items.STORAGE_BLOCKS

    return Pair(arrayOf(forgeStorageBlockTag), arrayOf(forgeStorageItemTag))
  }

  fun woodenStairsTags(): Pair<Array<TagKey<Block>>, Array<TagKey<Item>>> {
    return Pair(arrayOf(BlockTags.STAIRS, BlockTags.WOODEN_STAIRS), arrayOf(ItemTags.STAIRS, ItemTags.WOODEN_STAIRS))
  }

  fun stairsTags(): Pair<Array<TagKey<Block>>, Array<TagKey<Item>>> {
    return Pair(arrayOf(BlockTags.STAIRS), arrayOf(ItemTags.STAIRS))
  }

  fun woodenSlabTags(): Pair<Array<TagKey<Block>>, Array<TagKey<Item>>> {
    return Pair(arrayOf(BlockTags.SLABS, BlockTags.WOODEN_SLABS), arrayOf(ItemTags.SLABS, ItemTags.WOODEN_SLABS))
  }

  fun slabTags(): Pair<Array<TagKey<Block>>, Array<TagKey<Item>>> {
    return Pair(arrayOf(BlockTags.SLABS), arrayOf(ItemTags.SLABS))
  }

  fun wallTags(): Pair<Array<TagKey<Block>>, Array<TagKey<Item>>> {
    return Pair(arrayOf(BlockTags.WALLS), arrayOf(ItemTags.WALLS))
  }


  fun ladderBlockTags(): Pair<Array<TagKey<Block>>, Array<TagKey<Item>>> {
    return Pair(arrayOf(BlockTags.CLIMBABLE), arrayOf())
  }

  fun woodenTrapdoorTags(): Pair<Array<TagKey<Block>>, Array<TagKey<Item>>> {
    return Pair(arrayOf(BlockTags.WOODEN_TRAPDOORS, BlockTags.TRAPDOORS), arrayOf(ItemTags.WOODEN_TRAPDOORS, ItemTags.TRAPDOORS))
  }

  fun trapdoorTags(): Pair<Array<TagKey<Block>>, Array<TagKey<Item>>> {
    return Pair(arrayOf(BlockTags.TRAPDOORS), arrayOf(ItemTags.TRAPDOORS))
  }


  fun fenceTags(isWooden: Boolean): Pair<Array<TagKey<Block>>, Array<TagKey<Item>>> {
    return if (isWooden) Pair(arrayOf(BlockTags.FENCES, BlockTags.WOODEN_FENCES), arrayOf(ItemTags.FENCES, ItemTags.WOODEN_FENCES))
    else Pair(arrayOf(BlockTags.FENCES), arrayOf(ItemTags.FENCES))
  }

  fun pressurePlateTags(isWooden: Boolean): Pair<Array<TagKey<Block>>, Array<TagKey<Item>>> {
    return if (isWooden) Pair(arrayOf(BlockTags.PRESSURE_PLATES, BlockTags.WOODEN_PRESSURE_PLATES), arrayOf(ItemTags.WOODEN_PRESSURE_PLATES))
    else Pair(arrayOf(BlockTags.PRESSURE_PLATES), arrayOf())
  }

  fun buttonTags(isWooden: Boolean): Pair<Array<TagKey<Block>>, Array<TagKey<Item>>> {
    return if (isWooden) Pair(arrayOf(BlockTags.BUTTONS, BlockTags.WOODEN_BUTTONS), arrayOf(ItemTags.WOODEN_BUTTONS, ItemTags.BUTTONS))
    else Pair(arrayOf(BlockTags.BUTTONS), arrayOf(ItemTags.BUTTONS))
  }

  fun doorTags(isWooden: Boolean): Pair<Array<TagKey<Block>>, Array<TagKey<Item>>> {
    return if (isWooden) Pair(arrayOf(BlockTags.DOORS, BlockTags.WOODEN_DOORS), arrayOf(ItemTags.DOORS, ItemTags.WOODEN_DOORS))
    else Pair(arrayOf(BlockTags.DOORS), arrayOf(ItemTags.DOORS))
  }

  fun caveReplaceableTags(): Pair<Array<TagKey<Block>>, Array<TagKey<Item>>> {
    return Pair(arrayOf(BlockTags.DRIPSTONE_REPLACEABLE, BlockTags.AZALEA_ROOT_REPLACEABLE, BlockTags.MOSS_REPLACEABLE, BlockTags.LUSH_GROUND_REPLACEABLE), arrayOf())
  }
}
