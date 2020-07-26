@file:JvmName("Main")

package name.denyago.tcpsrvz.rapidoid

import name.denyago.tcpsrvz.core.GameState
import org.rapidoid.net.TCP

fun main(vararg args: String) {
    val game = GameState()
    println("Game initialized with ${game.numbers}")
    TCP.server().protocol(GameProtocol(game)).port(5555).build().start()
}
