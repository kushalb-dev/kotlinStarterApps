package com.starterkotlin.guessinggame

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class GameViewModel: ViewModel() {

    val words = listOf("apple", "banana", "cherry", "date", "elderberry")
    var currentCorrectWord = words.random().uppercase()
        private set
    var currentWord by mutableStateOf(currentCorrectWord.map { '_' }.joinToString(""))
        private set
    var guessInput by mutableStateOf("")
        private set
    var incorrectGuesses by mutableIntStateOf(0)
        private set
    var remainingGuesses by mutableIntStateOf(7)
        private set

    fun guessLetter(word: String) {
        if (remainingGuesses > 0) {
            if (currentCorrectWord.contains(word)) {
                currentWord = currentWord.mapIndexed { index, char ->
                    if (currentCorrectWord[index].toString() == word) {
                        word
                    } else {
                        char
                    }
                }.joinToString("")
            } else {
                incorrectGuesses++
                remainingGuesses--
            }
        }
    }

    fun onGuessInputChange(newInput: String) {
        guessInput = newInput
    }
}