package com.gurunars.android_libs.app_closer

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FakeAppCloser @Inject constructor(): AppCloser {

  var isClosed: Boolean = false
    private set

  fun reset() {
    this.isClosed = false
  }

  override fun closeApp(): Nothing {
    this.isClosed = true
    throw Exception("Closed")
  }
}
