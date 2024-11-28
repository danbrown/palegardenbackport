package com.dannbrown.deltaboxlib.platform.registrate.generators.item

import com.dannbrown.deltaboxlib.platform.registrate.DeltaboxRegistrate
import net.minecraft.world.item.Item

class ItemGenerator(val registrate: DeltaboxRegistrate) {
  fun  <T: Item> create(name: String): ItemGeneratorBuilder<T> {
    return ItemGeneratorBuilder(name, registrate)
  }
}