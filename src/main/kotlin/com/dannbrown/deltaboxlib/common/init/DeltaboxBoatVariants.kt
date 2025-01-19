package com.dannbrown.deltaboxlib.common.init

import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.Blocks
import java.util.function.Supplier


abstract class DeltaboxBoatVariants(_map: Map<String, Supplier<Block>> = HashMap()) {
  init {
    register(_map)
  }

  fun register(map: Map<String, Supplier<Block>>) {
    VARIANT_MAP.putAll(map)
  }

  fun register() {
    // just to build the class
  }

  companion object{
    val VARIANT_MAP: MutableMap<String, Supplier<Block>> = HashMap()

    // Static block to initialize default variants
    init {
      VARIANT_MAP["oak"] = Supplier { Blocks.OAK_PLANKS }
    }

    fun addVariant(name: String, block: Supplier<Block>) {
      VARIANT_MAP[name] = block
    }

    fun getBlockForVariant(name: String): Supplier<Block> {
      return VARIANT_MAP[name]!!
    }
  }
}