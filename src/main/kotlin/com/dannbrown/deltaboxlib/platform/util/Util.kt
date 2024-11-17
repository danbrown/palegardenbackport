package com.dannbrown.deltaboxlib.platform.util

import com.mojang.serialization.DataResult
import net.minecraft.resources.ResourceLocation
import java.nio.file.Path

object Util {
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

