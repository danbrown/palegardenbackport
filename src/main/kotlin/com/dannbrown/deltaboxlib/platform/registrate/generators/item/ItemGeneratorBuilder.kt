package com.dannbrown.deltaboxlib.platform.registrate.generators.item

import com.dannbrown.deltaboxlib.platform.registrate.DeltaboxRegistrate
import com.dannbrown.deltaboxlib.platform.util.DeltaboxUtil
import com.tterrag.registrate.builders.ItemBuilder
import com.tterrag.registrate.providers.DataGenContext
import com.tterrag.registrate.providers.RegistrateItemModelProvider
import com.tterrag.registrate.providers.RegistrateRecipeProvider
import com.tterrag.registrate.util.entry.ItemEntry
import com.tterrag.registrate.util.nullness.NonNullBiConsumer
import com.tterrag.registrate.util.nullness.NonNullUnaryOperator
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item
import net.minecraft.world.item.Item.Properties

class ItemGeneratorBuilder<T : Item>(name: String, private val registrate: DeltaboxRegistrate) {
  private var _itemFactory: (Properties) -> T = { p: Properties -> Item(p) as T }
  private var _itemTags = mutableListOf<TagKey<Item>>()
  private var _builder: NonNullUnaryOperator<ItemBuilder<T, DeltaboxRegistrate>> = NonNullUnaryOperator { b: ItemBuilder<T, DeltaboxRegistrate> -> b }
  private val _name: String = name
  private var _prefix = ""
  private var _suffix = ""
  private var _textureName = name

  fun createBlockBase(registerName: String): ItemBuilder<T, DeltaboxRegistrate> {
    return registrate
      .item<T>(registerName, _itemFactory)
      .tag(*_itemTags.toTypedArray())
      .lang(DeltaboxUtil.LANG.asName(registerName))
  }

  fun itemFactory(factory: (Properties) -> T): ItemGeneratorBuilder<T> {
    _itemFactory = factory
    return this
  }

  fun tags(vararg tags: TagKey<Item>): ItemGeneratorBuilder<T> {
    _itemTags.addAll(tags)
    return this
  }


  fun properties(props: (Properties) -> Properties = { p: Properties -> p }): ItemGeneratorBuilder<T> {
    addBuilder { b -> b.properties { p -> props(p) } }
    return this
  }

  fun transform(t: NonNullUnaryOperator<ItemBuilder<T, DeltaboxRegistrate>>): ItemGeneratorBuilder<T> {
    this.checkCurrentBuilder()
    addBuilder { b ->
      b.transform(t)
    }
    return this
  }

  fun model(m: NonNullBiConsumer<DataGenContext<Item, T>, RegistrateItemModelProvider>): ItemGeneratorBuilder<T> {
    addBuilder { b -> b.model(m) }
    return this
  }

  fun recipe(r: NonNullBiConsumer<DataGenContext<Item, T>, RegistrateRecipeProvider>): ItemGeneratorBuilder<T> {
    this.checkCurrentBuilder()
    addBuilder { b ->
      b.recipe(r)
    }
    return this
  }

  fun prefix(prefix: String = ""): ItemGeneratorBuilder<T> {
    this._prefix += prefix
    return this
  }

  fun suffix(suffix: String = ""): ItemGeneratorBuilder<T> {
    this._suffix += suffix
    return this
  }

  fun textureName(textureName: String = _name): ItemGeneratorBuilder<T> {
    this._textureName = textureName
    return this
  }

  private fun fullName(): String {
    return _prefix + _name + _suffix
  }

  private fun checkCurrentBuilder() {
    if (_builder == null) throw Exception("No item started")
  }

  private fun addBuilder(toApply: (ItemBuilder<T, DeltaboxRegistrate>) -> ItemBuilder<T, DeltaboxRegistrate>): ItemGeneratorBuilder<T> {
    this.checkCurrentBuilder()
    val oldBuilder = _builder
    _builder = NonNullUnaryOperator { b: ItemBuilder<T, DeltaboxRegistrate> -> toApply(b).transform(oldBuilder) }
    return this
  }

  fun register(): ItemEntry<T> {
    this.checkCurrentBuilder()
    // create the item to return
    val item = createBlockBase(fullName()).transform(_builder).register()

    // return
    return item
  }
}