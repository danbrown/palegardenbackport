package com.dannbrown.palegardenbackport.common.init

import com.dannbrown.palegardenbackport.common.ModCommon
import com.dannbrown.deltaboxlib.platform.registrate.generators.recipe.DeltaboxRecipeSlice
import com.dannbrown.deltaboxlib.platform.util.DeltaboxUtil

class ModRecipes: DeltaboxRecipeSlice(ModCommon.MOD_ID) {
  override fun name(): String {
    return "example recipes"
  }
  override fun addRecipes() {

  }

  companion object {
    fun register() {
      ModCommon.REGISTRATE.recipe { ModRecipes() }
      DeltaboxUtil.logInfo("Registering recipes...")
    }
  }
}