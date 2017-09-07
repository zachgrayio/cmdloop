package io.zachgray.cmdloop

typealias CommandAction = (CommandLoop) -> LoopControlOperator

fun commandLoop(init: CommandLoopBuilder.() -> Unit) : CommandLoop {
    val builder = CommandLoopBuilder()
    builder.init()
    return builder.build()
}