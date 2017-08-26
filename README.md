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
            "/" //example: /exit, /getTime
        }

        // define a custom command to get the current time
        command("getTime") {
            println("the time is ${Date().time}")
            CONTINUE
        }

        // optional: the "default" case gets executed when user input is not a command
        default {
            { input ->
                input?.let { println("you said \"$input\"") }
            }
        }
    }
}
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