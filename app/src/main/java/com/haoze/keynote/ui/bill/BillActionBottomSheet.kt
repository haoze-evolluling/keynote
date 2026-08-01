package com.haoze.keynote.ui.bill

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.haoze.keynote.ui.common.ActionRow
import com.haoze.keynote.ui.theme.LocalAppColors
import com.haoze.keynote.ui.theme.ModalTokens
import androidx.compose.ui.res.painterResource
import com.haoze.keynote.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BillActionBottomSheet(
    billItem: String,
    billAmount: Double,
    onEdit: () -> Unit,
    onViewDetails: () -> Unit,
    onCopyItem: () -> Unit,
    onCopyAmount: () -> Unit,
    onDelete: () -> Unit,
    onDismiss: () -> Unit
) {
    val colors = LocalAppColors.current
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        containerColor = colors.surface
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
        ) {
            ActionRow(painterResource(R.drawable.ic_edit), "编辑账单", onEdit)
            ActionRow(painterResource(R.drawable.ic_info), "查看详情", onViewDetails)

            HorizontalDivider(modifier = Modifier.padding(vertical = ModalTokens.menuDividerPaddingVertical))

            ActionRow(painterResource(R.drawable.ic_content_copy), "复制项目名称", onCopyItem)
            ActionRow(painterResource(R.drawable.ic_content_copy), "复制金额（¥${"%.2f".format(billAmount)}）", onCopyAmount)

            HorizontalDivider(modifier = Modifier.padding(vertical = ModalTokens.menuDividerPaddingVertical))

            ActionRow(painterResource(R.drawable.ic_delete), "删除账单", onDelete, isDestructive = true)

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}
