package com.example.accountsmanagerapplication.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun CalculatorBottomSheet(
    state: CalculatorState,
    onAction: (CalculatorAction) -> Unit,
    onConfirm: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        // Display
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Text(
                text = state.displayValue,
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.End,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            )
        }

        // Calculator Buttons Grid
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Row 1: Clear, Percentage, Operator (÷), Operator (×)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                CalculatorButton(
                    text = "C",
                    modifier = Modifier.weight(1f),
                    backgroundColor = MaterialTheme.colorScheme.error,
                    textColor = MaterialTheme.colorScheme.onError
                ) {
                    onAction(CalculatorAction.Clear)
                }
                CalculatorButton(
                    text = "%",
                    modifier = Modifier.weight(1f),
                    backgroundColor = MaterialTheme.colorScheme.secondary,
                    textColor = MaterialTheme.colorScheme.onSecondary
                ) {
                    onAction(CalculatorAction.Percentage)
                }
                CalculatorButton(
                    text = "÷",
                    modifier = Modifier.weight(1f),
                    backgroundColor = MaterialTheme.colorScheme.secondary,
                    textColor = MaterialTheme.colorScheme.onSecondary
                ) {
                    onAction(CalculatorAction.Operator("÷"))
                }
                CalculatorButton(
                    text = "×",
                    modifier = Modifier.weight(1f),
                    backgroundColor = MaterialTheme.colorScheme.secondary,
                    textColor = MaterialTheme.colorScheme.onSecondary
                ) {
                    onAction(CalculatorAction.Operator("×"))
                }
            }

            // Row 2: 7, 8, 9, Operator (-)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                CalculatorButton(
                    text = "7",
                    modifier = Modifier.weight(1f)
                ) {
                    onAction(CalculatorAction.Number(7))
                }
                CalculatorButton(
                    text = "8",
                    modifier = Modifier.weight(1f)
                ) {
                    onAction(CalculatorAction.Number(8))
                }
                CalculatorButton(
                    text = "9",
                    modifier = Modifier.weight(1f)
                ) {
                    onAction(CalculatorAction.Number(9))
                }
                CalculatorButton(
                    text = "-",
                    modifier = Modifier.weight(1f),
                    backgroundColor = MaterialTheme.colorScheme.secondary,
                    textColor = MaterialTheme.colorScheme.onSecondary
                ) {
                    onAction(CalculatorAction.Operator("-"))
                }
            }

            // Row 3: 4, 5, 6, Operator (+)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                CalculatorButton(
                    text = "4",
                    modifier = Modifier.weight(1f)
                ) {
                    onAction(CalculatorAction.Number(4))
                }
                CalculatorButton(
                    text = "5",
                    modifier = Modifier.weight(1f)
                ) {
                    onAction(CalculatorAction.Number(5))
                }
                CalculatorButton(
                    text = "6",
                    modifier = Modifier.weight(1f)
                ) {
                    onAction(CalculatorAction.Number(6))
                }
                CalculatorButton(
                    text = "+",
                    modifier = Modifier.weight(1f),
                    backgroundColor = MaterialTheme.colorScheme.secondary,
                    textColor = MaterialTheme.colorScheme.onSecondary
                ) {
                    onAction(CalculatorAction.Operator("+"))
                }
            }

            // Row 4: 1, 2, 3, Equals
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                CalculatorButton(
                    text = "1",
                    modifier = Modifier.weight(1f)
                ) {
                    onAction(CalculatorAction.Number(1))
                }
                CalculatorButton(
                    text = "2",
                    modifier = Modifier.weight(1f)
                ) {
                    onAction(CalculatorAction.Number(2))
                }
                CalculatorButton(
                    text = "3",
                    modifier = Modifier.weight(1f)
                ) {
                    onAction(CalculatorAction.Number(3))
                }
                CalculatorButton(
                    text = "=",
                    modifier = Modifier.weight(1f),
                    backgroundColor = MaterialTheme.colorScheme.primary,
                    textColor = MaterialTheme.colorScheme.onPrimary
                ) {
                    onAction(CalculatorAction.Equals)
                }
            }

            // Row 5: 0, Decimal
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                CalculatorButton(
                    text = "0",
                    modifier = Modifier.weight(2f)
                ) {
                    onAction(CalculatorAction.Number(0))
                }
                CalculatorButton(
                    text = ".",
                    modifier = Modifier.weight(1f)
                ) {
                    onAction(CalculatorAction.Decimal)
                }
                // Empty space for alignment
                Box(modifier = Modifier.weight(1f))
            }
        }

        // Confirm Button
        Button(
            onClick = { onConfirm(state.displayValue) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary
            )
        ) {
            Text(
                text = "Confirm",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
private fun CalculatorButton(
    text: String,
    modifier: Modifier = Modifier,
    backgroundColor: Color = MaterialTheme.colorScheme.surface,
    textColor: Color = MaterialTheme.colorScheme.onSurface,
    onClick: () -> Unit
) {
    Box(
        modifier = modifier
            .height(60.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(backgroundColor)
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Medium,
            color = textColor,
            textAlign = TextAlign.Center
        )
    }
}
