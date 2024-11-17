package com.dannbrown.deltaboxlib.platform

import com.dannbrown.deltaboxlib.common.*

/*? if fabric {*/

/*import net.fabricmc.api.ModInitializer;

object DeltaboxLibWrapper : ModInitializer {
    override fun onInitialize() {
        DeltaboxLib.init()
    }
}
*//*?} elif forge {*/
import net.minecraftforge.fml.common.Mod

@Mod(DeltaboxLib.MOD_ID)
object DeltaboxLibWrapper {
    init {
        DeltaboxLib.init()
    }
}
/*?} else {*/
/*import net.neoforged.fml.common.Mod

@Mod(DeltaboxLib.MOD_ID)
class DeltaboxLibWrapper() {
    init {
        DeltaboxLib.init()
    }
}
*//*?}*/