package com.dannbrown.deltaboxlib.platform.registrate.generators.family

import com.dannbrown.deltaboxlib.common.content.block.FlammableBlock
import com.dannbrown.deltaboxlib.common.content.block.FlammablePillarBlock
import com.dannbrown.deltaboxlib.common.content.block.GenericSaplingBlock
import com.dannbrown.deltaboxlib.common.content.block.StrippableFlammablePillarBlock
import com.dannbrown.deltaboxlib.common.content.worldgen.tree.DeltaboxTreeGrower
import com.dannbrown.deltaboxlib.platform.registrate.generators.block.BlockGenerator
import com.dannbrown.deltaboxlib.platform.registrate.generators.recipe.RecipePresets
import com.dannbrown.deltaboxlib.platform.util.DeltaboxUtil
import net.minecraft.core.BlockPos
import net.minecraft.tags.BlockTags
import net.minecraft.tags.ItemTags
import net.minecraft.tags.TagKey
import net.minecraft.world.item.crafting.Ingredient
import net.minecraft.world.level.BlockGetter
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.state.BlockBehaviour
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.block.state.properties.BlockSetType
import net.minecraft.world.level.block.state.properties.WoodType
import net.minecraft.world.level.material.MapColor
import java.util.function.Supplier

/**
 * Returns a wood block family
 */
class WoodBlockFamilySet(
  private val generator: BlockGenerator,
  private val _name: String,
  private val _sharedProps: (BlockBehaviour.Properties) -> BlockBehaviour.Properties = { p: BlockBehaviour.Properties -> p },
  private val _toolType: TagKey<Block>? = null,
  private val _toolTier: TagKey<Block>? = null,
  private val _color: MapColor? = null,
  private val _accentColor: MapColor? = null,
  private val _copyFrom: Supplier<Block> = Supplier { Blocks.STONE },
  private val _denyList: List<BlockFamily.Type> = mutableListOf(),
  woodType: WoodType,
  grower: DeltaboxTreeGrower,
  placeOn: ((blockState: BlockState, blockGetter: BlockGetter, blockPos: BlockPos) -> Boolean)? = null
): AbstractBlockFamilySet() {
  init{
    val LOG_TAG_BLOCK = DeltaboxUtil.TAGS.modBlockTag(generator.registrate.modid,_name + "_log_blocks")
    val LOG_TAG_ITEM = DeltaboxUtil.TAGS.modItemTag(generator.registrate.modid, _name + "_log_blocks")
    val FORGE_LEAVES_TAG_BLOCK = DeltaboxUtil.TAGS.modloaderBlockTag("leaves")
    val FORGE_LEAVES_TAG_ITEM = DeltaboxUtil.TAGS.modloaderItemTag("leaves")
    val FORGE_STRIPPED_LOGS_TAG_BLOCK = DeltaboxUtil.TAGS.modloaderBlockTag("stripped_logs")
    val FORGE_STRIPPED_LOGS_TAG_ITEM = DeltaboxUtil.TAGS.modloaderItemTag("stripped_logs")
    // Logs

    _blockFamily.setVariant(BlockFamily.Type.LOG) {
      generator.createRotatedPillar<StrippableFlammablePillarBlock>(_name + "_log")
        .blockFactory { p, c ->
          StrippableFlammablePillarBlock(
            p,
            c.strippedBlock!!,
            c.flammability!!.first,
            c.flammability.second
          )
        }
        .flammable()
        .strippable { _blockFamily.blocks[BlockFamily.Type.STRIPPED_LOG]!!.get() }
        .copyFrom { Blocks.OAK_LOG }
        .color(_color?: MapColor.WOOD)
        .toolAndTier(BlockTags.MINEABLE_WITH_AXE, null, false)
        .blockTags(listOf(BlockTags.LOGS, LOG_TAG_BLOCK, BlockTags.LOGS_THAT_BURN))
        .itemTags(listOf(ItemTags.LOGS, LOG_TAG_ITEM, ItemTags.LOGS_THAT_BURN))
        .register()
    }

    _blockFamily.setVariant(BlockFamily.Type.WOOD) {
      generator.createRotatedPillar<StrippableFlammablePillarBlock>(_name + "_wood", _name + "_log", _name + "_log")
        .blockFactory { p, c ->
          StrippableFlammablePillarBlock(
            p,
            c.strippedBlock!!,
            c.flammability!!.first,
            c.flammability.second
          )
        }
        .flammable()
        .strippable { _blockFamily.blocks[BlockFamily.Type.STRIPPED_WOOD]!!.get() }
        .copyFrom { Blocks.OAK_WOOD }
        .color(_color?: MapColor.WOOD)
        .toolAndTier(BlockTags.MINEABLE_WITH_AXE, null, false)
        .blockTags(listOf(BlockTags.LOGS, LOG_TAG_BLOCK, BlockTags.LOGS_THAT_BURN))
        .itemTags(listOf(ItemTags.LOGS, LOG_TAG_ITEM, ItemTags.LOGS_THAT_BURN))
        .recipe { c, p ->
          RecipePresets(generator.registrate, p).polishedCraftingRecipe({ c.get() }, { Ingredient.of(_blockFamily.blocks[BlockFamily.Type.LOG]!!.get()) }, 3)
        }
        .register()
    }
    // Stripped Logs
    _blockFamily.setVariant(BlockFamily.Type.STRIPPED_LOG) {
      generator.createRotatedPillar<FlammablePillarBlock>("stripped_$_name" + "_log")
        .blockFactory { p, c -> FlammablePillarBlock(p, c.flammability!!.first, c.flammability.second) }
        .flammable()
        .copyFrom { Blocks.STRIPPED_OAK_LOG }
        .color(_accentColor?: MapColor.WOOD)
        .toolAndTier(BlockTags.MINEABLE_WITH_AXE, null, false)
        .blockTags(listOf(BlockTags.LOGS, LOG_TAG_BLOCK, *FORGE_STRIPPED_LOGS_TAG_BLOCK.toTypedArray(), BlockTags.LOGS_THAT_BURN))
        .itemTags(listOf(ItemTags.LOGS, LOG_TAG_ITEM, *FORGE_STRIPPED_LOGS_TAG_ITEM.toTypedArray(), ItemTags.LOGS_THAT_BURN))
        .register()
    }

    _blockFamily.setVariant(BlockFamily.Type.STRIPPED_WOOD) {
      generator.createRotatedPillar<FlammablePillarBlock>("stripped_$_name" + "_wood", "stripped_$_name" + "_log", "stripped_$_name" + "_log")
        .blockFactory { p, c -> FlammablePillarBlock(p, c.flammability!!.first, c.flammability.second) }
        .flammable()
        .copyFrom { Blocks.STRIPPED_OAK_WOOD }
        .color(_accentColor?: MapColor.WOOD)
        .toolAndTier(BlockTags.MINEABLE_WITH_AXE, null, false)
        .blockTags(listOf(BlockTags.LOGS, LOG_TAG_BLOCK, *FORGE_STRIPPED_LOGS_TAG_BLOCK.toTypedArray(), BlockTags.LOGS_THAT_BURN))
        .itemTags(listOf(ItemTags.LOGS, LOG_TAG_ITEM, *FORGE_STRIPPED_LOGS_TAG_ITEM.toTypedArray(), ItemTags.LOGS_THAT_BURN))
        .recipe { c, p ->
          RecipePresets(generator.registrate, p).polishedCraftingRecipe({ c.get() }, { Ingredient.of(_blockFamily.blocks[BlockFamily.Type.STRIPPED_LOG]!!.get()) }, 3)
        }
        .register()
    }

    _blockFamily.setVariant(BlockFamily.Type.SAPLING) {
      generator.saplingBlock(_name, grower, placeOn).register()
    }

    _blockFamily.setVariant(BlockFamily.Type.POTTED_SAPLING) {
      generator.pottedBlock(_name, _blockFamily.blocks[BlockFamily.Type.SAPLING]!!, "_sapling").register()
    }

    if(!_denyList.contains(BlockFamily.Type.LEAVES)) {
        _blockFamily.setVariant(BlockFamily.Type.LEAVES) {
          generator.createLeavesBlock(_name, { _blockFamily.blocks[BlockFamily.Type.SAPLING]!!.get() as GenericSaplingBlock })
            .blockTags(listOf(BlockTags.LEAVES, *FORGE_LEAVES_TAG_BLOCK.toTypedArray(), BlockTags.MINEABLE_WITH_HOE))
            .itemTags(listOf(ItemTags.LEAVES, *FORGE_LEAVES_TAG_ITEM.toTypedArray()))
            .register()
        }
      }
    // Main Block
    _blockFamily.setVariant(BlockFamily.Type.MAIN) {
      generator.create<FlammableBlock>(_name + "_planks")
        .blockFactory { p, c -> FlammableBlock(p, c.flammability!!.first,c.flammability.second) }
        .flammable(20, 5)
        .copyFrom { Blocks.OAK_PLANKS }
        .toolAndTier(BlockTags.MINEABLE_WITH_AXE, null, false)
        .blockTags(listOf(BlockTags.PLANKS))
        .itemTags(listOf(ItemTags.PLANKS))
        .recipe { c, p ->
          RecipePresets(generator.registrate, p).directShapelessRecipe({ c.get() }, { Ingredient.of(_blockFamily.blocks[BlockFamily.Type.LOG]!!.get()) }, 4, "_from_log")
          RecipePresets(generator.registrate, p).directShapelessRecipe({ c.get() }, { Ingredient.of(_blockFamily.blocks[BlockFamily.Type.STRIPPED_LOG]!!.get()) }, 4, "_from_stripped_log")
          RecipePresets(generator.registrate, p).directShapelessRecipe({ c.get() }, { Ingredient.of(_blockFamily.blocks[BlockFamily.Type.WOOD]!!.get()) }, 4, "_from_wood")
          RecipePresets(generator.registrate, p).directShapelessRecipe({ c.get() }, { Ingredient.of(_blockFamily.blocks[BlockFamily.Type.STRIPPED_WOOD]!!.get()) }, 4, "_from_stripped_wood")
        }
        .register()
    }
    // Stairs
    _blockFamily.setVariant(BlockFamily.Type.STAIRS) {
      generator.createStairs(_name, _name + "_planks", false, true)
        .toolAndTier(BlockTags.MINEABLE_WITH_AXE, null, false)
        .color(_accentColor?: MapColor.WOOD)
        .blockTags(listOf(BlockTags.STAIRS, BlockTags.WOODEN_STAIRS))
        .itemTags(listOf(ItemTags.STAIRS, ItemTags.WOODEN_STAIRS))
        .recipe { c, p ->
          RecipePresets(generator.registrate, p).stairsCraftingRecipe({ c.get() }) {
            Ingredient.of(_blockFamily.blocks[BlockFamily.Type.MAIN]!!.get().asItem())
          }
        }
        .register()
    }
    // Slab
    _blockFamily.setVariant(BlockFamily.Type.SLAB) {
      generator.createSlab(_name, _name + "_planks", false, true)
        .toolAndTier(BlockTags.MINEABLE_WITH_AXE, null, false)
        .color(_accentColor?: MapColor.WOOD)
        .recipe { c, p ->
          RecipePresets(generator.registrate, p).slabCraftingRecipe({ c.get() }) {
            Ingredient.of(_blockFamily.blocks[BlockFamily.Type.MAIN]!!.get().asItem())
          }
        }
        .register()
    }
    // Fence
    _blockFamily.setVariant(BlockFamily.Type.FENCE) {
      generator.createFence(_name, _name + "_planks", false)
        .toolAndTier(BlockTags.MINEABLE_WITH_AXE, null, false)
        .color(_accentColor?: MapColor.WOOD)
        .recipe { c, p ->
          RecipePresets(generator.registrate, p).fenceCraftingRecipe({ c.get() }) {
            Ingredient.of(_blockFamily.blocks[BlockFamily.Type.MAIN]!!.get().asItem())
          }
        }
        .register()
    }
    // Fence Gate
    _blockFamily.setVariant(BlockFamily.Type.FENCE_GATE) {
      generator.createFenceGate(_name, _name + "_planks", woodType)
        .toolAndTier(BlockTags.MINEABLE_WITH_AXE, null, false)
        .color(_accentColor?: MapColor.WOOD)
        .recipe { c, p ->
          RecipePresets(generator.registrate, p).fenceGateCraftingRecipe({ c.get() }) {
            Ingredient.of(_blockFamily.blocks[BlockFamily.Type.MAIN]!!.get().asItem())
          }
        }
        .register()
    }
    // Pressure Plate
    _blockFamily.setVariant(BlockFamily.Type.PRESSURE_PLATE) {
      generator.createPressurePlate(_name, _name + "_planks", BlockSetType.OAK, true)
        .toolAndTier(BlockTags.MINEABLE_WITH_AXE, null, false)
        .color(_accentColor?: MapColor.WOOD)
        .recipe { c, p ->
          RecipePresets(generator.registrate, p).pressurePlateCraftingRecipe({ c.get() }) {
            Ingredient.of(_blockFamily.blocks[BlockFamily.Type.MAIN]!!.get().asItem())
          }
        }
        .register()
    }
    // Button
    _blockFamily.setVariant(BlockFamily.Type.BUTTON) {
      generator.createButton(_name, _name + "_planks", BlockSetType.OAK, true)
        .toolAndTier(BlockTags.MINEABLE_WITH_AXE, null, false)
        .color(_accentColor?: MapColor.WOOD)
        .recipe { c, p ->
          RecipePresets(generator.registrate, p).directShapelessRecipe({ c.get() }, { Ingredient.of(_blockFamily.blocks[BlockFamily.Type.MAIN]!!.get()) }, 1)
        }
        .register()
    }
    // Door
    _blockFamily.setVariant(BlockFamily.Type.DOOR) {
      generator.createDoor(_name, BlockSetType.OAK, true)
        .toolAndTier(BlockTags.MINEABLE_WITH_AXE, null, false)
        .color(_accentColor?: MapColor.WOOD)
        .recipe { c, p ->
          RecipePresets(generator.registrate, p).doorCraftingRecipe({ c.get() }) {
            Ingredient.of(_blockFamily.blocks[BlockFamily.Type.MAIN]!!.get().asItem())
          }
        }
        .register()
    }
    // Trapdoor
    _blockFamily.setVariant(BlockFamily.Type.TRAPDOOR) {
      generator.createWoodenTrapdoor(_name, BlockSetType.OAK, true)
        .toolAndTier(BlockTags.MINEABLE_WITH_AXE, null, false)
        .color(_accentColor?: MapColor.WOOD)
        .recipe { c, p ->
          RecipePresets(generator.registrate, p).trapdoorCraftingRecipe({ c.get() }) {
            Ingredient.of(_blockFamily.blocks[BlockFamily.Type.MAIN]!!.get().asItem())
          }
        }
        .register()
    }
//    // Wall Sign
//    _blockFamily.setVariant(BlockFamily.Type.WALL_SIGN) {
//      generator.create<WallSignBlock>(_name + "_wall_sign")
//        .copyFrom { Blocks.OAK_WALL_SIGN }
//        .blockFactory { p -> WallSignBlock(p, woodType) }
//        .properties { p -> p.strength(1.0F).sound(SoundType.WOOD).noOcclusion() }
//        .color(_accentColor?: MapColor.WOOD)
//        .blockTags(listOf(BlockTags.WALL_SIGNS, BlockTags.SIGNS))
//        .toolAndTier(BlockTags.MINEABLE_WITH_AXE, null, false)
//        .blockstate(BlockstatePresets.noBlockState())
//        .loot(BlockLootPresets.dropOtherLoot { _blockFamily.blocks[BlockFamily.Type.SIGN]!!.get() })
//        .noItem()
//        .transform { t ->
//          t
//            .lang { _ -> "block.${generator.registrate.modid}.${_name + "_wall_sign"}" }
//        }
//        .register()
//    }
//
//    // Sign
//    _blockFamily.setVariant(BlockFamily.Type.SIGN) {
//      generator.create<StandingSignBlock>(_name + "_sign")
//        .copyFrom { Blocks.OAK_SIGN }
//        .blockFactory { p -> StandingSignBlock(p, woodType) }
//        .properties { p-> p.strength(1.0F).sound(SoundType.WOOD).noOcclusion() }
//        .toolAndTier(BlockTags.MINEABLE_WITH_AXE, null, false)
//        .color(_accentColor?: MapColor.WOOD)
//        .blockTags(listOf(BlockTags.STANDING_SIGNS, BlockTags.SIGNS))
//        .itemTags(listOf(ItemTags.SIGNS))
//        .recipe { c, p ->
//          RecipePresets.signCraftingRecipe(c, p) { DataIngredient.items(_blockFamily.blocks[BlockFamily.Type.MAIN]!!.get().asItem()) }
//        }
//        .blockstate { c, p ->
//          val signModel: ModelFile = p.models().sign(c.name, p.modLoc("block/${_name + "_planks"}"))
//          p.simpleBlock(c.get() as StandingSignBlock, signModel)
//          p.simpleBlock(_blockFamily.blocks[BlockFamily.Type.WALL_SIGN]!!.get() as WallSignBlock, signModel)
//        }
//        .transform { b ->
//          b
//            .item { block, p -> GenericSignItem(p.stacksTo(16), block, _blockFamily.blocks[BlockFamily.Type.WALL_SIGN]!!.get()) }
//            .model { c, p ->
//              p.withExistingParent(c.name, p.mcLoc("item/generated"))
//                .texture("layer0", p.modLoc("item/${c.name}"))
//            }
//            .build()
//        }
//        .register()
//    }
//
//    // Hanging Wall Sign
//    _blockFamily.setVariant(BlockFamily.Type.WALL_HANGING_SIGN) {
//      generator.create<WallHangingSignBlock>(_name + "_hanging_wall_sign")
//        .copyFrom { Blocks.OAK_WALL_HANGING_SIGN }
//        .blockFactory { p -> WallHangingSignBlock(p, woodType) }
//        .properties { p -> p.strength(1.0F).sound(SoundType.WOOD).noOcclusion() }
//        .color(_accentColor?: MapColor.WOOD)
//        .blockTags(listOf(BlockTags.ALL_HANGING_SIGNS, BlockTags.WALL_HANGING_SIGNS))
//        .toolAndTier(BlockTags.MINEABLE_WITH_AXE, null, false)
//        .blockstate(BlockstatePresets.noBlockState())
//        .loot(BlockLootPresets.dropOtherLoot { _blockFamily.blocks[BlockFamily.Type.HANGING_SIGN]!!.get() })
//        .noItem()
//        .transform { t ->
//          t
//            .lang { _ -> "block.${generator.registrate.modid}.${_name + "_hanging_wall_sign"}" }
//        }
//        .register()
//    }
//
//    // Sign
//    _blockFamily.setVariant(BlockFamily.Type.HANGING_SIGN) {
//      generator.create<CeilingHangingSignBlock>(_name + "_hanging_sign")
//        .copyFrom { Blocks.OAK_HANGING_SIGN }
//        .blockFactory { p -> CeilingHangingSignBlock(p, woodType) }
//        .properties { p-> p.strength(1.0F).sound(SoundType.WOOD).noOcclusion() }
//        .toolAndTier(BlockTags.MINEABLE_WITH_AXE, null, false)
//        .color(_accentColor?: MapColor.WOOD)
//        .blockTags(listOf(BlockTags.ALL_HANGING_SIGNS, BlockTags.CEILING_HANGING_SIGNS))
//        .itemTags(listOf(ItemTags.HANGING_SIGNS))
//        .recipe { c, p ->
//          ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, c.get(), 6)
//            .define('C', Blocks.CHAIN)
//            .define('X', _blockFamily.blocks[BlockFamily.Type.STRIPPED_LOG]!!.get())
//            .pattern("C C")
//            .pattern("XXX")
//            .pattern("XXX")
//            .unlockedBy("has_" + p.safeName(_blockFamily.blocks[BlockFamily.Type.STRIPPED_LOG]!!.get()), RegistrateRecipeProvider.has(_blockFamily.blocks[BlockFamily.Type.STRIPPED_LOG]!!.get()))
//            .save(p, p.safeId(ResourceLocation(p.safeName(c.get()))))
//        }
//        .blockstate { c, p ->
//          val signModel: ModelFile = p.models().sign(c.name, p.modLoc("block/${_name + "_planks"}"))
//          p.simpleBlock(c.get() as CeilingHangingSignBlock, signModel)
//          p.simpleBlock(_blockFamily.blocks[BlockFamily.Type.WALL_HANGING_SIGN]!!.get() as WallHangingSignBlock, signModel)
//        }
//        .transform { b ->
//          b
//            .item { block, p -> GenericHangingSignItem(p.stacksTo(16), block, _blockFamily.blocks[BlockFamily.Type.WALL_HANGING_SIGN]!!.get()) }
//            .model { c, p ->
//              p.withExistingParent(c.name, p.mcLoc("item/generated"))
//                .texture("layer0", p.modLoc("item/${c.name}"))
//            }
//            .build()
//        }
//        .register()
//    }

    // DONE: BLOCK, LOG, STRIPPED LOG, WOOD, STRIPPED WOOD
    // DONE: STAIRS, SLAB, FENCE, FENCE GATE, BUTTON, PRESSURE PLATE
    // DONE: STALK, STRIPPED STALK, LEAVES, DOOR, TRAPDOOR, SIGN
    // TODO: SAPLING, WINDOW, WINDOW PANE, BOAT, CHEST BOAT
  }
}