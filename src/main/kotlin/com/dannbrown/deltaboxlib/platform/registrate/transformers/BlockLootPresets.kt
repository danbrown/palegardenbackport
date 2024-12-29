package com.dannbrown.deltaboxlib.platform.registrate.transformers

import com.dannbrown.deltaboxlib.common.content.block.CropLeavesBlock
import com.tterrag.registrate.providers.loot.RegistrateBlockLootTables
import com.tterrag.registrate.util.nullness.NonNullBiConsumer
import net.minecraft.advancements.critereon.BlockPredicate
import net.minecraft.advancements.critereon.EnchantmentPredicate
import net.minecraft.advancements.critereon.ItemPredicate
import net.minecraft.advancements.critereon.LocationPredicate
import net.minecraft.advancements.critereon.MinMaxBounds
import net.minecraft.advancements.critereon.StatePropertiesPredicate
import net.minecraft.core.BlockPos
import net.minecraft.core.Holder
import net.minecraft.core.HolderLookup
import net.minecraft.core.registries.Registries
import net.minecraft.resources.ResourceKey
import net.minecraft.world.item.Items
import net.minecraft.world.item.enchantment.Enchantment
import net.minecraft.world.item.enchantment.Enchantments
import net.minecraft.world.level.ItemLike
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.CropBlock
import net.minecraft.world.level.block.DoublePlantBlock
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf
import net.minecraft.world.level.block.state.properties.Property
import net.minecraft.world.level.storage.loot.LootPool
import net.minecraft.world.level.storage.loot.LootTable
import net.minecraft.world.level.storage.loot.entries.LootItem
import net.minecraft.world.level.storage.loot.entries.LootPoolEntryContainer
import net.minecraft.world.level.storage.loot.functions.ApplyBonusCount
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction
import net.minecraft.world.level.storage.loot.predicates.LocationCheck
import net.minecraft.world.level.storage.loot.predicates.LootItemBlockStatePropertyCondition
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition
import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceCondition
import net.minecraft.world.level.storage.loot.predicates.MatchTool
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue
import java.util.function.Supplier

/*? if >1.21 {*/
/*import net.minecraft.advancements.critereon.ItemSubPredicates
import net.minecraft.advancements.critereon.ItemEnchantmentsPredicate
*//*?}*/

object BlockLootPresets {
  /**
   * Drops nothing
   */
  fun <B : Block> noLoot(): NonNullBiConsumer<RegistrateBlockLootTables, B> {
    return NonNullBiConsumer { lt, b ->
      lt.add(b, LootTable.lootTable())
    }
  }

  /**
   * Drops the block itself
   */
  fun <B : Block> dropItselfLoot(): NonNullBiConsumer<RegistrateBlockLootTables, B> {
    return NonNullBiConsumer { lt, b -> lt.dropSelf(b) }
  }

  /**z
   * Drops the other loot instead
   * @param other the item to drop
   */
  fun <B : Block> dropOtherLoot(other: Supplier<ItemLike>): NonNullBiConsumer<RegistrateBlockLootTables, B> {
    return NonNullBiConsumer { lt, b ->
      lt.dropOther(b, other.get().asItem())
    }
  }

  /**
   * Drops the block itself if the block has a integer property is equal to a value, and the other item if not
   * @param other the item to drop if the block does not have the property
   * @param property the property to check
   * @param value the value to check
   */
  fun <B : Block> dropItselfOtherConditionLoot(other: Supplier<ItemLike>, property: Property<Int>, value: Int): NonNullBiConsumer<RegistrateBlockLootTables, B>  {
    return NonNullBiConsumer { lt, b ->
      // drop itself if hasProperty equal value, drop other if not
      val pool1 = LootPool.lootPool()
        .setRolls(ConstantValue.exactly(1.0f))
        .`when`(LootItemBlockStatePropertyCondition.hasBlockStateProperties(b)
          .setProperties(StatePropertiesPredicate.Builder.properties()
            .hasProperty(property, value)
          )
        )
        .add(LootItem.lootTableItem(b))

      // inverted
      val pool2 = LootPool.lootPool()
        .setRolls(ConstantValue.exactly(1.0f))
        .`when`(LootItemBlockStatePropertyCondition.hasBlockStateProperties(b)
          .setProperties(StatePropertiesPredicate.Builder.properties()
            .hasProperty(property, value)
          )
          .invert()
        )
        .add(LootItem.lootTableItem(other.get()))

      lt.add(b,
        LootTable.lootTable()
          .withPool(pool1)
          .withPool(pool2)
      )
    }
  }

  /**
   * Create a door loot table, it will drop the door item itself, ignores drops from the second half of the door
   */
  fun <B : Block> doorLoot(): NonNullBiConsumer<RegistrateBlockLootTables, B> {
    return NonNullBiConsumer { lt, b ->
      lt.add(b, lt.createDoorTable(b))
    }
  }

  /**
   * Create a potted plant loot table, it will drop the flower pot and the plant item
   * @param item the item to drop
   */
  fun <B : Block> pottedPlantLoot(item: Supplier<ItemLike>): NonNullBiConsumer<RegistrateBlockLootTables, B> {
    return NonNullBiConsumer { lt, b ->
      lt.add(b, lt.createPotFlowerItemTable(item.get()))
    }
  }

  /**
   * Create a Leaves loot table, it will drop the sapling with a 5% chance, and 1-2 sticks with a 1/200 chance
   * Also adds silk touch and shears support
   * @param saplingDrop the sapling to drop
   */
  fun <B : Block> leavesLoot(saplingDrop: Supplier<Block>): NonNullBiConsumer<RegistrateBlockLootTables, B> {
    return NonNullBiConsumer { lt, b ->
      lt.add(b, lt.createLeavesDrops(b, saplingDrop.get(), 0.05f, 0.0625f, 0.083333336f, 0.1f))
    }
  }

  /**
   * Create a Leaves loot table, it will drop the sapling if not fully grown, and the stick if fully grown
   * @param cropItem the item to drop if the block is fully grown
   * @param saplingItem the item to drop if the block is not fully grown
   * @param cropChance the chance to drop the crop item
   * @param cropMultiplier the amount of items to drop
   */
  fun <B : Block> dropLeafCropLoot(cropItem: Supplier<ItemLike>, saplingItem: Supplier<ItemLike>, cropChance: Float = 0.5f, cropMultiplier: Int = 2, saplingChance: Float = 0.1f, saplingMultiplier: Int = 1): NonNullBiConsumer<RegistrateBlockLootTables, B> {
    return NonNullBiConsumer { lt, b ->
      // drop crop at max age
      val pool1 = LootPool.lootPool()
        .setRolls(ConstantValue.exactly(cropMultiplier.toFloat()))
        .`when`(LootItemBlockStatePropertyCondition.hasBlockStateProperties(b)
          .setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(CropLeavesBlock.AGE, CropLeavesBlock.MAX_AGE))
          .and(LootItemRandomChanceCondition.randomChance(cropChance))
        )
        .add(LootItem.lootTableItem(cropItem.get()))

      // drop sapling at any age
      val pool2 = LootPool.lootPool()
        .setRolls(ConstantValue.exactly(saplingMultiplier.toFloat()))
        .`when`(LootItemRandomChanceCondition.randomChance(saplingChance))
        .add(LootItem.lootTableItem(saplingItem.get()))

      lt.add(b,
        LootTable.lootTable()
          .withPool(pool1)
          .withPool(pool2)
      )
    }
  }
//
//  /**
//   * Create a Crop loot table, it will drop the item if fully grown, and the seed if not fully grown
//   * @param dropItem the item to drop if the block is fully grown
//   * @param includeSeedOnDrop if it will drop itself as a seed
//   * @param chance the chance to drop the crop item
//   * @param multiplier the amount of items to drop
//   */
//  fun <B : Block> dropCropLoot(dropItem: Supplier<ItemLike>, includeSeedOnDrop: Boolean, chance: Float = 0.5f, multiplier: Int = 2): NonNullBiConsumer<RegistrateBlockLootTables, B> {
//    return NonNullBiConsumer { lt, b ->
//      val cropItem: Supplier<ItemLike> = dropItem
//      val seedItem: Supplier<ItemLike>? = if (includeSeedOnDrop) Supplier { b.asItem() } else null
//      val age = 7
//
//      val dropGrownCondition = LootItemRandomChanceCondition.randomChance(chance)
//        .and(
//          LootItemBlockStatePropertyCondition.hasBlockStateProperties(b)
//            .setProperties(
//              StatePropertiesPredicate.Builder.properties()
//                .hasProperty(CropBlock.AGE, age)
//            )
//        )
//      val itemBuilder = LootItem.lootTableItem(cropItem.get())
//        .`when`(dropGrownCondition)
//
//      if (seedItem !== null) {
//        itemBuilder.otherwise(LootItem.lootTableItem(seedItem.get()))
//      }
//      val lootBuilder = LootTable.lootTable()
//        .withPool(
//          LootPool.lootPool()
//            .add(
//              itemBuilder
//            )
//            .setRolls(ConstantValue.exactly(multiplier.toFloat()))
//        )
//
//      if (seedItem !== null) {
//        lootBuilder.withPool(
//          LootPool.lootPool()
//            .`when`(dropGrownCondition)
//            .apply(ApplyBonusCount.addBonusBinomialDistributionCount(Enchantments.BLOCK_FORTUNE, 0.5714286f, 3))
//            .add(LootItem.lootTableItem(seedItem.get()))
//        )
//      }
//
//      lt.add(b, lt.applyExplosionDecay(b, lootBuilder))
//    }
//  }



  /**
   * Drops the silk item if the block is mined with silk touch or shears, and the other with a chance and multiplier if not
   * @param silk the item to drop if the block is mined with silk touch or shears
   * @param other the item to drop if the block is mined normally
   * @param chance the chance to drop the silk item
   * @param multiplier the amount of items to drop
   */
  fun <B : Block> dropSilkShearsOtherLoot(silk: Supplier<ItemLike>, other: Supplier<ItemLike>, chance: Float = 1f, multiplier: Int = 1): NonNullBiConsumer<RegistrateBlockLootTables, B> {
    return NonNullBiConsumer { lt, b ->
      simpleSilkShearsLootTable(lt, b, silk.get(), other, chance, multiplier)
    }
  }

  /**
   * Drops the block itself if mined with silk touch or shears, and the other with a chance and multiplier if not
   * @param other the item to drop if the block is mined normally
   * @param chance the chance to drop the silk item
   * @param multiplier the amount of items to drop
   */
  fun <B : Block> dropSelfSilkShearsOtherLoot(other: Supplier<ItemLike>, chance: Float = 1f, multiplier: Int = 1): NonNullBiConsumer<RegistrateBlockLootTables, B> {
    return NonNullBiConsumer { lt, b ->
      simpleSilkShearsLootTable(lt, b, b, other, chance, multiplier)
    }
  }

  /**
   * Drops the silk item if the block is mined with silk touch, and the other with a chance and multiplier if not
   * @param silk the item to drop if the block is mined with silk touch
   * @param other the item to drop if the block is mined normally
   * @param chance the chance to drop the silk item
   * @param multiplier the amount of items to drop
   */
  fun <B : Block> dropSilkOtherLoot(silk: Supplier<ItemLike>, other: Supplier<ItemLike>, chance: Float = 1f, multiplier: Int = 1): NonNullBiConsumer<RegistrateBlockLootTables, B> {
    return NonNullBiConsumer { lt, b ->
      simpleSilkLootTable(lt, b, silk.get(), other, chance, multiplier)
    }
  }

  /**
   * Drops the block itself if mined with silk touch, and the other with a chance and multiplier if not
   * @param other the item to drop if the block is mined normally
   * @param chance the chance to drop the silk item
   * @param multiplier the amount of items to drop
   */
  fun <B : Block> dropSelfSilkOtherLoot(other: Supplier<ItemLike>, chance: Float = 1f, multiplier: Int = 1): NonNullBiConsumer<RegistrateBlockLootTables, B> {
    return NonNullBiConsumer { lt, b ->
      simpleSilkLootTable(lt, b, b, other, chance, multiplier)
    }
  }

  /**
   * Drops the crop item if the block is fully grown, and the seed item if not
   * @param cropItem the item to drop if the block is fully grown
   * @param seedItem the item to drop if the block is not fully grown, if null set itself as a seed drop (optional)
   * @param includeSeedOnDrop if the seed should be included in drops, if false seeds won't be dropped
   * @param chance the chance to drop the crop item
   * @param multiplier the amount of items to drop
   * @param age the age to check for
   */
  fun <B : Block> dropCropLoot(cropItem: Supplier<ItemLike>, _seedItem: Supplier<ItemLike>?, includeSeedOnDrop: Boolean, chance: Float = 0.5f, multiplier: Int = 1, age: Int = 7): NonNullBiConsumer<RegistrateBlockLootTables, B> {
    return NonNullBiConsumer { lt, b ->
      /*? if >1.21 {*/
      /*val registries = lt.registries
      val enchant = hasEnchant(Enchantments.FORTUNE, registries)
      *//*?} else {*/
      val registries = null
      val enchant = Enchantments.BLOCK_FORTUNE
      /*?}*/

      val seedItem = _seedItem?: Supplier { b.asItem() }

      val dropGrownCondition = LootItemRandomChanceCondition.randomChance(chance)
        .and(LootItemBlockStatePropertyCondition.hasBlockStateProperties(b).setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(CropBlock.AGE, age)))

      val itemBuilder = LootItem.lootTableItem(cropItem.get()).`when`(dropGrownCondition)

      if (seedItem !== null && includeSeedOnDrop) {
        itemBuilder.otherwise(LootItem.lootTableItem(seedItem.get()))
      }

      val lootBuilder = LootTable.lootTable().withPool(
        LootPool.lootPool().add(
          itemBuilder
        ).setRolls(ConstantValue.exactly(multiplier.toFloat()))
      )

      if (seedItem !== null && includeSeedOnDrop) {
        lootBuilder.withPool(
          LootPool.lootPool()
            .`when`(dropGrownCondition)
            .apply(ApplyBonusCount.addBonusBinomialDistributionCount(enchant, 0.5714286f, 3))
            .add(LootItem.lootTableItem(seedItem.get()))
        )
      }

      lt.add(b, lt.applyExplosionDecay(b,lootBuilder))
    }
  }

  fun <B : Block> dropDoubleCropLoot(cropItem: Supplier<ItemLike>, seedItem: Supplier<ItemLike>? = null, chance: Float = 0.25f, count: Float = 2f): NonNullBiConsumer<RegistrateBlockLootTables, B> {
    return NonNullBiConsumer { lt, b ->
      /*? if >1.21 {*/
      /*val registries = lt.registries
      *//*?} else {*/
      val registries = null
      /*?}*/

      var builder: LootPoolEntryContainer.Builder<*> = LootItem.lootTableItem(b)
        .apply(SetItemCountFunction.setCount(ConstantValue.exactly(1f)))
        .`when`(hasShearsOrSilkTouch(registries))
      builder = if (seedItem !== null) {
        builder.otherwise(lt.applyExplosionCondition(b, LootItem.lootTableItem(seedItem.get()))
          .apply(SetItemCountFunction.setCount(ConstantValue.exactly(count)))
          .`when`(LootItemRandomChanceCondition.randomChance(chance))
          .otherwise(LootItem.lootTableItem(cropItem.get())))
      } else{
        builder.otherwise(LootItem.lootTableItem(cropItem.get()))
      }
      val pool = LootTable.lootTable()
        .withPool(LootPool.lootPool()
          .add(builder)
          .`when`(LootItemBlockStatePropertyCondition.hasBlockStateProperties(b)
            .setProperties(StatePropertiesPredicate.Builder.properties()
              .hasProperty(DoublePlantBlock.HALF, DoubleBlockHalf.LOWER)))
          .`when`(LocationCheck.checkLocation(LocationPredicate.Builder.location()
            .setBlock(BlockPredicate.Builder.block()
              .of(b)
              .setProperties(StatePropertiesPredicate.Builder.properties()
                .hasProperty(DoublePlantBlock.HALF, DoubleBlockHalf.UPPER)
                /*? if <1.21 {*/
                .build()
                /*?}*/
              )
              /*? if <1.21 {*/
              .build()
              /*?}*/
            ), BlockPos(0, 1, 0))
          )
        )
      .withPool(LootPool.lootPool()
      .add(builder)
      .`when`(LootItemBlockStatePropertyCondition.hasBlockStateProperties(b)
        .setProperties(StatePropertiesPredicate.Builder.properties()
          .hasProperty(DoublePlantBlock.HALF, DoubleBlockHalf.UPPER)))
        .`when`(LocationCheck.checkLocation(LocationPredicate.Builder.location()
          .setBlock(BlockPredicate.Builder.block()
            .of(b)
            .setProperties(StatePropertiesPredicate.Builder.properties()
              .hasProperty(DoublePlantBlock.HALF, DoubleBlockHalf.LOWER)
              /*? if <1.21 {*/
              .build()
              /*?}*/
            )
            /*? if <1.21 {*/
            .build()
            /*?}*/
          ), BlockPos(0, -1, 0))
        )
      )
    lt.add(b, pool)
  }
}

// @ Helpers
private val HAS_SHEARS = MatchTool.toolMatches(ItemPredicate.Builder.item().of(Items.SHEARS))

private fun hasEnchant(enchant: ResourceKey<Enchantment>, registries: HolderLookup.Provider): Holder.Reference<Enchantment> {
val registryLookup: HolderLookup.RegistryLookup<Enchantment> = registries.lookupOrThrow(Registries.ENCHANTMENT)
return registryLookup.getOrThrow(enchant)
}

private fun hasSilkTouch(registries: HolderLookup.Provider? = null): LootItemCondition.Builder {
/*? if >1.21 {*/
/*return MatchTool.toolMatches(ItemPredicate.Builder.item().withSubPredicate(ItemSubPredicates.ENCHANTMENTS, ItemEnchantmentsPredicate.enchantments(listOf(EnchantmentPredicate(hasEnchant(Enchantments.SILK_TOUCH, registries!!), MinMaxBounds.Ints.atLeast(1))))))
*//*?} else {*/
return MatchTool.toolMatches(ItemPredicate.Builder.item().hasEnchantment(EnchantmentPredicate(Enchantments.SILK_TOUCH, MinMaxBounds.Ints.atLeast(1))))
/*?}*/
}

private fun hasShearsOrSilkTouch(registries: HolderLookup.Provider? = null): LootItemCondition.Builder {
return HAS_SHEARS.or(hasSilkTouch(registries))
}

private fun createSilkTouchOrShearsDispatchTable(arg: ItemLike, arg2: LootPoolEntryContainer.Builder<*>, registries: HolderLookup.Provider? = null): LootTable.Builder? {
return LootTable.lootTable()
.withPool(LootPool.lootPool()
  .setRolls(ConstantValue.exactly(1.0f))
  .add((LootItem.lootTableItem(arg).`when`(this.hasShearsOrSilkTouch(registries))).otherwise(arg2)));
}

private fun createSilkTouchDispatchTable(arg: ItemLike, arg2: LootPoolEntryContainer.Builder<*>, registries: HolderLookup.Provider? = null): LootTable.Builder? {
return LootTable.lootTable()
.withPool(LootPool.lootPool()
  .setRolls(ConstantValue.exactly(1.0f))
  .add((LootItem.lootTableItem(arg).`when`(this.hasSilkTouch(registries))).otherwise(arg2)));
}

private fun <B : Block> simpleSilkShearsLootTable(lt: RegistrateBlockLootTables, b: B, silk: ItemLike, other: Supplier<ItemLike>, chance: Float = 1f, multiplier: Int = 1) {
/*? if >1.21 {*/
/*val registries = lt.registries
val enchant = hasEnchant(Enchantments.FORTUNE, registries)
*//*?} else {*/
val registries = null
val enchant = Enchantments.BLOCK_FORTUNE
/*?}*/

lt.add(b,
createSilkTouchOrShearsDispatchTable(
  silk,
  lt.applyExplosionDecay(silk, LootItem.lootTableItem(other.get())
    .`when`(LootItemRandomChanceCondition.randomChance(chance))
    .apply(ApplyBonusCount.addUniformBonusCount(enchant, 2))
  ),
  registries
)!!.withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(multiplier.toFloat())))
)
}

private fun <B : Block> simpleSilkLootTable(lt: RegistrateBlockLootTables, b: B, silk: ItemLike, other: Supplier<ItemLike>, chance: Float = 1f, multiplier: Int = 1) {
/*? if >1.21 {*/
/*val registries = lt.registries
val enchant = hasEnchant(Enchantments.FORTUNE, registries)
*//*?} else {*/
val registries = null
val enchant = Enchantments.BLOCK_FORTUNE
/*?}*/

lt.add(b,
createSilkTouchDispatchTable(
  silk,
  lt.applyExplosionDecay(silk, LootItem.lootTableItem(other.get())
    .`when`(LootItemRandomChanceCondition.randomChance(chance))
    .apply(ApplyBonusCount.addUniformBonusCount(enchant, 2))
  ),
  registries
)!!.withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(multiplier.toFloat()))))
}
}