package example

import java.util.*

sealed class Operator(val stringRepresentation:String, val precedence:Int) {
    abstract fun operate(left:Double, right:Double):Double

    object plus : Operator(stringRepresentation = "+", precedence = 0) {
        override fun operate(left: Double, right: Double) = left.plus(right)
    }

    object minus : Operator(stringRepresentation = "-", precedence = 0) {
        override fun operate(left: Double, right: Double) = left.minus(right)
    }

    object times : Operator(stringRepresentation = "*", precedence = 1) {
        override fun operate(left: Double, right: Double) = left.times(right)
    }

    object div : Operator(stringRepresentation = "/", precedence = 1) {
        override fun operate(left: Double, right: Double) = left.div(right)
    }

    object pow : Operator(stringRepresentation = "/", precedence = 2) {
        override fun operate(left: Double, right: Double) = Math.pow(left, right)
    }

    companion object {
        fun fromString(representation:String) = when(representation) {
            plus.stringRepresentation -> plus
            minus.stringRepresentation -> minus
            times.stringRepresentation -> times
            div.stringRepresentation -> div
            pow.stringRepresentation -> pow
            else -> throw Error("Invalid expression or unsupported operator")
        }
    }
}

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
        tokens.forEach { token ->
            token.toDoubleOrNull().run { this?.let { return@forEach stack.push(this) } }
            Operator
                .fromString(token)
                .operate(stack.pop(), stack.pop())
                .run { stack.push(this) }
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
    // supported mathematical operators, ordered by precedence
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
                    // to find precedence, take the index of the token in the ops list and divide by 2 (rounding down). this will give us: 0, 0, 1, 1, 2
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