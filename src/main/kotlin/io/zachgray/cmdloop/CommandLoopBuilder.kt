package io.zachgray.cmdloop

class CommandLoopBuilder {

    private lateinit var commandLoop: CommandLoop
    private var welcomeMessage: String? = null
    private var commandPrefix: String = ""
    private var nonCommandInputHandler: ((String?)->Unit)? = null
    private var commandList = CommandDictionary().apply {
        // Default commands are defined here
        this["exit"] = {
            println("  bye! \uD83D\uDC4B")
            LoopControlOperator.BREAK
        }
    }

    fun build(): CommandLoop {
        commandLoop = CommandLoop(commandPrefix, commandList, nonCommandInputHandler, welcomeMessage ?: "Welcome.")
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

    fun command(key:String, action:CommandAction) {
        commandList[key] = action
    }

    /**
     * default
     */
    fun default(afterCommandClosure: () -> ((String?)->Unit)?) {
        nonCommandInputHandler = afterCommandClosure()
    }
}

