package com.arcsys.tictacto.di

import com.arcsys.tictacto.data.network.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class CloverModule {

    @Provides
    @Singleton
    fun provideFbLink(): FbRepo = FbRepoImpl()

    @Provides
    @Singleton
    fun provideOneSignalInt(): OneSignalRepo = OneSignalRepoImpl()
}