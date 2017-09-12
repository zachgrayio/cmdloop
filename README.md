# cmdloop

A lightweight, dependency-free library for building simple, declarative command-line programs in Kotlin.

### Example program

Here's a CLI calculator implemented on top of cmdloop: [Kalk](https://github.com/zachgrayio/kalk).

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
    compile "io.zachgray.cmdloop:cmdloop:2.0.2"
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
command("exit", loopControl = BREAK) {
    println("custom exit!")
}
```

### Build this repository
`./gradlew assemble` 