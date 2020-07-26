package name.denyago.tcpsrvz.core

data class RawInput(val userName: String, val guess: Int)

object Game {
    const val MAX_NUM = 100_000

    private const val BANNER =
        """Guess a number game.
|=============================================
|   Format: YYY;XXX -  
|       where YYY is your username,
|       and XXX in a number between 0 and $MAX_NUM
|============================================="""

    private fun makeGuess(userName: String, isCorrect: Boolean, whoGuessed: String?): String {
        val outcome = if (isCorrect) {
            "correct"
        } else {
            "wrong"
        }

        return if (whoGuessed.isNullOrBlank()) {
            outcome
        } else {
            if (whoGuessed == userName) {
                "$outcome, you did it already"
            } else {
                "$outcome, but $whoGuessed did it already"
            }
        }
    }

    private fun validate(userName: String, guess: Int): String {
        val userNameResult = if (userName.isBlank()) {
            "No username given"
        } else {
            "OK"
        }
        val guessResult = if ((0..MAX_NUM).contains(guess)) {
            "OK"
        } else {
            "The number $guess is not between 0 and 100000"
        }

        val errors = listOf(userNameResult, guessResult).filter { it != "OK" }.joinToString(", ")

        return if (errors.isBlank()) "OK" else errors
    }

    private fun parse(string: String): RawInput =
        RawInput(
            userName = string.split(';').first().toString(),
            guess = string.split(';').last().toIntOrNull().let { it ?: -1 }
        )

    fun play(
        input: String?,
        gameState: GameState,
        onError: (ErrorResult) -> Unit,
        onSuccess: (GameResult) -> Unit,
        onMessage: (String) -> Unit
    ) {
        val parsed = parse(input.toString())
        val validatorResult = validate(
            userName = parsed.userName,
            guess = parsed.guess
        )
        if (validatorResult == "OK") {
            val guessResult = makeGuess(
                userName = parsed.userName,
                isCorrect = gameState.checkGuess(parsed.guess),
                whoGuessed = gameState.findWhoGuessed(parsed.guess)
            )
            onSuccess(GameResult(guessResult, parsed.guess, parsed.userName))
        } else {
            onMessage(BANNER)
            onError(ErrorResult(validatorResult))
        }
    }
}

data class GameResult(val outcome: String, val guess: Int, val userName: String)

data class ErrorResult(val outcome: String)
