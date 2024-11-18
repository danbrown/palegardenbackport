package com.dannbrown.deltaboxlib.platform.registrate.abstract

import com.dannbrown.deltaboxlib.platform.util.Util
import com.tterrag.registrate.providers.RegistrateLangProvider
import net.minecraft.network.chat.MutableComponent
import oshi.util.tuples.Quartet

abstract class IDeltaboxLang(val modid: String) {
  abstract fun addLang(provider: RegistrateLangProvider)

  fun addFormulaLang(
    provider: RegistrateLangProvider,
    formula: String,
    name: String,
    mId: String = modid,
  ) {
    provider.add("formula.${mId}.$formula", name)
  }

  fun addEntityLang(
    provider: RegistrateLangProvider,
    name: String,
    phrase: String,
    mId: String = modid,
  ) {
    provider.add("entity.${mId}.$name", phrase)
  }

  fun addItemTooltipLang(
    provider: RegistrateLangProvider,
    itemId: String,
    phrase: String,
    mId: String = modid,
  ) {
    provider.add(Util.LANG.getTooltipKey(mId, itemId), phrase)
  }

  fun addGenericTooltipLang(
    provider: RegistrateLangProvider,
    itemId: String,
    phrase: String,
  ) {
    provider.add(Util.LANG.getTooltipKey(null, itemId), phrase)
  }

  fun addCreativeTabLang(
    provider: RegistrateLangProvider,
    tab: String,
    name: String,
    mId: String = modid,
  ) {
    provider.add("itemGroup.${mId}.$tab", name)
  }

  fun addPotionLang(
    provider: RegistrateLangProvider,
    name: String,
    phrase: String,
  ) {
    provider.add("item.${"minecraft"}.potion.effect.$name", "Potion of $phrase")
    provider.add("item.${"minecraft"}.splash_potion.effect.$name", "Splash Potion of $phrase")
    provider.add("item.${"minecraft"}.lingering_potion.effect.$name", "Lingering Potion of $phrase")
    provider.add("item.${"minecraft"}.tipped_arrow.effect.$name", "Arrow of $phrase")
  }

  fun addAdvancementLang(
    provider: RegistrateLangProvider,
    name: String,
    title: String,
    description: String,
    mId: String = modid,
  ) {
    provider.add("advancements.${mId}.$name.title", title)
    provider.add("advancements.${mId}.$name.description", description)
  }

  fun addEffectLang(
    provider: RegistrateLangProvider,
    name: String,
    phrase: String,
    mId: String = modid,
  ) {
    provider.add("effect.${mId}.$name", phrase)
  }

  fun addDeathMessageLang(
    provider: RegistrateLangProvider,
    name: String,
    phrase: String,
    mId: String = modid,
  ) {
    provider.add("death.attack.$name", phrase)
  }

  fun addBiomeLang(
    provider: RegistrateLangProvider,
    name: String,
    phrase: String,
    mId: String = modid,
  ) {
    provider.add("biome.${mId}.$name", phrase)
  }

  fun addSoundLang(
    provider: RegistrateLangProvider,
    name: String,
    phrase: String,
    mId: String = modid,
  ) {
    provider.add("sound.${mId}.$name", phrase)
  }

  fun addDimensionLang(
    provider: RegistrateLangProvider,
    name: String,
    phrase: String,
    mId: String = modid,
  ) {
    provider.add("dimension.${mId}.$name", phrase)
  }

  fun addWorldPresetLang(
    provider: RegistrateLangProvider,
    name: String,
    phrase: String,
    mId: String = modid,
  ) {
    provider.add("generator.${mId}.$name", phrase)
  }

  fun addPaintingVariantLang(
    provider: RegistrateLangProvider,
    name: String,
    phrase: String,
    author: String,
    mId: String = modid,
  ) {
    provider.add("painting.${mId}.$name.title", phrase)
    provider.add("painting.${mId}.$name.author", author)
  }
}