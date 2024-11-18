package com.dannbrown.deltaboxlib.platform.registrate

import com.tterrag.registrate.AbstractRegistrate

class DeltaboxRegistrate(modId: String): AbstractRegistrate<DeltaboxRegistrate>(modId) {

  /*? if forge {*/
  /*public override fun registerEventListeners(bus: net.minecraftforge.eventbus.api.IEventBus): DeltaboxRegistrate {
    return super.registerEventListeners(bus)
  }
  *//*?} elif neoforge {*/
  /*public override fun registerEventListeners(bus: net.neoforged.bus.api.IEventBus): DeltaboxRegistrate {
    return super.registerEventListeners(bus)
  }
  *//*?}*/
}