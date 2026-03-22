package com.example.composedemo.utils

import kotlinx.coroutines.CoroutineDispatcher

interface DispatcherProvider {
    val io : CoroutineDispatcher
    val default: CoroutineDispatcher
    val main: CoroutineDispatcher
}