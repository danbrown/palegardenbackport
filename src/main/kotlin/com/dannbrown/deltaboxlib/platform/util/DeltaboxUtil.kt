package com.dannbrown.deltaboxlib.platform.util

import com.dannbrown.deltaboxlib.common.DeltaboxLibCommon
import com.mojang.serialization.DataResult
import net.minecraft.core.Registry
import net.minecraft.core.registries.Registries
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.MutableComponent
import net.minecraft.resources.ResourceKey
import net.minecraft.resources.ResourceLocation
import net.minecraft.tags.TagKey
import net.minecraft.world.entity.EntityType
import net.minecraft.world.item.Item
import net.minecraft.world.item.crafting.Ingredient
import net.minecraft.world.level.ItemLike
import net.minecraft.world.level.biome.Biome
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.material.Fluid
import org.apache.logging.log4j.LogManager
import java.nio.file.Path
import java.util.function.Supplier

object DeltaboxUtil {
    private val LOGGER = LogManager.getLogger()

    fun logInfo(message: String, modId: String = DeltaboxLibCommon.MOD_ID) {
        LOGGER.info("[${modId}] $message")
    }

    fun <K, V> memoize(provider: (K) -> V): (K) -> V = object : (K) -> V {
        val cache: MutableMap<K, V> = mutableMapOf()

        override fun invoke(key: K): V = cache.computeIfAbsent(key, provider)
    }

    fun <R> DataResult<R>.getAnyway(): R =
      /*? if <=1.20.4*/getOrThrow(false) {}
        /*? if >1.20.4*//*orThrow*/

    fun resourceLocation(id: String): ResourceLocation =
      /*? if <1.21 {*/ResourceLocation(id)
    /*?} else*//*if (':' in id) ResourceLocation.parse(id) else ResourceLocation.fromNamespaceAndPath("minecraft", id)*/

    fun resourceLocation(path: String, id: String): ResourceLocation =
      /*? if <1.21 {*/ResourceLocation(path, id)
    /*?} else*//*ResourceLocation.fromNamespaceAndPath(path, id)*/

    fun itemId(item: Supplier<ItemLike>): String {
        val names = item.get().asItem().descriptionId.split(".")
        return names[names.size - 1]
    }
    
    object PATH {
        fun getConfigPath(modID: String, configFileName: String, configExtension: String): Path {
            /*? if fabric {*/

            /*return net.fabricmc.loader.api.FabricLoader.getInstance().configDir.resolve(modID).resolve("$configFileName.$configExtension")

            *//*?} elif forge {*/

            return net.minecraftforge.fml.loading.FMLLoader.getGamePath().resolve("config").resolve(modID).resolve("$configFileName.$configExtension");

            /*?} else {*/

            /*return net.neoforged.fml.loading.FMLLoader.getGamePath().resolve("config").resolve(modID).resolve("$configFileName.$configExtension");

            *//*?}*/
        }

        fun isModInstalled(modid: String): Boolean {
            /*? if fabric {*/

            /*return net.fabricmc.loader.api.FabricLoader.getInstance().isModLoaded(modid)

            *//*?} elif forge {*/

            return net.minecraftforge.fml.loading.FMLLoader.getLoadingModList().getModFileById(modid) != null;

            /*?} else {*/

            /*return net.neoforged.fml.loading.FMLLoader.getLoadingModList().getModFileById(modid) != null;

            *//*?}*/
        }

        fun gameDir(): Path {
            /*? if fabric {*/
            /*return net.fabricmc.loader.api.FabricLoader.getInstance().getGameDir()

            *//*?} elif forge {*/

            return net.minecraftforge.fml.loading.FMLLoader.getGamePath();

            /*?} else {*/

            /*return net.neoforged.fml.loading.FMLLoader.getGamePath();

            *//*?}*/
        }

        fun getConfigFolder(modID: String): Path {
            /*? if fabric {*/

            /*return net.fabricmc.loader.api.FabricLoader.getInstance().gameDir.resolve("config").resolve(modID)

            *//*?} elif forge {*/

            return net.minecraftforge.fml.loading.FMLLoader.getGamePath().resolve("config").resolve(modID);

            /*?} else {*/

            /*return net.neoforged.fml.loading.FMLLoader.getGamePath().resolve("config").resolve(modID);

            *//*?}*/
        }
    }

    object LANG {
        val CONNECTING_WORDS = setOf("of", "the", "and", "in", "on", "at", "to", "with", "by", "for", "as", "or", "nor", "but", "so", "yet", "a", "an")

        fun asId(name: String): String {
            return name.lowercase().replace(" ", "_")
        }

        fun asName(id: String): String {
            return id.split("_")
              .joinToString(" ") { word ->
                  if (word.lowercase() in CONNECTING_WORDS) {
                      word.lowercase()
                  } else {
                      word.replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() }
                  }
              }
              .replace("  ", " ")
              .replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() }
        }

        fun nonPluralId(name: String): String {
            val asId = asId(name)
            return if (asId.endsWith("s")) asId.substring(0, asId.length - 1) else asId
        }

        @JvmStatic
        fun translateDirect(key: String): MutableComponent {
            return Component.translatable(key)
        }

        @JvmStatic
        fun getTooltipKey(modId: String?, itemId: String): String {
            return "tooltip." + DeltaboxLibCommon.MOD_ID + (if(modId !== null) ".$modId" else "") + "." + itemId
        }
    }

    object TAGS {
        fun <R, T: Registry<R>> optionalTag(registry: ResourceKey<T>, id: ResourceLocation): TagKey<R> {
            return TagKey.create(registry, id)
        }

        // VANILLA
        fun <R, T: Registry<R>> vanillaTag(registry: ResourceKey<T>, path: String): TagKey<R> {
            return optionalTag(registry, resourceLocation("minecraft", path))
        }

        fun vanillaBlockTag(path: String): TagKey<Block> {
            return vanillaTag(Registries.BLOCK, path)
        }

        fun vanillaItemTag(path: String): TagKey<Item> {
            return vanillaTag(Registries.ITEM, path)
        }

        // DELTABOX
        fun <R, T: Registry<R>> deltaboxTag(registry: ResourceKey<T>, path: String): TagKey<R> {
            return optionalTag(registry, resourceLocation(DeltaboxLibCommon.MOD_ID, path))
        }

        fun deltaboxBlockTag(path: String): TagKey<Block> {
            return deltaboxTag(Registries.BLOCK, path)
        }

        fun deltaboxItemTag(path: String): TagKey<Item> {
            return deltaboxTag(Registries.ITEM, path)
        }

        fun deltaboxFluidTag(path: String): TagKey<Fluid> {
            return deltaboxTag(Registries.FLUID, path)
        }

        fun deltaboxBiomeTag(path: String): TagKey<Biome> {
            return deltaboxTag(Registries.BIOME, path)
        }

        fun deltaboxEntityTag(path: String): TagKey<EntityType<*>> {
            return deltaboxTag(Registries.ENTITY_TYPE, path)
        }

        // ANY MOD
        fun <R, T: Registry<R>> modTag(modId: String, registry: ResourceKey<T>, path: String): TagKey<R> {
            return optionalTag(registry, resourceLocation(modId, path))
        }

        fun modBlockTag(modId: String, path: String): TagKey<Block> {
            return modTag(modId, Registries.BLOCK, path)
        }

        fun modItemTag(modId: String, path: String): TagKey<Item> {
            return modTag(modId, Registries.ITEM, path)
        }

        fun modBiomeTag(modId: String, path: String): TagKey<Biome> {
            return modTag(modId, Registries.BIOME, path)
        }

        fun modEntityTag(modId: String, path: String): TagKey<EntityType<*>> {
            return modTag(modId, Registries.ENTITY_TYPE, path)
        }

        fun modFluidTag(modId: String, path: String): TagKey<Fluid> {
            return modTag(modId, Registries.FLUID, path)
        }

        // MODLOADERS
        fun <R, T: Registry<R>> modloaderTag(registry: ResourceKey<T>, path: String): MutableList<TagKey<R>> {
            return mutableListOf(
                optionalTag(registry, resourceLocation("c", path)), // tag for fabric
                optionalTag(registry, resourceLocation("forge", path)), // tag for forge
                optionalTag(registry, resourceLocation("neoforge", path)) // tag for neoforged
            )
        }

        fun modloaderBlockTag(path: String): MutableList<TagKey<Block>> {
            return modloaderTag(Registries.BLOCK, path)
        }

        fun modloaderItemTag(path: String): MutableList<TagKey<Item>> {
            return modloaderTag(Registries.ITEM, path)
        }

        fun modloaderFluidTag(path: String): MutableList<TagKey<Fluid>> {
            return modloaderTag(Registries.FLUID, path)
        }

        fun modloaderBiomeTag(path: String): MutableList<TagKey<Biome>> {
            return modloaderTag(Registries.BIOME, path)
        }

        fun modloaderEntityTag(path: String): MutableList<TagKey<EntityType<*>>> {
            return modloaderTag(Registries.ENTITY_TYPE, path)
        }

        // allow to use for recipe tags
        fun modloaderItemIngredient(path: String): Ingredient {
            return Ingredient.of(modloaderItemTag(path).map { Ingredient.of(it).items }.toTypedArray().flatten().stream())
        }
    }
}

