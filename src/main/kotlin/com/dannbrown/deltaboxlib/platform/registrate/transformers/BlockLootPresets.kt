package com.dannbrown.deltaboxlib.platform.registrate.transformers

import com.tterrag.registrate.providers.loot.RegistrateBlockLootTables
import com.tterrag.registrate.util.nullness.NonNullBiConsumer
import net.minecraft.advancements.critereon.EnchantmentPredicate
import net.minecraft.advancements.critereon.ItemPredicate
import net.minecraft.advancements.critereon.MinMaxBounds
import net.minecraft.advancements.critereon.StatePropertiesPredicate
import net.minecraft.core.Holder
import net.minecraft.core.HolderLookup
import net.minecraft.core.registries.Registries
import net.minecraft.resources.ResourceKey
import net.minecraft.world.item.Items
import net.minecraft.world.item.enchantment.Enchantment
import net.minecraft.world.item.enchantment.Enchantments
import net.minecraft.world.level.ItemLike
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.state.properties.Property
import net.minecraft.world.level.storage.loot.LootPool
import net.minecraft.world.level.storage.loot.LootTable
import net.minecraft.world.level.storage.loot.entries.LootItem
import net.minecraft.world.level.storage.loot.entries.LootPoolEntryContainer
import net.minecraft.world.level.storage.loot.functions.ApplyBonusCount
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

  /**
   * Drops the other loot instead
   * @param other the item to drop
   */
  fun <B : Block> dropOtherLoot(other: Supplier<ItemLike>): NonNullBiConsumer<RegistrateBlockLootTables, B> {
    return NonNullBiConsumer { lt, b ->
      lt.dropOther(b,
        other.get()
          .asItem())
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