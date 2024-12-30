package com.dannbrown.deltaboxlib.platform.registrate.generators.trades

enum class WandererTradeRarity {
    GENERIC,
    RARE;

    override fun toString(): String{
        return when(this){
            GENERIC -> "generic"
            RARE -> "rare"
        }
    }

    fun toInt(): Int{
        return when(this){
            GENERIC -> 1
            RARE -> 2
        }
    }

    companion object {
        fun fromString(value: String): WandererTradeRarity {
            return when(value){
                "generic" -> GENERIC
                "rare" -> RARE
                else -> {GENERIC}
            }
        }
    }
}