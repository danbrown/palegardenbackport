package com.dannbrown.palegardenbackport.mixins;

import com.dannbrown.palegardenbackport.content.entity.creaking.CreakingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.AvoidEntityGoal;
import net.minecraft.world.entity.monster.Vindicator;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Vindicator.class)
public class VindicatorMixin extends MobMixin {
  @Inject(method = "registerGoals", at = @At("HEAD"))
  private void registerGoals(CallbackInfo ci) {
    PathfinderMob self = (PathfinderMob)(Object) this;
    this.goalSelector.addGoal(3, new AvoidEntityGoal<>(self, CreakingEntity.class, 8.0F, (double)1.0F, 1.2));
  }
}
