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
import net.minecraft.world.level.block.state.properties.BlockSetType
import net.minecraft.world.level.block.state.properties.WoodType

class CommonBlockPreset(
  val _name: String,
) : IBlockBuilderPreset<Block>() {
  override fun create(generator: BlockGenerator): BlockGeneratorBuilder<Block> {
    return generator.create(_name)
  }
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

  fun <T : RotatedPillarBlock> createRotatedPillar(
    generator: BlockGenerator,
    _topTexture: String = "",
    _sideTexture: String = ""
  ): BlockGeneratorBuilder<T> {
    val topTextureName = _topTexture.ifEmpty { "${_name}_top" }
    val sideTextureName = _sideTexture.ifEmpty { _name }
    return generator
      .create<T>(_name)
      .blockFactory { p -> RotatedPillarBlock(p) as T }
      .blockstate { c, p ->
        val topTexture = p.modLoc("block/$topTextureName")
        val sideTexture = p.modLoc("block/$sideTextureName")
        p.axisBlock(c.get() as RotatedPillarBlock, sideTexture, topTexture)
      }
  }

  fun createStairs(
    generator: BlockGenerator,
    textureName: String,
    bottomTop: Boolean = false,
    isWooden: Boolean = false,
    addSuffix: Boolean = true
  ): BlockGeneratorBuilder<StairBlock> {
    val nameWithSuffix = if (addSuffix) this._name + "_stairs" else _name
    return generator
      .create<StairBlock>(nameWithSuffix)
      .blockFactory { p -> StairBlock(Blocks.STONE.defaultBlockState(), p) }
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

  fun createFenceGate(
    generator: BlockGenerator,
    textureName: String,
    woodType: WoodType,
    addSuffix: Boolean = true
  ): BlockGeneratorBuilder<FenceGateBlock> {
    val nameWithSuffix = if (addSuffix) this._name + "_fence_gate" else _name
    return generator
      .create<FenceGateBlock>(nameWithSuffix)
      /*? if >1.21 {*/
      /*.blockFactory { p -> FenceGateBlock(woodType, p) }
      *//*?} else {*/
      .blockFactory { p -> FenceGateBlock(p, woodType) }
      /*?}*/
      .copyFrom { Blocks.OAK_FENCE_GATE }
      .blockstate(BlockstatePresets.fenceGateBlock(textureName))
      .loot(BlockLootPresets.dropItselfLoot())
      .transform { t ->
        t.tag(BlockTags.FENCE_GATES)
      }
  }

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
      /*? if >1.21 {*/
      /*.blockFactory { p -> PressurePlateBlock(blockSetType, p) }
      *//*?} else {*/
      .blockFactory { p -> PressurePlateBlock(PressurePlateBlock.Sensitivity.EVERYTHING, p, blockSetType) }
      /*?}*/
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
      /*? if >1.21 {*/
      /*.blockFactory { p -> ButtonBlock(blockSetType, 30, p) }
      *//*?} else {*/
      .blockFactory { p -> ButtonBlock(p, blockSetType, 30, isWooden) }
      /*?}*/
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

  fun createWoodenTrapdoor(
    generator: BlockGenerator,
    blockSetType: BlockSetType,
    orientable: Boolean = true,
    addSuffix: Boolean = true
  ): BlockGeneratorBuilder<TrapDoorBlock> {
    val nameWithSuffix = if (addSuffix) "${_name}_trapdoor" else _name
    return generator
      .create<TrapDoorBlock>(nameWithSuffix)
      /*? if >1.21 {*/
      /*.blockFactory { p -> TrapDoorBlock(blockSetType, p) }
      *//*?} else {*/
      .blockFactory { p -> TrapDoorBlock(p, blockSetType) }
      /*?}*/
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

  fun createDoor(
    generator: BlockGenerator,
    blockSetType: BlockSetType,
    isWooden: Boolean = true,
    addSuffix: Boolean = true
  ): BlockGeneratorBuilder<DoorBlock> {
    val nameWithSuffix = if (addSuffix) "${_name}_door" else _name
    return generator
      .create<DoorBlock>(nameWithSuffix)
      /*? if >1.21 {*/
      /*.blockFactory { p -> DoorBlock(blockSetType, p) }
      *//*?} else {*/
      .blockFactory { p -> DoorBlock(p, blockSetType) }
      /*?}*/
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