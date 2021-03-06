package io.zachgray.cmdloop

import io.zachgray.cmdloop.LoopControlOperator.*

class CommandLoopBuilder {

    private lateinit var commandLoop: CommandLoop
    private var welcomeMessage: String? = null
    private var commandPrefix: String = ""
    private var nonCommandInputHandler: ((String?)->Unit)? = null
    private var errorHandler: ((Throwable)->Unit)? = null
    private var commandList = CommandDictionary().apply {
        // Default commands are defined here
        this["exit"] = { _, _ ->
            println("  bye! \uD83D\uDC4B")
            BREAK
        }
        this["history"] = { _, cmdLoop ->
            cmdLoop.commandHistory.forEachIndexed { index,command -> println("$index $command") }
            CONTINUE
        }
    }

    fun build(): CommandLoop {
        commandLoop = CommandLoop(commandPrefix, commandList, nonCommandInputHandler, welcomeMessage ?: "Welcome.", errorHandler)
        return commandLoop
    }

    /**
     * welcomeMessage
     */
    fun welcomeMessage(welcomeMessageClosure: () -> String) {
        welcomeMessage = welcomeMessageClosure()
    }

    /**
     * commandPrefix
     */
    fun commandPrefix(prefixClosure: () -> String) {
        commandPrefix = prefixClosure()
    }

    /**
     * shorthand to define a command
     */
    fun command(keyAndParams:String, loopControl:LoopControlOperator = CONTINUE, action:(List<String>, CommandLoop)->Unit) {
        commandList[keyAndParams.split("\\s".toRegex()).first()] = { args, cmdLoop ->
            action(args, cmdLoop)
            loopControl
        }
    }

    /**
     * default
     */
    fun default(nonCommandInputHandlerClosure: () -> ((String?)->Unit)?) {
        nonCommandInputHandler = nonCommandInputHandlerClosure()
    }

    /**
     * catch
     */
    fun catch(errorHandlerClosure: () -> ((Throwable)->Unit)?) {
        errorHandler = errorHandlerClosure()
    }
}

