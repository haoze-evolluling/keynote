package com.haoze.keynote.ui.edit

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.jeziellago.compose.markdowntext.MarkdownText
import com.haoze.keynote.ui.theme.LocalAppColors
import com.haoze.keynote.ui.theme.SpacingTokens

import androidx.compose.foundation.shape.RoundedCornerShape

@Composable
fun MarkdownPreview(
    content: String,
    modifier: Modifier = Modifier
) {
    val colors = LocalAppColors.current
    if (content.isBlank()) {
        Text(
            "暂无内容",
            style = MaterialTheme.typography.bodyLarge,
            color = colors.onSurfaceVariant,
            modifier = modifier
                .border(1.dp, colors.outline.copy(alpha = 0.45f), RoundedCornerShape(12.dp))
                .padding(SpacingTokens.screenPadding)
        )
        return
    }

    val scrollState = rememberScrollState()

    Column(
        modifier = modifier
            .border(1.dp, colors.outline.copy(alpha = 0.45f), RoundedCornerShape(12.dp))
            .padding(SpacingTokens.screenPadding)
            .verticalScroll(scrollState)
    ) {
        MarkdownText(
            markdown = content,
            modifier = Modifier.fillMaxWidth(),
            style = MaterialTheme.typography.titleMedium
        )
    }
}
