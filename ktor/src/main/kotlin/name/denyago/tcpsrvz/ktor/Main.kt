@file:JvmName("Main")

package name.denyago.tcpsrvz.ktor

import io.ktor.network.selector.ActorSelectorManager
import io.ktor.network.sockets.aSocket
import io.ktor.network.sockets.awaitClosed
import io.ktor.network.sockets.openReadChannel
import io.ktor.network.sockets.openWriteChannel
import io.ktor.util.KtorExperimentalAPI
import io.ktor.util.cio.write
import io.ktor.utils.io.readUTF8Line
import io.ktor.utils.io.write
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import name.denyago.tcpsrvz.core.Game
import name.denyago.tcpsrvz.core.GameState
import java.net.InetSocketAddress

@KtorExperimentalAPI
fun main(vararg args: String) {
    val gameState = GameState()
    println("Game initialized with ${gameState.numbers}")

    val newLine = "\r\n"

    runBlocking {
        val server = aSocket(ActorSelectorManager(Dispatchers.IO)).tcp().bind(
            InetSocketAddress("127.0.0.1", 5555)
        )
        println("Started game server at ${server.localAddress}")

        while (true) {
            val socket = server.accept()

            launch {
                println("Socket accepted: ${socket.remoteAddress}")

                val rawInput = socket.openReadChannel()
                val output = socket.openWriteChannel(autoFlush = true)

                try {
                    while (true) {
                        val input = rawInput.readUTF8Line().toString().trim()
                        //println("Debug: $input")

                        if (input == "bye") {
                            socket.close()
                        } else {
                            Game.play(
                                input = input,
                                gameState = gameState,
                                onError = { result ->
                                    //println("Debug: error - $result")
                                    async {
                                        output.write("Failed to read: ${result.outcome}")
                                        output.write(newLine.toString())
                                        output.flush()
                                    }
                                },
                                onSuccess = { result ->
                                    //println("Debug: success - $result")
                                    async {
                                        output.write(result.outcome)
                                        output.write(newLine.toString())
                                        output.flush()
                                    }
                                    if (result.outcome == "correct" || result.outcome == "wrong") {
                                        gameState.saveWhoGuessed(result.guess, result.userName)
                                    }
                                },
                                onMessage = { message ->
                                    //println("Debug: message - $message")
                                    async {
                                        output.write(message)
                                        output.write(newLine.toString())
                                        output.flush()
                                    }
                                }
                            )
                        }
                    }
                } catch (e: Throwable) {
                    e.printStackTrace()
                    socket.close()
                }
            }
        }
    }
}
