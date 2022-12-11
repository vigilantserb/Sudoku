package com.stameni.sudoku.domain

data class UserStatistics(
    val fourEasy: Long,
    val fourMedium: Long,
    val fourHard: Long,
    val nineEasy: Long,
    val nineMedium: Long,
    val nineHard: Long
)
