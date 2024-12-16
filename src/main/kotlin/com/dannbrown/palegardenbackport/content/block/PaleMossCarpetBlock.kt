package com.dannbrown.palegardenbackport.content.block

import com.dannbrown.palegardenbackport.ModContent
import com.google.common.collect.ImmutableMap
import com.google.common.collect.Maps
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
import net.minecraft.world.level.block.state.properties.WallSide
import net.minecraft.world.phys.shapes.CollisionContext
import net.minecraft.world.phys.shapes.Shapes
import net.minecraft.world.phys.shapes.VoxelShape
import net.minecraftforge.client.model.generators.ModelFile
import java.util.function.BooleanSupplier
import java.util.function.Function
import java.util.stream.Collectors

class PaleMossCarpetBlock(props: Properties) : CarpetBlock(props), BonemealableBlock {
  private val shapesCache: Map<BlockState, VoxelShape> = ImmutableMap.copyOf(
    stateDefinition.possibleStates.stream()
      .collect(Collectors.toMap(Function.identity()) { state -> calculateShape(state) })
  )

  init {
    this.registerDefaultState((((((stateDefinition.any()).setValue(BASE, true)).setValue(NORTH, WallSide.NONE)).setValue(EAST, WallSide.NONE)).setValue(SOUTH, WallSide.NONE)).setValue(WEST, WallSide.NONE))
  }

  override fun getShape(
    state: BlockState,
    level: BlockGetter,
    pos: BlockPos,
    context: CollisionContext
  ): VoxelShape = shapesCache[state] ?: Shapes.empty()

  override fun getCollisionShape(
    state: BlockState,
    level: BlockGetter,
    pos: BlockPos,
    context: CollisionContext
  ): VoxelShape = if (state.getValue(BASE)) DOWN_AABB else Shapes.empty()

  override fun propagatesSkylightDown(state: BlockState, level: BlockGetter, pos: BlockPos): Boolean = true

  override fun canSurvive(state: BlockState, level: LevelReader, pos: BlockPos): Boolean {
    val belowBlockState = level.getBlockState(pos.below())
    return if (state.getValue(BASE)) !belowBlockState.isAir else belowBlockState.`is`(this) && belowBlockState.getValue(BASE)
  }

  override fun getStateForPlacement(context: BlockPlaceContext): BlockState? {
    return getUpdatedState(this.defaultBlockState(), context.level, context.clickedPos, true)
  }

  override fun setPlacedBy(
    level: Level,
    pos: BlockPos,
    state: BlockState,
    entity: LivingEntity?,
    stack: ItemStack
  ) {
    if (!level.isClientSide) {
      val random = level.random
      val topperState = createTopperWithSideChance(level, pos) { random.nextBoolean() }
      if (!topperState.isAir) {
        level.setBlock(pos.above(), topperState, 3)
      }
    }
  }

  override fun updateShape(
    state: BlockState,
    direction: Direction,
    neighborState: BlockState,
    level: LevelAccessor,
    pos: BlockPos,
    neighborPos: BlockPos
  ): BlockState {
    return if (!state.canSurvive(level, pos)) {
      Blocks.AIR.defaultBlockState()
    } else {
      val updatedState = getUpdatedState(state, level, pos, false)
      if (!hasFaces(updatedState)) Blocks.AIR.defaultBlockState() else updatedState
    }
  }

  override fun createBlockStateDefinition(builder: StateDefinition.Builder<Block, BlockState>) {
    builder.add(BASE, NORTH, EAST, SOUTH, WEST)
  }

  override fun rotate(blockState: BlockState, rotation: Rotation): BlockState {
    val var10000 = when (rotation) {
      Rotation.CLOCKWISE_180 -> (((blockState.setValue(NORTH, blockState.getValue(SOUTH))).setValue(EAST, blockState.getValue(WEST))).setValue(SOUTH, blockState.getValue(
        NORTH))).setValue(WEST, blockState.getValue(EAST))

      Rotation.COUNTERCLOCKWISE_90 -> (((blockState.setValue(NORTH, blockState.getValue(EAST))).setValue(EAST, blockState.getValue(SOUTH))).setValue(SOUTH, blockState.getValue(WEST))).setValue(
        WEST, blockState.getValue(NORTH))

      Rotation.CLOCKWISE_90 -> (((blockState.setValue(NORTH, blockState.getValue(WEST))).setValue(EAST, blockState.getValue(NORTH))).setValue(SOUTH, blockState.getValue(EAST))).setValue(
        WEST, blockState.getValue(SOUTH))

      else -> blockState
    }
    return var10000
  }

  override fun mirror(blockState: BlockState, mirror: Mirror): BlockState {
    val var10000 = when (mirror) {
      Mirror.LEFT_RIGHT -> (blockState.setValue(NORTH, blockState.getValue(SOUTH))).setValue(SOUTH, blockState.getValue(NORTH))
      Mirror.FRONT_BACK -> (blockState.setValue(EAST, blockState.getValue(WEST))).setValue(WEST, blockState.getValue(EAST))
      else -> super.mirror(blockState, mirror)
    }
    return var10000
  }

  override fun isValidBonemealTarget(
    level: LevelReader,
    pos: BlockPos,
    state: BlockState,
    isClientSide: Boolean
  ): Boolean = state.getValue(BASE) && !createTopperWithSideChance(level, pos) { true }.isAir

  override fun isBonemealSuccess(
    level: Level,
    random: RandomSource,
    pos: BlockPos,
    state: BlockState
  ): Boolean = true

  override fun performBonemeal(level: ServerLevel, random: RandomSource, pos: BlockPos, state: BlockState) {
    val topperState = createTopperWithSideChance(level, pos) { true }
    if (!topperState.isAir) {
      level.setBlock(pos.above(), topperState, 3)
    }
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
    private val PROPERTY_BY_DIRECTION: Map<Direction, EnumProperty<WallSide>> = ImmutableMap.copyOf(Util.make(Maps.newEnumMap(Direction::class.java)
    ) { enumMap  ->
      enumMap[Direction.NORTH] = NORTH
      enumMap[Direction.EAST] = EAST
      enumMap[Direction.SOUTH] = SOUTH
      enumMap[Direction.WEST] = WEST
    })
    private val DOWN_AABB: VoxelShape = box(0.0, 0.0, 0.0, 16.0, 1.0, 16.0)
    private val WEST_AABB: VoxelShape = box(0.0, 0.0, 0.0, 1.0, 16.0, 16.0)
    private val EAST_AABB: VoxelShape = box(15.0, 0.0, 0.0, 16.0, 16.0, 16.0)
    private val NORTH_AABB: VoxelShape = box(0.0, 0.0, 0.0, 16.0, 16.0, 1.0)
    private val SOUTH_AABB: VoxelShape = box(0.0, 0.0, 15.0, 16.0, 16.0, 16.0)
    private val WEST_SHORT_AABB: VoxelShape = box(0.0, 0.0, 0.0, 1.0, 10.0, 16.0)
    private val EAST_SHORT_AABB: VoxelShape = box(15.0, 0.0, 0.0, 16.0, 10.0, 16.0)
    private val NORTH_SHORT_AABB: VoxelShape = box(0.0, 0.0, 0.0, 16.0, 10.0, 1.0)
    private val SOUTH_SHORT_AABB: VoxelShape = box(0.0, 0.0, 15.0, 16.0, 10.0, 16.0)

    fun <B : Block> generatePaleMossCarpetBlockState(): NonNullBiConsumer<DataGenContext<Block, B>, RegistrateBlockstateProvider> {
      return NonNullBiConsumer { c, p ->
        val bottomModel = p.models().withExistingParent("pale_moss_carpet", p.mcLoc("block/carpet")).texture("wool", p.modLoc("block/pale_moss_carpet"))
        val tallSideModel = p.models().withExistingParent("pale_moss_carpet_side_tall", p.mcLoc("block/pale_moss_carpet_side_tall")).texture("side", p.modLoc("block/pale_moss_carpet_side_tall")).renderType("cutout")
        val smallSideModel = p.models().withExistingParent("pale_moss_carpet_side_small", p.mcLoc("block/pale_moss_carpet_side_small")).texture("side", p.modLoc("block/pale_moss_carpet_side_small")).renderType("cutout")

        val builder = p.getMultipartBuilder(c.get())

        builder.part().modelFile(bottomModel)
          .addModel()
          .condition(BASE, true)
          .end()

        builder.part().modelFile(bottomModel)
          .addModel()
          .condition(BASE, false)
          .condition(EAST, WallSide.NONE)
          .condition(NORTH, WallSide.NONE)
          .condition(SOUTH, WallSide.NONE)
          .condition(WEST, WallSide.NONE)
          .end()

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

        addSideConditions(tallSideModel, false, 0, "north", "tall")
        addSideConditions(smallSideModel, false, 0, "north", "low")
        addSideConditions(tallSideModel, true, 90, "east", "tall")
        addSideConditions(smallSideModel, true, 90, "east", "low")
        addSideConditions(tallSideModel, true, 180, "south", "tall")
        addSideConditions(smallSideModel, true, 180, "south", "low")
        addSideConditions(tallSideModel, true, 270, "west", "tall")
        addSideConditions(smallSideModel, true, 270, "west", "low")

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

    private fun calculateShape(blockState: BlockState): VoxelShape {
      var voxelShape = Shapes.empty()
      if (blockState.getValue(BASE) as Boolean) {
        voxelShape = DOWN_AABB
      }
      voxelShape = when (blockState.getValue(NORTH)) {
        WallSide.NONE -> voxelShape
        WallSide.LOW -> Shapes.or(voxelShape, NORTH_SHORT_AABB)
        WallSide.TALL -> Shapes.or(voxelShape, NORTH_AABB)
        else -> throw Error("Missing when branch")
      }
      voxelShape = when (blockState.getValue(SOUTH)) {
        WallSide.NONE -> voxelShape
        WallSide.LOW -> Shapes.or(voxelShape, SOUTH_SHORT_AABB)
        WallSide.TALL -> Shapes.or(voxelShape, SOUTH_AABB)
        else -> throw Error("Missing when branch")
      }
      voxelShape = when (blockState.getValue(EAST)) {
        WallSide.NONE -> voxelShape
        WallSide.LOW -> Shapes.or(voxelShape, EAST_SHORT_AABB)
        WallSide.TALL -> Shapes.or(voxelShape, EAST_AABB)
        else -> throw Error("Missing when branch")
      }
      voxelShape = when (blockState.getValue(WEST)) {
        WallSide.NONE -> voxelShape
        WallSide.LOW -> Shapes.or(voxelShape, WEST_SHORT_AABB)
        WallSide.TALL -> Shapes.or(voxelShape, WEST_AABB)
        else -> throw Error("Missing when branch")
      }
      return if (voxelShape.isEmpty) Shapes.block() else voxelShape
    }

    private fun hasFaces(blockState: BlockState): Boolean {
      if (blockState.getValue(BASE) as Boolean) {
        return true
      }
      else {
        val iterator: Iterator<*> = PROPERTY_BY_DIRECTION.values.iterator()
        var property: EnumProperty<*>?
        do {
          if (!iterator.hasNext()) {
            return false
          }

          property = iterator.next() as EnumProperty<*>?
        } while (blockState.getValue(property) === WallSide.NONE)

        return true
      }
    }

    private fun canSupportAtFace(getter: BlockGetter, blockPos: BlockPos, direction: Direction): Boolean {
      return if (direction == Direction.UP) false else MultifaceBlock.canAttachTo(getter, direction, blockPos, getter.getBlockState(blockPos.relative(direction)))
    }

    private fun getUpdatedState(
      blockState: BlockState,
      blockGetter: BlockGetter,
      blockPos: BlockPos,
      isBase: Boolean
    ): BlockState {
      var updatedState = blockState
      var isBaseBlock = isBase
      var stateAbove: BlockState? = null
      var stateBelow: BlockState? = null
      isBaseBlock = isBaseBlock || updatedState.getValue(BASE) as Boolean

      val directions = Direction.Plane.HORIZONTAL.iterator()

      while (directions.hasNext()) {
        val direction = directions.next() as Direction
        val faceProperty = getPropertyForFace(direction)
        var wallSide = if (canSupportAtFace(blockGetter, blockPos, direction)) {
          if (isBaseBlock) WallSide.LOW else updatedState.getValue(faceProperty)
        } else {
          WallSide.NONE
        }

        // Handle state when wallSide is LOW
        if (wallSide == WallSide.LOW) {
          // Check the block state above
          if (stateAbove == null) {
            stateAbove = blockGetter.getBlockState(blockPos.above())
          }

          stateAbove?.let {
            if (it.`is`(ModContent.PALE_MOSS_CARPET_BLOCK.get()) &&
              it.getValue(faceProperty) != WallSide.NONE &&
              !it.getValue<Boolean>(BASE)
            ) {
              wallSide = WallSide.TALL
            }
          }

          // Check the block state below if the current block is not the base
          if (!updatedState.getValue<Boolean>(BASE)) {
            if (stateBelow == null) {
              stateBelow = blockGetter.getBlockState(blockPos.below())
            }

            stateBelow?.let {
              if (it.`is`(ModContent.PALE_MOSS_CARPET_BLOCK.get()) &&
                it.getValue(faceProperty) == WallSide.NONE
              ) {
                wallSide = WallSide.NONE
              }
            }
          }
        }

        updatedState = updatedState.setValue(faceProperty, wallSide)
      }

      return updatedState
    }

    private fun createTopperWithSideChance(
      blockGetter: BlockGetter,
      blockPos: BlockPos,
      isSideChance: BooleanSupplier
    ): BlockState {
      val abovePos = blockPos.above()
      val stateAbove = blockGetter.getBlockState(abovePos)
      val isMossCarpet = stateAbove.`is`(ModContent.PALE_MOSS_CARPET_BLOCK.get())

      // Check if the topper can be placed or replaced
      if ((!isMossCarpet || !stateAbove.getValue(BASE)) &&
        (isMossCarpet || stateAbove.canBeReplaced())
      ) {
        val mossCarpetState = ModContent.PALE_MOSS_CARPET_BLOCK.get()
          .defaultBlockState()
          .setValue(BASE, false)

        var updatedState = getUpdatedState(mossCarpetState, blockGetter, abovePos, true)
        val directions = Direction.Plane.HORIZONTAL.iterator()

        while (directions.hasNext()) {
          val direction = directions.next() as Direction
          val faceProperty = getPropertyForFace(direction)

          if (updatedState.getValue(faceProperty) != WallSide.NONE && !isSideChance.asBoolean) {
            updatedState = updatedState.setValue(faceProperty, WallSide.NONE)
          }
        }

        return if (hasFaces(updatedState) && updatedState != stateAbove) {
          updatedState
        } else {
          Blocks.AIR.defaultBlockState()
        }
      } else {
        return Blocks.AIR.defaultBlockState()
      }
    }

    fun getPropertyForFace(direction: Direction): EnumProperty<WallSide>? {
      return PROPERTY_BY_DIRECTION[direction]
    }
  }
}
