package com.dannbrown.deltaboxlib.platform.registrate.generators.trades


import com.dannbrown.deltaboxlib.common.DeltaboxLibCommon
import com.dannbrown.deltaboxlib.platform.registrate.DeltaboxRegistrate
import com.dannbrown.deltaboxlib.platform.util.DeltaboxUtil
import com.dannbrown.deltaboxlib.platform.util.DeltaboxUtil.getAnyway
import com.mojang.serialization.JsonOps
import net.minecraft.data.CachedOutput
import net.minecraft.data.DataGenerator
import net.minecraft.data.DataProvider
import net.minecraft.data.PackOutput
import java.io.IOException
import java.nio.file.Path
import java.util.concurrent.CompletableFuture

/*? if forge || neoforge {*/
class WandererTradeProvider(
  private val registrate: DeltaboxRegistrate,
  private val generator: DataGenerator
) : DataProvider {
    private val pathProvider = generator.packOutput.createPathProvider(PackOutput.Target.DATA_PACK, WandererTradeDeserializer.PATH)
    override fun getName(): String = "Wanderer Trades Datagen for: ${registrate.modid}"

    override fun run(cachedOutput: CachedOutput): CompletableFuture<*> {
        val futures: MutableList<CompletableFuture<*>> = ArrayList()
        for (trade in registrate.WANDERER_TRADES) {
            val tradeName = trade.rarity.toString() + "_" + DeltaboxUtil.itemId({trade.tradeCosts.first().item.get()}) + "_for_" + DeltaboxUtil.itemId({trade.tradeSells.first().item.get()})
            val tradePath = pathProvider.json(DeltaboxUtil.resourceLocation(registrate.modid, tradeName))
            futures.add(saveTradeData(cachedOutput, tradePath, trade))
        }
        return CompletableFuture.allOf(*futures.toTypedArray())
    }

    private fun saveTradeData(cachedOutput: CachedOutput, path: Path, trade: WandererTradeCodec): CompletableFuture<*> {
        return try {
            val jsonObject = WandererTradeCodec.CODEC
              .encodeStart(JsonOps.INSTANCE, trade)
              .getAnyway()
              .asJsonObject
            DataProvider.saveStable(cachedOutput, jsonObject, path)
        } catch (ioException: IOException) {
            DeltaboxLibCommon.LOGGER.error("Couldn't save wanderer trade at {}", path, ioException)
            throw ioException
        }
    }
}
/*?}*/