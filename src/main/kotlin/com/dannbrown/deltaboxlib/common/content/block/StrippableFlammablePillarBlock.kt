package com.dannbrown.deltaboxlib.common.content.block

import net.minecraft.world.item.AxeItem
import net.minecraft.world.item.context.UseOnContext
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.state.BlockState

import java.util.function.Supplier

open class StrippableFlammablePillarBlock(props: Properties, private val strippedBlock: Supplier<out Block>, private val flammability: Int = 20, private val fireSpread: Int = 5): FlammablePillarBlock(props, flammability, fireSpread) {
  /*? if forge || neoforge {*/
  override fun getToolModifiedState(
    state: BlockState,
    context: UseOnContext,
    /*? if forge {*/
    toolAction: net.minecraftforge.common.ToolAction,
    /*?} else if neoforge {*/
    /*toolAction: net.neoforged.neoforge.common.ItemAbility,
    *//*?}*/
    simulate: Boolean
  ): BlockState? {
    if (context.itemInHand.item is AxeItem) {
      return strippedBlock.get()
        .defaultBlockState()
        .setValue(AXIS, state.getValue(AXIS))
    }

    return super.getToolModifiedState(state, context, toolAction, simulate)
  }
  /*?}*/
}