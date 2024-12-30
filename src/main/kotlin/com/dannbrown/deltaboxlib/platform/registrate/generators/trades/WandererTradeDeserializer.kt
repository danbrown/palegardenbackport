package com.dannbrown.deltaboxlib.platform.registrate.generators.trades

import com.dannbrown.deltaboxlib.common.DeltaboxLibCommon
import com.dannbrown.deltaboxlib.platform.registrate.DeltaboxRegistrate
import com.dannbrown.deltaboxlib.platform.util.DeltaboxUtil.getAnyway
import com.google.gson.GsonBuilder
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.mojang.serialization.JsonOps
import net.minecraft.resources.ResourceLocation
import net.minecraft.server.packs.resources.ResourceManager
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener
import net.minecraft.util.GsonHelper
import net.minecraft.util.profiling.ProfilerFiller
import java.io.IOException

class WandererTradeDeserializer(private val registrate: DeltaboxRegistrate) : SimpleJsonResourceReloadListener(GSON, PATH) {
    companion object {
        const val PATH = "wanderer_trades"
        private val GSON = GsonBuilder()
            .setPrettyPrinting()
            .disableHtmlEscaping()
            .create()
    }
    override fun apply(pObject: MutableMap<ResourceLocation, JsonElement>, pResourceManager: ResourceManager, pProfiler: ProfilerFiller) {
        pProfiler.push("Wanderer Trades Deserialization")
        val wandererTrades: MutableList<WandererTradeCodec> = ArrayList()
        for ((resourceLocation, jsonElement) in pObject.entries) {
            val jsonObject: JsonObject = GsonHelper.convertToJsonObject(jsonElement, PATH)
            try {
                val wandererTrade = WandererTradeCodec.CODEC
                    .parse(JsonOps.INSTANCE, jsonObject)
                    .getAnyway()
                wandererTrades.add(wandererTrade)
            } catch (ioException: IOException) {
                DeltaboxLibCommon.LOGGER.error("Couldn't load wanderer trade in {}", resourceLocation, ioException)
                throw ioException
            }
        }
        registrate.WANDERER_TRADES.clear()
        registrate.WANDERER_TRADES.addAll(wandererTrades)
        pProfiler.pop()
    }
}