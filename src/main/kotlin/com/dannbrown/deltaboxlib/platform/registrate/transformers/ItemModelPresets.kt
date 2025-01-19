package com.dannbrown.deltaboxlib.platform.registrate.transformers

import com.tterrag.registrate.providers.DataGenContext
import com.tterrag.registrate.providers.RegistrateItemModelProvider
import com.tterrag.registrate.util.nullness.NonNullBiConsumer
import net.minecraft.world.item.Item

object ItemModelPresets {

  fun <B : Item> simpleItem(name: String? = null): NonNullBiConsumer<DataGenContext<Item, B>, RegistrateItemModelProvider> {
    return NonNullBiConsumer { c, p ->
      p.withExistingParent(c.name, p.mcLoc("item/generated"))
        .texture("layer0", p.modLoc("item/" + (name ?: c.name)))
    }
  }

  fun <B : Item> handheldItem(name: String? = null): NonNullBiConsumer<DataGenContext<Item, B>, RegistrateItemModelProvider> {
    return NonNullBiConsumer { c, p ->
      p.withExistingParent(c.name, p.mcLoc("item/handheld"))
        .texture("layer0", p.modLoc("item/" + (name ?: c.name)))
    }
  }


  fun <B : Item> simpleLayerItem(name: String? = null): NonNullBiConsumer<DataGenContext<Item, B>, RegistrateItemModelProvider> {
    return NonNullBiConsumer { c, p ->
      p.withExistingParent(c.name, p.mcLoc("item/generated"))
        .texture("layer0", p.modLoc("block/" + (name ?: c.name)))
    }
  }

  fun <B : Item> simpleBlockItem(name: String? = null): NonNullBiConsumer<DataGenContext<Item, B>, RegistrateItemModelProvider> {
    return NonNullBiConsumer { c, p ->
      p.withExistingParent(c.name, p.modLoc("block/" + (name ?: c.name)))
    }
  }


  fun <B : Item> tallPlantBottomItem(name: String, customItem: Boolean = true): NonNullBiConsumer<DataGenContext<Item, B>, RegistrateItemModelProvider> {
    return NonNullBiConsumer { c, p ->
      p.withExistingParent(c.name, p.mcLoc("item/generated"))
        .texture("layer0", p.modLoc((if (customItem) "item/" else "block/") + name))
    }
  }

  fun <B : Item> wallItem(name: String): NonNullBiConsumer<DataGenContext<Item, B>, RegistrateItemModelProvider> {
    return NonNullBiConsumer { c, p ->
      p.wallInventory(c.name, p.modLoc("block/$name"))
    }
  }

  fun <B : Item> bottomTopWallItem(name: String): NonNullBiConsumer<DataGenContext<Item, B>, RegistrateItemModelProvider> {
    return NonNullBiConsumer { c, p ->
      p.withExistingParent(c.name, p.mcLoc("block/wall_inventory_special_top"))
        .texture("wall", p.modLoc("block/$name"))
        .texture("down", p.modLoc("block/$name" + "_top"))
        .texture("top", p.modLoc("block/$name" + "_top"))
    }
  }

  fun <B : Item> barsItem(name: String, specialEdge: Boolean): NonNullBiConsumer<DataGenContext<Item, B>, RegistrateItemModelProvider> {
    return NonNullBiConsumer { c, p ->
      val barsTexture = p.modLoc("block/bars/" + name + "_bars")
      p.withExistingParent(c.name, p.modLoc("item/bars"))
        .texture("bars", barsTexture)
        .texture("edge", if (specialEdge) p.modLoc("block/bars/" + name + "_bars_edge") else barsTexture)
    }
  }

  fun <B : Item> trapdoorItem(name: String): NonNullBiConsumer<DataGenContext<Item, B>, RegistrateItemModelProvider> {
    return NonNullBiConsumer { c, p ->
      val texture = p.modLoc("block/$name")
      p.withExistingParent(c.name, p.modLoc("block/$name" + "_bottom"))
        .texture("texture", texture)
        .texture("particle", texture)
        .renderType("cutout_mipped")
    }
  }

  fun <B : Item> stickerItem(name: String): NonNullBiConsumer<DataGenContext<Item, B>, RegistrateItemModelProvider> {
    return NonNullBiConsumer { c, p ->
      val texture = p.modLoc("block/stickers/$name" + "_sticker")
      p.withExistingParent(c.name, p.mcLoc("item/generated"))
        .texture("layer0", texture)
    }
  }

  fun <B : Item> fenceItem(name: String): NonNullBiConsumer<DataGenContext<Item, B>, RegistrateItemModelProvider> {
    return NonNullBiConsumer { c, p ->
      p.fenceInventory(c.name, p.modLoc("block/${name}"))
    }
  }

  fun <B : Item> pressurePlateItem(name: String): NonNullBiConsumer<DataGenContext<Item, B>, RegistrateItemModelProvider> {
    return NonNullBiConsumer { c, p ->
      p.pressurePlate(c.name, p.modLoc("block/${name}"))
    }
  }

  fun <B : Item> buttonItem(name: String): NonNullBiConsumer<DataGenContext<Item, B>, RegistrateItemModelProvider> {
    return NonNullBiConsumer { c, p ->
      p.buttonInventory(c.name, p.modLoc("block/${name}"))
    }
  }

  fun <B : Item> doorItem(name: String): NonNullBiConsumer<DataGenContext<Item, B>, RegistrateItemModelProvider> {
    return NonNullBiConsumer { c, p ->
      p.withExistingParent(c.name, p.mcLoc("item/generated"))
        .texture("layer0", p.modLoc("item/${name}"))
    }
  }
}