package com.dannbrown.deltaboxlib.platform.registrate

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
import com.dannbrown.deltaboxlib.platform.registrate.generators.worldgen.ConfiguredFeaturesUtil
import com.dannbrown.deltaboxlib.platform.registrate.generators.worldgen.PlacedFeaturesUtil
import net.minecraft.core.RegistrySetBuilder
/*? if >=1.21 {*/
/*import net.minecraft.data.worldgen.BootstrapContext
*//*?} else {*/
import net.minecraft.data.worldgen.BootstapContext as BootstrapContext
/*?}*/
import net.minecraft.resources.ResourceKey
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacer
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacerType
import net.minecraft.world.level.levelgen.feature.trunkplacers.TrunkPlacer
import net.minecraft.world.level.levelgen.feature.trunkplacers.TrunkPlacerType
import net.minecraft.world.level.levelgen.placement.PlacedFeature
import java.util.function.Supplier

/*? if fabric {*/
/*import net.fabricmc.fabric.api.registry.FlammableBlockRegistry
import net.fabricmc.fabric.api.registry.StrippableBlockRegistry
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup
import net.fabricmc.fabric.api.resource.IdentifiableResourceReloadListener
import net.fabricmc.fabric.api.resource.ResourceManagerHelper
import net.fabricmc.fabric.api.`object`.builder.v1.trade.TradeOfferHelper
import net.minecraft.core.Registry
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.server.MinecraftServer
import net.minecraft.server.packs.PackType
import net.minecraft.server.packs.resources.CloseableResourceManager
import net.minecraft.server.packs.resources.PreparableReloadListener.PreparationBarrier
import net.minecraft.server.packs.resources.ResourceManager
import net.minecraft.util.profiling.ProfilerFiller
import java.util.concurrent.CompletableFuture
import java.util.concurrent.Executor
*//*?}*/

/*? if forge {*/
import net.minecraftforge.registries.DeferredRegister
import net.minecraftforge.common.data.DatapackBuiltinEntriesProvider
/*?}*/

/*? if neoforge {*/
/*import net.neoforged.neoforge.registries.DeferredRegister
import net.neoforged.neoforge.common.data.DatapackBuiltinEntriesProvider
import net.neoforged.neoforge.registries.DeferredHolder

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
    this.defaultCreativeTab(null as ResourceKey<CreativeModeTab>?) // remove the default creative tab to avoid duplicate entries
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
              net.minecraft.world.item.trading.ItemCost(it.tradeCosts.first().item.get(), it.tradeCosts.first().amount),
              /^?} else {^/
              /^ItemStack(it.tradeCosts.first().item.get(), it.tradeCosts.first().amount),
              ^//^?}^/
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
              net.minecraft.world.item.trading.ItemCost(it.tradeCosts.first().item.get(), it.tradeCosts.first().amount),
              /^?} else {^/
              /^ItemStack(it.tradeCosts.first().item.get(), it.tradeCosts.first().amount),
              ^//^?}^/
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

    // register pot plants
    onRegisterFlowerPots(bus)

    // datapack reload listeners
    onDatapackReload(forgeBus)

    // load villager trades
    onLoadVillagerTrades(forgeBus)
    // load wanderer trades
    onLoadWandererTrades(forgeBus)
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
    event.generator.addProvider(event.includeServer(), object : DatapackBuiltinEntriesProvider(event.generator.packOutput, event.lookupProvider, registrySetBuilder, mutableSetOf(this.modid)) {})
  }

  /*?} elif neoforge {*/
  /*fun register(bus: net.neoforged.bus.api.IEventBus, forgeBus: net.neoforged.bus.api.IEventBus) {
    super.registerEventListeners(bus)

    CREATIVE_TABS.register(bus)

    // register pot plants
    onRegisterFlowerPots(bus)

    // datapack reload listeners
    onDatapackReload(forgeBus)

    // load villager trades
    onLoadVillagerTrades(forgeBus)
    // load wanderer trades
    onLoadWandererTrades(forgeBus)
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
    event.generator.addProvider(event.includeServer(), object : DatapackBuiltinEntriesProvider(event.generator.packOutput, event.lookupProvider, registrySetBuilder, mutableSetOf(this.modid)) {})
  }
  *//*?}*/
}