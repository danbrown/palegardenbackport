package com.dannbrown.palegardenbackport.fabric.init

import com.dannbrown.deltaboxlib.fabric.registrate.datagen.RegistrateDatagenFabric
import com.dannbrown.palegardenbackport.init.ModContent
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator

class DeltaboxLibDatagenFabric: DataGeneratorEntrypoint {
  override fun onInitializeDataGenerator(fabricDataGenerator: FabricDataGenerator) {
    val pack = fabricDataGenerator.createPack()

    RegistrateDatagenFabric.buildDatagenResources(pack, ModContent.REGISTRATE)
  }
}