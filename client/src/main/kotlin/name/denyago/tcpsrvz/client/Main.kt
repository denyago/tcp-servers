@file:JvmName("Main")

package name.denyago.tcpsrvz.client

import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.PrintWriter
import java.net.Socket


fun main(vararg args: String) {
    println("Game client started")
    val rightPicks = mutableListOf<Int>()
    val wrongPicks = mutableListOf<Int>()
    val socket = Socket("127.0.0.1", 5555)
    repeat(100000) { sendGuess(socket, "Client-1", it, rightPicks, wrongPicks)}
    socket.close()

    println("Right: $rightPicks")
}

val newLine = byteArrayOf(13, 10)

fun sendGuess(socket: Socket, name: String, guess: Int, right: MutableList<Int>, wrong: MutableList<Int>) {
    //println("Connecting $name...")

    socket.getOutputStream().write("$name;$guess".toByteArray() + newLine)
    socket.getOutputStream().flush()
    val text = BufferedReader(InputStreamReader(socket.inputStream)).readLine()

    //println("Try $guess of $name: $text")
    if (text == "correct") {
        right.add(guess)
    } else {
        wrong.add(guess)
    }

}
