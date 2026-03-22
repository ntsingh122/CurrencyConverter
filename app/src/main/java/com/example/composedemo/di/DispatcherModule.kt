package com.example.composedemo.di

import com.example.composedemo.utils.DefaultDispatcherProvider
import com.example.composedemo.utils.DispatcherProvider
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class DispatcherModule {

    @Binds
    @Singleton
    abstract fun bindsDispatcherProvider(impl: DefaultDispatcherProvider): DispatcherProvider

}