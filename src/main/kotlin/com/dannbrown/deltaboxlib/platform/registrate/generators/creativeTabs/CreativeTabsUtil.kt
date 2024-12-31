package com.dannbrown.deltaboxlib.platform.registrate.generators.creativeTabs

import com.dannbrown.deltaboxlib.common.init.DeltaboxTags
import com.dannbrown.deltaboxlib.platform.registrate.DeltaboxRegistrate
import net.minecraft.core.registries.Registries
import net.minecraft.tags.TagKey
import net.minecraft.world.item.BlockItem
import net.minecraft.world.item.BucketItem
import net.minecraft.world.item.CreativeModeTab
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items
import net.minecraft.world.level.block.Block

object CreativeTabsUtil {
   // Utility method to display all items in the creative tab
    fun displayAllRegistrate(registrate: DeltaboxRegistrate, parameters: CreativeModeTab.ItemDisplayParameters, output:CreativeModeTab.Output) {
        for (entry in registrate.getAll(Registries.BLOCK)) {
          val block = entry.get()
          if (block.asItem() === Items.AIR) continue // avoid fluids and blocks without items
          if (block.asItem().defaultInstance.tags.anyMatch { itemTag: TagKey<Item> -> DeltaboxTags.ITEM.EXCLUDE_FROM_CREATIVE == itemTag }) continue // avoid items with the tag "deltaboxlib:exclude_from_creative"
          output.accept(ItemStack(block.asItem()))
        }

      for (entry in registrate.getAll(Registries.ITEM)) {
        val item = entry.get()
        if (item === Items.AIR) continue // avoid fluids and blocks without items
        if (item.defaultInstance.tags.anyMatch { itemTag: TagKey<Item> -> DeltaboxTags.ITEM.EXCLUDE_FROM_CREATIVE == itemTag }) continue // avoid items with the tag "deltaboxlib:exclude_from_creative"
        output.accept(ItemStack(item))
      }
    }

    fun displayAll(registrate: DeltaboxRegistrate, parameters: CreativeModeTab.ItemDisplayParameters, output:CreativeModeTab.Output, filterTag: TagKey<Item>? = null) {
      displayItems(registrate, parameters, output, filterTag)
      displayBlocks(registrate, parameters, output, filterTag)
      displayBuckets(registrate, parameters, output, filterTag)
    }

    /**
     * Display all registrate blocks in the creative tab, avoid machine blocks
     */
    fun displayBlocks(registrate: DeltaboxRegistrate, parameters: CreativeModeTab.ItemDisplayParameters, output:CreativeModeTab.Output, filterTag: TagKey<Item>? = null) {
      for (entry in registrate.getAll(Registries.BLOCK)) {
        if (validateBlock(entry.get(), filterTag)) output.accept(ItemStack(entry.get().asItem()))
      }
    }

    /**
     * Display all registrate items in the creative tab, if the item is a BacktankCustomArmorItem, also display it with max air
     */
    fun displayItems(registrate: DeltaboxRegistrate, parameters: CreativeModeTab.ItemDisplayParameters, output: CreativeModeTab.Output, filterTag: TagKey<Item>? = null) {
      for (entry in registrate.getAll(Registries.ITEM)) {
        if (validateItem(entry.get(), filterTag)) output.accept(ItemStack(entry.get()))
      }
    }

    /**
     * Display all registrate bucket items in the creative tab
     */
    fun displayBuckets(registrate: DeltaboxRegistrate, parameters: CreativeModeTab.ItemDisplayParameters, output: CreativeModeTab.Output, filterTag: TagKey<Item>? = null) {
      for (entry in registrate.getAll(Registries.ITEM)) {
        if (validateBucket(entry.get(), filterTag)) output.accept(ItemStack(entry.get()))
      }
    }


    fun validateBucket(item: Item, filterTag: TagKey<Item>? = null): Boolean {
      if (item !is BucketItem) return false // avoid non-buckets
      if (item.defaultInstance.tags.anyMatch { itemTag: TagKey<Item> -> DeltaboxTags.ITEM.EXCLUDE_FROM_CREATIVE == itemTag }) return false  // avoid items with the tag "deltaboxlib:exclude_from_creative"
      if (filterTag != null && item.defaultInstance.tags.anyMatch { itemTag: TagKey<Item> -> filterTag == itemTag }) return true // only add items with the specified tag
      else if (filterTag == null) return true
      return false
    }

    fun validateItem(item: Item, filterTag: TagKey<Item>? = null): Boolean {
      if (item is BlockItem) return false // avoid blocks
      if (item is BucketItem) return false // avoid buckets
      if (item.defaultInstance.tags.anyMatch { itemTag: TagKey<Item> -> DeltaboxTags.ITEM.EXCLUDE_FROM_CREATIVE == itemTag }) return false // avoid items with the tag "deltaboxlib:exclude_from_creative"
      if (filterTag != null && item.defaultInstance.tags.anyMatch { itemTag: TagKey<Item> -> filterTag == itemTag }) return true // only add items with the specified tag
      else if (filterTag == null) return true
      return false
    }


    fun validateBlock(block: Block, filterTag: TagKey<Item>? = null): Boolean {
      if (block.asItem() === Items.AIR) return false // avoid fluids and blocks without items
      if (block.asItem().defaultInstance.tags.anyMatch { itemTag: TagKey<Item> -> DeltaboxTags.ITEM.EXCLUDE_FROM_CREATIVE == itemTag }) return false // avoid items with the tag "deltaboxlib:exclude_from_creative"
      if (filterTag != null && block.asItem().defaultInstance.tags.anyMatch { itemTag: TagKey<Item> -> filterTag == itemTag }) return true // only add blocks with the specified tag
      else if (filterTag == null) return true
      return false
    }
}
