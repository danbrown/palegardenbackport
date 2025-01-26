package com.dannbrown.deltaboxlib.platform.registrate

import com.dannbrown.deltaboxlib.common.DeltaboxLibCommon
import com.tterrag.registrate.AbstractRegistrate
import com.tterrag.registrate.util.entry.BlockEntry
import net.minecraft.core.registries.Registries
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceLocation
import net.minecraft.server.packs.resources.PreparableReloadListener
import net.minecraft.world.entity.npc.VillagerProfession
import net.minecraft.world.item.CreativeModeTab
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.trading.MerchantOffer
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.FlowerPotBlock
import java.util.function.BiConsumer
import com.dannbrown.deltaboxlib.platform.registrate.generators.trades.*
import com.dannbrown.deltaboxlib.platform.util.DeltaboxUtil
import com.dannbrown.deltaboxlib.platform.registrate.generators.recipe.DeltaboxRecipeSlice
import com.dannbrown.deltaboxlib.platform.registrate.generators.worldgen.BiomeModifiersUtil
import com.dannbrown.deltaboxlib.platform.registrate.generators.worldgen.ConfiguredFeaturesUtil
import com.dannbrown.deltaboxlib.platform.registrate.generators.worldgen.PlacedFeaturesUtil
import net.minecraft.client.model.BoatModel
import net.minecraft.client.model.ChestBoatModel
import net.minecraft.client.model.geom.ModelLayerLocation
import net.minecraft.client.model.geom.builders.LayerDefinition
import net.minecraft.client.particle.ParticleEngine
import net.minecraft.client.particle.ParticleProvider
import net.minecraft.client.particle.SpriteSet
import net.minecraft.core.Holder
import net.minecraft.core.HolderLookup
import net.minecraft.core.HolderSet
import net.minecraft.core.RegistrySetBuilder
import net.minecraft.core.particles.ParticleOptions
import net.minecraft.core.particles.ParticleType
import net.minecraft.core.particles.SimpleParticleType
import net.minecraft.data.tags.BiomeTagsProvider
import net.minecraft.data.tags.EntityTypeTagsProvider
import net.minecraft.data.tags.FluidTagsProvider
import net.minecraft.data.tags.IntrinsicHolderTagsProvider.IntrinsicTagAppender
import net.minecraft.data.tags.ItemTagsProvider
import net.minecraft.data.tags.PaintingVariantTagsProvider
import net.minecraft.data.tags.TagsProvider
import net.minecraft.data.tags.TagsProvider.TagAppender
import net.minecraft.data.tags.WorldPresetTagsProvider
import net.minecraft.tags.TagKey
import net.minecraft.util.profiling.ProfilerFiller
import java.util.concurrent.CompletableFuture
import java.util.concurrent.Executor
import java.util.function.Predicate
/*? if >=1.21 {*/
/*import net.minecraft.data.worldgen.BootstrapContext
*//*?} else {*/
import net.minecraft.data.worldgen.BootstapContext as BootstrapContext
/*?}*/
import net.minecraft.resources.ResourceKey
import net.minecraft.tags.BiomeTags
import net.minecraft.util.Tuple
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.decoration.PaintingVariant
import net.minecraft.world.item.Item
import net.minecraft.world.level.biome.Biome
import net.minecraft.world.level.levelgen.GenerationStep
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacer
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacerType
import net.minecraft.world.level.levelgen.feature.trunkplacers.TrunkPlacer
import net.minecraft.world.level.levelgen.feature.trunkplacers.TrunkPlacerType
import net.minecraft.world.level.levelgen.placement.PlacedFeature
import net.minecraft.world.level.levelgen.presets.WorldPreset
import net.minecraft.world.level.material.Fluid
import java.util.function.Supplier
/*? if fabric {*/
/*import net.fabricmc.fabric.api.registry.FlammableBlockRegistry
import net.fabricmc.fabric.api.registry.StrippableBlockRegistry
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.fabricmc.fabric.api.biome.v1.BiomeModifications
import net.fabricmc.fabric.api.biome.v1.BiomeSelectionContext
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry.TexturedModelDataProvider
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup
import net.fabricmc.fabric.api.resource.IdentifiableResourceReloadListener
import net.fabricmc.fabric.api.resource.ResourceManagerHelper
import net.fabricmc.fabric.api.`object`.builder.v1.trade.TradeOfferHelper
import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes
import net.minecraft.core.Registry
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.server.MinecraftServer
import net.minecraft.server.packs.PackType
import net.minecraft.server.packs.resources.CloseableResourceManager
import net.minecraft.server.packs.resources.PreparableReloadListener.PreparationBarrier
import net.minecraft.server.packs.resources.ResourceManager
*//*?}*/

/*? if forge {*/
import net.minecraftforge.registries.DeferredRegister
import net.minecraftforge.common.data.DatapackBuiltinEntriesProvider
import net.minecraftforge.registries.ForgeRegistries
import net.minecraftforge.common.data.BlockTagsProvider
import net.minecraftforge.common.util.NonNullFunction
import net.minecraftforge.registries.RegistryObject
import kotlin.reflect.jvm.internal.impl.resolve.calls.inference.CapturedType

/*?}*/

/*? if neoforge {*/
/*import net.neoforged.neoforge.registries.DeferredRegister
import net.neoforged.neoforge.common.data.DatapackBuiltinEntriesProvider
import net.neoforged.neoforge.registries.DeferredHolder
import net.neoforged.neoforge.registries.NeoForgeRegistries
import net.neoforged.neoforge.common.data.BlockTagsProvider
*//*?}*/

class DeltaboxRegistrate(modId: String): AbstractRegistrate<DeltaboxRegistrate>(modId) {
  // Special Blocks lists
  private val FLAMMABLE_BLOCKS: MutableList<Triple<BlockEntry<out Block>, Int, Int>> = mutableListOf()
  private val STRIPPABLE_BLOCKS: MutableList<Pair<BlockEntry<out Block>, Supplier<out Block>>> = mutableListOf()
  private val POTTED_BLOCKS: MutableList<Pair<BlockEntry<out Block>, BlockEntry<out Block>>> = mutableListOf()
  private val CUTOUT_RENDERS: MutableList<BlockEntry<out Block>> = mutableListOf()

  // Recipe slices list
  val RECIPES: MutableList<Supplier<out DeltaboxRecipeSlice>> = mutableListOf()

  fun <T : DeltaboxRecipeSlice> recipe(factory: Supplier<out T>) {
    RECIPES.add(factory)
  }

  // flammable
  fun addFlammableBlock(block: BlockEntry<out Block>, burnChance: Int, spreadChance: Int) {
    FLAMMABLE_BLOCKS.add(Triple(block, burnChance, spreadChance))
  }

  fun getFlammableBlocks(): List<Triple<BlockEntry<out Block>, Number, Number>> {
    return FLAMMABLE_BLOCKS
  }

  // strippable
  fun addStrippableBlock(block: BlockEntry<out Block>, strippedBlock: Supplier<out Block>) {
    STRIPPABLE_BLOCKS.add(Pair(block, strippedBlock))
  }

  fun getStrippableBlocks(): List<Pair<BlockEntry<out Block>, Supplier<out Block>>> {
    return STRIPPABLE_BLOCKS
  }

  // potted
  fun addPottedBlock(block: BlockEntry<out Block>, pottedBlock: BlockEntry<out Block>) {
    POTTED_BLOCKS.add(Pair(block, pottedBlock))
  }

  // cutout
  fun addCutoutRender(block: BlockEntry<out Block>) {
    CUTOUT_RENDERS.add(block)
  }

  fun getCutoutRenders(): List<BlockEntry<out Block>> {
    return CUTOUT_RENDERS
  }

  // Villager trades
  val TRADES: MutableList<VillagerTradeCodec> = ArrayList()
  val WANDERER_TRADES: MutableList<WandererTradeCodec> = ArrayList()

  fun villagerTrade(
    profession: VillagerProfession,
    level: VillagerLevel,
    tradeCosts: List<VillagerTradeItem>,
    tradeSells: List<VillagerTradeItem>,
    maxUses: Int,
    xpAmount: Int,
    priceMultiplier: Float
  ) {
      TRADES.add(VillagerTradeCodec(profession, level, tradeCosts, tradeSells, maxUses, xpAmount, priceMultiplier))
  }

  fun wandererTrade(
    rarity: WandererTradeRarity,
    tradeCosts: List<VillagerTradeItem>,
    tradeSells: List<VillagerTradeItem>,
    maxUses: Int,
    xpAmount: Int,
    priceMultiplier: Float
  ) {
      WANDERER_TRADES.add(WandererTradeCodec(rarity, tradeCosts, tradeSells, maxUses, xpAmount, priceMultiplier))
  }

  // Creative tabs
  init {
    this.defaultCreativeTab(null as ResourceKey<CreativeModeTab>?) // IMPORTANT: remove the default creative tab to avoid duplicate entries
  }

  /*? if forge {*/
  private val CREATIVE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, modid)

  fun creativeTab(
    name: String,
    icon: () -> ItemStack,
    displayItems: CreativeModeTab.DisplayItemsGenerator,
    title: String? = null
  ) {
    CREATIVE_TABS.register(name) {
      CreativeModeTab.builder()
        .title(if (title != null) Component.literal(title) else Component.translatable("itemGroup.${modid}.$name"))
        .icon(icon)
        .displayItems(displayItems)
        .build()
    }
  }
  /*?} elif neoforge {*/
  /*private val CREATIVE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, modId)

  fun creativeTab(
    name: String,
    icon: () -> ItemStack,
    displayItems: CreativeModeTab.DisplayItemsGenerator,
    title: String? = null
  ) {
    CREATIVE_TABS.register(name) { _->
      CreativeModeTab.builder()
        .title(if (title != null) Component.literal(title) else Component.translatable("itemGroup.${modid}.$name"))
        .icon(icon)
        .displayItems(displayItems)
        .build()
    }
  }
  *//*?} elif fabric {*/
  /*fun creativeTab(
    name: String,
    icon: () -> ItemStack,
    displayItems: CreativeModeTab.DisplayItemsGenerator,
    title: String? = null
  ) {
    Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB, name, FabricItemGroup.builder()
      .title(if (title != null) Component.literal(title) else Component.translatable("itemGroup.${modid}.$name"))
      .icon(icon)
      .displayItems(displayItems)
      .build()
    )
  }
  *//*?}*/

  // @ Lang
  fun addFormulaLang(
    formula: String,
    name: String,
    mId: String = modid,
  ) {
    addRawLang("formula.${mId}.$formula", name)
  }

  fun addEntityLang(
    name: String,
    phrase: String,
    mId: String = modid,
  ) {
    addRawLang("entity.${mId}.$name", phrase)
  }

  fun addItemTooltipLang(
    itemId: String,
    phrase: String,
    mId: String = modid,
  ) {
    addRawLang(DeltaboxUtil.LANG.getTooltipKey(mId, itemId), phrase)
  }

  fun addGenericTooltipLang(
    itemId: String,
    phrase: String,
  ) {
    addRawLang(DeltaboxUtil.LANG.getTooltipKey(null, itemId), phrase)
  }

  fun addCreativeTabLang(
    tab: String,
    name: String,
    mId: String = modid,
  ) {
    addRawLang("itemGroup.${mId}.$tab", name)
  }

  fun addPotionLang(
    name: String,
    phrase: String,
  ) {
    addRawLang("item.${"minecraft"}.potion.effect.$name", "Potion of $phrase")
    addRawLang("item.${"minecraft"}.splash_potion.effect.$name", "Splash Potion of $phrase")
    addRawLang("item.${"minecraft"}.lingering_potion.effect.$name", "Lingering Potion of $phrase")
    addRawLang("item.${"minecraft"}.tipped_arrow.effect.$name", "Arrow of $phrase")
  }

  fun addAdvancementLang(
    name: String,
    title: String,
    description: String,
    mId: String = modid,
  ) {
    addRawLang("advancements.${mId}.$name.title", title)
    addRawLang("advancements.${mId}.$name.description", description)
  }

  fun addEffectLang(
    name: String,
    phrase: String,
    mId: String = modid,
  ) {
    addRawLang("effect.${mId}.$name", phrase)
  }

  fun addDeathMessageLang(
    name: String,
    phrase: String,
    mId: String = modid,
  ) {
    addRawLang("death.attack.$name", phrase)
  }

  fun addBiomeLang(
    name: String,
    phrase: String,
    mId: String = modid,
  ) {
    addRawLang("biome.${mId}.$name", phrase)
  }

  fun addSoundLang(
    name: String,
    phrase: String,
    mId: String = modid,
  ) {
    addRawLang("sound.${mId}.$name", phrase)
  }

  fun addDimensionLang(
    name: String,
    phrase: String,
    mId: String = modid,
  ) {
    addRawLang("dimension.${mId}.$name", phrase)
  }

  fun addWorldPresetLang(
    name: String,
    phrase: String,
    mId: String = modid,
  ) {
    addRawLang("generator.${mId}.$name", phrase)
  }

  fun addPaintingVariantLang(
    name: String,
    phrase: String,
    author: String,
    mId: String = modid,
  ) {
    addRawLang("painting.${mId}.$name.title", phrase)
    addRawLang("painting.${mId}.$name.author", author)
  }

  // @ Placer Types

  /*? if forge || neoforge {*/
  val FOLIAGE_PLACER_TYPES = DeferredRegister.create(Registries.FOLIAGE_PLACER_TYPE, modId)
  val TRUNK_PLACER_TYPES = DeferredRegister.create(Registries.TRUNK_PLACER_TYPE, modId)

  /*? if >1.21 {*/
  /*fun trunkPlacer(name: String, codec: Supplier<com.mojang.serialization.MapCodec<out TrunkPlacer>>): DeferredHolder<TrunkPlacerType<*>, TrunkPlacerType<out TrunkPlacer>> {
    return TRUNK_PLACER_TYPES.register(name) { r ->  TrunkPlacerType(codec.get()) }
  }

  fun foliagePlacer(name: String, codec: Supplier<com.mojang.serialization.MapCodec<out FoliagePlacer>>): DeferredHolder<FoliagePlacerType<*>, FoliagePlacerType<out FoliagePlacer>> {
    return FOLIAGE_PLACER_TYPES.register(name) { r ->  FoliagePlacerType(codec.get()) }
  }
  *//*?} else {*/
  fun trunkPlacer(name: String, codec: Supplier<com.mojang.serialization.Codec<out TrunkPlacer>>): net.minecraftforge.registries.RegistryObject<TrunkPlacerType<*>> {
    return TRUNK_PLACER_TYPES.register(name) { TrunkPlacerType(codec.get()) }
  }

  fun foliagePlacer(name: String, codec: Supplier<com.mojang.serialization.Codec<out FoliagePlacer>>): net.minecraftforge.registries.RegistryObject<FoliagePlacerType<*>>{
    return FOLIAGE_PLACER_TYPES.register(name) { FoliagePlacerType(codec.get()) }
  }
  /*?}*/
  /*?} elif fabric {*/
  /*/^? if >=1.21 {^/
  /^fun trunkPlacer(name: String, codec: Supplier<com.mojang.serialization.MapCodec<out TrunkPlacer>>): Supplier<TrunkPlacerType<*>> {
    return Supplier { Registry.register(BuiltInRegistries.TRUNK_PLACER_TYPE, name, TrunkPlacerType(codec.get())) }
  }

  fun foliagePlacer(name: String, codec: Supplier<com.mojang.serialization.MapCodec<out FoliagePlacer>>): Supplier<FoliagePlacerType<*>> {
    return Supplier { Registry.register(BuiltInRegistries.FOLIAGE_PLACER_TYPE, name, FoliagePlacerType(codec.get())) }
  }
  ^//^?} else {^/
  fun trunkPlacer(name: String, codec: Supplier<com.mojang.serialization.Codec<out TrunkPlacer>>): Supplier<TrunkPlacerType<*>> {
    return Supplier { Registry.register(BuiltInRegistries.TRUNK_PLACER_TYPE, name, TrunkPlacerType(codec.get())) }
  }

  fun foliagePlacer(name: String, codec: Supplier<com.mojang.serialization.Codec<out FoliagePlacer>>): Supplier<FoliagePlacerType<*>> {
    return Supplier { Registry.register(BuiltInRegistries.FOLIAGE_PLACER_TYPE, name, FoliagePlacerType(codec.get())) }
  }
  /^?}^/

  *//*?}*/

  // @ Configured Features
  private val CONFIGURED_FEATURES: MutableMap<ResourceKey<ConfiguredFeature<*, *>>, (ResourceKey<ConfiguredFeature<*, *>>, BootstrapContext<ConfiguredFeature<*, *>>, ConfiguredFeaturesUtil) -> Unit> = mutableMapOf()

  fun configuredFeature(
      name: String,
      consumer: (ResourceKey<ConfiguredFeature<*, *>>, BootstrapContext<ConfiguredFeature<*, *>>, ConfiguredFeaturesUtil) -> Unit
  ): ResourceKey<ConfiguredFeature<*, *>> {
      val key = ConfiguredFeaturesUtil.registerKey(name, this.modid)
      CONFIGURED_FEATURES[key] = consumer
      return key
  }

  private fun bootstrapConfiguredfeatures(context: BootstrapContext<ConfiguredFeature<*, *>>) {
    CONFIGURED_FEATURES.forEach { key, consumer ->
      consumer(key, context, ConfiguredFeaturesUtil)
    }
  }

  // @ Placed Features
  private val PLACED_FEATURES: MutableMap<ResourceKey<PlacedFeature>, (ResourceKey<PlacedFeature>, BootstrapContext<PlacedFeature>, PlacedFeaturesUtil) -> Unit> = mutableMapOf()

  fun placedFeature(
    name: String,
    consumer: (ResourceKey<PlacedFeature>, BootstrapContext<PlacedFeature>, PlacedFeaturesUtil) -> Unit
  ): ResourceKey<PlacedFeature> {
    val key = PlacedFeaturesUtil.registerKey(name, this.modid)
    PLACED_FEATURES[key] = consumer
    return key
  }

  private fun bootstrapPlacedFeatures(context: BootstrapContext<PlacedFeature>){
    PLACED_FEATURES.forEach { key, consumer ->
      consumer(key, context, PlacedFeaturesUtil)
    }
  }

  // @ Biome Modifiers

  /*? if forge {*/
  private val BIOME_MODIFIERS: MutableMap<ResourceKey<net.minecraftforge.common.world.BiomeModifier>, Triple<TagKey<Biome>, ResourceKey<PlacedFeature>, GenerationStep.Decoration>> = mutableMapOf()

  fun biomeModifier(
    name: String,
    biome: TagKey<Biome>,
    placedFeature: ResourceKey<PlacedFeature>,
    step: GenerationStep.Decoration
  ): ResourceKey<net.minecraftforge.common.world.BiomeModifier> {
    val key = BiomeModifiersUtil.registerKey(name, this.modid)
    BIOME_MODIFIERS[key] = Triple(biome, placedFeature, step)
    return key
  }

  private fun bootstrapBiomeModifiers(context: BootstrapContext<net.minecraftforge.common.world.BiomeModifier>){
    BIOME_MODIFIERS.forEach { key, props ->
      val biomes = BiomeModifiersUtil.lookupBiomeNamed(context, props.first)
      val feature = BiomeModifiersUtil.lookupPlacedFeatureDirect(context, props.second)
      val step = props.third
      context.register(key, net.minecraftforge.common.world.ForgeBiomeModifiers.AddFeaturesBiomeModifier(
        biomes, feature, step
      ))
    }
  }
  /*?} elif neoforge {*/
  /*private val BIOME_MODIFIERS: MutableMap<ResourceKey<net.neoforged.neoforge.common.world.BiomeModifier>, Triple<TagKey<Biome>, ResourceKey<PlacedFeature>, GenerationStep.Decoration>> = mutableMapOf()

  fun biomeModifier(
    name: String,
    biome: TagKey<Biome>,
    placedFeature: ResourceKey<PlacedFeature>,
    step: GenerationStep.Decoration
  ): ResourceKey<net.neoforged.neoforge.common.world.BiomeModifier> {
    val key = BiomeModifiersUtil.registerKey(name, this.modid)
    BIOME_MODIFIERS[key] = Triple(biome, placedFeature, step)
    return key
  }

  private fun bootstrapBiomeModifiers(context: BootstrapContext<net.neoforged.neoforge.common.world.BiomeModifier>){
    BIOME_MODIFIERS.forEach { key, props ->
      val biomes = BiomeModifiersUtil.lookupBiomeNamed(context, props.first)
      val feature = BiomeModifiersUtil.lookupPlacedFeatureDirect(context, props.second)
      val step = props.third
      context.register(key, net.neoforged.neoforge.common.world.BiomeModifiers.AddFeaturesBiomeModifier(
        biomes, feature, step
      ))
    }
  }

  *//*?} elif fabric {*/
  /*fun biomeModifier(
    name: String,
    biome: TagKey<Biome>,
    placedFeature: ResourceKey<PlacedFeature>,
    step: GenerationStep.Decoration
  ) {
    BiomeModifications.addFeature(BiomeSelectors.tag(biome), step, placedFeature)
  }
  *//*?}*/

  // @ Tags
  private val BIOME_TAGS_CONSUMERS: MutableMap<TagKey<Biome>, Tuple<MutableList<TagKey<Biome>>, MutableList<Supplier<ResourceKey<Biome>>>>> = mutableMapOf()
  private val BLOCK_TAGS_CONSUMERS: MutableMap<TagKey<Block>, Tuple<MutableList<TagKey<Block>>, MutableList<Supplier<Block>>>> = mutableMapOf()
  private val ITEM_TAGS_CONSUMERS: MutableMap<TagKey<Item>, Tuple<MutableList<TagKey<Item>>, MutableList<Supplier<Item>>>> = mutableMapOf()
  private val FLUID_TAGS_CONSUMERS: MutableMap<TagKey<Fluid>, Tuple<MutableList<TagKey<Fluid>>, MutableList<Supplier<Fluid>>>> = mutableMapOf()
  private val ENTITY_TAGS_CONSUMERS: MutableMap<TagKey<EntityType<*>>, Tuple<MutableList<TagKey<EntityType<*>>>, MutableList<Supplier<EntityType<*>>>>> = mutableMapOf()
  private val PAINTING_TAGS_CONSUMERS: MutableMap<TagKey<PaintingVariant>, Tuple<MutableList<TagKey<PaintingVariant>>, MutableList<Supplier<ResourceKey<PaintingVariant>>>>> = mutableMapOf()
  private val WORLD_PRESETS_TAGS_CONSUMERS: MutableMap<TagKey<WorldPreset>, Tuple<MutableList<TagKey<WorldPreset>>, MutableList<Supplier<ResourceKey<WorldPreset>>>>> = mutableMapOf()

  fun biomeTags(tag: TagKey<Biome>, vararg tags: TagKey<Biome>) {
    if(BIOME_TAGS_CONSUMERS[tag] == null) BIOME_TAGS_CONSUMERS[tag] = Tuple(mutableListOf(), mutableListOf())
    BIOME_TAGS_CONSUMERS[tag]?.a?.addAll(tags)
  }
  fun biomeTags(tag: TagKey<Biome>, vararg items: Supplier<ResourceKey<Biome>>) {
    if(BIOME_TAGS_CONSUMERS[tag] == null) BIOME_TAGS_CONSUMERS[tag] = Tuple(mutableListOf(), mutableListOf())
    BIOME_TAGS_CONSUMERS[tag]?.b?.addAll(items)
  }
  fun blockTags(tag: TagKey<Block>, vararg tags: TagKey<Block>) {
    if(BLOCK_TAGS_CONSUMERS[tag] == null) BLOCK_TAGS_CONSUMERS[tag] = Tuple(mutableListOf(), mutableListOf())
    BLOCK_TAGS_CONSUMERS[tag]?.a?.addAll(tags)
  }
  fun blockTags(tag: TagKey<Block>, vararg items: Supplier<Block>) {
    if(BLOCK_TAGS_CONSUMERS[tag] == null) BLOCK_TAGS_CONSUMERS[tag] = Tuple(mutableListOf(), mutableListOf())
    BLOCK_TAGS_CONSUMERS[tag]?.b?.addAll(items)
  }
  fun itemTags(tag: TagKey<Item>, vararg tags: TagKey<Item>) {
    if(ITEM_TAGS_CONSUMERS[tag] == null) ITEM_TAGS_CONSUMERS[tag] = Tuple(mutableListOf(), mutableListOf())
    ITEM_TAGS_CONSUMERS[tag]?.a?.addAll(tags)
  }
  fun itemTags(tag: TagKey<Item>, vararg items: Supplier<Item>) {
    if(ITEM_TAGS_CONSUMERS[tag] == null) ITEM_TAGS_CONSUMERS[tag] = Tuple(mutableListOf(), mutableListOf())
    ITEM_TAGS_CONSUMERS[tag]?.b?.addAll(items)
  }
  fun fluidTags(tag: TagKey<Fluid>, vararg tags: TagKey<Fluid>) {
    if(FLUID_TAGS_CONSUMERS[tag] == null) FLUID_TAGS_CONSUMERS[tag] = Tuple(mutableListOf(), mutableListOf())
    FLUID_TAGS_CONSUMERS[tag]?.a?.addAll(tags)
  }
  fun fluidTags(tag: TagKey<Fluid>, vararg items: Supplier<Fluid>) {
    if(FLUID_TAGS_CONSUMERS[tag] == null) FLUID_TAGS_CONSUMERS[tag] = Tuple(mutableListOf(), mutableListOf())
    FLUID_TAGS_CONSUMERS[tag]?.b?.addAll(items)
  }
  fun entityTags(tag: TagKey<EntityType<*>>, vararg tags: TagKey<EntityType<*>>) {
    if(ENTITY_TAGS_CONSUMERS[tag] == null) ENTITY_TAGS_CONSUMERS[tag] = Tuple(mutableListOf(), mutableListOf())
    ENTITY_TAGS_CONSUMERS[tag]?.a?.addAll(tags)
  }
  fun entityTags(tag: TagKey<EntityType<*>>, vararg items: Supplier<EntityType<*>>) {
    if(ENTITY_TAGS_CONSUMERS[tag] == null) ENTITY_TAGS_CONSUMERS[tag] = Tuple(mutableListOf(), mutableListOf())
    ENTITY_TAGS_CONSUMERS[tag]?.b?.addAll(items)
  }
  fun paintingTags(tag: TagKey<PaintingVariant>, vararg tags: TagKey<PaintingVariant>) {
    if(PAINTING_TAGS_CONSUMERS[tag] == null) PAINTING_TAGS_CONSUMERS[tag] = Tuple(mutableListOf(), mutableListOf())
    PAINTING_TAGS_CONSUMERS[tag]?.a?.addAll(tags)
  }
  fun paintingTags(tag: TagKey<PaintingVariant>, vararg items: Supplier<ResourceKey<PaintingVariant>>) {
    if(PAINTING_TAGS_CONSUMERS[tag] == null) PAINTING_TAGS_CONSUMERS[tag] = Tuple(mutableListOf(), mutableListOf())
    PAINTING_TAGS_CONSUMERS[tag]?.b?.addAll(items)
  }
  fun worldPresetTags(tag: TagKey<WorldPreset>, vararg tags: TagKey<WorldPreset>) {
    if(WORLD_PRESETS_TAGS_CONSUMERS[tag] == null) WORLD_PRESETS_TAGS_CONSUMERS[tag] = Tuple(mutableListOf(), mutableListOf())
    WORLD_PRESETS_TAGS_CONSUMERS[tag]?.a?.addAll(tags)
  }
  fun worldPresetTags(tag: TagKey<WorldPreset>, vararg items: Supplier<ResourceKey<WorldPreset>>) {
    if(WORLD_PRESETS_TAGS_CONSUMERS[tag] == null) WORLD_PRESETS_TAGS_CONSUMERS[tag] = Tuple(mutableListOf(), mutableListOf())
    WORLD_PRESETS_TAGS_CONSUMERS[tag]?.b?.addAll(items)
  }

  // @ Boat Variants
  private val BOAT_VARIANTS: MutableList<String> = mutableListOf()
  fun boatVariant(name: String) {
    BOAT_VARIANTS.add(name)
  }

  // @ Model Layers
  private val MODEL_LAYERS: MutableMap<String, Pair<Supplier<LayerDefinition>, String>> = mutableMapOf()
  fun modelLayer(path: String, model: Supplier<LayerDefinition>, folder: String = "main"){
    MODEL_LAYERS[path] = Pair(model, folder)
  }

  // @ Particles

  /*? if forge {*/
  private val PARTICLE_TYPES = DeferredRegister.create(Registries.PARTICLE_TYPE, modId)
  private val PARTICLE_REGISTRATIONS = mutableListOf<ParticleRegistration<out ParticleOptions>>()
  private class ParticleRegistration<T : ParticleOptions>(
    val type: Supplier<ParticleType<T>>,
    val provider: (sprite: SpriteSet) -> ParticleProvider<T>
  )
  fun <T : ParticleOptions> particleType(
    name: String, supplier: Supplier<ParticleType<T>>,
    provider: (sprite: SpriteSet) -> ParticleProvider<T>
  ): Supplier<ParticleType<T>> {
    val type = PARTICLE_TYPES.register(name, supplier)
    PARTICLE_REGISTRATIONS.add(ParticleRegistration({ type.get() }, provider))
    return Supplier { type.get() }
  }
  /*?} elif neoforge {*/
  /*private val PARTICLE_TYPES = DeferredRegister.create(Registries.PARTICLE_TYPE, modId)
  private val PARTICLE_REGISTRATIONS = mutableListOf<ParticleRegistration<out ParticleOptions>>()
  private class ParticleRegistration<T : ParticleOptions>(
    val type: Supplier<ParticleType<T>>,
    val provider: (sprite: SpriteSet) -> ParticleProvider<T>
  )
  fun <T : ParticleOptions> particleType(
    name: String, supplier: Supplier<ParticleType<T>>,
    provider: (sprite: SpriteSet) -> ParticleProvider<T>
  ): Supplier<ParticleType<T>> {
    val type = PARTICLE_TYPES.register(name, supplier)
    PARTICLE_REGISTRATIONS.add(ParticleRegistration({ type.get() }, provider))
    return Supplier { type.get() }
  }
  *//*?} elif fabric {*/
  /*private val PARTICLE_REGISTRATIONS = mutableListOf<ParticleRegistration<out ParticleOptions>>()
  private class ParticleRegistration<T : ParticleOptions>(
    val type: Supplier<ParticleType<T>>,
    val provider: (sprite: SpriteSet) -> ParticleProvider<T>
  )
  fun <T : ParticleOptions> particleType(
    name: String, supplier: Supplier<ParticleType<T>>,
    provider: (sprite: SpriteSet) -> ParticleProvider<T>
  ): Supplier<ParticleType<T>> {
    /^? if <1.21 {^/
    val type = Registry.register(BuiltInRegistries.PARTICLE_TYPE, DeltaboxUtil.resourceLocation(modid, name), FabricParticleTypes.complex(supplier.get().deserializer))
    /^?} else {^/
    /^val type = Registry.register(BuiltInRegistries.PARTICLE_TYPE, DeltaboxUtil.resourceLocation(modid, name), FabricParticleTypes.complex(supplier.get().codec(), supplier.get().streamCodec()))
    ^//^?}^/
    PARTICLE_REGISTRATIONS.add(ParticleRegistration({ type }, provider))
    return Supplier { type }
  }
  *//*?}*/

  // FABRIC SPECIFIC BLOCKS FEATURES REGISTRATION
  /*? if fabric {*/
  /*override fun register() {
    super.register()
    // register flammable blocks
    onRegisterFlammableBlocks()

    // register strippable blocks
    onRegisterStrippableBlocks()

    // load datapack contents
    onDatapackReload()
  }

  @Environment(EnvType.CLIENT)
  fun registerClient(){
    BlockRenderLayerMap.INSTANCE.putBlocks(net.minecraft.client.renderer.RenderType.cutout(), *CUTOUT_RENDERS.map { it.get() }.toTypedArray())
    BOAT_VARIANTS.forEach { t ->
      EntityModelLayerRegistry.registerModelLayer(ModelLayerLocation(DeltaboxUtil.resourceLocation(modid, "boat/${t}"), "main"), BoatModel::createBodyModel)
      EntityModelLayerRegistry.registerModelLayer(ModelLayerLocation(DeltaboxUtil.resourceLocation(modid, "chest_boat/${t}"), "main"), ChestBoatModel::createBodyModel)
    }
    MODEL_LAYERS.forEach { path, (model, folder) ->
      EntityModelLayerRegistry.registerModelLayer(ModelLayerLocation(DeltaboxUtil.resourceLocation(modid, path), folder), model as TexturedModelDataProvider)
    }
    PARTICLE_REGISTRATIONS.forEach { particle ->
      handleParticleRegistration(particle)
    }
  }

  private fun <T : ParticleOptions> handleParticleRegistration(
    registration: ParticleRegistration<T>
  ) {
    ParticleFactoryRegistry.getInstance().register(registration.type.get(), registration.provider)
  }

  private fun onDatapackReload() {
    val registry: (ResourceLocation, PreparableReloadListener) -> Unit = { id, listener ->
        ResourceManagerHelper.get(PackType.SERVER_DATA).registerReloadListener(object : IdentifiableResourceReloadListener {
            override fun getFabricId(): ResourceLocation = id

            override fun reload(
                synchronizer: PreparationBarrier,
                manager: ResourceManager,
                prepareProfiler: ProfilerFiller,
                applyProfiler: ProfilerFiller,
                prepareExecutor: Executor,
                applyExecutor: Executor
            ): CompletableFuture<Void> {
                return listener.reload(synchronizer, manager, prepareProfiler, applyProfiler, prepareExecutor, applyExecutor)
            }
        })
    }

    // registries
    registry(DeltaboxUtil.resourceLocation(modid, VillagerTradeDeserializer.PATH), VillagerTradeDeserializer(this))
    registry(DeltaboxUtil.resourceLocation(modid, WandererTradeDeserializer.PATH), WandererTradeDeserializer(this))

    // call other events
    net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents.SERVER_STARTED.register(::onServerStarted);
    net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents.END_DATA_PACK_RELOAD.register(::onEndDatapackReload);
  }

  private fun onServerStarted(server: MinecraftServer) {
    handleLoadVillagerTrades()
  }

  private fun onEndDatapackReload(server: MinecraftServer, resourceManager: CloseableResourceManager, success: Boolean) {
    handleLoadVillagerTrades()
  }

  private fun handleLoadVillagerTrades() {
    val tradesByProfession: MutableMap<Pair<VillagerProfession, VillagerLevel>, MutableList<VillagerTradeCodec>> = mutableMapOf()
    val wandererTradesByRarity: MutableMap<WandererTradeRarity, MutableList<WandererTradeCodec>> = mutableMapOf()

    TRADES.map {
      val pair = Pair(it.profession, it.level)
      val currentList = (tradesByProfession[pair]?: mutableListOf())
      currentList.add(it)
      tradesByProfession[pair] = currentList
    }

    WANDERER_TRADES.forEach {
      val currentList = (wandererTradesByRarity[it.rarity]?: mutableListOf())
      currentList.add(it)
      wandererTradesByRarity[it.rarity] = currentList
    }

    tradesByProfession.forEach { t, u ->
      TradeOfferHelper.registerVillagerOffers(t.first, t.second.toInt(), { factories ->
        u.forEach {
          factories.add({e, r->
            MerchantOffer(
              /^? if >1.21 {^/
              /^net.minecraft.world.item.trading.ItemCost(it.tradeCosts.first().item.get(), it.tradeCosts.first().amount),
              ^//^?} else {^/
              ItemStack(it.tradeCosts.first().item.get(), it.tradeCosts.first().amount),
              /^?}^/
              ItemStack(it.tradeSells.first().item.get(), it.tradeSells.first().amount),
              it.maxUses,
              it.xpAmount,
              it.priceMultiplier
            )
          })
        }
      })
    }

    wandererTradesByRarity.forEach { t, u ->
      TradeOfferHelper.registerWanderingTraderOffers(t.toInt(),  { factories ->
        u.forEach {
          factories.add({e, r->
            MerchantOffer(
              /^? if >1.21 {^/
              /^net.minecraft.world.item.trading.ItemCost(it.tradeCosts.first().item.get(), it.tradeCosts.first().amount),
              ^//^?} else {^/
              ItemStack(it.tradeCosts.first().item.get(), it.tradeCosts.first().amount),
              /^?}^/
              ItemStack(it.tradeSells.first().item.get(), it.tradeSells.first().amount),
              it.maxUses,
              it.xpAmount,
              it.priceMultiplier
            )
          })
        }
      })
    }
  }

  private fun onRegisterStrippableBlocks() {
    STRIPPABLE_BLOCKS.map {
      StrippableBlockRegistry.register(it.first.get(), it.second.get())
    }
  }

  private fun onRegisterFlammableBlocks() {
    FLAMMABLE_BLOCKS.map {
      FlammableBlockRegistry.getDefaultInstance().add(it.first.get(), it.second.toInt(), it.third.toInt())
    }
  }
  *//*?}*/

  /*? if forge {*/
  fun register(bus: net.minecraftforge.eventbus.api.IEventBus, forgeBus: net.minecraftforge.eventbus.api.IEventBus) {
    super.registerEventListeners(bus)

    CREATIVE_TABS.register(bus)
    TRUNK_PLACER_TYPES.register(bus)
    FOLIAGE_PLACER_TYPES.register(bus)
    PARTICLE_TYPES.register(bus)

    // register pot plants
    onRegisterFlowerPots(bus)

    // datapack reload listeners
    onDatapackReload(forgeBus)

    // load villager trades
    onLoadVillagerTrades(forgeBus)
    // load wanderer trades
    onLoadWandererTrades(forgeBus)
  }

  fun registerClient(bus: net.minecraftforge.eventbus.api.IEventBus, forgeBus: net.minecraftforge.eventbus.api.IEventBus) {
    bus.addListener { event: net.minecraftforge.client.event.EntityRenderersEvent.RegisterLayerDefinitions ->
      BOAT_VARIANTS.forEach { t ->
        event.registerLayerDefinition(ModelLayerLocation(DeltaboxUtil.resourceLocation(modid, "boat/${t}"), "main"), BoatModel::createBodyModel);
        event.registerLayerDefinition(ModelLayerLocation(DeltaboxUtil.resourceLocation(modid, "chest_boat/${t}"), "main"), ChestBoatModel::createBodyModel);
      }
      MODEL_LAYERS.forEach { path, (model, folder) ->
        event.registerLayerDefinition(ModelLayerLocation(DeltaboxUtil.resourceLocation(modid, path), folder), model);
      }
    }
    bus.addListener { event: net.minecraftforge.client.event.RegisterParticleProvidersEvent ->
      PARTICLE_REGISTRATIONS.forEach { registration ->
        handleParticleRegistration(event, registration)
      }
    }
  }

  private fun <T : ParticleOptions> handleParticleRegistration(
    event: net.minecraftforge.client.event.RegisterParticleProvidersEvent,
    registration: ParticleRegistration<T>
  ) {
    event.registerSpriteSet(registration.type.get(), registration.provider)
  }

  private fun onDatapackReload(forgeBus: net.minecraftforge.eventbus.api.IEventBus) {
    forgeBus.addListener(net.minecraftforge.eventbus.api.EventPriority.HIGH) { event: net.minecraftforge.event.AddReloadListenerEvent ->
      val registry = BiConsumer<ResourceLocation, PreparableReloadListener> { id, listener -> event.addListener(listener) }
      // deserialize villager trades
      registry.accept(DeltaboxUtil.resourceLocation(modid, VillagerTradeDeserializer.PATH), VillagerTradeDeserializer(this))
      // deserialize wanderer trades
      registry.accept(DeltaboxUtil.resourceLocation(modid, WandererTradeDeserializer.PATH), WandererTradeDeserializer(this))
    }
  }

  private fun onLoadVillagerTrades(forgeBus: net.minecraftforge.eventbus.api.IEventBus) {
    forgeBus.addListener { event: net.minecraftforge.event.village.VillagerTradesEvent ->
      TRADES.forEach { trade ->
        if(event.type == trade.profession){
          event.trades[trade.level.toInt()].add { _, _ -> MerchantOffer(ItemStack(trade.tradeCosts.first().item.get(), trade.tradeCosts.first().amount), ItemStack(trade.tradeSells.first().item.get(), trade.tradeSells.first().amount), trade.maxUses, trade.xpAmount, trade.priceMultiplier) }
        }
      }
    }
  }

  private fun onLoadWandererTrades(forgeBus: net.minecraftforge.eventbus.api.IEventBus) {
    forgeBus.addListener { event: net.minecraftforge.event.village.WandererTradesEvent ->
      val genericTrades = event.genericTrades
      val rareTrades = event.rareTrades
      WANDERER_TRADES.forEach { trade ->
        if(trade.rarity == WandererTradeRarity.GENERIC) {
          genericTrades.add { _, _ -> MerchantOffer(ItemStack(trade.tradeCosts.first().item.get(), trade.tradeCosts.first().amount), ItemStack(trade.tradeSells.first().item.get(), trade.tradeSells.first().amount), trade.maxUses, trade.xpAmount, trade.priceMultiplier) }
        } else {
          rareTrades.add { _, _ -> MerchantOffer(ItemStack(trade.tradeCosts.first().item.get(), trade.tradeCosts.first().amount), ItemStack(trade.tradeSells.first().item.get(), trade.tradeSells.first().amount), trade.maxUses, trade.xpAmount, trade.priceMultiplier) }
        }
      }
    }
  }

  private fun onRegisterFlowerPots(bus: net.minecraftforge.eventbus.api.IEventBus) {
    bus.addListener { e: net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent ->
      POTTED_BLOCKS.forEach { (plant, pot) ->
        try {
          (Blocks.FLOWER_POT as FlowerPotBlock).addPlant(plant.id, pot)
        } catch (e: Exception) {
          println("Failed to add plant ${plant.get().name} to flower pot ${pot.get().name}")
        }
      }
    }
  }

  // @ Forge Datagen
  fun gatherData(event: net.minecraftforge.data.event.GatherDataEvent) {
    event.generator.addProvider(event.includeServer(), com.dannbrown.deltaboxlib.platform.registrate.generators.trades.VillagerTradeProvider(this, event.generator))
    event.generator.addProvider(event.includeServer(), com.dannbrown.deltaboxlib.platform.registrate.generators.trades.WandererTradeProvider(this, event.generator))
    event.generator.addProvider(event.includeServer(), com.dannbrown.deltaboxlib.platform.registrate.generators.recipe.DeltaboxRecipeProvider(this, event.generator, event.lookupProvider))

    // Other Data Generators
    val registrySetBuilder = RegistrySetBuilder()
      .add(Registries.CONFIGURED_FEATURE, ::bootstrapConfiguredfeatures)
      .add(Registries.PLACED_FEATURE, ::bootstrapPlacedFeatures)
      .add(ForgeRegistries.Keys.BIOME_MODIFIERS, ::bootstrapBiomeModifiers)
    event.generator.addProvider(event.includeServer(), object : DatapackBuiltinEntriesProvider(event.generator.packOutput, event.lookupProvider, registrySetBuilder, mutableSetOf(this.modid)) {})

    // Biome Tags
    event.generator.addProvider(
      event.includeServer(),
      object : BiomeTagsProvider(event.generator.packOutput, event.lookupProvider, this.modid, event.existingFileHelper) {
        override fun getName(): String {return "${this.modId} Biome Tags"}
        override fun addTags(arg: HolderLookup.Provider) { BIOME_TAGS_CONSUMERS.forEach { tag(it.key).addTags(*it.value.a.toTypedArray()).add(*it.value.b.map { b -> b.get() }.toTypedArray()) } }
      }
    )
    // Block Tags
    val blockTags = object : BlockTagsProvider(event.generator.packOutput, event.lookupProvider, this.modid, event.existingFileHelper) {
      override fun getName(): String {return "${this.modId} Block Tags"}
      override fun addTags(arg: HolderLookup.Provider) { BLOCK_TAGS_CONSUMERS.forEach { tag(it.key).addTags(*it.value.a.toTypedArray()).add(*it.value.b.map { b -> b.get() }.toTypedArray()) } }
    }
    event.generator.addProvider(event.includeServer(), blockTags)
    // Item Tags
    event.generator.addProvider(
      event.includeServer(),
      object : ItemTagsProvider(event.generator.packOutput, event.lookupProvider, blockTags.contentsGetter(), this.modid, event.existingFileHelper) {
        override fun getName(): String {return "${this.modId} Item Tags"}
        override fun addTags(arg: HolderLookup.Provider) { ITEM_TAGS_CONSUMERS.forEach { tag(it.key).addTags(*it.value.a.toTypedArray()).add(*it.value.b.map { b -> b.get() }.toTypedArray()) } }
      }
    )
    // Fluid Tags
    event.generator.addProvider(
      event.includeServer(),
      object : FluidTagsProvider(event.generator.packOutput, event.lookupProvider, this.modid, event.existingFileHelper) {
        override fun getName(): String {return "${this.modId} Fluid Tags"}
        override fun addTags(arg: HolderLookup.Provider) { FLUID_TAGS_CONSUMERS.forEach { tag(it.key).addTags(*it.value.a.toTypedArray()).add(*it.value.b.map { b -> b.get() }.toTypedArray()) } }
      }
    )
    // Entity Tags
    event.generator.addProvider(
      event.includeServer(),
      object : EntityTypeTagsProvider(event.generator.packOutput, event.lookupProvider, this.modid, event.existingFileHelper) {
        override fun getName(): String {return "${this.modId} Entity Types Tags"}
        override fun addTags(arg: HolderLookup.Provider) { ENTITY_TAGS_CONSUMERS.forEach { tag(it.key).addTags(*it.value.a.toTypedArray()).add(*it.value.b.map { b -> b.get() }.toTypedArray()) } }
      }
    )
    // Painting Tags
    event.generator.addProvider(
      event.includeServer(),
      object : PaintingVariantTagsProvider(event.generator.packOutput, event.lookupProvider, this.modid, event.existingFileHelper) {
        override fun getName(): String {return "${this.modId} Painting Variants Tags"}
        override fun addTags(arg: HolderLookup.Provider) { PAINTING_TAGS_CONSUMERS.forEach { tag(it.key).addTags(*it.value.a.toTypedArray()).add(*it.value.b.map { b -> b.get() }.toTypedArray()) } }
      }
    )
    // World Preset Tags
    event.generator.addProvider(
      event.includeServer(),
      object : WorldPresetTagsProvider(event.generator.packOutput, event.lookupProvider, this.modid, event.existingFileHelper) {
        override fun getName(): String {return "${this.modId} World Presets Tags"}
        override fun addTags(arg: HolderLookup.Provider) { WORLD_PRESETS_TAGS_CONSUMERS.forEach { tag(it.key).addTags(*it.value.a.toTypedArray()).add(*it.value.b.map { b -> b.get() }.toTypedArray()) } }
      }
    )
  }

  /*?} elif neoforge {*/
  /*fun register(bus: net.neoforged.bus.api.IEventBus, forgeBus: net.neoforged.bus.api.IEventBus) {
    super.registerEventListeners(bus)

    CREATIVE_TABS.register(bus)
    PARTICLE_TYPES.register(bus)

    // register pot plants
    onRegisterFlowerPots(bus)

    // datapack reload listeners
    onDatapackReload(forgeBus)

    // load villager trades
    onLoadVillagerTrades(forgeBus)
    // load wanderer trades
    onLoadWandererTrades(forgeBus)
  }

  fun registerClient(bus: net.neoforged.bus.api.IEventBus, forgeBus: net.neoforged.bus.api.IEventBus) {
    bus.addListener { event: net.neoforged.neoforge.client.event.EntityRenderersEvent.RegisterLayerDefinitions ->
      BOAT_VARIANTS.forEach { t ->
        event.registerLayerDefinition(ModelLayerLocation(DeltaboxUtil.resourceLocation(modid, "boat/${t}"), "main"), BoatModel::createBodyModel);
        event.registerLayerDefinition(ModelLayerLocation(DeltaboxUtil.resourceLocation(modid, "chest_boat/${t}"), "main"), ChestBoatModel::createBodyModel);
      }
      MODEL_LAYERS.forEach { path, (model, folder) ->
        event.registerLayerDefinition(ModelLayerLocation(DeltaboxUtil.resourceLocation(modid, path), folder), model);
      }
    }
    bus.addListener { event: net.neoforged.neoforge.client.event.RegisterParticleProvidersEvent ->
      PARTICLE_REGISTRATIONS.forEach { registration ->
        handleParticleRegistration(event, registration)
      }
    }
  }

  private fun <T : ParticleOptions> handleParticleRegistration(
    event: net.neoforged.neoforge.client.event.RegisterParticleProvidersEvent,
    registration: ParticleRegistration<T>
  ) {
    event.registerSpriteSet(registration.type.get(), registration.provider)
  }

  private fun onRegisterFlowerPots(bus: net.neoforged.bus.api.IEventBus) {
    bus.addListener { e: net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent ->
      POTTED_BLOCKS.forEach { (plant, pot) ->
        try {
          (Blocks.FLOWER_POT as FlowerPotBlock).addPlant(plant.id, pot)
        } catch (e: Exception) {
          println("Failed to add plant ${plant.get().name} to flower pot ${pot.get().name}")
        }
      }
    }
  }

  private fun onDatapackReload(forgeBus: net.neoforged.bus.api.IEventBus) {
    forgeBus.addListener(net.neoforged.bus.api.EventPriority.HIGH) { event: net.neoforged.neoforge.event.AddReloadListenerEvent ->
      val registry = BiConsumer<ResourceLocation, PreparableReloadListener> { id, listener -> event.addListener(listener) }
      // deserialize villager trades
      registry.accept(DeltaboxUtil.resourceLocation(modid, VillagerTradeDeserializer.PATH), VillagerTradeDeserializer(this))
      // deserialize wanderer trades
      registry.accept(DeltaboxUtil.resourceLocation(modid, WandererTradeDeserializer.PATH), WandererTradeDeserializer(this))
    }
  }

  private fun onLoadVillagerTrades(forgeBus: net.neoforged.bus.api.IEventBus) {
    forgeBus.addListener { event: net.neoforged.neoforge.event.village.VillagerTradesEvent ->
      TRADES.forEach { trade ->
        if(event.type == trade.profession){
          event.trades[trade.level.toInt()].add { _, _ -> MerchantOffer(net.minecraft.world.item.trading.ItemCost(trade.tradeCosts.first().item.get(), trade.tradeCosts.first().amount), ItemStack(trade.tradeSells.first().item.get(), trade.tradeSells.first().amount), trade.maxUses, trade.xpAmount, trade.priceMultiplier) }
        }
      }
    }
  }

  private fun onLoadWandererTrades(forgeBus: net.neoforged.bus.api.IEventBus) {
    forgeBus.addListener { event: net.neoforged.neoforge.event.village.WandererTradesEvent ->
      val genericTrades = event.genericTrades
      val rareTrades = event.rareTrades
      WANDERER_TRADES.forEach { trade ->
        if(trade.rarity == WandererTradeRarity.GENERIC) {
          genericTrades.add { _, _ -> MerchantOffer(net.minecraft.world.item.trading.ItemCost(trade.tradeCosts.first().item.get(), trade.tradeCosts.first().amount), ItemStack(trade.tradeSells.first().item.get(), trade.tradeSells.first().amount), trade.maxUses, trade.xpAmount, trade.priceMultiplier) }
        } else {
          rareTrades.add { _, _ -> MerchantOffer(net.minecraft.world.item.trading.ItemCost(trade.tradeCosts.first().item.get(), trade.tradeCosts.first().amount), ItemStack(trade.tradeSells.first().item.get(), trade.tradeSells.first().amount), trade.maxUses, trade.xpAmount, trade.priceMultiplier) }
        }
      }
    }
  }

  // @ NeoForge Datagen
  fun gatherData(event: net.neoforged.neoforge.data.event.GatherDataEvent) {
    event.generator.addProvider(event.includeServer(), com.dannbrown.deltaboxlib.platform.registrate.generators.trades.VillagerTradeProvider(this, event.generator))
    event.generator.addProvider(event.includeServer(), com.dannbrown.deltaboxlib.platform.registrate.generators.trades.WandererTradeProvider(this, event.generator))
    event.generator.addProvider(event.includeServer(), com.dannbrown.deltaboxlib.platform.registrate.generators.recipe.DeltaboxRecipeProvider(this, event.generator, event.lookupProvider))

    // Other Data Generators
    val registrySetBuilder = RegistrySetBuilder()
      .add(Registries.CONFIGURED_FEATURE, ::bootstrapConfiguredfeatures)
      .add(Registries.PLACED_FEATURE, ::bootstrapPlacedFeatures)
      .add(NeoForgeRegistries.Keys.BIOME_MODIFIERS, ::bootstrapBiomeModifiers)
    event.generator.addProvider(event.includeServer(), object : DatapackBuiltinEntriesProvider(event.generator.packOutput, event.lookupProvider, registrySetBuilder, mutableSetOf(this.modid)) {})

    // Biome Tags
    event.generator.addProvider(
      event.includeServer(),
      object : BiomeTagsProvider(event.generator.packOutput, event.lookupProvider, this.modid, event.existingFileHelper) {
        override fun getName(): String {return "${this.modId} Biome Tags"}
        override fun addTags(arg: HolderLookup.Provider) { BIOME_TAGS_CONSUMERS.forEach { tag(it.key).addTags(*it.value.a.toTypedArray()).add(*it.value.b.map { b -> b.get() }.toTypedArray()) } }
      }
    )
    // Block Tags
    val blockTags = object : BlockTagsProvider(event.generator.packOutput, event.lookupProvider, this.modid, event.existingFileHelper) {
      override fun getName(): String {return "${this.modId} Block Tags"}
      override fun addTags(arg: HolderLookup.Provider) { BLOCK_TAGS_CONSUMERS.forEach { tag(it.key).addTags(*it.value.a.toTypedArray()).add(*it.value.b.map { b -> b.get() }.toTypedArray()) } }
    }
    event.generator.addProvider(event.includeServer(), blockTags)
    // Item Tags
    event.generator.addProvider(
      event.includeServer(),
      object : ItemTagsProvider(event.generator.packOutput, event.lookupProvider, blockTags.contentsGetter(), this.modid, event.existingFileHelper) {
        override fun getName(): String {return "${this.modId} Item Tags"}
        override fun addTags(arg: HolderLookup.Provider) { ITEM_TAGS_CONSUMERS.forEach { tag(it.key).addTags(*it.value.a.toTypedArray()).add(*it.value.b.map { b -> b.get() }.toTypedArray()) } }
      }
    )
    // Fluid Tags
    event.generator.addProvider(
      event.includeServer(),
      object : FluidTagsProvider(event.generator.packOutput, event.lookupProvider, this.modid, event.existingFileHelper) {
        override fun getName(): String {return "${this.modId} Fluid Tags"}
        override fun addTags(arg: HolderLookup.Provider) { FLUID_TAGS_CONSUMERS.forEach { tag(it.key).addTags(*it.value.a.toTypedArray()).add(*it.value.b.map { b -> b.get() }.toTypedArray()) } }
      }
    )
    // Entity Tags
    event.generator.addProvider(
      event.includeServer(),
      object : EntityTypeTagsProvider(event.generator.packOutput, event.lookupProvider, this.modid, event.existingFileHelper) {
        override fun getName(): String {return "${this.modId} Entity Types Tags"}
        override fun addTags(arg: HolderLookup.Provider) { ENTITY_TAGS_CONSUMERS.forEach { tag(it.key).addTags(*it.value.a.toTypedArray()).add(*it.value.b.map { b -> b.get() }.toTypedArray()) } }
      }
    )
    // Painting Tags
    event.generator.addProvider(
      event.includeServer(),
      object : PaintingVariantTagsProvider(event.generator.packOutput, event.lookupProvider, this.modid, event.existingFileHelper) {
        override fun getName(): String {return "${this.modId} Painting Variants Tags"}
        override fun addTags(arg: HolderLookup.Provider) { PAINTING_TAGS_CONSUMERS.forEach { tag(it.key).addTags(*it.value.a.toTypedArray()).add(*it.value.b.map { b -> b.get() }.toTypedArray()) } }
      }
    )
    // World Preset Tags
    event.generator.addProvider(
      event.includeServer(),
      object : WorldPresetTagsProvider(event.generator.packOutput, event.lookupProvider, this.modid, event.existingFileHelper) {
        override fun getName(): String {return "${this.modId} World Presets Tags"}
        override fun addTags(arg: HolderLookup.Provider) { WORLD_PRESETS_TAGS_CONSUMERS.forEach { tag(it.key).addTags(*it.value.a.toTypedArray()).add(*it.value.b.map { b -> b.get() }.toTypedArray()) } }
      }
    )
  }
  *//*?}*/
}