package com.dannbrown.deltaboxlib.platform.registrate.generators.trades

import com.mojang.serialization.codecs.RecordCodecBuilder

class WandererTradeCodec(
    val rarity: WandererTradeRarity,
    val tradeCosts: List<VillagerTradeItem>,
    val tradeSells: List<VillagerTradeItem>,
    val maxUses: Int,
    val xpAmount: Int,
    val priceMultiplier: Float
) {
    companion object {
        val CODEC = RecordCodecBuilder.create { instance ->
            instance.group(
                com.mojang.serialization.Codec.STRING
                    .fieldOf("rarity")
                    .forGetter<WandererTradeCodec> { return@forGetter it.rarity.toString() },
                VillagerTradeItem.CODEC.listOf()
                    .fieldOf("tradeCosts")
                    .forGetter(WandererTradeCodec::tradeCosts),
                VillagerTradeItem.CODEC.listOf()
                    .fieldOf("tradeSells")
                    .forGetter(WandererTradeCodec::tradeSells),
                com.mojang.serialization.Codec.INT
                    .fieldOf("maxUses")
                    .forGetter(WandererTradeCodec::maxUses),
                com.mojang.serialization.Codec.INT
                    .fieldOf("xpAmount")
                    .forGetter(WandererTradeCodec::xpAmount),
                com.mojang.serialization.Codec.FLOAT
                    .fieldOf("priceMultiplier")
                    .forGetter(WandererTradeCodec::priceMultiplier)
            ).apply(instance) { rarity, tradeCosts, tradeSells, maxUses, xpAmount, priceMultiplier ->
                WandererTradeCodec(
                    WandererTradeRarity.fromString(rarity),
                    tradeCosts,
                    tradeSells,
                    maxUses,
                    xpAmount,
                    priceMultiplier
                )
            }
        }
    }
}