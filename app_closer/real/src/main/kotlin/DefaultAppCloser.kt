package com.gurunars.flashcards.app_closer

import javax.inject.Singleton
import kotlin.system.exitProcess

@Singleton
class DefaultAppCloser: AppCloser {
  override fun closeApp(): Nothing {
    exitProcess(0)
  }
}
