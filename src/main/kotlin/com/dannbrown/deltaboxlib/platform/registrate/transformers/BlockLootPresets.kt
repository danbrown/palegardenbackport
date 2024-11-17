package com.dannbrown.deltaboxlib.platform.registrate.transformers

import com.tterrag.registrate.providers.loot.RegistrateBlockLootTables
import com.tterrag.registrate.util.nullness.NonNullBiConsumer
import net.minecraft.advancements.critereon.BlockPredicate
import net.minecraft.advancements.critereon.LocationPredicate
import net.minecraft.advancements.critereon.StatePropertiesPredicate
import net.minecraft.core.BlockPos
import net.minecraft.world.item.Item
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
import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceCondition
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue
import java.util.function.Supplier

object BlockLootPresets {
  fun <B : Block> noLoot(): NonNullBiConsumer<RegistrateBlockLootTables, B> {
    return NonNullBiConsumer { lt, b ->
      lt.add(b, LootTable.lootTable())
    }
  }

  fun <B : Block> pottedPlantLoot(item: Supplier<ItemLike>): NonNullBiConsumer<RegistrateBlockLootTables, B> {
    return NonNullBiConsumer { lt, b ->
      lt.add(b, lt.createPotFlowerItemTable(item.get()))
    }
  }



  fun <B : Block> dropItselfLoot(): NonNullBiConsumer<RegistrateBlockLootTables, B> {
    return NonNullBiConsumer { lt, b -> lt.dropSelf(b) }
  }

  fun <B : Block> dropOtherLoot(other: Supplier<ItemLike>): NonNullBiConsumer<RegistrateBlockLootTables, B> {
    return NonNullBiConsumer { lt, b ->
      lt.dropOther(b,
        other.get()
          .asItem())
    }
  }

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



  fun <B : Block> doorLoot(): NonNullBiConsumer<RegistrateBlockLootTables, B> {
    return NonNullBiConsumer { lt, b ->
      lt.add(b, lt.createDoorTable(b))
    }
  }

  fun <B : Block> leavesLoot(saplingDrop: Supplier<Block>): NonNullBiConsumer<RegistrateBlockLootTables, B> {
    return NonNullBiConsumer { lt, b ->
      lt.add(b, lt.createLeavesDrops(b, saplingDrop.get(), 0.05f, 0.0625f, 0.083333336f, 0.1f))
    }
  }
}