package io.zachgray.cmdloop

class CommandLoop(
    private val commandPrefix:String,
    private val commandDictionary: CommandDictionary,
    nonCommandInputHandler:((String?)->Unit)? = null,
    welcomeMessage:String,
    errorHandler:((Throwable)->Unit)?) {

    private val commandList:List<String> = commandDictionary.keys.map { "$commandPrefix$it" }.sortedBy { it }
    val commandHistory = mutableListOf<String>()

    init {
        println("$welcomeMessage Commands:")
        printCommands("  ")

        // loop
        repl@ while(true) {
            // get user input
            print("> ")
            val input = readLine()
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
                errorHandler(t)
            }
        }
    }

    /**
     * Execute a command and return the loop control. Returns CONTINUE if the command is not recognized, or NONE if it
     * is not a command at all.
     */
    private fun executeCommand(input:String?): LoopControlOperator {
        if(input == null || !input.startsWith(commandPrefix)) return LoopControlOperator.NONE

        val commandKey = input.substring(1)
        val command = commandDictionary[commandKey]

        return when (command) {
            null -> {
                println("  Command not recognized. valid commands are:")
                printCommands("    ")
                LoopControlOperator.CONTINUE
            }
            else -> {
                commandHistory.add("$commandPrefix$commandKey")
                command(this)
            }
        }
    }

    private fun printCommands(indentation:String) {
        commandList.forEach { println("$indentation$it") }
    }
}