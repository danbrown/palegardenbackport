package com.dannbrown.deltaboxlib.platform.registrate.generators.block

import net.minecraft.world.level.block.Block

abstract class IBlockBuilderPreset <T: Block> {
  abstract fun create(generator: BlockGenerator): BlockGeneratorBuilder<T>
}