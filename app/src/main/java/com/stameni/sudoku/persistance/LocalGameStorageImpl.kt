package com.stameni.sudoku.persistance

import com.stameni.sudoku.domain.GameStorageResult
import com.stameni.sudoku.domain.IGameDataStorage
import com.stameni.sudoku.domain.SudokuPuzzle
import com.stameni.sudoku.domain.getHash
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.ObjectInputStream
import java.io.ObjectOutputStream

private const val FILE_NAME = "game_state.txt"

class LocalGameStorageImpl(
    fileStorageDirectory: String,
    private val pathToStorage: File = File(fileStorageDirectory, FILE_NAME)
) : IGameDataStorage {

    override suspend fun updateGame(game: SudokuPuzzle): GameStorageResult =
        withContext(Dispatchers.IO) {
            try {
                updateGameData(game)
                GameStorageResult.OnSuccess(game)
            } catch (exception: Exception) {
                GameStorageResult.OnError(exception)
            }
        }

    private fun updateGameData(game: SudokuPuzzle) {
        try {
            val fileOutputStream = FileOutputStream(pathToStorage)
            val objectOutputStream = ObjectOutputStream(fileOutputStream)
            objectOutputStream.writeObject(game)
            objectOutputStream.close()
        } catch (e: Exception) {
            throw e
        }
    }

    override suspend fun updateNode(
        x: Int,
        y: Int,
        color: Int,
        elapsedTime: Long
    ): GameStorageResult =
        withContext(Dispatchers.IO) {
            try {
                val game = getGame()

                game.graph[getHash(x, y)]!!.first.color = color
                game.elapsedTime = elapsedTime
                updateGameData(game)
                GameStorageResult.OnSuccess(game)
            } catch (e: Exception) {
                GameStorageResult.OnError(e)
            }
        }

    private fun getGame(): SudokuPuzzle {
        try {
            var game: SudokuPuzzle

            val fileInputStream = FileInputStream(pathToStorage)
            val objectOutputStream = ObjectInputStream(fileInputStream)
            game = objectOutputStream.readObject() as SudokuPuzzle
            objectOutputStream.close()

            return game
        } catch (e: Exception) {
            throw e
        }
    }

    override suspend fun getCurrentGame(): GameStorageResult =
        withContext(Dispatchers.IO) {
            try {
                GameStorageResult.OnSuccess(getGame())
            } catch (e: Exception) {
                GameStorageResult.OnError(e)
            }
        }
}