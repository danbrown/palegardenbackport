package com.dannbrown.deltaboxlib.mixin.sign;

import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BlockEntityType.class)
public class BlockEntityTypeMixin {
  @Inject(method = "isValid", at = @At("HEAD"), cancellable = true)
  private void deltaboxlib$isValid(BlockState state, CallbackInfoReturnable<Boolean> cir) {
    var $this = BlockEntityType.class.cast(this);
    if ($this == BlockEntityType.SIGN && (state.is(BlockTags.STANDING_SIGNS) || state.is(BlockTags.WALL_SIGNS))) {
      cir.setReturnValue(true);
    }
    if ($this == BlockEntityType.HANGING_SIGN && (state.is(BlockTags.CEILING_HANGING_SIGNS) || state.is(BlockTags.WALL_HANGING_SIGNS))) {
      cir.setReturnValue(true);
    }
  }
}
