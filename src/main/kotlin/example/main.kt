package example

import example.math.ext.toRPNExpression
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
        command("time") { _, _ ->
            println("  the time is ${Date().asTimeString()}")
        }

        // override loop control (continue by default) with break - loop will exit
        command("die", loopControl = BREAK) { _, _ ->
            println("I'm dead")
        }

        // print any args that were passed to the program on launch
        command("args") { _, _ ->
            args.forEachIndexed { i, arg -> println("arg[$i]=$arg") }
        }

        // print command params
        command("printParams") { params, _ ->
            println("you included params: $params")
        }

        // optional: the "default" case gets executed when user input is not a command. in this case, the mathematical
        // expression is evaluated and results printed.
        default {
            { input ->
                input?.let {
                    val expression = it.toRPNExpression()
                    println("  numbers:   ${expression.numbers}")
                    println("  operators: ${expression.operators}")
                    println("  solution:  ${expression.evaluate()}")
                }
            }
        }

        // optional: catch error
        catch {
            { _ ->
                println("  invalid expression, please try again.")
            }
        }
    }
}