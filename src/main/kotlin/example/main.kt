package example

import io.zachgray.cmdloop.LoopControlOperator.*
import io.zachgray.cmdloop.commandLoop
import io.zachgray.cmdloop.ext.asTimeString
import java.util.*

fun main(args:Array<String>) {
    commandLoop {
        // optional: a custom welcome message
        welcomeMessage {
            "Hi! Enter a mathematical expression to be evaluated, or enter a command."
        }

        // define the command prefix
        commandPrefix {
            "/" //example: /exit, /time
        }

        // define a custom command to get the current time
        command("time") {
            println("  the time is ${Date().asTimeString()}")
        }

        // override loop control (continue by default) with break - loop will exit
        command("die", loopControl = BREAK) {
            println("I'm dead")
        }

        // print any args that were passed to the program on launch
        command("args") {
            args.forEachIndexed { i, arg -> println("arg[$i]=$arg") }
        }

        // optional: the "default" case gets executed when user input is not a command
        default {
            { input ->
                input?.let { println("  ${RPN.evaluate(RPN.from(it))}") }
            }
        }

        // optional: catch error
        catch {
            { e ->
                println("  invalid expression, please try again.")
            }
        }
    }
}