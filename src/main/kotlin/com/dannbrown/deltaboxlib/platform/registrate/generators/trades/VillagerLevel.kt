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
          1 -> NOVICE
          2 -> APPRENTICE
          3 -> JOURNEYMAN
          4 -> EXPERT
          5 -> MASTER
          else -> {NOVICE}
        }
      }
    }
  }