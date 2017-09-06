package example

import java.util.*
import java.util.LinkedList


sealed class Operator(val stringRepresentation:String, val precedence:Int, val isRightAssociative:Boolean) {
    abstract fun operate(right:Double, left:Double):Double

    object plus : Operator(stringRepresentation = "+", precedence = 0, isRightAssociative = false) {
        override fun operate(right: Double, left: Double) = left.plus(right)
    }

    object minus : Operator(stringRepresentation = "-", precedence = 0, isRightAssociative = false) {
        override fun operate(right: Double, left: Double) = left.minus(right)
    }

    object times : Operator(stringRepresentation = "*", precedence = 1, isRightAssociative = false) {
        override fun operate(right: Double, left: Double) = left.times(right)
    }

    object div : Operator(stringRepresentation = "/", precedence = 1, isRightAssociative = false) {
        override fun operate(right: Double, left: Double) = left.div(right)
    }

    object pow : Operator(stringRepresentation = "^", precedence = 2, isRightAssociative = true) {
        override fun operate(right: Double, left: Double) = Math.pow(left, right)
    }

    companion object {
        fun fromString(representation:String) = when(representation) {
            plus.stringRepresentation -> plus
            minus.stringRepresentation -> minus
            times.stringRepresentation -> times
            div.stringRepresentation -> div
            pow.stringRepresentation -> pow
            else -> null
        }
    }
}

operator fun Operator.compareTo(other:Operator):Int {
    return when {
        this.precedence > other.precedence -> 1
        this.precedence == other.precedence -> 0
        else -> -1
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
            (Operator.fromString(token) ?: throw Error("Invalid expression or unsupported operator"))
                .operate(stack.pop(), stack.pop())
                .run(stack::push)
        }
        return stack.pop()
    }

    private fun clean(expr: String): String {
        return expr.replace("[^\\^*+\\-\\d/\\s]".toRegex(), "")
    }
}

/**
 * Converts a mathematical expression from infix to reverse polish notation
 * infix:   3 + 4 * 2 / ( 1 - 5 ) ^ 2 ^ 3
 * postfix: 3 4 2 * 1 5 - 2 3 ^ ^ / +
 */
fun String.toRPN(): String {
    // tokenize string
    val tokens =
        // tokenize - first pass: split on spaces
        this.split(Regex("\\s"))
            .filterNot { it.isEmpty() }
            // tokenize - second pass: split numbers and non numbers
            .flatMap { it ->  it.split(Regex("(?<=\\D)(?=\\d)|(?<=\\d)(?=\\D)")) }
    // process tokens
    val output = StringBuilder()
    val stack = Stack<String>()
    fun popToOutput() { output.append(stack.pop()).append(' ') }
    tokens.forEach { token ->
        val operator = Operator.fromString(token)
        when {
            operator != null -> {
                while (!stack.isEmpty() && Operator.fromString(stack.peek()) != null) {
                    val operator2 = Operator.fromString(stack.peek())!!
                    val c = operator.compareTo(operator2)
                    if(c < 0 || !operator.isRightAssociative && c <= 0) popToOutput()
                    else break
                }
                stack.push(token)
            }
            token == "(" -> stack.push(token)
            token == ")" -> {
                while (stack.peek() != "(") popToOutput()
                stack.pop()
            }
            else -> output.append(token).append(' ')
        }
    }
    while (!stack.isEmpty()) popToOutput()
    return output.toString()
}