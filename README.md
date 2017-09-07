# cmdloop

A lightweight, dependency-free library for building simple, declarative command-line programs in Kotlin

### Binaries

First add the bintray repo:

```groovy
repositories {
    maven {
        url  "https://zachgray.bintray.com/cmdloop"
    }
}
```
then the binary dependency can be added:

```groovy
dependencies {
    compile "io.zachgray.cmdloop:cmdloop:0.1.4"
}
```

### Example program

```kotlin
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
            { e ->
                println("  invalid expression, please try again.")
            }
        }
    }
}
```

### Example program output

```
Hi! Enter a message and it will be echoed, or enter a command. Commands:
  /args
  /die
  /exit
  /getTime
  /history
> /getTime
  the time is 02:39
> /history
0 /getTime
1 /history
> hello
  you said "hello"
> /args
  arg[0]=argh
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

### Build this repository
`./gradlew assemble` 