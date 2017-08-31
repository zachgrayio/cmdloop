package example

import java.util.*

object RPN {
    /**
     * Create an RPN expression string from an infix expression string
     */
    fun from(string:String):String {
        return string.toRPN()
    }

    /**
     * Evaluate a RPN expression string and return the result
     */
    fun evaluate(rpnExpression: String): Double {
        val stack = LinkedList<Double>()
        val tokens = clean(rpnExpression).split("\\s".toRegex()).dropLastWhile { it.isEmpty() }
        fun operands() = Pair(stack.pop(), stack.pop())

        tokens.forEach { token ->
            token.toDoubleOrNull().run { this?.let { return@forEach stack.push(this) } }
            stack.push(when (token) {
                "*" -> operands().let { it.first * it.second }
                "/" -> operands().let { it.first / it.second }
                "-" -> operands().let { it.first - it.second }
                "+" -> operands().let { it.first + it.second }
                "^" -> operands().let { Math.pow(it.first, it.second) }
                else -> throw Error("Invalid expression")
            })
        }
        return stack.pop()
    }

    private fun clean(expr: String): String {
        return expr.replace("[^\\^*+\\-\\d/\\s]".toRegex(), "")
    }
}

/**
 * Converts a mathematical expression from infix to reverse polish notation
 */
fun String.toRPN(): String {
    // supported mathematical operators
    val operators = listOf("+", "-", "/", "*", "^")
    // tokenize string
    val tokens =
        // tokenize - first pass: split on spaces
        this.split(Regex("\\s"))
            .filterNot { it.isEmpty() }
            // tokenize - second pass: split numbers and non numbers
            .flatMap { it ->  it.split(Regex("(?<=\\D)(?=\\d)|(?<=\\d)(?=\\D)")) }
    // --
    // process tokens. todo: find a more idiomatic way to do this with less mutable state?
    // initial state
    val stack = Stack<Int>()
    val builder = StringBuilder()
    fun popAndAppendLastOperator() = builder.append(operators[stack.pop()]).append(' ')
    // process each token
    tokens
        .forEach { token ->
            val index = operators.indexOf(token)
            if (index != -1) {
                if (stack.isEmpty()) stack.push(index)
                else {
                    /* To find out the precedence, we take the index of the token in the ops string and divide by 2 (rounding down). This will give us: 0, 0, 1, 1, 2 */
                    while (!stack.isEmpty()) {
                        val prec2 = stack.peek() / 2
                        val prec1 = index / 2
                        if (prec2 > prec1 || (prec2 == prec1 && token != "^")) popAndAppendLastOperator()
                        else break
                    }
                    stack.push(index)
                }
            } else if (token == "(") stack.push(-2) // -2 stands for '('
            else if (token == ")") {
                // until '(' on stack, pop operators.
                while (stack.peek() != -2) popAndAppendLastOperator()
                stack.pop()
            }
            else builder.append(token).append(' ')
        }
    while (!stack.isEmpty()) popAndAppendLastOperator()
    return builder.toString()
}