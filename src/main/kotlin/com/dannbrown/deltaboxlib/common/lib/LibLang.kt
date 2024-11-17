package com.dannbrown.deltaboxlib.common.lib

import com.dannbrown.deltaboxlib.common.DeltaboxLib
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.MutableComponent

class LibLang {
  companion object {
    val CONNECTING_WORDS = setOf("of", "the", "and", "in", "on", "at", "to", "with", "by", "for", "as", "or", "nor", "but", "so", "yet", "a", "an")

    fun asId(name: String): String {
      return name.lowercase().replace(" ", "_")
    }

    fun asName(id: String): String {
      return id.split("_")
        .joinToString(" ") { word ->
          if (word.lowercase() in CONNECTING_WORDS) {
            word.lowercase()
          } else {
            word.replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() }
          }
        }
        .replace("  ", " ")
        .replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() }
    }

    fun nonPluralId(name: String): String {
      val asId = asId(name)
      return if (asId.endsWith("s")) asId.substring(0, asId.length - 1) else asId
    }

    @JvmStatic
    fun translateDirect(key: String): MutableComponent {
      return Component.translatable(key)
    }

    fun getTooltipKey(modId: String?, itemId: String): String {
      return "tooltip." + DeltaboxLib.MOD_ID + (if(modId !== null) ".$modId" else "") + "." + itemId
    }
  }
}