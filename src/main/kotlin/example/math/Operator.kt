package example.math

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

    override fun toString(): String {
        return stringRepresentation
    }
}