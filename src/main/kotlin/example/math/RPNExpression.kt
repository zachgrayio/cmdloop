package example.math

import java.util.*

class RPNExpression(val expression:String, val operators:MutableList<Operator>, val numbers: MutableList<Double>) {
    /**
     * Evaluate a RPN expression string and return the result
     */
    fun evaluate(): Double = LinkedList<Double>().apply {
        val stack = this
        expression
            // clean
            .replace("[^\\^*+\\-\\d/\\s]".toRegex(), "")
            // split
            .split("\\s".toRegex())
            // drop empties
            .dropLastWhile { it.isEmpty() }
            // process tokens
            .forEach { token ->
                // push numbers onto stack
                token.toDoubleOrNull().run { this?.let { return@forEach stack.push(this) } }
                // NaN, lookup operator
                (Operator.fromString(token) ?: throw Error("Invalid expression or unsupported operator"))
                    // apply operator to previous 2 values on stack
                    .operate(stack.pop(), stack.pop())
                    // push result to stack
                    .run(stack::push)
            }
    }.pop()
}