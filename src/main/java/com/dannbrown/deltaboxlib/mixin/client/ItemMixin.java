package com.dannbrown.deltaboxlib.mixin.client;

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
public class ItemMixin {
    @SuppressWarnings("InvalidInjectorMethodSignature")
    @Inject(method = "appendHoverText", at = @At("HEAD"))
    protected void deltaboxlib$appendHoverText(
        //? if >=1.21 {
        /*ItemStack itemStack, Item.TooltipContext tooltipContext, List<Component> list, TooltipFlag tooltipFlag, CallbackInfo ci
        *///?} else
        ItemStack arg, net.minecraft.world.level.Level arg2, List<Component> list, TooltipFlag arg3, CallbackInfo ci
    ) {

    }
}