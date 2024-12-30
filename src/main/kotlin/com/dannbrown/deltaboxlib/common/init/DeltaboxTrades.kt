package com.dannbrown.deltaboxlib.common.init

import com.dannbrown.deltaboxlib.common.DeltaboxLibCommon
import com.dannbrown.deltaboxlib.platform.registrate.generators.trades.VillagerLevel
import com.dannbrown.deltaboxlib.platform.registrate.generators.trades.VillagerTradeItem
import com.dannbrown.deltaboxlib.platform.registrate.generators.trades.WandererTradeRarity
import net.minecraft.world.entity.npc.VillagerProfession
import net.minecraft.world.item.Items

object DeltaboxTrades {
  fun register(){
    // init
  }

  val TRADE0 = DeltaboxLibCommon.REGISTRATE.villagerTrade(
    VillagerProfession.FARMER,
    VillagerLevel.NOVICE,
    listOf(VillagerTradeItem({DeltaboxItems.BEAN_POD.get()}, 2)),
    listOf(VillagerTradeItem({Items.EMERALD}, 2)),
    5,
    10,
    0.5f
  )

  val TRADE1 = DeltaboxLibCommon.REGISTRATE.villagerTrade(
    VillagerProfession.FARMER,
    VillagerLevel.NOVICE,
    listOf(VillagerTradeItem({Items.WHEAT_SEEDS}, 8)),
    listOf(VillagerTradeItem({DeltaboxItems.BEAN_POD.get()}, 2)),
    5,
    10,
    0.5f
  )

  val TRADE2 = DeltaboxLibCommon.REGISTRATE.wandererTrade(
    WandererTradeRarity.GENERIC,
    listOf(VillagerTradeItem({Items.EMERALD}, 2)),
    listOf(VillagerTradeItem({DeltaboxItems.BEAN_POD.get()}, 2)),
    5,
    10,
    0.5f
  )
}