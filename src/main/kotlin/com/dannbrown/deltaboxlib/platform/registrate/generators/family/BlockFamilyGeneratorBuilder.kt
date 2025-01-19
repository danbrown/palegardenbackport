package com.dannbrown.deltaboxlib.platform.registrate.generators.family

import com.dannbrown.deltaboxlib.platform.registrate.generators.block.BlockGenerator
import net.minecraft.tags.TagKey
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.state.BlockBehaviour
import net.minecraft.world.level.material.MapColor
import java.util.function.Supplier

class BlockFamilyGeneratorBuilder(name: String, private val generator: BlockGenerator) {
  private val _name = name
  private var _sharedProps: (BlockBehaviour.Properties) -> BlockBehaviour.Properties = { p: BlockBehaviour.Properties -> p }
  private var _toolType: TagKey<Block>? = null
  private var _toolTier: TagKey<Block>? = null
  private var _color: MapColor? = null
  private var _accentColor: MapColor? = null
  private var _copyFrom: Supplier<Block> = Supplier { Blocks.STONE }
  private val _denyList = mutableListOf<BlockFamily.Type>()
  private val _blockFamily: BlockFamily = BlockFamily()

  // @ Family Presets
  fun longBlockFamily(mainBlock: Supplier<out Block>? = null, isRotatedBlock: Boolean = false): BlockFamily {
    return LongBlockFamilySet(
      generator,
      _name,
      _sharedProps,
      _toolType,
      _toolTier,
      _color,
      _accentColor,
      _copyFrom,
      _denyList,
      mainBlock,
      isRotatedBlock
    ).getFamily()
  }


  // @ Builder Chaining Methods
  fun sharedProps(props: (BlockBehaviour.Properties) -> BlockBehaviour.Properties = { p: BlockBehaviour.Properties -> p }): BlockFamilyGeneratorBuilder {
    _sharedProps = { p -> props(p) }
    return this
  }

  fun toolAndTier(
    tool: TagKey<Block>? = null,
    tier: TagKey<Block>? = null,
    requiredForDrops: Boolean = true
  ): BlockFamilyGeneratorBuilder {
    _toolTier = tier
    _toolType = tool
    if (requiredForDrops) {
      sharedProps { p -> p.requiresCorrectToolForDrops() }
    }
    return this
  }

  fun color(
    color: MapColor,
    accentColor: MapColor? = null
  ): BlockFamilyGeneratorBuilder {
    this._color = color
    this._accentColor = accentColor
    return this
  }

  fun copyFrom(block: Supplier<Block>): BlockFamilyGeneratorBuilder {
    _copyFrom = block
    return this
  }

  fun denyList(vararg deny: BlockFamily.Type): BlockFamilyGeneratorBuilder {
    this._denyList.addAll(deny)
    return this
  }

  fun getColor(): MapColor? {
    return _color
  }

  fun getAccentColor(): MapColor? {
    return _accentColor
  }

  fun getCopyFrom(): Supplier<Block> {
    return _copyFrom
  }
  fun getDenyList(): List<BlockFamily.Type> {
    return _denyList
  }

  fun getSharedProps(): (BlockBehaviour.Properties) -> BlockBehaviour.Properties {
    return _sharedProps
  }

  fun getToolTier(): TagKey<Block>? {
    return _toolTier
  }

  fun getToolType(): TagKey<Block>? {
    return _toolType
  }

  fun getName(): String {
    return _name
  }

  fun getBlockFamily(): BlockFamily {
    return _blockFamily
  }

  fun getGenerator(): BlockGenerator {
    return generator
  }

  /**
   * Allow for custom block family generation
   */
  fun custom(functionToExecute: (BlockFamilyGeneratorBuilder) -> BlockFamily): BlockFamily {
    return functionToExecute(this)
  }
}