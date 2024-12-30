package com.dannbrown.deltaboxlib.platform.registrate.generators.trades

import com.dannbrown.deltaboxlib.platform.util.DeltaboxUtil
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.core.registries.Registries
import net.minecraft.resources.ResourceKey
import net.minecraft.world.entity.npc.VillagerProfession

class VillagerTradeCodec(
    val profession: VillagerProfession,
    val level: VillagerLevel,
    val tradeCosts: List<VillagerTradeItem>,
    val tradeSells: List<VillagerTradeItem>,
    val maxUses: Int,
    val xpAmount: Int,
    val priceMultiplier: Float
) {
    companion object {
        val CODEC = RecordCodecBuilder.create { instance ->
            instance.group(
                ResourceKey.codec(Registries.VILLAGER_PROFESSION)
                    .fieldOf("profession")
                    .forGetter<VillagerTradeCodec>{return@forGetter ResourceKey.create(Registries.VILLAGER_PROFESSION, DeltaboxUtil.resourceLocation(it.profession.name))},
                com.mojang.serialization.Codec.INT
                    .fieldOf("level")
                    .forGetter<VillagerTradeCodec> { return@forGetter it.level.toInt() },
                VillagerTradeItem.CODEC.listOf()
                    .fieldOf("tradeCosts")
                    .forGetter(VillagerTradeCodec::tradeCosts),
                VillagerTradeItem.CODEC.listOf()
                    .fieldOf("tradeSells")
                    .forGetter(VillagerTradeCodec::tradeSells),
                com.mojang.serialization.Codec.INT
                    .fieldOf("maxUses")
                    .forGetter(VillagerTradeCodec::maxUses),
                com.mojang.serialization.Codec.INT
                    .fieldOf("xpAmount")
                    .forGetter(VillagerTradeCodec::xpAmount),
                com.mojang.serialization.Codec.FLOAT
                    .fieldOf("priceMultiplier")
                    .forGetter(VillagerTradeCodec::priceMultiplier)
            ).apply(instance) { professionKey, level, tradeCosts, tradeSells, maxUses, xpAmount, priceMultiplier ->
                val profession = BuiltInRegistries.VILLAGER_PROFESSION.get(professionKey)
                if (profession === null) throw Exception("Villager profession $professionKey not found in entries")
                VillagerTradeCodec(
                    profession,
                    VillagerLevel.fromInt(level),
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