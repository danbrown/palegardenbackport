package com.dannbrown.palegardenbackport.content.block

import com.dannbrown.palegardenbackport.ModContent
import com.google.common.collect.ImmutableMap
import com.google.common.collect.Maps
import com.mojang.serialization.MapCodec
import com.tterrag.registrate.providers.DataGenContext
import com.tterrag.registrate.providers.RegistrateBlockstateProvider
import com.tterrag.registrate.util.nullness.NonNullBiConsumer
import net.minecraft.Util
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.server.level.ServerLevel
import net.minecraft.util.RandomSource
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.context.BlockPlaceContext
import net.minecraft.world.level.BlockGetter
import net.minecraft.world.level.Level
import net.minecraft.world.level.LevelAccessor
import net.minecraft.world.level.LevelReader
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.BonemealableBlock
import net.minecraft.world.level.block.CarpetBlock
import net.minecraft.world.level.block.Mirror
import net.minecraft.world.level.block.MultifaceBlock
import net.minecraft.world.level.block.RenderShape
import net.minecraft.world.level.block.Rotation
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.block.state.StateDefinition
import net.minecraft.world.level.block.state.properties.BlockStateProperties
import net.minecraft.world.level.block.state.properties.BooleanProperty
import net.minecraft.world.level.block.state.properties.EnumProperty
import net.minecraft.world.level.block.state.properties.Property
import net.minecraft.world.level.block.state.properties.WallSide
import net.minecraft.world.phys.shapes.CollisionContext
import net.minecraft.world.phys.shapes.Shapes
import net.minecraft.world.phys.shapes.VoxelShape
import net.minecraftforge.client.model.generators.ModelFile
import java.util.*
import java.util.function.BooleanSupplier
import java.util.function.Function
import java.util.stream.Collectors

class PaleMossCarpetBlock(props: Properties) : CarpetBlock(props), BonemealableBlock {
  private val shapesCache: Map<BlockState, VoxelShape>

  fun getOcclusionShape(p_365538_: BlockState?): VoxelShape {
    return Shapes.empty()
  }

  override fun getShape(p_363320_: BlockState, p_360809_: BlockGetter, p_366148_: BlockPos, p_364013_: CollisionContext): VoxelShape {
    return shapesCache[p_363320_] as VoxelShape
  }

  override fun getCollisionShape(p_362636_: BlockState, p_367446_: BlockGetter, p_361178_: BlockPos, p_362275_: CollisionContext): VoxelShape {
    return if (p_362636_.getValue(BASE) as Boolean) DOWN_AABB else Shapes.empty()
  }

  fun propagatesSkylightDown(p_366765_: BlockState?): Boolean {
    return true
  }

  override fun canSurvive(p_367593_: BlockState, p_363307_: LevelReader, p_365117_: BlockPos): Boolean {
    val `$$3` = p_363307_.getBlockState(p_365117_.below())
    return if (p_367593_.getValue(BASE) as Boolean) {
      !`$$3`.isAir
    }
    else {
      `$$3`.`is`(this) && `$$3`.getValue(BASE) as Boolean
    }
  }

  override fun getStateForPlacement(p_363369_: BlockPlaceContext): BlockState? {
    return getUpdatedState(this.defaultBlockState(), p_363369_.level, p_363369_.clickedPos, true)
  }

  override fun setPlacedBy(p_362741_: Level, p_360970_: BlockPos, p_365361_: BlockState, p_369935_: LivingEntity?, p_364687_: ItemStack) {
    if (!p_362741_.isClientSide) {
      val `$$5` = p_362741_.getRandom()
      Objects.requireNonNull(`$$5`)
      val `$$6` = createTopperWithSideChance(p_362741_, p_360970_) { `$$5`.nextBoolean() }
      if (!`$$6`.isAir) {
        p_362741_.setBlock(p_360970_.above(), `$$6`, 3)
      }
    }
  }

  override fun updateShape(p_367293_: BlockState, p_361388_: Direction, p_368028_: BlockState, pLevel: LevelAccessor, p_370081_: BlockPos, p_367050_: BlockPos): BlockState {
    if (!p_367293_.canSurvive(pLevel, p_370081_)) {
      return Blocks.AIR.defaultBlockState()
    }
    else {
      val `$$8` = getUpdatedState(p_367293_, pLevel, p_370081_, false)
      return if (!hasFaces(`$$8`)) Blocks.AIR.defaultBlockState() else `$$8`
    }
  }

  override fun createBlockStateDefinition(p_362311_: StateDefinition.Builder<Block, BlockState>) {
    p_362311_.add(*arrayOf<Property<*>>(BASE, NORTH, EAST, SOUTH, WEST))
  }

  override fun rotate(p_363231_: BlockState, p_369895_: Rotation): BlockState {
    val var10000 = when (p_369895_) {
      Rotation.CLOCKWISE_180 -> (((p_363231_.setValue(NORTH, p_363231_.getValue(SOUTH) as WallSide) as BlockState).setValue(EAST, p_363231_.getValue(WEST) as WallSide) as BlockState).setValue(SOUTH, p_363231_.getValue(
        NORTH) as WallSide) as BlockState).setValue(WEST, p_363231_.getValue(EAST) as WallSide) as BlockState

      Rotation.COUNTERCLOCKWISE_90 -> (((p_363231_.setValue(NORTH, p_363231_.getValue(EAST) as WallSide) as BlockState).setValue(EAST, p_363231_.getValue(SOUTH) as WallSide) as BlockState).setValue(SOUTH, p_363231_.getValue(WEST) as WallSide) as BlockState).setValue(
        WEST, p_363231_.getValue(NORTH) as WallSide) as BlockState

      Rotation.CLOCKWISE_90 -> (((p_363231_.setValue(NORTH, p_363231_.getValue(WEST) as WallSide) as BlockState).setValue(EAST, p_363231_.getValue(NORTH) as WallSide) as BlockState).setValue(SOUTH, p_363231_.getValue(EAST) as WallSide) as BlockState).setValue(
        WEST, p_363231_.getValue(SOUTH) as WallSide) as BlockState

      else -> p_363231_
    }
    return var10000
  }

  override fun mirror(p_368204_: BlockState, p_366787_: Mirror): BlockState {
    val var10000 = when (p_366787_) {
      Mirror.LEFT_RIGHT -> (p_368204_.setValue(NORTH, p_368204_.getValue(SOUTH) as WallSide) as BlockState).setValue(SOUTH, p_368204_.getValue(NORTH) as WallSide) as BlockState
      Mirror.FRONT_BACK -> (p_368204_.setValue(EAST, p_368204_.getValue(WEST) as WallSide) as BlockState).setValue(WEST, p_368204_.getValue(EAST) as WallSide) as BlockState
      else -> super.mirror(p_368204_, p_366787_)
    }
    return var10000
  }

  override fun isValidBonemealTarget(p_362652_: LevelReader, p_369062_: BlockPos, p_361167_: BlockState, p3: Boolean): Boolean {
    return p_361167_.getValue(BASE) as Boolean && !createTopperWithSideChance(p_362652_, p_369062_) { true }.isAir
  }

  override fun isBonemealSuccess(p_362053_: Level, p_363617_: RandomSource, p_362482_: BlockPos, p_365063_: BlockState): Boolean {
    return true
  }

  override fun performBonemeal(p_366160_: ServerLevel, p_369242_: RandomSource, p_362249_: BlockPos, p_362904_: BlockState) {
    val `$$4` = createTopperWithSideChance(p_366160_, p_362249_) { true }
    if (!`$$4`.isAir) {
      p_366160_.setBlock(p_362249_.above(), `$$4`, 3)
    }
  }

  init {
    this.registerDefaultState((((((stateDefinition.any() as BlockState).setValue(BASE, true) as BlockState).setValue(NORTH, WallSide.NONE) as BlockState).setValue(EAST, WallSide.NONE) as BlockState).setValue(SOUTH, WallSide.NONE) as BlockState).setValue(
      WEST, WallSide.NONE) as BlockState)
    this.shapesCache = ImmutableMap.copyOf(stateDefinition.possibleStates.stream()
      .collect(Collectors.toMap(Function.identity()
      ) { p_363638_: BlockState -> calculateShape(p_363638_) }))
  }

  override fun getRenderShape(pState: BlockState): RenderShape {
    return RenderShape.MODEL
  }

  companion object {
    val BASE: BooleanProperty = BlockStateProperties.BOTTOM
    private val NORTH: EnumProperty<WallSide> = BlockStateProperties.NORTH_WALL
    private val EAST: EnumProperty<WallSide> = BlockStateProperties.EAST_WALL
    private val SOUTH: EnumProperty<WallSide> = BlockStateProperties.SOUTH_WALL
    private val WEST: EnumProperty<WallSide> = BlockStateProperties.WEST_WALL
    private val PROPERTY_BY_DIRECTION: Map<Direction, EnumProperty<WallSide>>
    private const val AABB_OFFSET = 1.0f
    private val DOWN_AABB: VoxelShape
    private val WEST_AABB: VoxelShape
    private val EAST_AABB: VoxelShape
    private val NORTH_AABB: VoxelShape
    private val SOUTH_AABB: VoxelShape
    private const val SHORT_HEIGHT = 10
    private val WEST_SHORT_AABB: VoxelShape
    private val EAST_SHORT_AABB: VoxelShape
    private val NORTH_SHORT_AABB: VoxelShape
    private val SOUTH_SHORT_AABB: VoxelShape

    fun <B : Block> generatePaleMossCarpetBlockState(): NonNullBiConsumer<DataGenContext<Block, B>, RegistrateBlockstateProvider> {
      return NonNullBiConsumer { c, p ->
        val bottomModel = p.models().withExistingParent("pale_moss_carpet", p.mcLoc("block/carpet")).texture("wool", p.modLoc("block/pale_moss_carpet"))
        val tallSideModel = p.models().withExistingParent("pale_moss_carpet_side_tall", p.mcLoc("block/pale_moss_carpet_side_tall")).texture("side", p.modLoc("block/pale_moss_carpet_side_tall")).renderType("cutout")
        val smallSideModel = p.models().withExistingParent("pale_moss_carpet_side_small", p.mcLoc("block/pale_moss_carpet_side_small")).texture("side", p.modLoc("block/pale_moss_carpet_side_small")).renderType("cutout")

        val builder = p.getMultipartBuilder(c.get())

        // Bottom true
        builder.part().modelFile(bottomModel)
          .addModel()
          .condition(BASE, true)
          .end()

        // Bottom false and all sides none
        builder.part().modelFile(bottomModel)
          .addModel()
          .condition(BASE, false)
          .condition(EAST, WallSide.NONE)
          .condition(NORTH, WallSide.NONE)
          .condition(SOUTH, WallSide.NONE)
          .condition(WEST, WallSide.NONE)
          .end()

        // Side conditions
        fun addSideConditions(model: ModelFile, uvLock: Boolean, rotationY: Int, direction: String, state: String) {
          val directionState = when (direction) {
            "north" -> NORTH
            "east" -> EAST
            "south" -> SOUTH
            "west" -> WEST
            else -> throw Error("Invalid direction")
          }

          val wallState = when (state) {
            "tall" -> WallSide.TALL
            "low" -> WallSide.LOW
            else -> throw Error("Invalid state")
          }

          builder.part().modelFile(model)
            .rotationY(rotationY)
            .uvLock(uvLock)
            .addModel()
            .condition(directionState, wallState)
            .end()
        }

        // Adding all the sides
        addSideConditions(tallSideModel, false, 0, "north", "tall")
        addSideConditions(smallSideModel, false, 0, "north", "low")
        addSideConditions(tallSideModel, true, 90, "east", "tall")
        addSideConditions(smallSideModel, true, 90, "east", "low")
        addSideConditions(tallSideModel, true, 180, "south", "tall")
        addSideConditions(smallSideModel, true, 180, "south", "low")
        addSideConditions(tallSideModel, true, 270, "west", "tall")
        addSideConditions(smallSideModel, true, 270, "west", "low")

        // Add fallback for "bottom: false" and all sides "none"
        builder.part().modelFile(tallSideModel)
          .rotationY(0)
          .uvLock(true)
          .addModel()
          .condition(BASE, false)
          .condition(EAST, WallSide.NONE)
          .condition(NORTH, WallSide.NONE)
          .condition(SOUTH, WallSide.NONE)
          .condition(WEST, WallSide.NONE)
          .end()
      }
    }

    private fun calculateShape(p_363638_: BlockState): VoxelShape {
      var `$$1` = Shapes.empty()
      if (p_363638_.getValue(BASE) as Boolean) {
        `$$1` = DOWN_AABB
      }
      var var10000 = when (p_363638_.getValue<WallSide>(NORTH) as WallSide) {
        WallSide.NONE -> `$$1`
        WallSide.LOW -> Shapes.or(`$$1`, NORTH_SHORT_AABB)
        WallSide.TALL -> Shapes.or(`$$1`, NORTH_AABB)
        else -> throw Error("Missing when branch")
      }
      `$$1` = var10000
      var10000 = when (p_363638_.getValue<WallSide>(SOUTH) as WallSide) {
        WallSide.NONE -> `$$1`
        WallSide.LOW -> Shapes.or(`$$1`, SOUTH_SHORT_AABB)
        WallSide.TALL -> Shapes.or(`$$1`, SOUTH_AABB)
        else -> throw Error("Missing when branch")
      }
      `$$1` = var10000
      var10000 = when (p_363638_.getValue<WallSide>(EAST) as WallSide) {
        WallSide.NONE -> `$$1`
        WallSide.LOW -> Shapes.or(`$$1`, EAST_SHORT_AABB)
        WallSide.TALL -> Shapes.or(`$$1`, EAST_AABB)
        else -> throw Error("Missing when branch")
      }
      `$$1` = var10000
      var10000 = when (p_363638_.getValue<WallSide>(WEST) as WallSide) {
        WallSide.NONE -> `$$1`
        WallSide.LOW -> Shapes.or(`$$1`, WEST_SHORT_AABB)
        WallSide.TALL -> Shapes.or(`$$1`, WEST_AABB)
        else -> throw Error("Missing when branch")
      }
      `$$1` = var10000
      return if (`$$1`.isEmpty) Shapes.block() else `$$1`
    }

    private fun hasFaces(p_361239_: BlockState): Boolean {
      if (p_361239_.getValue(BASE) as Boolean) {
        return true
      }
      else {
        val var1: Iterator<*> = PROPERTY_BY_DIRECTION.values.iterator()
        var `$$1`: EnumProperty<*>?
        do {
          if (!var1.hasNext()) {
            return false
          }

          `$$1` = var1.next() as EnumProperty<*>?
        } while (p_361239_.getValue(`$$1`) === WallSide.NONE)

        return true
      }
    }

    private fun canSupportAtFace(p_370010_: BlockGetter, p_362757_: BlockPos, p_361992_: Direction): Boolean {
      return if (p_361992_ == Direction.UP) false else MultifaceBlock.canAttachTo(p_370010_, p_361992_, p_362757_, p_370010_.getBlockState(p_362757_.relative(p_361992_)))
    }

    private fun getUpdatedState(p_368960_: BlockState, p_360799_: BlockGetter, p_361234_: BlockPos, p_368579_: Boolean): BlockState {
      var p_368960_ = p_368960_
      var p_368579_ = p_368579_
      var `$$4`: BlockState? = null
      var `$$5`: BlockState? = null
      p_368579_ = p_368579_ or p_368960_.getValue(BASE) as Boolean
      var `$$7`: EnumProperty<*>?
      var `$$8`: WallSide
      val var6: Iterator<*> = Direction.Plane.HORIZONTAL.iterator()
      while (var6.hasNext()) {
        val `$$6` = var6.next() as Direction
        `$$7` = getPropertyForFace(`$$6`)
        `$$8` = if (canSupportAtFace(p_360799_, p_361234_, `$$6`)) ((if (p_368579_) WallSide.LOW else p_368960_.getValue(`$$7`) as WallSide)) else WallSide.NONE
        if (`$$8` == WallSide.LOW) {
          if (`$$4` == null) {
            `$$4` = p_360799_.getBlockState(p_361234_.above())
          }

          if (`$$4` != null) {
            if (`$$4`.`is`(ModContent.PALE_MOSS_CARPET_BLOCK.get()) && `$$4`.getValue(`$$7`) !== WallSide.NONE && !`$$4`!!.getValue<Boolean>(BASE)) {
              `$$8` = WallSide.TALL
            }
          }

          if (!p_368960_.getValue<Boolean>(BASE)) {
            if (`$$5` == null) {
              `$$5` = p_360799_.getBlockState(p_361234_.below())
            }

            if (`$$5` != null) {
              if (`$$5`.`is`(ModContent.PALE_MOSS_CARPET_BLOCK.get()) && `$$5`.getValue(`$$7`) === WallSide.NONE) {
                `$$8` = WallSide.NONE
              }
            }
          }
        }
        p_368960_ = p_368960_.setValue(`$$7`, `$$8`) as BlockState
      }

      return p_368960_
    }

    fun placeAt(p_369832_: LevelAccessor, p_369165_: BlockPos, p_364489_: RandomSource, p_362052_: Int) {
      val `$$4`: BlockState = ModContent.PALE_MOSS_CARPET_BLOCK.get().defaultBlockState()
      val `$$5` = getUpdatedState(`$$4`, p_369832_, p_369165_, true)
      p_369832_.setBlock(p_369165_, `$$5`, 3)
      Objects.requireNonNull(p_364489_)
      val `$$6` = createTopperWithSideChance(p_369832_, p_369165_) { p_364489_.nextBoolean() }
      if (!`$$6`.isAir) {
        p_369832_.setBlock(p_369165_.above(), `$$6`, p_362052_)
      }
    }

    private fun createTopperWithSideChance(p_362586_: BlockGetter, p_370077_: BlockPos, p_367276_: BooleanSupplier): BlockState {
      val `$$3` = p_370077_.above()
      val `$$4` = p_362586_.getBlockState(`$$3`)
      val `$$5`: Boolean = `$$4`.`is`(ModContent.PALE_MOSS_CARPET_BLOCK.get())
      if ((!`$$5` || !`$$4`.getValue<Boolean>(BASE)) && (`$$5` || `$$4`.canBeReplaced())) {
        val `$$6` = ModContent.PALE_MOSS_CARPET_BLOCK.get().defaultBlockState()
          .setValue(BASE, false) as BlockState
        var `$$7` = getUpdatedState(`$$6`, p_362586_, p_370077_.above(), true)
        val var8: Iterator<*> = Direction.Plane.HORIZONTAL.iterator()

        while (var8.hasNext()) {
          val `$$8` = var8.next() as Direction
          val `$$9` = getPropertyForFace(`$$8`)
          if (`$$7`.getValue(`$$9`) != WallSide.NONE && !p_367276_.asBoolean) {
            `$$7` = `$$7`.setValue(`$$9`, WallSide.NONE) as BlockState
          }
        }

        return if (hasFaces(`$$7`) && `$$7` !== `$$4`) {
          `$$7`
        }
        else {
          Blocks.AIR.defaultBlockState()
        }
      }
      else {
        return Blocks.AIR.defaultBlockState()
      }
    }

    fun getPropertyForFace(p_368837_: Direction): EnumProperty<WallSide>? {
      return PROPERTY_BY_DIRECTION[p_368837_]
    }

    init {
      PROPERTY_BY_DIRECTION = ImmutableMap.copyOf(Util.make(Maps.newEnumMap(Direction::class.java)
      ) { enumMap  ->
        enumMap[Direction.NORTH] = NORTH
        enumMap[Direction.EAST] = EAST
        enumMap[Direction.SOUTH] = SOUTH
        enumMap[Direction.WEST] = WEST
      })
      DOWN_AABB = box(0.0, 0.0, 0.0, 16.0, 1.0, 16.0)
      WEST_AABB = box(0.0, 0.0, 0.0, 1.0, 16.0, 16.0)
      EAST_AABB = box(15.0, 0.0, 0.0, 16.0, 16.0, 16.0)
      NORTH_AABB = box(0.0, 0.0, 0.0, 16.0, 16.0, 1.0)
      SOUTH_AABB = box(0.0, 0.0, 15.0, 16.0, 16.0, 16.0)
      WEST_SHORT_AABB = box(0.0, 0.0, 0.0, 1.0, 10.0, 16.0)
      EAST_SHORT_AABB = box(15.0, 0.0, 0.0, 16.0, 10.0, 16.0)
      NORTH_SHORT_AABB = box(0.0, 0.0, 0.0, 16.0, 10.0, 1.0)
      SOUTH_SHORT_AABB = box(0.0, 0.0, 15.0, 16.0, 10.0, 16.0)
    }
  }
}
