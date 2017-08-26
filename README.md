# cmdloop

A lightweight, dependency-free library for building simple, declarative command-line programs in Kotlin

### Example program

```kotlin
fun main(args:Array<String>) {
    commandLoop {
        // optional: a custom welcome message
        welcomeMessage {
            "Hi! Enter a message and it will be echoed, or enter a command."
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
                input?.let { println("  you said \"$input\"") }
            }
        }
    }
}
```

### Example program output

```
Hi! Enter a message and it will be echoed, or enter a command. Commands:
  /die
  /exit
  /getTime
  /history
> /getTime
  the time is 1503734761550
> /history
0 /getTime
1 /history
> hello
  you said "hello"
> /exit
  bye! 👋
```

### Default Commands

Out of the box, the following commands are included:
- `exit` - exit the program
- `history` - view command history

To provide a custom exit function, you could do the following:

```kotlin
command("exit") {
    println("custom exit!")
    BREAK
}
```

### Build
`./gradlew assemble` 