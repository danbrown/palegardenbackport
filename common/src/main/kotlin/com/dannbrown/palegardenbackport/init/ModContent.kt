package com.dannbrown.palegardenbackport.init

import com.dannbrown.deltaboxlib.init.DeltaboxRegistrate
import com.dannbrown.deltaboxlib.registrate.util.CreativeTabsUtil
import net.minecraft.world.item.ItemStack

object ModContent {
  const val MOD_ID = "palegardenbackport"
  var REGISTRATE = DeltaboxRegistrate(MOD_ID)

  val TAB = REGISTRATE.creativeTab(MOD_ID, "Pale Garden Backport", { ItemStack(ModItems.MOD_ICON.get()) }, { p, o ->
    CreativeTabsUtil.displayAll(
      REGISTRATE, p, o
    )
  })

  fun init() {
    ModSounds.register()
    ModBlocks.register()
    ModItems.register()
    ModParticles.register()
    ModConfiguredFeatures.register()
    ModModelLayers.register()
    ModBlockEntities.register()
    REGISTRATE.buildRegistries()
  }
}