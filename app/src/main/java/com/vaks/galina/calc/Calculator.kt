package com.vaks.galina.calc;

import java.math.BigDecimal

public class Calculator (private val mainActivity: MainActivity){

    // Main member - what is shown
    var output = "0" // Should always be not empty
    var memory: BigDecimal = BigDecimal.ZERO // Calc memory, like M+ and stuff

    var isOperatorJustPressed = false
    var operatorPressed = ""
    var firstOperand = ""
    var isEqualJustPressed = false
    var isBeyondDecimal = false
    // TODO: recheck operatorjustpressed, equaljustpressed

    // Error message in output
    val DIVIDE_BY_ZERO = "Error: Divide by zero"
    val DECIMAL_SCALE = 10

    // Add digit to output, typing a number
    // If an operator was just pressed, just put the digit as a new number
    // Will throw IllegalArgumentException if value is not a digit 0..9
    fun addDigit(i: Int) {
        if (i<0 || i>9) throw IllegalArgumentException("Method addDigit only accepts digits 0..9")

        if (isOperatorJustPressed || isEqualJustPressed || output == DIVIDE_BY_ZERO || output == "0") {
            output = i.toString() // Start new typing
            isBeyondDecimal = false
        }
        else output += i.toString()

        mainActivity.updateOutput()
        isOperatorJustPressed = false
        isEqualJustPressed = false
    }

    // Mark an operator was just pressed, store it and store the first operand
    // Will throw IllegalArgumentException if value is not a basic arithmetic function
    fun pressOperator(operator: String) {
        if (operator!="+" && operator!="-" && operator!="*" && operator!="/")
            throw IllegalArgumentException("Unrecognized operator")
        if (output == DIVIDE_BY_ZERO) return // We don't operate on an error string

        isOperatorJustPressed = true
        isEqualJustPressed = false
        operatorPressed = operator
        // The following could happen due to backspace, but I'd like
        // to keep the option of displaying "-"
        firstOperand = if (output == "-") "0" else output
        isBeyondDecimal = false // New operand
    }

    // If decimal point was already pressed, nothing happens
    fun decimalPointPressed() {
        when {
            isOperatorJustPressed || isEqualJustPressed || output == DIVIDE_BY_ZERO -> {
                output = "0." // Start new number with decimal point
                isOperatorJustPressed = false
                isEqualJustPressed = false
            }
            !isBeyondDecimal -> output += "."
        }
        isBeyondDecimal = true
        mainActivity.updateOutput()
    }

    fun equalPressed() {
        // Should happen together, but just in case
        if (operatorPressed == "" || firstOperand == "" || output == DIVIDE_BY_ZERO) return
        output = when (operatorPressed) {
            "+" -> (firstOperand.toBigDecimal()+output.toBigDecimal()).toPlainString()
            "-" -> (firstOperand.toBigDecimal()-output.toBigDecimal()).toPlainString()
            "*" -> (firstOperand.toBigDecimal()*output.toBigDecimal()).toPlainString()
            "/" -> {
                if (output == "0") DIVIDE_BY_ZERO
                else (firstOperand.toBigDecimal().divide(output.toBigDecimal(),DECIMAL_SCALE, BigDecimal.ROUND_HALF_UP)).toPlainString()
            }
            // We know the operator is not empty, or we would've returned
            else -> throw IllegalArgumentException("Unrecognized operator")
        }

        if (output != DIVIDE_BY_ZERO) output = normalizeNumberInText(output)
        mainActivity.updateOutput()
        operatorPressed = ""
        firstOperand = ""
        isEqualJustPressed = true
        isOperatorJustPressed = false
    }

    // Switched from button to menu option which is always visible
    // Deletes last entered char, makes zero if there was only one digit
    // or if last operation resulted in a divide by zero
    fun backspace(): Boolean {
        if (output.length < 2 || output == DIVIDE_BY_ZERO ) output = "0"
        else {
            if (output[output.length-1] == '.')
                isBeyondDecimal = false //We;re deleyting the decimal point, so no longer beyond it
            output = output.substring(0, output.length-1)
        }
        mainActivity.updateOutput()
        return true
    }

    fun memPlus() {
        memory += output.toBigDecimal()
    }

    fun memMinus() {
        memory -= output.toBigDecimal()
    }

    fun memR() {
        output = memory.toPlainString()
        isBeyondDecimal = memory.toBigInteger() != memory
        mainActivity.updateOutput()
    }

    fun changeSign() {
        output = (-output.toBigDecimal()).toPlainString()
        mainActivity.updateOutput()
    }

    // Remove redundant zeros and/or decimal point
    private fun normalizeNumberInText (num: String): String {
        return num.toBigDecimal().stripTrailingZeros().toPlainString()
    }
}