package com.dannbrown.deltaboxlib.platform.registrate.generators.trades

  enum class VillagerLevel {
    NOVICE,
    APPRENTICE,
    JOURNEYMAN,
    EXPERT,
    MASTER;

    fun toInt(): Int {
      return when(this) {
        NOVICE -> 1
        APPRENTICE -> 2
        JOURNEYMAN -> 3
        EXPERT -> 4
        MASTER -> 5
      }
    }

    fun toName(): String{
      return when(this) {
        NOVICE -> "novice"
        APPRENTICE -> "apprentice"
        JOURNEYMAN -> "journeyman"
        EXPERT -> "expert"
        MASTER -> "master"
      }
    }

    companion object{
      fun fromInt(value: Int): VillagerLevel {
        return when(value){
          1 -> VillagerLevel.NOVICE
          2 -> VillagerLevel.APPRENTICE
          3 -> VillagerLevel.JOURNEYMAN
          4 -> VillagerLevel.EXPERT
          5 -> VillagerLevel.MASTER
          else -> {VillagerLevel.NOVICE}
        }
      }
    }
  }