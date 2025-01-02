package com.gurunars.android_libs.app_closer

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface FakeModule {
  @Singleton
  @Binds
  fun appCloser(impl: FakeAppCloser): AppCloser
}
