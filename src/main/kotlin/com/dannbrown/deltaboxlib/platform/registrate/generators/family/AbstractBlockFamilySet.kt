package com.dannbrown.deltaboxlib.platform.registrate.generators.family
abstract class AbstractBlockFamilySet {
  protected val _blockFamily: BlockFamily = BlockFamily()

  fun getFamily(): BlockFamily {
    return _blockFamily
  }
}