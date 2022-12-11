package com.stameni.sudoku.domain

import java.lang.Exception

interface ISettingsStorage {
    suspend fun getSettings(): SettingsStorageResult
    suspend fun updateSettings(settings: Settings): SettingsStorageResult
}

sealed class SettingsStorageResult {
    data class OnSuccess(
        val settings: Settings
    ): GameStorageResult()

    data class OnError(
        val exception: Exception
    ): GameStorageResult()
}
