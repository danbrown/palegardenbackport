package com.dannbrown.deltaboxlib.platform.util

import java.nio.file.Path

interface IModStatus {
  val isClient: Boolean
  val isDev: Boolean
  val configDir: Path
  val platform: String

  fun isLoaded(mod: String): Boolean
  fun getVersion(mod: String): String?
}