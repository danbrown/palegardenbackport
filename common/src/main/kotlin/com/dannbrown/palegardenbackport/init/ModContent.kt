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
    ModConfig.register()
    ModSounds.register()
    ModTags.register()
    ModBlocks.register()
    ModEntityTypes.register()
    ModItems.register()
    ModParticles.register()
    ModConfiguredFeatures.register()
    ModPlacedFeatures.register()
    ModBlockEntities.register()
    ModModelLayers.register()
    ModPlacerTypes.register()
    ModBiomes.register()
    ModLang.register()
    ModBiomeModifiers.register()
    REGISTRATE.buildRegistries()
  }
}