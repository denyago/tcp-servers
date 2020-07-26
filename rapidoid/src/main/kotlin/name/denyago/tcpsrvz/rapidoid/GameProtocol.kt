package name.denyago.tcpsrvz.rapidoid

import name.denyago.tcpsrvz.core.Game
import name.denyago.tcpsrvz.core.GameState
import org.rapidoid.net.Protocol
import org.rapidoid.net.abstracts.Channel
import org.rapidoid.util.Constants

class GameProtocol(private val gameState: GameState) : Protocol {
    override fun process(ctx: Channel) {
        val input = ctx.readln()
        //println("Debug: $input")

        if (input == "bye") {
            ctx.closeIf(true)
        } else {
            Game.play(
                input = input,
                gameState = gameState,
                onError = { result ->
                    ctx.write("Failed to read: ${result.outcome}").write(Constants.CR_LF)
                },
                onSuccess = { result ->
                    ctx.write(result.outcome).write(Constants.CR_LF)
                    if (result.outcome == "correct" || result.outcome == "wrong") {
                        gameState.saveWhoGuessed(result.guess, result.userName)
                    }
                },
                onMessage = { ctx.write(it).write(Constants.CR_LF) }
            )
        }
    }
}
