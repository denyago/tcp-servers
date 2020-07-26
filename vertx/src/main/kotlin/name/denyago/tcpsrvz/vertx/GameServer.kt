package name.denyago.tcpsrvz.vertx

import io.vertx.core.AbstractVerticle
import name.denyago.tcpsrvz.core.Game
import name.denyago.tcpsrvz.core.GameState


class GameServer : AbstractVerticle() {

    private val newLine = "\r\n"

    override fun start() {
        val gameState = GameState()
        println("Game initialized with ${gameState.numbers}")

        vertx.createNetServer().connectHandler { socket ->
            socket.handler { buffer ->
                val input = buffer.toString().trim()
                //println("Debug: `$input`")

                if (input == "bye") {
                    socket.close()
                } else {
                    Game.play(
                        input = input,
                        gameState = gameState,
                        onError = { result ->
                            socket.write("Failed to read: ${result.outcome}")
                            socket.write(newLine)
                        },
                        onSuccess = { result ->
                            socket.write(result.outcome)
                            socket.write(newLine)
                            if (result.outcome == "correct" || result.outcome == "wrong") {
                                gameState.saveWhoGuessed(result.guess, result.userName)
                            }
                        },
                        onMessage = {
                            socket.write(it)
                            socket.write(newLine)
                        }
                    )
                }
            }
        }.listen(5555)
    }
}
