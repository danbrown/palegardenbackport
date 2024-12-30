package com.dannbrown.deltaboxlib.platform.registrate.generators.trades

import com.dannbrown.deltaboxlib.common.DeltaboxLib
import com.dannbrown.deltaboxlib.platform.registrate.DeltaboxRegistrate
import com.google.gson.GsonBuilder
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.mojang.serialization.JsonOps
import net.minecraft.resources.ResourceLocation
import net.minecraft.server.packs.resources.ResourceManager
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener
import net.minecraft.util.GsonHelper
import net.minecraft.util.profiling.ProfilerFiller

class VillagerTradeDeserializer(private val registrate: DeltaboxRegistrate) : SimpleJsonResourceReloadListener(GSON, VillagerTradeProvider.PATH) {
    companion object {
        private val GSON = GsonBuilder()
            .setPrettyPrinting()
            .disableHtmlEscaping()
            .create()
    }
    override fun apply(pObject: MutableMap<ResourceLocation, JsonElement>, pResourceManager: ResourceManager, pProfiler: ProfilerFiller) {
        pProfiler.push("Villager Trades Deserialization")
        val villagerTrades: MutableList<VillagerTradeCodec> = ArrayList()
        for ((resourceLocation, jsonElement) in pObject.entries) {
            val jsonObject: JsonObject = GsonHelper.convertToJsonObject(jsonElement, "villager_trades")
            val villagerTrade = VillagerTradeCodec.CODEC
                .parse(JsonOps.INSTANCE, jsonObject)
                .orThrow
            villagerTrades.add(villagerTrade)
        }
        registrate.updateTradesData(villagerTrades)
        pProfiler.pop()
    }
}