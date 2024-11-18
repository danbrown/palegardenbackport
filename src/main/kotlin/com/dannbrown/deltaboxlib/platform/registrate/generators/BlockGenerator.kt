package com.dannbrown.deltaboxlib.platform.registrate.generators

import com.dannbrown.deltaboxlib.platform.registrate.DeltaboxRegistrate
import net.minecraft.world.level.block.Block

class BlockGenerator(val registrate: DeltaboxRegistrate) {
  fun  <T: Block> create(name: String): BlockGeneratorBuilder<T> {
    return BlockGeneratorBuilder(name, registrate)
  }

  // TODO: implement block family generator
  //  fun createFamily(name: String): BlockFamilyGen {
  //    return BlockFamilyGen(name, this)
  //  }
}