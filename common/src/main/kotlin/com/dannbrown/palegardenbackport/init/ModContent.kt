package com.dannbrown.palegardenbackport.init

import com.dannbrown.deltaboxlib.init.DeltaboxRegistrate
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.Blocks

object ModContent {
  const val MOD_ID = "palegardenbackport"
  var REGISTRATE = DeltaboxRegistrate(MOD_ID)

  val ADAMANTIUM_BLOCK = REGISTRATE
    .block<Block>("adamantium_block")
    .copyFrom { Blocks.STONE }
    .factory { c, p -> Block(p) }
    .loot { c, b -> c.dropItself(b.get()) }
    .register()

  fun init() {
    REGISTRATE.buildRegistries()
  }
}