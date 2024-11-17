package com.dannbrown.deltaboxlib.platform.registrate

import com.dannbrown.deltaboxlib.platform.util.Util
import com.tterrag.registrate.AbstractRegistrate
import net.minecraft.network.chat.MutableComponent
import oshi.util.tuples.Quartet

class DeltaboxRegistrate(modId: String): AbstractRegistrate<DeltaboxRegistrate>(modId) {
  // @ LANG
  fun addFormulaLang(
    formula: String,
    name: String,
  ) : MutableComponent{
    return addRawLang("formula.${modid}.$formula", name)
  }

  fun addEntityLang(
    name: String,
    phrase: String,
  ) :MutableComponent {
    return addRawLang("entity.${modid}.$name", phrase)
  }

  fun addItemTooltipLang(
    itemId: String,
    phrase: String,
    mId: String = modid,
  ) :MutableComponent {
    return addRawLang(Util.LANG.getTooltipKey(mId, itemId), phrase)
  }

  fun addGenericTooltipLang(
    itemId: String,
    phrase: String,
  ) :MutableComponent {
    return addRawLang(Util.LANG.getTooltipKey(null, itemId), phrase)
  }

  fun addCreativeTabLang(
    tab: String,
    name: String,
    _modid: String = modid,
  ) :MutableComponent {
    return addRawLang("itemGroup.${_modid}.$tab", name)
  }

  fun addPotionLang(
    name: String,
    phrase: String,
  ) :Quartet<MutableComponent, MutableComponent, MutableComponent, MutableComponent> {
    val potion = addRawLang("item.${"minecraft"}.potion.effect.$name", "Potion of $phrase")
    val splash = addRawLang("item.${"minecraft"}.splash_potion.effect.$name", "Splash Potion of $phrase")
    val lingering = addRawLang("item.${"minecraft"}.lingering_potion.effect.$name", "Lingering Potion of $phrase")
    val arrow = addRawLang("item.${"minecraft"}.tipped_arrow.effect.$name", "Arrow of $phrase")
    return Quartet(potion, splash, lingering, arrow)
  }

  fun addAdvancementLang(
    name: String,
    title: String,
    description: String,
    _modid: String = modid,
  ) :Pair<MutableComponent, MutableComponent> {
    return Pair(addRawLang("advancements.${_modid}.$name.title", title), addRawLang("advancements.${_modid}.$name.description", description))
  }

  fun addEffectLang(
    name: String,
    phrase: String,
    _modid: String = modid,
  ) :MutableComponent {
    return addRawLang("effect.${_modid}.$name", phrase)
  }

  fun addDeathMessageLang(
    name: String,
    phrase: String,
    _modid: String = modid,
  ) :MutableComponent {
    return addRawLang("death.attack.$name", phrase)
  }

  fun addGogglesLang(
    name: String,
    phrase: String,
    _modid: String = modid,
  ) :MutableComponent {
    return  addRawLang("${_modid}.gui.goggles.$name", phrase)
  }

  fun addBiomeLang(
    name: String,
    phrase: String,
    _modid: String = modid,
  ) :MutableComponent {
    return addRawLang("biome.${_modid}.$name", phrase)
  }

  fun addSoundLang(
    name: String,
    phrase: String,
    _modid: String = modid,
  ) :MutableComponent {
    return addRawLang("sound.${_modid}.$name", phrase)
  }

  fun addDimensionLang(
    name: String,
    phrase: String,
    _modid: String = modid,
  ) :MutableComponent {
    return addRawLang("dimension.${_modid}.$name", phrase)
  }

  fun addWorldPresetLang(
    name: String,
    phrase: String,
    _modid: String = modid,
  ) :MutableComponent {
    return addRawLang("generator.${_modid}.$name", phrase)
  }

  fun addPaintingVariantLang(
    name: String,
    phrase: String,
    author: String,
    _modid: String = modid,
  ) :Pair<MutableComponent, MutableComponent> {
    return Pair(addRawLang("painting.${_modid}.$name.title", phrase), addRawLang("painting.${_modid}.$name.author", author))
  }
}