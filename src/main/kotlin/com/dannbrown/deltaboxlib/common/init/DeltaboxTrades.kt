package com.dannbrown.deltaboxlib.common.init

import com.dannbrown.deltaboxlib.common.DeltaboxLibCommon
import com.dannbrown.deltaboxlib.platform.registrate.generators.trades.VillagerLevel
import com.dannbrown.deltaboxlib.platform.registrate.generators.trades.VillagerTradeItem
import com.dannbrown.deltaboxlib.platform.registrate.generators.trades.WandererTradeRarity
import net.minecraft.world.entity.npc.VillagerProfession
import net.minecraft.world.item.Items

object DeltaboxTrades {
  init {
    DeltaboxLibCommon.REGISTRATE.villagerTrade(
      VillagerProfession.FARMER,
      VillagerLevel.NOVICE,
      listOf(VillagerTradeItem({DeltaboxItems.BEAN_POD.get()}, 2)),
      listOf(VillagerTradeItem({Items.OBSIDIAN}, 5)),
      5,
      10,
      0.5f
    )
    DeltaboxLibCommon.REGISTRATE.villagerTrade(
      VillagerProfession.FARMER,
      VillagerLevel.NOVICE,
      listOf(VillagerTradeItem({Items.WHEAT_SEEDS}, 8)),
      listOf(VillagerTradeItem({DeltaboxItems.BEAN_POD.get()}, 2)),
      5,
      10,
      0.5f
    )
    DeltaboxLibCommon.REGISTRATE.wandererTrade(
      WandererTradeRarity.GENERIC,
      listOf(VillagerTradeItem({Items.EMERALD}, 2)),
      listOf(VillagerTradeItem({DeltaboxItems.BEAN_POD.get()}, 2)),
      5,
      10,
      0.5f
    )
  }
  fun register(){
    // init
  }
}