package com.dannbrown.palegardenbackport.fabric.init

import com.dannbrown.deltaboxlib.fabric.init.loaders.DeltaboxLibLoadTradesFabric
import com.dannbrown.deltaboxlib.fabric.registrate.RegistrateInitFabric
import com.dannbrown.palegardenbackport.fabric.compat.ModTerraBlenderAPIFabric
import com.dannbrown.palegardenbackport.init.ModContent
import net.fabricmc.api.ClientModInitializer
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.fabricmc.api.ModInitializer
import terrablender.api.TerraBlenderApi

object ModContentFabric : ModInitializer, ClientModInitializer, TerraBlenderApi {
  val registrateInit = RegistrateInitFabric(ModContent.REGISTRATE)
  override fun onInitialize() {
    ModContent.init()
    registrateInit.init()

    // register datapack entries, like villager trades
    DeltaboxLibLoadTradesFabric.onDatapackReload(ModContent.REGISTRATE)
  }

  @Environment(EnvType.CLIENT)
  override fun onInitializeClient() {
    ModContent.initClient()
    registrateInit.initClient()
  }

  override fun onTerraBlenderInitialized() {
    ModTerraBlenderAPIFabric.registerRegions()
  }
}
