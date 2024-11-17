package com.dannbrown.deltaboxlib.common

import com.dannbrown.deltaboxlib.platform.registrate.DeltaboxRegistrate
import com.dannbrown.deltaboxlib.platform.registrate.transformers.BlockLootPresets
import com.dannbrown.deltaboxlib.platform.registrate.transformers.BlockstatePresets
import com.dannbrown.deltaboxlib.platform.util.Util
import com.tterrag.registrate.providers.ProviderType
import net.minecraft.data.DataGenerator
import net.minecraft.world.level.block.Block
import org.slf4j.LoggerFactory

object DeltaboxLib {
    const val MOD_ID: String = "deltaboxlib"

    @JvmField
    val LOGGER = LoggerFactory.getLogger(MOD_ID)

    val REGISTRATE = DeltaboxRegistrate(MOD_ID)

    fun id(path: String) = Util.resourceLocation(MOD_ID, path)

    val ANY_BLOCK = REGISTRATE.block<Block>("adamantium_block") { p -> Block(p) }
      .loot(BlockLootPresets.dropItselfLoot())
      .blockstate(BlockstatePresets.simpleBlock())
      .register()

    private var initialized = false

    fun init() {
        println("HELLO WORLD IM A MOD INITIALIZER")
    }

    fun gatherData(gen: DataGenerator.PackGenerator) {
//        REGISTRATE.addDataGenerator(ProviderType.LANG, CRLangGen::generate)
    }

    @JvmStatic
    fun postInit() {
        if (initialized) return
        initialized = true
    }
}