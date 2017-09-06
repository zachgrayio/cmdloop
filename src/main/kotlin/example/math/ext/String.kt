package example.math.ext

import example.math.Operator
import example.math.RPNExpression
import java.util.*

/**
 * Converts a mathematical expression from infix to reverse polish notation
 * infix:   3 + 4 * 2 / ( 1 - 5 ) ^ 2 ^ 3
 * postfix: 3 4 2 * 1 5 - 2 3 ^ ^ / +
 */
fun String.toRPNExpression(): RPNExpression = this
    // tokenize - first pass
    .split(Regex("\\s"))
    .filterNot { it.isEmpty() }
    // tokenize - second pass: split numbers and non numbers
    .flatMap { it ->  it.split(Regex("(?<=\\D)(?=\\d)|(?<=\\d)(?=\\D)")) }
    // process tokens
    .let { tokens ->
        val output = StringBuilder()
        val stack = Stack<String>()
        val operators = mutableListOf<Operator>()
        val numbers = mutableListOf<Double>()
        fun popToOutput() { output.append(stack.pop()).append(' ') }
        tokens.forEach { token ->
            val operator = Operator.fromString(token)
            when {
                operator != null -> {
                    operators.add(operator)
                    while (!stack.isEmpty() && Operator.fromString(stack.peek()) != null) {
                        val operator2 = Operator.fromString(stack.peek())!!
                        if(operator < operator2 || !operator.isRightAssociative && operator <= operator2) popToOutput()
                        else break
                    }
                    stack.push(token)
                }
                token == "(" -> stack.push(token)
                token == ")" -> {
                    while (stack.peek() != "(") popToOutput()
                    stack.pop()
                }
                else -> {
                    token.toDoubleOrNull()?.let {
                        numbers.add(it)
                    }
                    output.append(token).append(' ')
                }
            }
        }
        while (!stack.isEmpty()) popToOutput()
        return RPNExpression(output.toString(), operators, numbers)
    }