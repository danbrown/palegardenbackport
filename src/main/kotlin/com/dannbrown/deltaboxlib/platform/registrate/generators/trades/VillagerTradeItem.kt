package com.dannbrown.deltaboxlib.platform.registrate.generators.trades

import net.minecraft.world.item.Item
import java.util.function.Supplier

data class VillagerTradeItem(
    val item: Supplier<Item>,
    val amount: Int
)