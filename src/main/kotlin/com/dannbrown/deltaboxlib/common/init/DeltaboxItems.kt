package com.dannbrown.deltaboxlib.common.init

import com.dannbrown.deltaboxlib.common.DeltaboxLibCommon
import com.dannbrown.deltaboxlib.platform.registrate.generators.item.ItemGenerator
import com.dannbrown.deltaboxlib.platform.util.DeltaboxUtil
import net.minecraft.world.item.Item
import net.minecraft.world.item.Items
import net.minecraft.world.item.crafting.Ingredient
import java.util.function.Supplier

object DeltaboxItems {
  val ITEMS = ItemGenerator(DeltaboxLibCommon.REGISTRATE)

  val WARP_CRYSTAL = ITEMS.create<Item>("warp_crystal")
    .recipe { c, p, b ->
      b.simpleShapedRecipe(
        { c.get() },
        arrayOf("GSG", "SBS", "GSG"),
        mapOf(
          'G' to Supplier { Ingredient.of(Items.GLASS) },
          'S' to Supplier { Ingredient.of(Items.STICK) },
          'B' to Supplier { Ingredient.of(Items.BLAZE_POWDER) }
        )
      )
    }
    .register()

  val BEAN_POD = ITEMS.create<Item>("bean_pod").register()

  fun register() {
    DeltaboxUtil.logInfo("Registering items...")
  }
}