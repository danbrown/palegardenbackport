package com.dannbrown.deltaboxlib.platform.registrate

/*? if fabric {*/
/*import com.dannbrown.deltaboxlib.common.DeltaboxLib
import com.dannbrown.deltaboxlib.platform.util.Util
import io.github.fabricators_of_create.porting_lib.data.ExistingFileHelper
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator
import java.nio.file.Paths

class DataFabric: DataGeneratorEntrypoint {
  override fun onInitializeDataGenerator(gen: FabricDataGenerator) {
    val mods = listOf("minecraft")
    // Ensure that all mods are present if they are needed for data gen
    for (mod in mods) {
      if(!Util.PATH.isModInstalled(mod)) throw IllegalStateException("Mod $mod is not installed!")
    }

    val resources = Paths.get(System.getProperty("user.dir"), "../src", "main", "resources")
    val helper = ExistingFileHelper.withResources(resources)
    val pack: FabricDataGenerator.Pack = gen.createPack()
    DeltaboxLib.REGISTRATE.setupDatagen(pack, helper)
    DeltaboxLib.gatherData(pack)
  }
}
*//*?}*/