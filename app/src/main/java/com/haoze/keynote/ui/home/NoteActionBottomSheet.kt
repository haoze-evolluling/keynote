package com.haoze.keynote.ui.home

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import com.haoze.keynote.ui.common.ActionRow
import com.haoze.keynote.ui.theme.LocalAppColors
import com.haoze.keynote.ui.theme.ModalTokens
import androidx.compose.ui.res.painterResource
import com.haoze.keynote.R

@Composable
fun NoteActionBottomSheet(
    noteTitle: String,
    noteContent: String = "",
    isAiTagLoading: Boolean = false,
    isSummarizing: Boolean = false,
    isGeneratingTitle: Boolean = false,
    onEdit: () -> Unit,
    onShare: () -> Unit,
    onAiSummary: () -> Unit,
    onCopyContent: () -> Unit,
    onViewDetails: () -> Unit,
    onAiTag: () -> Unit,
    onAiGenerateTitle: () -> Unit,
    onAddTag: () -> Unit,
    onManageTags: () -> Unit,
    onDelete: () -> Unit,
    onDismiss: () -> Unit
) {
    val colors = LocalAppColors.current
    val screenHeight = LocalConfiguration.current.screenHeightDp.dp

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "笔记操作",
                style = MaterialTheme.typography.titleLarge
            )
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(max = screenHeight * 0.58f)
                    .verticalScroll(rememberScrollState())
            ) {
                ActionRow(painterResource(R.drawable.ic_edit), "编辑笔记", onEdit, rowHeight = 48.dp, horizontalPadding = 16.dp)
                ActionRow(painterResource(R.drawable.ic_info), "查看详情", onViewDetails, rowHeight = 48.dp, horizontalPadding = 16.dp)

                HorizontalDivider(modifier = Modifier.padding(vertical = ModalTokens.menuDividerPaddingVertical))

                ActionRow(painterResource(R.drawable.ic_label), "添加标签", onAddTag, rowHeight = 48.dp, horizontalPadding = 16.dp)
                ActionRow(painterResource(R.drawable.ic_label), "管理标签", onManageTags, rowHeight = 48.dp, horizontalPadding = 16.dp)

                HorizontalDivider(modifier = Modifier.padding(vertical = ModalTokens.menuDividerPaddingVertical))

                ActionRow(painterResource(R.drawable.ic_auto_awesome), "AI 摘要", onAiSummary, isLoading = isSummarizing, loadingLabel = "正在摘要...", rowHeight = 48.dp, horizontalPadding = 16.dp)
                ActionRow(painterResource(R.drawable.ic_auto_awesome), "AI 生成标题", onAiGenerateTitle, isLoading = isGeneratingTitle, loadingLabel = "生成中...", rowHeight = 48.dp, horizontalPadding = 16.dp)
                ActionRow(painterResource(R.drawable.ic_label), "AI 标签", onAiTag, isLoading = isAiTagLoading, loadingLabel = "生成中...", rowHeight = 48.dp, horizontalPadding = 16.dp)

                HorizontalDivider(modifier = Modifier.padding(vertical = ModalTokens.menuDividerPaddingVertical))

                ActionRow(painterResource(R.drawable.ic_share), "分享笔记", onShare, rowHeight = 48.dp, horizontalPadding = 16.dp)
                ActionRow(painterResource(R.drawable.ic_content_copy), "复制内容", onCopyContent, rowHeight = 48.dp, horizontalPadding = 16.dp)

                HorizontalDivider(modifier = Modifier.padding(vertical = ModalTokens.menuDividerPaddingVertical))

                ActionRow(painterResource(R.drawable.ic_delete), "删除笔记", onDelete, isDestructive = true, rowHeight = 48.dp, horizontalPadding = 16.dp)
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("关闭")
            }
        },
        containerColor = colors.surface,
        titleContentColor = colors.onSurface,
        textContentColor = colors.onSurface,
        shape = MaterialTheme.shapes.extraLarge
    )
}
