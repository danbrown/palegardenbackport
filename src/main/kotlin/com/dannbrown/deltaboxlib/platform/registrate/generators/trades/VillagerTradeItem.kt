package com.dannbrown.deltaboxlib.platform.registrate.generators.trades

import com.dannbrown.deltaboxlib.platform.util.DeltaboxUtil
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.core.registries.Registries
import net.minecraft.resources.ResourceKey
import net.minecraft.world.item.Item
import java.util.function.Supplier

data class VillagerTradeItem(
    val item: Supplier<Item>,
    val amount: Int
) {
    companion object {
        val CODEC = RecordCodecBuilder.create { instance ->
            instance.group(
                ResourceKey.codec(Registries.ITEM).fieldOf("item").forGetter<VillagerTradeItem> {
                    val itemModId = it.item.get().descriptionId.split(".")[1]
                    val itemPath = it.item.get().descriptionId.split(".")[2]
                    return@forGetter ResourceKey.create(Registries.ITEM, DeltaboxUtil.resourceLocation(itemModId, itemPath))},
                com.mojang.serialization.Codec.INT.fieldOf("amount").forGetter(VillagerTradeItem::amount)
            ).apply(instance, { itemKey, amount ->
                val item = BuiltInRegistries.ITEM.get(itemKey)
                if (item === null) throw Exception("Item $itemKey not found in entries")
                VillagerTradeItem({item}, amount)
            })
        }
    }
}