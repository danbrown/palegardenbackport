package com.dannbrown.deltaboxlib.common.init

import com.dannbrown.deltaboxlib.common.DeltaboxLibCommon
import com.dannbrown.deltaboxlib.platform.registrate.generators.recipe.DeltaboxRecipeSlice
import com.dannbrown.deltaboxlib.platform.util.DeltaboxUtil
import net.minecraft.advancements.critereon.ItemPredicate
import net.minecraft.world.item.Items

class DeltaboxRecipes: DeltaboxRecipeSlice(DeltaboxLibCommon.MOD_ID) {
  override fun name(): String {
    return "example recipes"
  }
  override fun addRecipes() {
    val WARP_COMPASS = crafting({ DeltaboxItems.WARP_CRYSTAL.get() }) { b ->
      b
        .unlockedBy { ItemPredicate.Builder.item().of(Items.IRON_INGOT, Items.ENDER_PEARL).build() }
        .shaped(1) { c ->
          c
            .pattern(" G ")
            .pattern("GCG")
            .pattern(" G ")
            .define('G', Items.IRON_INGOT)
            .define('C', Items.ENDER_PEARL)
        }
    }
  }

  companion object {
    fun register() {
      DeltaboxLibCommon.REGISTRATE.recipe { DeltaboxRecipes() }
      DeltaboxUtil.logInfo("Registering recipes...")
    }
  }
}