package io.zachgray.cmdloop

import java.util.*

class CommandLoop(
    private val commandPrefix:String,
    private val commandDictionary: CommandDictionary,
    private val nonCommandInputHandler:((String?)->Unit)? = null,
    private val welcomeMessage:String,
    private val errorHandler:((Throwable)->Unit)?) {

    private val commandList:List<String> = commandDictionary.keys.map { "$commandPrefix$it" }.sortedBy { it }
    val commandHistory = mutableListOf<String>()
    private val commands = Stack<String?>()

    fun start():CommandLoop {
        println("$welcomeMessage Commands:")
        printCommands("  ")

        // loop
        repl@ while(true) {
            // get user input
            print("> ")
            val input = if(!commands.isEmpty()) commands.pop() else readLine()
            // execute command
            try {
                when(executeCommand(input)) {
                    LoopControlOperator.BREAK -> break@repl
                    LoopControlOperator.CONTINUE -> continue@repl
                    LoopControlOperator.NONE -> Unit
                }
                // execute default if it exists
                nonCommandInputHandler?.let { it(input) }
            } catch(t:Throwable) {
                if (errorHandler == null) throw t
                errorHandler.invoke(t)
            }
        }
        return this
    }

    fun run(input:String?):CommandLoop {
        commands.push(input)
        return this
    }

    /**
     * Execute a command and return the loop control. Returns CONTINUE if the command is not recognized, or NONE if it
     * is not a command at all.
     */
    private fun executeCommand(input:String?): LoopControlOperator {
        if(input == null || !input.startsWith(commandPrefix)) return LoopControlOperator.NONE

        val commandAndArgs = input.substring(1).split("\\s".toRegex())
        val commandKey = commandAndArgs[0]
        val commandArgs = if(commandAndArgs.size > 1) commandAndArgs.takeLast(commandAndArgs.size - 1) else listOf()
        val command = commandDictionary[commandKey]

        return when (command) {
            null -> {
                println("  Command not recognized. valid commands are:")
                printCommands("    ")
                LoopControlOperator.CONTINUE
            }
            else -> {
                commandHistory.add("$commandPrefix$commandKey")
                command(commandArgs, this)
            }
        }
    }

    private fun printCommands(indentation:String) {
        commandList.forEach { println("$indentation$it") }
    }
}