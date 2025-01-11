package com.dannbrown.deltaboxlib.platform.registrate.generators.block

import com.dannbrown.deltaboxlib.platform.registrate.DeltaboxRegistrate
import com.dannbrown.deltaboxlib.platform.registrate.generators.recipe.RecipePresets
import com.dannbrown.deltaboxlib.platform.util.DeltaboxUtil
import com.tterrag.registrate.builders.BlockBuilder
import com.tterrag.registrate.providers.DataGenContext
import com.tterrag.registrate.providers.RegistrateBlockstateProvider
import com.tterrag.registrate.providers.RegistrateRecipeProvider
import com.tterrag.registrate.providers.loot.RegistrateBlockLootTables
import com.tterrag.registrate.util.entry.BlockEntry
import com.tterrag.registrate.util.nullness.NonNullBiConsumer
import com.tterrag.registrate.util.nullness.NonNullUnaryOperator
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.state.BlockBehaviour.Properties
import net.minecraft.world.level.material.MapColor
import java.util.function.Supplier

class BlockGeneratorBuilder<T : Block>(name: String, private val registrate: DeltaboxRegistrate) {
  private val _name: String = name
  private var _prefix = ""
  private var _suffix = ""
  private var _blockFactory: (Properties, BlockGeneratorContext) -> T = { p, c -> Block(p) as T }
  private var _blockTags = mutableListOf<TagKey<Block>>()
  private var _itemTags = mutableListOf<TagKey<Item>>()
  private var _color: MapColor? = null
  private var _copyFrom: Supplier<Block> = Supplier { Blocks.STONE }
  private var _toolTier: TagKey<Block>? = null
  private var _toolType: TagKey<Block>? = null
  private var _correctToolForDrops = false
  private var _textureName = name
  private var _noItem = false
  private var _builder: NonNullUnaryOperator<BlockBuilder<T, DeltaboxRegistrate>> = NonNullUnaryOperator { b: BlockBuilder<T, DeltaboxRegistrate> -> b }

  // @ Other properties
  private var _flammability: Pair<Int, Int>? = null
  private var _strippedBlock: Supplier<out Block>? = null
  private var _pottedBlock: BlockEntry<out Block>? = null
  private var _cutoutRender: Boolean = false
  // End of properties

  private fun createBlockBase(registerName: String): BlockBuilder<T, DeltaboxRegistrate> {
    val ctx = BlockGeneratorContext(_flammability, _strippedBlock, _pottedBlock, _cutoutRender)
    return registrate.block<T>(registerName) { p -> _blockFactory(p, ctx)}
      .initialProperties { _copyFrom.get() }
      .properties { p -> p.mapColor(if (_color !== null) { _color } else { MapColor.COLOR_GRAY }) }
      .properties(if (_correctToolForDrops) { p -> p.requiresCorrectToolForDrops() } else { p -> p })
      .transform(if (this._toolType != null) { p -> p.tag(this._toolType) } else { p -> p })
      .transform(if (this._toolTier != null) { p -> p.tag(this._toolTier) } else { p -> p })
      .tag(*_blockTags.toTypedArray())
      .lang(DeltaboxUtil.LANG.asName(registerName))
      .transform(if (this._noItem) { b -> b }
      else { b ->
        b.item()
          .tag(*_itemTags.toTypedArray())
          .build()
      })
  }

  // @ Properties
  fun flammable(burnChance: Int = 20, spreadChance: Int = 5): BlockGeneratorBuilder<T> {
    this.checkCurrentBuilder()
    this._flammability = Pair(burnChance, spreadChance)
    return this
  }

  fun strippable(block: Supplier<out Block>): BlockGeneratorBuilder<T> {
    this.checkCurrentBuilder()
    this._strippedBlock = block
    return this
  }

  fun potted(block: BlockEntry<out Block>): BlockGeneratorBuilder<T> {
    this.checkCurrentBuilder()
    this._pottedBlock = block
    return this
  }

  fun cutoutRender(): BlockGeneratorBuilder<T> {
    this.checkCurrentBuilder()
    this._cutoutRender = true
    return this
  }

  // @ Builder Chaining Methods
  fun blockFactory(factory: (Properties, BlockGeneratorContext) -> T = { p, c -> Block(p) as T }): BlockGeneratorBuilder<T> {
    this._blockFactory = { p, c -> factory(p, c) }
    return this
  }

  fun blockFactory(factory: (Properties) -> T = { p -> Block(p) as T }): BlockGeneratorBuilder<T> {
    this._blockFactory = { p, c -> factory(p) }
    return this
  }

  fun properties(props: (Properties) -> Properties = { p: Properties -> p }): BlockGeneratorBuilder<T> {
    addBuilder { b -> b.properties { p -> props(p) } }
    return this
  }

  fun transform(t: NonNullUnaryOperator<BlockBuilder<T, DeltaboxRegistrate>>): BlockGeneratorBuilder<T> {
    this.checkCurrentBuilder()
    addBuilder { b ->
      b.transform(t)
    }
    return this
  }

  fun recipe(r: NonNullBiConsumer<DataGenContext<Block, T>, RegistrateRecipeProvider>): BlockGeneratorBuilder<T> {
    this.checkCurrentBuilder()
    addBuilder { b ->
      b.recipe(r)
    }
    return this
  }

  fun recipe(r: (DataGenContext<Block, T>, RegistrateRecipeProvider, RecipePresets) -> Unit): BlockGeneratorBuilder<T> {
    this.checkCurrentBuilder()
    addBuilder { b ->
      b.recipe { c, p -> r(c, p, RecipePresets(registrate, p))}
    }
    return this
  }

  fun loot(l: NonNullBiConsumer<RegistrateBlockLootTables, T>): BlockGeneratorBuilder<T> {
    this.checkCurrentBuilder()
    addBuilder { b ->
      b.loot(l)
    }
    return this
  }

  fun blockstate(bs: NonNullBiConsumer<DataGenContext<Block, T>, RegistrateBlockstateProvider>): BlockGeneratorBuilder<T> {
    this.checkCurrentBuilder()
    addBuilder { b ->
      b.blockstate(bs)
    }
    return this
  }

  fun copyFrom(block: Supplier<Block> = Supplier { Blocks.STONE }): BlockGeneratorBuilder<T> {
    this._copyFrom = block
    return this
  }

  fun blockTags(tags: List<TagKey<Block>> = mutableListOf()): BlockGeneratorBuilder<T> {
    this._blockTags.addAll(tags)
    return this
  }

  fun itemTags(tags: List<TagKey<Item>> = mutableListOf()): BlockGeneratorBuilder<T> {
    this._itemTags.addAll(tags)
    return this
  }

  fun color(color: MapColor = MapColor.COLOR_GRAY): BlockGeneratorBuilder<T> {
    this._color = color
    return this
  }

  fun noItem(): BlockGeneratorBuilder<T> {
    this._noItem = true
    return this
  }

  fun prefix(prefix: String = ""): BlockGeneratorBuilder<T> {
    this._prefix += prefix
    return this
  }

  fun suffix(suffix: String = ""): BlockGeneratorBuilder<T> {
    this._suffix += suffix
    return this
  }

  fun textureName(textureName: String = _name): BlockGeneratorBuilder<T> {
    this._textureName = textureName
    return this
  }

  fun toolAndTier(
    tool: TagKey<Block>? = null,
    tier: TagKey<Block>? = null,
    correctToolForDrops: Boolean = true,
  ): BlockGeneratorBuilder<T> {
    this._toolTier = tier
    this._toolType = tool
    this._correctToolForDrops = correctToolForDrops
    return this
  }

  // Only called when creating a family
  fun fromFamily(
    copyFrom: Supplier<Block>,
    props: (Properties) -> Properties = { p: Properties -> p },
    color: MapColor? = null,
    tool: TagKey<Block>? = null,
    tier: TagKey<Block>? = null,
    correctToolForDrops: Boolean = true,
  ): BlockGeneratorBuilder<T> {
    this._copyFrom = copyFrom
    addBuilder { b -> b.properties { p -> props(p) } }
    this._color = color
    this._toolType = tool
    this._toolTier = tier
    this._correctToolForDrops = correctToolForDrops
    return this
  }

  private fun checkCurrentBuilder() {
    if (_builder == null) throw Exception("No block started")
  }

  private fun fullName(): String {
    return _prefix + _name + _suffix
  }

  // @ Builder Registering
  private fun addBuilder(toApply: (BlockBuilder<T, DeltaboxRegistrate>) -> BlockBuilder<T, DeltaboxRegistrate>): BlockGeneratorBuilder<T> {
    this.checkCurrentBuilder()
    val oldBuilder = _builder
    _builder = NonNullUnaryOperator { b: BlockBuilder<T, DeltaboxRegistrate> -> toApply(b).transform(oldBuilder) }
    return this
  }

  fun register(): BlockEntry<T> {
    this.checkCurrentBuilder()

    // create the block to return
    val block = createBlockBase(fullName()).transform(_builder).register()

    // add to flammable blocks if flammable
    if (_flammability != null) {
      registrate.addFlammableBlock(block, _flammability!!.first, _flammability!!.second)
    }

    // add stripped block capability if present
    if (_strippedBlock != null) {
      registrate.addStrippableBlock(block, _strippedBlock!!)
    }

    // add potted block capability if present
    if (_pottedBlock != null) {
      registrate.addPottedBlock(_pottedBlock!!, block)
    }

    // add cutout render capability if present
    if (_cutoutRender) {
      registrate.addCutoutRender(block)
    }

    // return
    return block
  }

  class BlockGeneratorContext(val flammability: Pair<Int, Int>?, val strippedBlock: Supplier<out Block>?, val pottedBlock: BlockEntry<out Block>?, val cutoutRender: Boolean) {
  }
}