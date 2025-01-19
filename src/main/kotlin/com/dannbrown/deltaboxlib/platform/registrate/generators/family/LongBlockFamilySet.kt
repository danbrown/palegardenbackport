package com.dannbrown.deltaboxlib.platform.registrate.generators.family

import com.dannbrown.deltaboxlib.platform.registrate.generators.block.BlockGenerator
import com.dannbrown.deltaboxlib.platform.registrate.generators.recipe.RecipePresets
import com.dannbrown.deltaboxlib.platform.util.DeltaboxUtil
import com.dannbrown.deltaboxlib.registry.transformers.BlockTagPresets
import net.minecraft.tags.TagKey
import net.minecraft.world.item.crafting.Ingredient
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.RotatedPillarBlock
import net.minecraft.world.level.block.state.BlockBehaviour
import net.minecraft.world.level.material.MapColor
import java.util.function.Supplier

/**
 * Returns a Long block family composed by Normals, Polished, Bricks, Cut, Chiseled variants (stairs, slabs, walls)
 */
class LongBlockFamilySet(
  private val generator: BlockGenerator,
  private val _name: String,
  private val _sharedProps: (BlockBehaviour.Properties) -> BlockBehaviour.Properties = { p: BlockBehaviour.Properties -> p },
  private val _toolType: TagKey<Block>? = null,
  private val _toolTier: TagKey<Block>? = null,
  private val _color: MapColor? = null,
  private val _accentColor: MapColor? = null,
  private val _copyFrom: Supplier<Block> = Supplier { Blocks.STONE },
  private val _denyList: List<BlockFamily.Type> = mutableListOf(),
  private var mainBlock: Supplier<out Block>? = null,
  isRotatedBlock: Boolean = false
): AbstractBlockFamilySet() {
  init {
    val MATERIAL_TAG = DeltaboxUtil.TAGS.modItemTag(generator.registrate.modid, _name + "_blocks")

    if (mainBlock == null) {
      _blockFamily.setVariant(BlockFamily.Type.MAIN) {
        generator.create<Block>(_name)
          .fromFamily(_copyFrom, _sharedProps, _color, _toolType, _toolTier)
          .itemTags(listOf(MATERIAL_TAG))
          .blockTags(listOf(*BlockTagPresets.caveReplaceableTags().first.toTypedArray()))
          .register()
      }
      mainBlock = _blockFamily.blocks[BlockFamily.Type.MAIN]!!
    }

    if (!_denyList.contains(BlockFamily.Type.MAIN)) {
      if (!_denyList.contains(BlockFamily.Type.STAIRS)) {
        _blockFamily.setVariant(BlockFamily.Type.STAIRS) {
          generator.createStairs(_name, _name, isRotatedBlock)
            .fromFamily(_copyFrom, _sharedProps, _color, _toolType, _toolTier)
            .itemTags(listOf(MATERIAL_TAG))
            .recipe { c, p ->
              RecipePresets(generator.registrate, p).simpleStonecuttingRecipe(
                { c.get() },
                { mainBlock!!.get().asItem() },
                1
              )
              RecipePresets(generator.registrate, p).stairsCraftingRecipe({ c.get() }) {
                Ingredient.of(mainBlock!!.get().asItem())
              }
            }
            .register()
        }
      }

      if (!_denyList.contains(BlockFamily.Type.SLAB)) {
        _blockFamily.setVariant(BlockFamily.Type.SLAB) {
          generator.createSlab(_name, _name, isRotatedBlock)
            .fromFamily(_copyFrom, _sharedProps, _color, _toolType, _toolTier)
            .itemTags(listOf(MATERIAL_TAG))
            .recipe { c, p ->
              RecipePresets(generator.registrate, p).simpleStonecuttingRecipe(
                { c.get() },
                { mainBlock!!.get().asItem() },
                2
              )
              RecipePresets(generator.registrate, p).slabCraftingRecipe({ c.get() }) {
                Ingredient.of(mainBlock!!.get().asItem())
              }
            }
            .register()
        }
      }

      if (!_denyList.contains(BlockFamily.Type.WALL)) {
        _blockFamily.setVariant(BlockFamily.Type.WALL) {
          generator.createWall(_name, _name, isRotatedBlock)
            .fromFamily(_copyFrom, _sharedProps, _color, _toolType, _toolTier)
            .itemTags(listOf(MATERIAL_TAG))
            .recipe { c, p ->
              RecipePresets(generator.registrate, p).simpleStonecuttingRecipe(
                { c.get() },
                { mainBlock!!.get().asItem() },
                1
              )
              RecipePresets(generator.registrate, p).wallCraftingRecipe({ c.get() }) {
                Ingredient.of(mainBlock!!.get().asItem())
              }
            }
            .register()
        }
      }
    }
    // start polished chain
    if (!_denyList.contains(BlockFamily.Type.POLISHED)) {
      _blockFamily.setVariant(BlockFamily.Type.POLISHED) {
        generator.create<Block>("polished_$_name")
          .fromFamily(_copyFrom, _sharedProps, _color, _toolType, _toolTier)
          .itemTags(listOf(MATERIAL_TAG))
          .recipe { c, p ->
            RecipePresets(generator.registrate, p).simpleStonecuttingRecipe(
              { c.get() },
              { mainBlock!!.get().asItem() },
              1
            )
            RecipePresets(generator.registrate, p).polishedCraftingRecipe({ c.get() }, {
              Ingredient.of(mainBlock!!.get().asItem())
            })
          }
          .register()
      }

      if (!_denyList.contains(BlockFamily.Type.POLISHED_STAIRS)) {
        _blockFamily.setVariant(BlockFamily.Type.POLISHED_STAIRS) {
          generator.createStairs("polished_$_name", "polished_$_name")
            .fromFamily(_copyFrom, _sharedProps, _color, _toolType, _toolTier)
            .itemTags(listOf(MATERIAL_TAG))
            .recipe { c, p ->
              RecipePresets(generator.registrate, p).simpleStonecuttingRecipe(
                { c.get() },
                { _blockFamily.blocks[BlockFamily.Type.POLISHED]!!.get().asItem() },
                1,
              )
              RecipePresets(generator.registrate, p).simpleStonecuttingRecipe(
                { c.get() },
                { mainBlock!!.get().asItem() },
                1
              )
              RecipePresets(generator.registrate, p).stairsCraftingRecipe({ c.get() }) {
                Ingredient.of(_blockFamily.blocks[BlockFamily.Type.POLISHED]!!.get().asItem())
              }
            }
            .register()
        }
      }

      if (!_denyList.contains(BlockFamily.Type.POLISHED_SLAB)) {
        _blockFamily.setVariant(BlockFamily.Type.POLISHED_SLAB) {
          generator.createSlab("polished_$_name", "polished_$_name")
            .fromFamily(_copyFrom, _sharedProps, _color, _toolType, _toolTier)
            .itemTags(listOf(MATERIAL_TAG))
            .recipe { c, p ->
              RecipePresets(generator.registrate, p).simpleStonecuttingRecipe(
                { c.get() },
                { _blockFamily.blocks[BlockFamily.Type.POLISHED]!!.get().asItem() },
                2
              )
              RecipePresets(generator.registrate, p).simpleStonecuttingRecipe(
                { c.get() },
                { mainBlock!!.get().asItem() },
                2
              )
              RecipePresets(generator.registrate, p).slabCraftingRecipe({ c.get() }) {
                Ingredient.of(_blockFamily.blocks[BlockFamily.Type.POLISHED]!!.get().asItem())
              }
            }
            .register()
        }
      }

      if (!_denyList.contains(BlockFamily.Type.POLISHED_WALL)) {
        _blockFamily.setVariant(BlockFamily.Type.POLISHED_WALL) {
          generator.createWall("polished_$_name", "polished_$_name")
            .fromFamily(_copyFrom, _sharedProps, _color, _toolType, _toolTier)
            .itemTags(listOf(MATERIAL_TAG))
            .recipe { c, p ->
              RecipePresets(generator.registrate, p).simpleStonecuttingRecipe(
                { c.get() },
                { _blockFamily.blocks[BlockFamily.Type.POLISHED]!!.get().asItem() },
                1
              )
              RecipePresets(generator.registrate, p).simpleStonecuttingRecipe(
                { c.get() },
                { mainBlock!!.get().asItem() },
                1
              )
              RecipePresets(generator.registrate, p).wallCraftingRecipe({ c.get() }) {
                Ingredient.of(_blockFamily.blocks[BlockFamily.Type.POLISHED]!!.get().asItem())
              }
            }
            .register()
        }
      }
    }
    // start bricks chain
    if (!_denyList.contains(BlockFamily.Type.BRICKS)) {
      _blockFamily.setVariant(BlockFamily.Type.BRICKS) {
        generator.create<Block>("${_name}_bricks")
          .fromFamily(_copyFrom, _sharedProps, _color, _toolType, _toolTier)
          .itemTags(listOf(MATERIAL_TAG))
          .recipe { c, p ->
            RecipePresets(generator.registrate, p).simpleStonecuttingRecipe(
              { c.get() },
              { _blockFamily.blocks[BlockFamily.Type.POLISHED]!!.get().asItem() },
              1
            )
            RecipePresets(generator.registrate, p).simpleStonecuttingRecipe(
              { c.get() },
              { mainBlock!!.get().asItem() },
              1
            )
            RecipePresets(generator.registrate, p).polishedCraftingRecipe({ c.get() }, {
              Ingredient.of(_blockFamily.blocks[BlockFamily.Type.POLISHED]!!.get().asItem())
            })
          }
          .register()
      }

      if (!_denyList.contains(BlockFamily.Type.BRICK_STAIRS)) {
        _blockFamily.setVariant(BlockFamily.Type.BRICK_STAIRS) {
          generator.createStairs("${_name}_brick", "${_name}_bricks")
            .fromFamily(_copyFrom, _sharedProps, _color, _toolType, _toolTier)
            .itemTags(listOf(MATERIAL_TAG))
            .recipe { c, p ->
              RecipePresets(generator.registrate, p).simpleStonecuttingRecipe(
                { c.get() },
                { _blockFamily.blocks[BlockFamily.Type.POLISHED]!!.get().asItem() },
                1
              )
              RecipePresets(generator.registrate, p).simpleStonecuttingRecipe(
                { c.get() },
                { _blockFamily.blocks[BlockFamily.Type.BRICKS]!!.get().asItem() },
                1
              )
              RecipePresets(generator.registrate, p).simpleStonecuttingRecipe(
                { c.get() },
                { mainBlock!!.get().asItem() },
                1
              )
              RecipePresets(generator.registrate, p).stairsCraftingRecipe({ c.get() }) {
                Ingredient.of(_blockFamily.blocks[BlockFamily.Type.BRICKS]!!.get().asItem())
              }
            }
            .register()
        }
      }

      if (!_denyList.contains(BlockFamily.Type.BRICK_SLAB)) {
        _blockFamily.setVariant(BlockFamily.Type.BRICK_SLAB) {
          generator.createSlab("${_name}_brick", "${_name}_bricks")
            .fromFamily(_copyFrom, _sharedProps, _color, _toolType, _toolTier)
            .itemTags(listOf(MATERIAL_TAG))
            .recipe { c, p ->
              RecipePresets(generator.registrate, p).simpleStonecuttingRecipe(
                { c.get() },
                { _blockFamily.blocks[BlockFamily.Type.POLISHED]!!.get().asItem() },
                2
              )
              RecipePresets(generator.registrate, p).simpleStonecuttingRecipe(
                { c.get() },
                { _blockFamily.blocks[BlockFamily.Type.BRICKS]!!.get().asItem() },
                2
              )
              RecipePresets(generator.registrate, p).simpleStonecuttingRecipe(
                { c.get() },
                { mainBlock!!.get().asItem() },
                2
              )
              RecipePresets(generator.registrate, p).slabCraftingRecipe({ c.get() }) {
                Ingredient.of(_blockFamily.blocks[BlockFamily.Type.BRICKS]!!.get().asItem())
              }
            }
            .register()
        }
      }

      if (!_denyList.contains(BlockFamily.Type.BRICK_WALL)) {
        _blockFamily.setVariant(BlockFamily.Type.BRICK_WALL) {
          generator.createWall("${_name}_brick", "${_name}_bricks")
            .fromFamily(_copyFrom, _sharedProps, _color, _toolType, _toolTier)
            .itemTags(listOf(MATERIAL_TAG))
            .recipe { c, p ->
              RecipePresets(generator.registrate, p).simpleStonecuttingRecipe(
                { c.get() },
                { _blockFamily.blocks[BlockFamily.Type.POLISHED]!!.get().asItem() },
                1
              )
              RecipePresets(generator.registrate, p).simpleStonecuttingRecipe(
                { c.get() },
                { _blockFamily.blocks[BlockFamily.Type.BRICKS]!!.get().asItem() },
                1
              )
              RecipePresets(generator.registrate, p).simpleStonecuttingRecipe(
                { c.get() },
                { mainBlock!!.get().asItem() },
                1
              )
              RecipePresets(generator.registrate, p).wallCraftingRecipe({ c.get() }) {
                Ingredient.of(_blockFamily.blocks[BlockFamily.Type.BRICKS]!!.get().asItem())
              }
            }
            .register()
        }
      }
    }
    // start chiseled chain
    if (!_denyList.contains(BlockFamily.Type.CHISELED)) {
      _blockFamily.setVariant(BlockFamily.Type.CHISELED) {
        generator.create<Block>("chiseled_$_name")
          .fromFamily(_copyFrom, _sharedProps, _color, _toolType, _toolTier)
          .itemTags(listOf(MATERIAL_TAG))
          .recipe { c, p ->
            RecipePresets(generator.registrate, p).simpleStonecuttingRecipe(
              { c.get() },
              { mainBlock!!.get().asItem() },
              1
            )
            RecipePresets(generator.registrate, p).simpleStonecuttingRecipe(
              { c.get() },
              { _blockFamily.blocks[BlockFamily.Type.POLISHED]!!.get().asItem() },
              1
            )
            RecipePresets(generator.registrate, p).slabToChiseledRecipe(
              { c.get() },
              { Ingredient.of(_blockFamily.blocks[BlockFamily.Type.SLAB]!!.get().asItem()) }
            )
          }
          .register()
      }
    }
    // PILLAR
    if (!_denyList.contains(BlockFamily.Type.PILLAR)) {
      _blockFamily.setVariant(BlockFamily.Type.PILLAR) {
        generator.createRotatedPillar<RotatedPillarBlock>("${_name}_pillar")
          .fromFamily(_copyFrom, _sharedProps, _color, _toolType, _toolTier)
          .itemTags(listOf(MATERIAL_TAG))
          .recipe { c, p ->
            RecipePresets(generator.registrate, p).simpleStonecuttingRecipe(
              { c.get() },
              { mainBlock!!.get().asItem() },
              1
            )
            RecipePresets(generator.registrate, p).simpleStonecuttingRecipe(
              { c.get() },
              { _blockFamily.blocks[BlockFamily.Type.POLISHED]!!.get().asItem() },
              1
            )
            RecipePresets(generator.registrate, p).slabToChiseledRecipe(
              { c.get() },
              { Ingredient.of(mainBlock!!.get().asItem()) }
            )
          }
          .register()
      }
    }
  }
}