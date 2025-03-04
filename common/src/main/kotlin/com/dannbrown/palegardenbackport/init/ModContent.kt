package com.dannbrown.palegardenbackport.init

import com.dannbrown.deltaboxlib.init.DeltaboxRegistrate
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.Blocks
import java.util.function.Supplier

object ModContent {
    const val MOD_ID = "palegardenbackport"
    var REGISTRATE = DeltaboxRegistrate(MOD_ID)

    val ADAMANTIUM_BLOCK: Supplier<Block> = REGISTRATE
      .block("adamantium_block")
      .copyFrom { Blocks.STONE }
      .factory { props -> Block(props) }
      .loot({ loot, block -> loot.dropSelf(block.get()) })
      .register()

    fun init() {
        REGISTRATE.buildRegistries()
    }
}