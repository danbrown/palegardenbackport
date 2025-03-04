package com.dannbrown.palegardenbackport.init

import com.dannbrown.deltaboxlib.init.DeltaboxRegistrate
import net.minecraft.world.food.FoodProperties
import net.minecraft.world.item.BlockItem
import net.minecraft.world.item.Item
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.Blocks
import java.util.function.Supplier

object ModContent {
    const val MOD_ID = "palegardenbackport"
    var REGISTRATE = DeltaboxRegistrate(MOD_ID)

    val ADAMANTIUM_BLOCK: Supplier<Block> = REGISTRATE
      .block("adamantium_block")
      .copyFrom { Blocks.OAK_PLANKS }
      .factory { props -> Block(props) }
      .loot({ loot, block -> loot.dropSelf(block.get()) })
      .item({ a, b ->  BlockItem(b, a.food(FoodProperties.Builder().fast().build())) })
      .build()
      .register()
    val SECOND_BLOCK: Supplier<Block> = REGISTRATE
      .block("second_block")
      .noItem()
      .register()

    val ACAI_CRATE = REGISTRATE
      .block("acai_berries_crate")
      .lang("Acai Berries Crate AHA")
      .blockstate({ ctx, block -> ctx.bottomTopBlock(block.get(), "crate_bottom") })
      .register()

    val ADAMANTIUM_INGOT: Supplier<Item> = REGISTRATE
      .item("adamantium_ingot")
      .register()

    val LANGS = REGISTRATE
      .langs()
      .genericTooltip("flint", "It's a Delta!")
      .register()

    fun init() {
        REGISTRATE.buildRegistries()
    }
}