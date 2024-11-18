package com.dannbrown.deltaboxlib.mixin.tooltip;

import com.dannbrown.deltaboxlib.platform.util.Util;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(Item.class)
public abstract class ItemMixin {
  @Inject(method = { "appendHoverText" }, at = { @At("HEAD") }, require = 1)
  protected void deltaboxlib$appendHoverText(
          //? if >=1.21 {
          /*ItemStack itemStack, Item.TooltipContext tooltipContext, List<Component> list, TooltipFlag tooltipFlag, CallbackInfo ci
          *///?} else
          ItemStack itemStack, net.minecraft.world.level.Level level, List<Component> list, TooltipFlag tooltipFlag, CallbackInfo ci
  ) {
    // create or get an item description id, get the last key and add as suffix to
    String itemDescription = itemStack.getDescriptionId();
    String[] parts = itemDescription.split("\\.");
    String tooltipTranslationKey = parts.length > 1 ? Util.LANG.getTooltipKey(parts[parts.length - 2], parts[parts.length - 1]) : "";
    String genericTranslationKey = parts.length > 1 ? Util.LANG.getTooltipKey(null, parts[parts.length - 1]) : "";

    // here we use translations as a logic matter,
    // it is not recommended but is the way to dynamically add formula tooltips to
    // vanilla and other mod items
    boolean doExistTooltip = parts.length > 1 && !Util.LANG.translateDirect(tooltipTranslationKey).getString().contains(".");
    boolean doExistGeneric = parts.length > 1 && !Util.LANG.translateDirect(genericTranslationKey).getString().contains(".");

    // check if the translation exists and add it to the tooltip
    if (doExistTooltip) {
      list.add(Util.LANG.translateDirect(tooltipTranslationKey).withStyle(ChatFormatting.GRAY));
    }
    if (doExistGeneric) {
      list.add(Util.LANG.translateDirect(genericTranslationKey).withStyle(ChatFormatting.GRAY));
    }
  }
}
