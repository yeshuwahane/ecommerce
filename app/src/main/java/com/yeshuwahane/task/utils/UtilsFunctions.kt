package com.yeshuwahane.task.utils

import androidx.compose.foundation.background
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color


fun cleanText(description: String): String {
    return description
        .replace("\\r", "") // Remove \r
        .replace("\\n", "") // Remove \n
        .replace("<\\/p>", "\n\n") // Replace paragraph tags with double newline
        .replace("<\\/li>", "\n") // Replace list items with newline
        .replace("<\\/ul>", "") // Remove closing unordered list tags
        .replace("<\\/ol>", "") // Remove closing ordered list tags
        .replace("<[^>]*>".toRegex(), "") // Remove any remaining HTML tags
        .trim()
}


fun Modifier.shimmerEffect(): Modifier = this.background(
    brush = Brush.linearGradient(
        colors = listOf(
            Color.LightGray.copy(alpha = 0.3f),
            Color.LightGray.copy(alpha = 0.7f),
            Color.LightGray.copy(alpha = 0.3f)
        ),
        start = Offset(0f, 0f),
        end = Offset(1000f, 0f)
    )
)
