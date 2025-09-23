package com.example.accountsmanagerapplication.ui

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class CalculatorState(
    var displayValue: String = "0",
    var currentOperator: String? = null,
    var firstOperand: String? = null
)

sealed interface CalculatorAction {
    data class Number(val number: Int) : CalculatorAction
    data class Operator(val symbol: String) : CalculatorAction
    object Decimal : CalculatorAction
    object Percentage : CalculatorAction
    object Clear : CalculatorAction
    object Equals : CalculatorAction
}

class CalculatorViewModel : ViewModel() {
    private val _state = MutableStateFlow(CalculatorState())
    val state: StateFlow<CalculatorState> = _state.asStateFlow()

    fun onAction(action: CalculatorAction) {
        val currentState = _state.value
        val newState = currentState.copy()

        when (action) {
            is CalculatorAction.Number -> {
                handleNumberInput(newState, action.number)
            }
            is CalculatorAction.Operator -> {
                handleOperatorInput(newState, action.symbol)
            }
            CalculatorAction.Decimal -> {
                handleDecimalInput(newState)
            }
            CalculatorAction.Percentage -> {
                handlePercentageInput(newState)
            }
            CalculatorAction.Clear -> {
                resetCalculator(newState)
            }
            CalculatorAction.Equals -> {
                handleEqualsInput(newState)
            }
        }

        _state.value = newState
    }

    private fun handleNumberInput(state: CalculatorState, number: Int) {
        // If we just completed a calculation (no current operator), start fresh
        if (state.currentOperator == null && state.firstOperand != null) {
            state.displayValue = number.toString()
            state.firstOperand = null
        } else if (state.displayValue == "0") {
            state.displayValue = number.toString()
        } else {
            state.displayValue += number.toString()
        }
    }

    private fun handleOperatorInput(state: CalculatorState, symbol: String) {
        if (state.firstOperand == null) {
            state.firstOperand = state.displayValue
            state.currentOperator = symbol
            state.displayValue = "0"
        } else if (state.currentOperator != null) {
            // Calculate intermediate result first
            val result = performCalculation(
                state.firstOperand!!.toDouble(),
                state.currentOperator!!,
                state.displayValue.toDouble()
            )
            state.firstOperand = result.toString()
            state.currentOperator = symbol
            state.displayValue = "0"
        }
    }

    private fun handleDecimalInput(state: CalculatorState) {
        if (!state.displayValue.contains(".")) {
            state.displayValue += "."
        }
    }

    private fun handlePercentageInput(state: CalculatorState) {
        if (state.firstOperand != null && state.currentOperator != null) {
            val result = state.firstOperand!!.toDouble() * (state.displayValue.toDouble() / 100.0)
            state.displayValue = formatResult(result)
            state.firstOperand = null
            state.currentOperator = null
        }
    }

    private fun handleEqualsInput(state: CalculatorState) {
        if (state.firstOperand != null && state.currentOperator != null) {
            val result = performCalculation(
                state.firstOperand!!.toDouble(),
                state.currentOperator!!,
                state.displayValue.toDouble()
            )
            state.displayValue = formatResult(result)
            state.firstOperand = null
            state.currentOperator = null
        }
    }

    private fun resetCalculator(state: CalculatorState) {
        state.displayValue = "0"
        state.currentOperator = null
        state.firstOperand = null
    }

    private fun performCalculation(firstOperand: Double, operator: String, secondOperand: Double): Double {
        return when (operator) {
            "+" -> firstOperand + secondOperand
            "-" -> firstOperand - secondOperand
            "×" -> firstOperand * secondOperand
            "÷" -> if (secondOperand != 0.0) firstOperand / secondOperand else 0.0
            else -> secondOperand
        }
    }

    private fun formatResult(result: Double): String {
        return if (result == result.toLong().toDouble()) {
            result.toLong().toString()
        } else {
            String.format("%.2f", result).trimEnd('0').trimEnd('.')
        }
    }
}
