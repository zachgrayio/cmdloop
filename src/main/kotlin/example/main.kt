package example

import io.zachgray.cmdloop.LoopControlOperator.*
import io.zachgray.cmdloop.commandLoop
import io.zachgray.ext.asTimeString
import java.util.*

fun main(args:Array<String>) {
    commandLoop {
        // optional: a custom welcome message
        welcomeMessage {
            "Hi! Enter a message and it will be echoed, or enter a command."
        }

        // define the command prefix
        commandPrefix {
            "/" //example: /exit, /getTime
        }

        // define a custom command to get the current time
        command("getTime") {
            println("  the time is ${Date().asTimeString()}")
        }

        // override loop control (continue by default) with break - loop will exit
        command("die", loopControl = BREAK) {
            println("I'm dead")
        }

        // optional: the "default" case gets executed when user input is not a command
        default {
            { input ->
                input?.let { println("  you said \"$input\"") }
            }
        }
    }
}