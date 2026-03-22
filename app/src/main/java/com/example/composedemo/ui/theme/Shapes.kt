package com.example.composedemo.ui.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Shapes
import androidx.compose.ui.unit.dp

val Shapes = Shapes(
    // Small — chips, text fields, small cards
    small  = RoundedCornerShape(8.dp),
    // Medium — cards, dialogs
    medium = RoundedCornerShape(12.dp),
    // Large — bottom sheets, large cards
    large  = RoundedCornerShape(16.dp),
    // Extra large — full screen sheets
    extraLarge = RoundedCornerShape(28.dp)
)
