package com.dannbrown.deltaboxlib.platform.registrate.generators.family

import com.tterrag.registrate.util.entry.BlockEntry
import net.minecraft.world.level.block.Block
import java.util.EnumMap
import java.util.function.Supplier

data class BlockFamily(
  val blocks: EnumMap<Type, BlockEntry<out Block>> = EnumMap(Type::class.java)
) {
  enum class Type {
    MAIN,

    SLAB, STAIRS, WALL, BARS,
    TRAPDOOR, FENCE, FENCE_GATE, DOOR,
    BUTTON, PRESSURE_PLATE, LAMP,
    SCAFFOLDING, LADDER, SIGN, WALL_SIGN, HANGING_SIGN, WALL_HANGING_SIGN, CHAIN,

    POLISHED, POLISHED_SLAB, POLISHED_STAIRS, POLISHED_WALL, POLISHED_BUTTON, POLISHED_PRESSURE_PLATE,
    SANDSTONE, SANDSTONE_SLAB, SANDSTONE_STAIRS, SANDSTONE_WALL, SANDSTONE_BUTTON, SANDSTONE_PRESSURE_PLATE,
    CUT, CUT_SLAB, CUT_STAIRS, CUT_WALL, CUT_BUTTON, CUT_PRESSURE_PLATE,
    CHISELED, CHISELED_SLAB, CHISELED_STAIRS, CHISELED_WALL, CHISELED_BUTTON, CHISELED_PRESSURE_PLATE,
    BRICKS, BRICK_SLAB, BRICK_STAIRS, BRICK_WALL, BRICK_BUTTON, BRICK_PRESSURE_PLATE,
    SMOOTH, SMOOTH_SLAB, SMOOTH_STAIRS, SMOOTH_WALL, SMOOTH_BUTTON, SMOOTH_PRESSURE_PLATE,
    PILLAR,

    LOG, STRIPPED_LOG, WOOD, STRIPPED_WOOD, STALK, STRIPPED_STALK, LEAVES, SAPLING, POTTED_SAPLING,

    FLOWER, POTTED_FLOWER,
  }

  fun serialize(): List<BlockEntry<out Block>> {
    return blocks.values.toList()
  }

  fun setVariant(type: Type, block: Supplier<BlockEntry<out Block>>) {
    blocks[type] = block.get()
  }
}