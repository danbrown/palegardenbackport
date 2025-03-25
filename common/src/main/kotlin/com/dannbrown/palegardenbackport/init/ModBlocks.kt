package com.dannbrown.palegardenbackport.init

import com.dannbrown.deltaboxlib.content.worldgen.tree.DeltaboxTreeGrower
import com.dannbrown.deltaboxlib.registrate.presets.family.BlockFamily
import com.dannbrown.deltaboxlib.content.block.FallingLeavesBlock
import com.dannbrown.deltaboxlib.registrate.registry.BlockEntry
import com.dannbrown.deltaboxlib.registrate.util.DataIngredient
import com.dannbrown.deltaboxlib.registrate.util.DeltaboxUtil
import com.dannbrown.palegardenbackport.content.blocks.*
import com.dannbrown.palegardenbackport.content.blocks.creakingHeart.CreakingHeartBlock
import com.dannbrown.palegardenbackport.content.blocks.eyeblossom.EyeBlossomBlock
import com.dannbrown.palegardenbackport.content.particle.PaleOakParticleOption
import com.dannbrown.palegardenbackport.content.presets.BlockstatePresets
import com.dannbrown.palegardenbackport.init.ModContent.MOD_ID
import com.dannbrown.palegardenbackport.init.ModContent.REGISTRATE
import net.minecraft.advancements.critereon.StatePropertiesPredicate
import net.minecraft.tags.BlockTags
import net.minecraft.tags.ItemTags
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.FlowerPotBlock
import net.minecraft.world.level.block.SoundType
import net.minecraft.world.level.block.state.BlockBehaviour.OffsetType
import net.minecraft.world.level.block.state.properties.BlockStateProperties
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument
import net.minecraft.world.level.material.MapColor
import net.minecraft.world.level.material.PushReaction
import net.minecraft.world.level.storage.loot.predicates.LootItemBlockStatePropertyCondition
import java.util.Optional
import java.util.function.Supplier

object ModBlocks {
  val CREAKING_HEART = REGISTRATE.block<CreakingHeartBlock>("creaking_heart")
    .copyFrom { Blocks.OAK_LOG }
    .color(MapColor.COLOR_ORANGE)
    .factory { c, p -> CreakingHeartBlock(p) }
    .properties { c, p ->
      p
        .sound(ModSounds.CREAKING_HEART_SOUNDS.get())
        .instrument(NoteBlockInstrument.BASEDRUM)
        .strength(10.0F)
        .randomTicks()
    }
    .toolAndTier(BlockTags.MINEABLE_WITH_AXE, null)
    .loot { g, b -> g.dropSelfSilkOtherLoot(b.get()) }
    .recipe { c, p ->
      c.simpleShapedRecipe(
        { p.get() },
        arrayOf("Y", "X", "Y"),
        mapOf(
          'X' to Supplier { DataIngredient(BLOCK_OF_RESIN.get()) },
          'Y' to Supplier { DataIngredient(PALE_OAK.blockFamily.blocks[BlockFamily.Type.LOG]!!.get()) }),
        1, "_from_resin_block"
      )
    }
    .blockstate(BlockstatePresets.creakingHeart())
    .register()


  val PALE_OAK_GROWER: DeltaboxTreeGrower = DeltaboxTreeGrower(
    "pale_oak",
    Optional.of(ModConfiguredFeatures.PALE_OAK_TREE),
    Optional.empty(),
    Optional.empty(),
  )
  val PALE_OAK_SET = REGISTRATE.blockSet("pale_oak")
  val PALE_OAK_WOOD_TYPE = REGISTRATE.woodType("pale_oak", PALE_OAK_SET)
  val PALE_OAK = REGISTRATE.blockfamily("pale_oak")
    .color(MapColor.SNOW, MapColor.COLOR_GRAY)
    .copyFrom { Blocks.OAK_LOG }
    .toolAndTier(BlockTags.MINEABLE_WITH_AXE, null)
    .denyList(BlockFamily.Type.LEAVES)
    .woodFamily(PALE_OAK_WOOD_TYPE, PALE_OAK_SET, PALE_OAK_GROWER)

  val PALE_OAK_LEAVES = REGISTRATE.blockPreset<FallingLeavesBlock>("pale_oak")
    .leaves({ PALE_OAK.blockFamily.blocks[BlockFamily.Type.SAPLING]!!.get() })
    .factory { c, p -> FallingLeavesBlock(p) { PaleOakParticleOption() } }
    .register()

  val PALE_MOSS_BLOCK = REGISTRATE.block<PaleMossBlock>("pale_moss_block")
    .copyFrom { Blocks.MOSS_BLOCK }
    .factory { c, p -> PaleMossBlock(p) }
    .color(MapColor.SNOW)
    .blockTags(
      BlockTags.DIRT,
      BlockTags.MOSS_REPLACEABLE,
      BlockTags.SNIFFER_DIGGABLE_BLOCK,
      BlockTags.SMALL_DRIPLEAF_PLACEABLE,
      BlockTags.SNIFFER_EGG_HATCH_BOOST
    )
    .compostable(0.65f)
    .itemTags(ItemTags.DIRT)
    .toolAndTier(BlockTags.MINEABLE_WITH_HOE, null, false)
    .properties { c, p -> p.strength(0.1F).sound(SoundType.MOSS).pushReaction(PushReaction.DESTROY) }
    .register()

  val PALE_MOSS_CARPET_BLOCK = REGISTRATE.block<PaleMossCarpetBlock>("pale_moss_carpet")
    .copyFrom { Blocks.MOSS_CARPET }
    .factory { c, p -> PaleMossCarpetBlock(p) }
    .color(MapColor.SNOW)
    .blockTags(
      BlockTags.COMBINATION_STEP_SOUND_BLOCKS,
      BlockTags.MANGROVE_LOGS_CAN_GROW_THROUGH,
      BlockTags.MANGROVE_ROOTS_CAN_GROW_THROUGH,
      BlockTags.SWORD_EFFICIENT
    )
    .toolAndTier(BlockTags.MINEABLE_WITH_HOE, null, false)
    .properties { c, p ->
      p.strength(0.1F).sound(SoundType.MOSS_CARPET).pushReaction(PushReaction.DESTROY).instabreak().noOcclusion()
    }
    .blockstate(BlockstatePresets.paleMossCarpetBlock())
    .cutoutRender()
    .compostable(0.3f)
    .loot { g, b ->
      g.add(
        b.get(), g.createSecondaryDispatchTable(
          b.get(), null, LootItemBlockStatePropertyCondition.hasBlockStateProperties(b.get())
            .setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(BlockStateProperties.BOTTOM, true))
        )
      )
    }
    .recipe { c, p ->
      c.simpleShapedRecipe(
        { p.get() },
        arrayOf("SS"),
        mapOf('S' to Supplier { DataIngredient(PALE_MOSS_BLOCK.get()) }),
        3
      )
    }
    .register()

  val PALE_HANGING_MOSS_PLANT: BlockEntry<PaleVinePlantBlock> =
    REGISTRATE.block<PaleVinePlantBlock>("pale_hanging_moss_plant")
      .copyFrom { Blocks.WEEPING_VINES_PLANT }
      .factory { c, p -> PaleVinePlantBlock({ PALE_HANGING_MOSS.get() }, p) }
      .color(MapColor.SNOW)
      .properties { c, p ->
        p.randomTicks().noCollission().instabreak().sound(SoundType.WEEPING_VINES).pushReaction(PushReaction.DESTROY)
      }
      .blockstate { g, b -> g.crossBlock(b.get(), "pale_hanging_moss") }
      .toolAndTier(BlockTags.MINEABLE_WITH_HOE, null, false)
      .cutoutRender()
      .noItem()
      .loot { g, b -> g.dropSilkOtherLoot(b.get(), { PALE_HANGING_MOSS.get() }) }
      .register()

  val PALE_HANGING_MOSS: BlockEntry<PaleVineBlock> = REGISTRATE.block<PaleVineBlock>("pale_hanging_moss")
    .copyFrom { Blocks.WEEPING_VINES }
    .factory { c, p -> PaleVineBlock({ PALE_HANGING_MOSS_PLANT.get() }, p) }
    .color(MapColor.SNOW)
    .blockTags(BlockTags.SWORD_EFFICIENT)
    .toolAndTier(BlockTags.MINEABLE_WITH_HOE, null, false)
    .properties { c, p ->
      p.randomTicks().noCollission().instabreak().sound(SoundType.WEEPING_VINES).pushReaction(PushReaction.DESTROY)
    }
    .blockstate { g, b -> g.crossBlock(b.get(), "pale_hanging_moss_tip") }
    .cutoutRender()
    .compostable(0.3f)
    .item()
    .model { g, i -> g.flatItemBlock(i.get(), "pale_hanging_moss") }
    .build()
    .loot { g, b -> g.dropSelfSilkOtherLoot(b.get()) }
    .register() as BlockEntry<PaleVineBlock>

  // Eye blossom
  val EYE_BLOSSOM: BlockEntry<EyeBlossomBlock> = REGISTRATE.block<EyeBlossomBlock>("open_eyeblossom")
    .factory { c, p -> EyeBlossomBlock(true, p) }
    .copyFrom { Blocks.POPPY }
    .color(MapColor.PLANT)
    .properties { c, p ->
      p.noCollission().instabreak().sound(SoundType.GRASS).offsetType(OffsetType.XZ).pushReaction(PushReaction.DESTROY)
        .randomTicks()
    }
    .blockstate { g, b -> g.crossBlock(b.get(), "open_eyeblossom") }
    .blockTags(BlockTags.FLOWERS)
    .itemTags(ItemTags.FLOWERS)
    .cutoutRender()
    .compostable(0.65f)
    .item()
    .model { g, i -> g.flatItemBlock(i.get(), "open_eyeblossom_item") }
    .build()
    .register() as BlockEntry<EyeBlossomBlock>
  val CLOSED_EYE_BLOSSOM: BlockEntry<EyeBlossomBlock> = REGISTRATE.block<EyeBlossomBlock>("closed_eyeblossom")
    .factory { c, p -> EyeBlossomBlock(false, p) }
    .copyFrom { Blocks.POPPY }
    .color(MapColor.PLANT)
    .properties { c, p ->
      p.noCollission().instabreak().sound(SoundType.GRASS).offsetType(OffsetType.XZ).pushReaction(PushReaction.DESTROY)
        .randomTicks()
    }
    .blockstate { g, b -> g.crossBlock(b.get(), "closed_eyeblossom") }
    .blockTags(BlockTags.FLOWERS)
    .itemTags(ItemTags.FLOWERS)
    .cutoutRender()
    .compostable(0.65f)
    .item()
    .model { g, i -> g.flatItemBlock(i.get(), "closed_eyeblossom") }
    .build()
    .register() as BlockEntry<EyeBlossomBlock>

  val POTTED_EYE_BLOSSOM: BlockEntry<FlowerPotBlock> = REGISTRATE.blockPreset<FlowerPotBlock>("open_eyeblossom")
    .pottedBlock({ EYE_BLOSSOM.get() })
    .register()
  val POTTED_CLOSED_EYE_BLOSSOM: BlockEntry<FlowerPotBlock> =
    REGISTRATE.blockPreset<FlowerPotBlock>("closed_eyeblossom")
      .pottedBlock({ CLOSED_EYE_BLOSSOM.get() })
      .register()

  val RESIN_CLUMP = REGISTRATE.block<ResinClumpBlock>("resin_clump")
    .factory { c, p -> ResinClumpBlock(p) }
    .copyFrom { Blocks.OAK_PLANKS }
    .properties { c, p ->
      p
        .strength(0.1F)
        .sound(ModSounds.BLOCK_OF_RESIN_SOUNDS.get())
        .pushReaction(PushReaction.DESTROY)
        .noCollission()
        .noOcclusion()
        .instabreak()
    }
    .color(MapColor.COLOR_ORANGE)
    .cutoutRender()
    .blockstate(BlockstatePresets.resinClump())
    .itemTags(ItemTags.TRIM_MATERIALS)
    .toolAndTier(BlockTags.MINEABLE_WITH_PICKAXE, null, false)
    .loot { lt, b -> lt.dropItself(b.get()) }
    .item()
    .model { g, i -> g.flatItem(i.get(), "resin_clump") }
    .build()
    .register() as BlockEntry<ResinClumpBlock>

  val BLOCK_OF_RESIN = REGISTRATE.blockPreset<Block>("block_of_resin")
    .storageBlock({ RESIN_CLUMP.get() }, { DataIngredient(RESIN_CLUMP.get()) }, "")
    .copyFrom { Blocks.WHITE_CARPET }
    .properties { c, p ->
      p
        .instabreak()
        .sound(ModSounds.BLOCK_OF_RESIN_SOUNDS.get())
        .instabreak()
    }
    .toolAndTier(BlockTags.MINEABLE_WITH_PICKAXE, null, false)
    .color(MapColor.COLOR_ORANGE)
    .cutoutRender()
    .itemTags(DeltaboxUtil.TAGS.modItemTag(MOD_ID, "resin_blocks"))
    .loot { lt, b -> lt.dropItself(b.get()) }
    .register()

  val RESIN_BRICKS = REGISTRATE.blockfamily("resin")
    .color(MapColor.COLOR_ORANGE, MapColor.COLOR_ORANGE)
    .sharedProps { c, p -> p.sound(ModSounds.RESIN_BRICK_SOUNDS.get()) }
    .copyFrom { Blocks.BRICKS }
    .toolAndTier(BlockTags.MINEABLE_WITH_PICKAXE, null, false)
    .bricksFamily { ModItems.RESIN_BRICK.get() }

  val CHISELED_RESIN_BRICKS = REGISTRATE.block<Block>("chiseled_resin_bricks")
    .itemTags(DeltaboxUtil.TAGS.modItemTag(MOD_ID, "resin_blocks"))
    .properties { c, p -> p.sound(ModSounds.RESIN_BRICK_SOUNDS.get()) }
    .copyFrom { Blocks.BRICKS }
    .color(MapColor.COLOR_ORANGE)
    .toolAndTier(BlockTags.MINEABLE_WITH_PICKAXE, null, false)
    .recipe { c, p ->
      c.slabToChiseledRecipe({ p.get() }, { DataIngredient(RESIN_BRICKS.blocks[BlockFamily.Type.BRICK_SLAB]!!.get()) })
      c.simpleStonecuttingRecipe({ p.get() }, { RESIN_BRICKS.blocks[BlockFamily.Type.BRICKS]!!.get() }, 1)
    }
    .loot { lt, b -> lt.dropSelf(b.get()) }
    .register()

  fun register() {
    // init
  }
}