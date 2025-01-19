package com.dannbrown.deltaboxlib.platform.registrate.generators.block

import com.dannbrown.deltaboxlib.platform.registrate.transformers.BlockLootPresets
import com.dannbrown.deltaboxlib.platform.registrate.transformers.BlockstatePresets
import com.dannbrown.deltaboxlib.platform.registrate.transformers.ItemModelPresets
import com.dannbrown.deltaboxlib.registry.transformers.BlockTagPresets
import net.minecraft.tags.BlockTags
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.ButtonBlock
import net.minecraft.world.level.block.DoorBlock
import net.minecraft.world.level.block.FenceBlock
import net.minecraft.world.level.block.FenceGateBlock
import net.minecraft.world.level.block.PressurePlateBlock
import net.minecraft.world.level.block.RotatedPillarBlock
import net.minecraft.world.level.block.SlabBlock
import net.minecraft.world.level.block.SoundType
import net.minecraft.world.level.block.StairBlock
import net.minecraft.world.level.block.TrapDoorBlock
import net.minecraft.world.level.block.WallBlock
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.block.state.properties.BlockSetType
import net.minecraft.world.level.block.state.properties.WoodType
import java.util.function.Supplier

class CommonBlockPreset(
  val _name: String,
) : IBlockBuilderPreset<Block>() {
  override fun create(generator: BlockGenerator): BlockGeneratorBuilder<Block> {
    return generator.create(_name)
  }

  //  // @ BOTTOM TOP BLOCK
  //  fun bottomTopBlock(bottomName: String = "", topName: String = "", sideName: String = ""): BlockGen<T> {
  //    this.checkCurrentBuilder()
  //    _blockFactory = { p -> Block(p) as T }
  //    _copyFrom = Supplier {Blocks.SANDSTONE }
  //    addBuilder { b ->
  //      b.blockstate(BlockstatePresets.cubeBottomTopBlock(sideName.ifEmpty { _textureName }, bottomName.ifEmpty { _textureName + "_bottom" }, topName.ifEmpty { _textureName + "_top" }))
  //    }
  //    return this
  //  }

  fun <T : Block> createBottomTop(
    generator: BlockGenerator,
    bottomName: String = "",
    topName: String = "",
    sideName: String = ""
  ): BlockGeneratorBuilder<T> {
    val bottomTextureName = bottomName.ifEmpty { "${_name}_bottom" }
    val topTextureName = topName.ifEmpty { "${_name}_top" }
    val sideTextureName = sideName.ifEmpty { _name }
    return generator
      .create<T>(_name)
      .blockstate(BlockstatePresets.cubeBottomTopBlock(sideTextureName, bottomTextureName, topTextureName))
  }

  //  /**
  //   * Changes the blockstate and model to be a rotated pillar
  //   */
  //  fun rotatedPillarBlock(topTexture: String = "", sideTexture: String = ""): BlockGen<T> {
  //    this.checkCurrentBuilder()
  //    _blockFactory = { p -> RotatedPillarBlock(p) as T }
  //    addBuilder { b ->
  //      b.blockstate { c, p ->
  //        val topTexture = p.modLoc("block/${topTexture.ifEmpty { c.name + "_top" }}")
  //        val sideTexture = p.modLoc("block/${sideTexture.ifEmpty { c.name }}")
  //
  //        p.axisBlock(c.get() as RotatedPillarBlock, sideTexture, topTexture)
  //      }
  //    }
  //    return this
  //  }

  fun createRotatedPillar(
    generator: BlockGenerator,
    _topTexture: String = "",
    _sideTexture: String = ""
  ): BlockGeneratorBuilder<RotatedPillarBlock> {
    val topTextureName = _topTexture.ifEmpty { "${_name}_top" }
    val sideTextureName = _sideTexture.ifEmpty { _name }
    return generator
      .create<RotatedPillarBlock>(_name)
      .blockFactory { p -> RotatedPillarBlock(p) }
      .blockstate { c, p ->
        val topTexture = p.modLoc("block/$topTextureName")
        val sideTexture = p.modLoc("block/$sideTextureName")
        p.axisBlock(c.get() as RotatedPillarBlock, sideTexture, topTexture)
      }
  }


//  // @ STAIRS
//  fun stairsBlock(referenceBlockState: Supplier<BlockState>, bottomTop: Boolean = false, isWooden: Boolean = false, addSuffix: Boolean = true
//  ): BlockGen<T> {
//    this.checkCurrentBuilder()
//    _blockFactory = { p -> StairBlock(referenceBlockState, p) as T }
//    _copyFrom = Supplier {Blocks.OAK_STAIRS }
//    if (addSuffix) _suffix += "_stairs"
//    addBuilder { b ->
//      b.blockstate(if (bottomTop) BlockstatePresets.bottomTopStairsBlock(_textureName) else BlockstatePresets.stairsBlock(_textureName))
//        .tag(*(if (isWooden) BlockTagPresets.woodenStairsTags().first else BlockTagPresets.stairsTags().first))
//        .item()
//        .tag(*(if (isWooden) BlockTagPresets.woodenStairsTags().second else BlockTagPresets.stairsTags().second))
//        .model(ItemModelPresets.simpleBlockItem())
//        .build()
//    }
//    return this
//  }
//
//  // @ WALL
//  fun wallBlock(bottomTop: Boolean = false, addSuffix: Boolean = true
//  ): BlockGen<T> {
//    this.checkCurrentBuilder()
//    _blockFactory = { p -> WallBlock(p) as T }
//    _copyFrom = Supplier {Blocks.COBBLESTONE_WALL }
//    if (addSuffix) _suffix += "_wall"
//    addBuilder { b ->
//      b.blockstate(if (bottomTop) BlockstatePresets.bottomTopWallBlock(_textureName) else BlockstatePresets.wallBlock(_textureName))
//        .tag(*BlockTagPresets.wallTags().first)
//        .item()
//        .tag(*BlockTagPresets.wallTags().second)
//        .model(if (bottomTop) ItemModelPresets.bottomTopWallItem(_textureName) else ItemModelPresets.wallItem(_textureName))
//        .build()
//    }
//    return this
//  }
//

  fun createStairs(
    generator: BlockGenerator,
    textureName: String,
    referenceBlockState: Supplier<BlockState>,
    bottomTop: Boolean = false,
    isWooden: Boolean = false,
    addSuffix: Boolean = true
  ): BlockGeneratorBuilder<StairBlock> {
    val nameWithSuffix = if (addSuffix) this._name + "_stairs" else _name
    return generator
      .create<StairBlock>(nameWithSuffix)
      .blockFactory { p -> StairBlock(referenceBlockState, p) }
      .copyFrom { if (isWooden) Blocks.OAK_STAIRS else Blocks.COBBLESTONE_STAIRS }
      .blockstate(
        if (bottomTop) BlockstatePresets.bottomTopStairsBlock(textureName) else BlockstatePresets.stairsBlock(
          textureName
        )
      )
      .loot(BlockLootPresets.dropItselfLoot())
      .transform { t ->
        t
          .tag(*(if (isWooden) BlockTagPresets.woodenStairsTags().first.toTypedArray() else BlockTagPresets.stairsTags().first.toTypedArray()))
          .item()
          .model(ItemModelPresets.simpleBlockItem(nameWithSuffix))
          .tag(*(if (isWooden) BlockTagPresets.woodenStairsTags().second.toTypedArray() else BlockTagPresets.stairsTags().second.toTypedArray()))
          .build()
      }
  }

  //
  //  // @ SLAB
  //  fun slabBlock(bottomTop: Boolean = false, isWooden: Boolean = false, addSuffix: Boolean = true
  //  ): BlockGen<T> {
  //    this.checkCurrentBuilder()
  //    _blockFactory = { p -> SlabBlock(p) as T }
  //    _copyFrom = Supplier {Blocks.OAK_SLAB }
  //    if (addSuffix) _suffix += "_slab"
  //    addBuilder { b ->
  //      b.blockstate(if (bottomTop) BlockstatePresets.bottomTopSlabBlock(_textureName) else BlockstatePresets.slabBlock(_textureName))
  //        .tag(*(if (isWooden) BlockTagPresets.woodenSlabTags().first else BlockTagPresets.slabTags().first))
  //        .item()
  //        .tag(*(if (isWooden) BlockTagPresets.woodenSlabTags().second else BlockTagPresets.slabTags().second))
  //        .model(ItemModelPresets.simpleBlockItem())
  //        .build()
  //    }
  //    return this
  //  }
  //

  fun createSlab(
    generator: BlockGenerator,
    textureName: String,
    bottomTop: Boolean = false,
    isWooden: Boolean = false,
    addSuffix: Boolean = true
  ): BlockGeneratorBuilder<SlabBlock> {
    val nameWithSuffix = if (addSuffix) this._name + "_slab" else _name
    return generator
      .create<SlabBlock>(nameWithSuffix)
      .blockFactory { p -> SlabBlock(p) }
      .copyFrom { if (isWooden) Blocks.OAK_SLAB else Blocks.COBBLESTONE_SLAB }
      .blockstate(
        if (bottomTop) BlockstatePresets.bottomTopSlabBlock(textureName) else BlockstatePresets.slabBlock(
          textureName
        )
      )
      .loot(BlockLootPresets.dropSlab())
      .transform { t ->
        t
          .tag(*(if (isWooden) BlockTagPresets.woodenSlabTags().first.toTypedArray() else BlockTagPresets.slabTags().first.toTypedArray()))
          .item()
          .model(ItemModelPresets.simpleBlockItem(nameWithSuffix))
          .tag(*(if (isWooden) BlockTagPresets.woodenSlabTags().second.toTypedArray() else BlockTagPresets.slabTags().second.toTypedArray()))
          .build()
      }
  }

  fun createWall(
    generator: BlockGenerator,
    textureName: String,
    bottomTop: Boolean = false,
    addSuffix: Boolean = true
  ): BlockGeneratorBuilder<WallBlock> {
    val nameWithSuffix = if (addSuffix) this._name + "_wall" else _name
    return generator
      .create<WallBlock>(nameWithSuffix)
      .blockFactory { p -> WallBlock(p) }
      .blockstate(
        if (bottomTop) BlockstatePresets.bottomTopWallBlock(textureName) else BlockstatePresets.wallBlock(
          textureName
        )
      )
      .loot(BlockLootPresets.dropItselfLoot())
      .transform { t ->
        t
          .tag(*BlockTagPresets.wallTags().first.toTypedArray())
          .item()
          .model(
            if (bottomTop) ItemModelPresets.bottomTopWallItem(textureName) else ItemModelPresets.wallItem(
              textureName
            )
          )
          .tag(*BlockTagPresets.wallTags().second.toTypedArray())
          .build()
      }
  }

  fun createFence(
    generator: BlockGenerator,
    textureName: String,
    isWooden: Boolean = false,
    addSuffix: Boolean = true
  ): BlockGeneratorBuilder<FenceBlock> {
    val nameWithSuffix = if (addSuffix) this._name + "_fence" else _name
    return generator
      .create<FenceBlock>(nameWithSuffix)
      .blockFactory { p -> FenceBlock(p) }
      .copyFrom { if (isWooden) Blocks.OAK_FENCE else Blocks.NETHER_BRICK_FENCE }
      .blockstate(BlockstatePresets.fenceBlock(textureName))
      .loot(BlockLootPresets.dropItselfLoot())
      .transform { t ->
        t
          .tag(*BlockTagPresets.fenceTags(isWooden).first.toTypedArray())
          .item()
          .model(ItemModelPresets.fenceItem(textureName))
          .tag(*BlockTagPresets.fenceTags(isWooden).second.toTypedArray())
          .build()
      }
  }

  // @ FENCE
  /**
   * Changes the blockstate and model to be a fence block
   */
//  fun fenceBlock(isWooden: Boolean = false, addSuffix: Boolean = true
//  ): BlockGen<T> {
//    this.checkCurrentBuilder()
//    _blockFactory = { p -> FenceBlock(p) as T }
//    _copyFrom = Supplier {Blocks.OAK_FENCE }
//    if (addSuffix) _suffix += "_fence"
//
//    addBuilder { b ->
//      b.blockstate(BlockstatePresets.fenceBlock(_textureName))
//        .tag(*BlockTagPresets.fenceTags(isWooden).first)
//        .item()
//        .tag(*BlockTagPresets.fenceTags(isWooden).second)
//        .model(ItemModelPresets.fenceItem(_textureName))
//        .build()
//    }
//    return this
//  } // @ FENCE GATE

  fun createFenceGate(
    generator: BlockGenerator,
    textureName: String,
    woodType: WoodType,
    addSuffix: Boolean = true
  ): BlockGeneratorBuilder<FenceGateBlock> {
    val nameWithSuffix = if (addSuffix) this._name + "_fence_gate" else _name
    return generator
      .create<FenceGateBlock>(nameWithSuffix)
      .blockFactory { p -> FenceGateBlock(p, woodType) }
      .copyFrom { Blocks.OAK_FENCE_GATE }
      .blockstate(BlockstatePresets.fenceGateBlock(textureName))
      .loot(BlockLootPresets.dropItselfLoot())
      .transform { t ->
        t.tag(BlockTags.FENCE_GATES)
      }
  }

//  /**
//   * Changes the blockstate and model to be a fence gate block
//   */
//  fun fenceGateBlock(woodType: WoodType, addSuffix: Boolean = true
//  ): BlockGen<T> {
//    this.checkCurrentBuilder()
//    _blockFactory = { p -> FenceGateBlock(p, woodType) as T }
//    _copyFrom = Supplier {Blocks.OAK_FENCE_GATE }
//    if (addSuffix) _suffix += "_fence_gate"
//    addBuilder { b ->
//      b.blockstate(BlockstatePresets.fenceGateBlock(_textureName))
//        .tag(BlockTags.FENCE_GATES)
//    }
//    return this
//  } // @ PRESSURE PLATE
//

  fun createPressurePlate(
    generator: BlockGenerator,
    textureName: String,
    blockSetType: BlockSetType,
    isWooden: Boolean = true,
    addSuffix: Boolean = true
  ): BlockGeneratorBuilder<PressurePlateBlock> {
    val nameWithSuffix = if (addSuffix) this._name + "_pressure_plate" else _name
    return generator
      .create<PressurePlateBlock>(nameWithSuffix)
      .blockFactory { p -> PressurePlateBlock(PressurePlateBlock.Sensitivity.EVERYTHING, p, blockSetType) }
      .copyFrom { if (isWooden) Blocks.OAK_PRESSURE_PLATE else Blocks.STONE_PRESSURE_PLATE }
      .blockstate(BlockstatePresets.pressurePlateBlock(textureName))
      .loot(BlockLootPresets.dropItselfLoot())
      .properties { p -> p.noCollission().strength(0.5F) }
      .transform { t ->
        t
          .tag(*BlockTagPresets.pressurePlateTags(isWooden).first.toTypedArray())
          .item()
          .model(ItemModelPresets.pressurePlateItem(textureName))
          .tag(*BlockTagPresets.pressurePlateTags(isWooden).second.toTypedArray())
          .build()
      }
  }
//  /**
//   * Changes the blockstate and model to be a pressure plate block
//   */
//  fun pressurePlateBlock(blockSetType: BlockSetType, isWooden: Boolean = true, addSuffix: Boolean = true
//  ): BlockGen<T> {
//    this.checkCurrentBuilder()
//    _blockFactory = { p -> PressurePlateBlock(PressurePlateBlock.Sensitivity.EVERYTHING, p, blockSetType) as T }
//    _copyFrom = Supplier {Blocks.OAK_PRESSURE_PLATE }
//    if (addSuffix) _suffix += "_pressure_plate"
//    addBuilder { b ->
//      b.blockstate(BlockstatePresets.pressurePlateBlock(_textureName))
//        .properties { p ->
//          p.noCollission()
//            .strength(0.5F)
//        }
//        .tag(*BlockTagPresets.pressurePlateTags(isWooden).first)
//        .item()
//        .tag(*BlockTagPresets.pressurePlateTags(isWooden).second)
//        .model(ItemModelPresets.pressurePlateItem(_textureName))
//        .build()
//    }
//    return this
//  }

  fun createButton(
    generator: BlockGenerator,
    textureName: String,
    blockSetType: BlockSetType,
    isWooden: Boolean = true,
    addSuffix: Boolean = true
  ): BlockGeneratorBuilder<ButtonBlock> {
    val nameWithSuffix = if (addSuffix) this._name + "_button" else _name
    return generator
      .create<ButtonBlock>(nameWithSuffix)
      .blockFactory { p -> ButtonBlock(p, blockSetType, 30, isWooden) }
      .copyFrom { if (isWooden) Blocks.OAK_BUTTON else Blocks.STONE_BUTTON }
      .properties { p -> p.noCollission().strength(0.5F) }
      .blockstate(BlockstatePresets.buttonBlock(textureName))
      .loot(BlockLootPresets.dropItselfLoot())
      .transform { t ->
        t
          .tag(*BlockTagPresets.buttonTags(isWooden).first.toTypedArray())
          .item()
          .model(ItemModelPresets.buttonItem(textureName))
          .tag(*BlockTagPresets.buttonTags(isWooden).second.toTypedArray())
          .build()
      }
  }

//  // @ BUTTON
//  /**
//   * Changes the blockstate and model to be a button block
//   */
//  fun buttonBlock(blockSetType: BlockSetType, isWooden: Boolean = true, addSuffix: Boolean = true
//  ): BlockGen<T> {
//    this.checkCurrentBuilder()
//    _blockFactory = { p -> ButtonBlock(p, blockSetType, 30, true) as T }
//    _copyFrom = Supplier {Blocks.OAK_BUTTON }
//    if (addSuffix) _suffix += "_button"
//    addBuilder { b ->
//      b.blockstate(BlockstatePresets.buttonBlock(_textureName))
//        .properties { p ->
//          p.noCollission()
//            .strength(0.5F)
//        }
//        .tag(*BlockTagPresets.buttonTags(isWooden).first)
//        .item()
//        .tag(*BlockTagPresets.buttonTags(isWooden).second)
//        .model(ItemModelPresets.buttonItem(_textureName))
//        .build()
//    }
//    return this
//  }


//  // Trapdoors
//  fun woodenTrapdoorBlock(ingredient: Supplier<DataIngredient>, blockSetType: BlockSetType, orientable: Boolean = true, addSuffix: Boolean = true): BlockGen<T> {
//    this.checkCurrentBuilder()
//    _blockFactory = { p -> TrapDoorBlock(p, blockSetType) as T }
//    _copyFrom = Supplier {Blocks.OAK_TRAPDOOR }
//    if (addSuffix) _suffix += "_trapdoor"
//
//    var tags =  BlockTagPresets.woodenTrapdoorTags()
//
//    addBuilder { b ->
//      b
//        .properties { p ->
//          p.sound(SoundType.WOOD)
//            .noOcclusion()
//        }
//        .blockstate(BlockstatePresets.trapdoorBlock(_textureName, orientable))
//        .tag(*tags.first)
//        .item()
//        .tag(*tags.second)
//        .model(ItemModelPresets.trapdoorItem(_textureName))
//        .build()
//        .recipe { c, p -> RecipePresets.trapdoorCraftingRecipe(c, p, ingredient) }
//
//    }
//    return this
//  }
//

  fun createWoodenTrapdoor(
    generator: BlockGenerator,
    blockSetType: BlockSetType,
    orientable: Boolean = true,
    addSuffix: Boolean = true
  ): BlockGeneratorBuilder<TrapDoorBlock> {
    val nameWithSuffix = if (addSuffix) "${_name}_trapdoor" else _name
    return generator
      .create<TrapDoorBlock>(nameWithSuffix)
      .blockFactory { p -> TrapDoorBlock(p, blockSetType) }
      .copyFrom { Blocks.OAK_TRAPDOOR }
      .blockstate(BlockstatePresets.trapdoorBlock(nameWithSuffix, orientable))
      .properties { p ->
        p.sound(SoundType.WOOD).noOcclusion()
      }
      .loot(BlockLootPresets.dropItselfLoot())
      .transform { t ->
        t
          .tag(*BlockTagPresets.woodenTrapdoorTags().first.toTypedArray())
          .item()
          .tag(*BlockTagPresets.woodenTrapdoorTags().second.toTypedArray())
          .model(ItemModelPresets.trapdoorItem(nameWithSuffix))
          .build()
      }
      .cutoutRender()
  }

  //  // DOOR
//  /**
//   * Changes the blockstate and model to be a door block
//   */
//  fun woodenDoorBlock(blockSetType: BlockSetType, isWooden: Boolean = true, addSuffix: Boolean = true
//  ): BlockGen<T> {
//    this.checkCurrentBuilder()
//    _blockFactory = { p -> DoorBlock(p, blockSetType) as T }
//    _copyFrom = Supplier {Blocks.OAK_PLANKS }
//    if (addSuffix) _suffix += "_door"
//    addBuilder { b ->
//      b.properties { p ->
//        p.strength(3.0F)
//          .sound(SoundType.WOOD)
//          .noOcclusion()
//      }
//        .blockstate(BlockstatePresets.doorTransparentBlock())
//        .tag(*BlockTagPresets.doorTags(isWooden).first)
//        .item()
//        .tag(*BlockTagPresets.doorTags(isWooden).second)
//        .model(ItemModelPresets.doorItem())
//        .build()
//    }
//    return this
//  }
  fun createDoor(
    generator: BlockGenerator,
    blockSetType: BlockSetType,
    isWooden: Boolean = true,
    addSuffix: Boolean = true
  ): BlockGeneratorBuilder<DoorBlock> {
    val nameWithSuffix = if (addSuffix) "${_name}_door" else _name
    return generator
      .create<DoorBlock>(nameWithSuffix)
      .blockFactory { p -> DoorBlock(p, blockSetType) }
      .copyFrom { if (isWooden) Blocks.OAK_DOOR else Blocks.IRON_DOOR }
      .blockstate(BlockstatePresets.doorTransparentBlock())
      .properties { p ->
        p.noOcclusion()
      }
      .loot(BlockLootPresets.doorLoot())
      .transform { t ->
        t
          .tag(*BlockTagPresets.doorTags(isWooden).first.toTypedArray())
          .item()
          .tag(*BlockTagPresets.doorTags(isWooden).second.toTypedArray())
          .model(ItemModelPresets.doorItem(nameWithSuffix))
          .build()
      }
      .cutoutRender()
  }
}