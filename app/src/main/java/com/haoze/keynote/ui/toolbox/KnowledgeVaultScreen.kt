package com.haoze.keynote.ui.toolbox

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import org.koin.compose.viewmodel.koinViewModel
import com.haoze.keynote.data.db.entity.KnowledgeVaultEntity
import com.haoze.keynote.ui.navigation.LocalDrawerScope
import com.haoze.keynote.ui.navigation.LocalDrawerState
import com.haoze.keynote.ui.theme.LocalAppColors
import com.haoze.keynote.ui.theme.SpacingTokens
import kotlinx.coroutines.launch
import androidx.compose.ui.res.painterResource
import com.haoze.keynote.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun KnowledgeVaultScreen() {
    val drawerState = LocalDrawerState.current
    val scope = LocalDrawerScope.current
    val viewModel: KnowledgeVaultViewModel = koinViewModel()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("资料库") },
                navigationIcon = {
                    IconButton(onClick = { scope.launch { drawerState.open() } }) {
                        Icon(painterResource(R.drawable.ic_menu), contentDescription = "菜单")
                    }
                }
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .navigationBarsPadding()
                .imePadding(),
            contentPadding = PaddingValues(
                horizontal = SpacingTokens.screenPadding,
                vertical = SpacingTokens.smallSpacing
            ),
            verticalArrangement = Arrangement.spacedBy(SpacingTokens.smallSpacing)
        ) {
            item {
                KnowledgeVaultContent(viewModel)
            }
        }
    }
}

@Composable
private fun KnowledgeVaultContent(viewModel: KnowledgeVaultViewModel) {
    val colors = LocalAppColors.current
    val knowledgeItems by viewModel.knowledgeItems.collectAsState()

    val reading = knowledgeItems.filter { it.category == "阅读清单" }
    val quotes = knowledgeItems.filter { it.category == "摘录卡片" }
    val ideas = knowledgeItems.filter { it.category == "灵感收件箱" }
    val sources = knowledgeItems.filter { it.category == "来源档案" }

    var searchQuery by remember { mutableStateOf("") }
    var selectedFilter by remember { mutableStateOf("全部") }

    val filterOptions = listOf("全部", "阅读清单", "摘录卡片", "灵感收件箱", "来源档案")

    val allItems = remember(knowledgeItems) { knowledgeItems }
    val filteredItems = remember(allItems, searchQuery, selectedFilter) {
        allItems.filter { item ->
            val matchSearch = searchQuery.isBlank() ||
                    item.title.contains(searchQuery, ignoreCase = true) ||
                    item.note.contains(searchQuery, ignoreCase = true) ||
                    item.source.contains(searchQuery, ignoreCase = true)
            val matchFilter = selectedFilter == "全部" || item.category == selectedFilter
            matchSearch && matchFilter
        }
    }

    FeatureCard("搜索资料", painterResource(R.drawable.ic_search)) {
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            placeholder = { Text("输入关键词搜索全部资料...") }
        )
        if (searchQuery.isNotBlank()) {
            if (filteredItems.isEmpty()) {
                Text(
                    "未找到匹配的资料",
                    style = MaterialTheme.typography.bodySmall,
                    color = colors.outline
                )
            } else {
                Text(
                    "找到 ${filteredItems.size} 条结果",
                    style = MaterialTheme.typography.labelSmall,
                    color = colors.onSurfaceVariant
                )
                filteredItems.take(8).forEach { item ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = SpacingTokens.tinySpacing),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        SuggestionChip(
                            onClick = {},
                            label = {
                                Text(
                                    item.category,
                                    style = MaterialTheme.typography.labelSmall
                                )
                            },
                            modifier = Modifier.height(SpacingTokens.chipHeight)
                        )
                        Spacer(Modifier.width(SpacingTokens.smallSpacing))
                        Text(
                            item.title,
                            style = MaterialTheme.typography.bodyMedium,
                            color = colors.onSurfaceVariant,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }
        }
    }

    Spacer(Modifier.height(SpacingTokens.contentSpacing))

    FeatureCard("资料概览", painterResource(R.drawable.ic_archive)) {
        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(SpacingTokens.smallSpacing),
            verticalArrangement = Arrangement.spacedBy(SpacingTokens.tinySpacing)
        ) {
            VaultStatChip(painterResource(R.drawable.ic_link), "阅读清单", reading.size, colors.primary)
            VaultStatChip(painterResource(R.drawable.ic_chat_bubble_outline), "摘录卡片", quotes.size, colors.tertiary)
            VaultStatChip(painterResource(R.drawable.ic_lightbulb), "灵感收件箱", ideas.size, colors.secondary)
            VaultStatChip(painterResource(R.drawable.ic_archive), "来源档案", sources.size, colors.outline)
        }
    }

    Spacer(Modifier.height(SpacingTokens.contentSpacing))

    FeatureCard("分类筛选", painterResource(R.drawable.ic_bookmark)) {
        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(SpacingTokens.smallSpacing),
            verticalArrangement = Arrangement.spacedBy(SpacingTokens.tinySpacing)
        ) {
            filterOptions.forEach { option ->
                FilterChip(
                    selected = selectedFilter == option,
                    onClick = { selectedFilter = option },
                    label = { Text(option, style = MaterialTheme.typography.labelSmall) }
                )
            }
        }
    }

    Spacer(Modifier.height(SpacingTokens.contentSpacing))

    if (selectedFilter == "全部" || selectedFilter == "阅读清单") {
        VaultListCard(
            title = "阅读清单",
            icon = painterResource(R.drawable.ic_link),
            items = reading,
            placeholder = "粘贴文章链接或书名...",
            onSave = { existing, value ->
                viewModel.saveKnowledgeItem(
                    existing?.copy(title = value, source = value, note = value)
                        ?: KnowledgeVaultEntity(title = value, source = value, category = "阅读清单", note = value)
                )
            },
            onDelete = { viewModel.deleteKnowledgeItem(it) }
        )
    }

    Spacer(Modifier.height(SpacingTokens.contentSpacing))

    if (selectedFilter == "全部" || selectedFilter == "摘录卡片") {
        VaultListCard(
            title = "摘录卡片",
            icon = painterResource(R.drawable.ic_chat_bubble_outline),
            items = quotes,
            placeholder = "记录高价值摘录...",
            onSave = { existing, value ->
                viewModel.saveKnowledgeItem(
                    existing?.copy(title = value.take(24), note = value)
                        ?: KnowledgeVaultEntity(title = value.take(24), source = "", category = "摘录卡片", note = value)
                )
            },
            onDelete = { viewModel.deleteKnowledgeItem(it) }
        )
    }

    Spacer(Modifier.height(SpacingTokens.contentSpacing))

    if (selectedFilter == "全部" || selectedFilter == "灵感收件箱") {
        VaultListCard(
            title = "灵感收件箱",
            icon = painterResource(R.drawable.ic_lightbulb),
            items = ideas,
            placeholder = "记录一闪而过的灵感...",
            onSave = { existing, value ->
                viewModel.saveKnowledgeItem(
                    existing?.copy(title = value.take(24), note = value)
                        ?: KnowledgeVaultEntity(title = value.take(24), source = "", category = "灵感收件箱", note = value)
                )
            },
            onDelete = { viewModel.deleteKnowledgeItem(it) }
        )
    }

    Spacer(Modifier.height(SpacingTokens.contentSpacing))

    if (selectedFilter == "全部" || selectedFilter == "来源档案") {
        VaultListCard(
            title = "来源档案",
            icon = painterResource(R.drawable.ic_archive),
            items = sources,
            placeholder = "记录作者、来源、可信度...",
            onSave = { existing, value ->
                viewModel.saveKnowledgeItem(
                    existing?.copy(title = value.take(24), source = value, note = value)
                        ?: KnowledgeVaultEntity(title = value.take(24), source = value, category = "来源档案", note = value)
                )
            },
            onDelete = { viewModel.deleteKnowledgeItem(it) }
        )
    }
}

@Composable
private fun VaultStatChip(
    icon: Painter,
    label: String,
    count: Int,
    accentColor: Color
) {
    val colors = LocalAppColors.current
    Row(
        modifier = Modifier
            .border(
                SpacingTokens.borderWidth,
                colors.outlineVariant,
                MaterialTheme.shapes.small
            )
            .padding(horizontal = SpacingTokens.smallSpacing, vertical = SpacingTokens.tinySpacing),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = icon,
            contentDescription = null,
            modifier = Modifier.size(SpacingTokens.iconSmall),
            tint = accentColor
        )
        Spacer(Modifier.width(SpacingTokens.tinySpacing))
        Text(
            "$count",
            style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.Bold,
            color = colors.onSurface
        )
        Spacer(Modifier.width(SpacingTokens.tinySpacing))
        Text(
            label,
            style = MaterialTheme.typography.labelSmall,
            color = colors.onSurfaceVariant
        )
    }
}

@Composable
private fun VaultListCard(
    title: String,
    icon: Painter,
    items: List<KnowledgeVaultEntity>,
    placeholder: String,
    onSave: (KnowledgeVaultEntity?, String) -> Unit,
    onDelete: (KnowledgeVaultEntity) -> Unit
) {
    val colors = LocalAppColors.current
    var input by remember { mutableStateOf("") }
    var editIndex by remember { mutableIntStateOf(-1) }

    FeatureCard(title, icon) {
        if (items.isEmpty()) {
            Text(
                "暂无内容，点击下方添加",
                style = MaterialTheme.typography.bodySmall,
                color = colors.outline
            )
        } else {
            items.forEachIndexed { index, item ->
                val isPinned = item.isPinned || index == 0
                Row(
                    Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (isPinned) {
                        Icon(
                            painterResource(R.drawable.ic_push_pin),
                            contentDescription = null,
                            modifier = Modifier.size(SpacingTokens.iconSmall),
                            tint = colors.primary
                        )
                        Spacer(Modifier.width(SpacingTokens.tinySpacing))
                    }
                    Text(
                        item.note.ifBlank { item.title },
                        modifier = Modifier.weight(1f),
                        style = MaterialTheme.typography.bodyMedium,
                        color = colors.onSurfaceVariant,
                        maxLines = 3,
                        overflow = TextOverflow.Ellipsis
                    )
                    IconButton(
                        onClick = {
                            editIndex = if (editIndex == index) -1 else index
                            input = if (editIndex == index) item.note.ifBlank { item.title } else ""
                        },
                        modifier = Modifier.size(48.dp)
                    ) {
                        Icon(
                            painterResource(R.drawable.ic_text_fields),
                            contentDescription = "编辑",
                            modifier = Modifier.size(16.dp),
                            tint = colors.onSurfaceVariant
                        )
                    }
                    IconButton(
                        onClick = { onDelete(item) },
                        modifier = Modifier.size(48.dp)
                    ) {
                        Icon(
                            painterResource(R.drawable.ic_delete),
                            contentDescription = "删除",
                            modifier = Modifier.size(16.dp),
                            tint = colors.outline
                        )
                    }
                }
                if (index < items.lastIndex) {
                    Spacer(
                        Modifier
                            .fillMaxWidth()
                            .height(0.5.dp)
                            .background(colors.outlineVariant)
                    )
                }
            }
        }

        Row(
            modifier = Modifier.padding(top = SpacingTokens.tinySpacing),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = input,
                onValueChange = { input = it },
                modifier = Modifier.weight(1f),
                singleLine = true,
                placeholder = { Text(if (editIndex >= 0) "编辑条目..." else placeholder) }
            )
            Spacer(Modifier.width(SpacingTokens.smallSpacing))
            IconButton(
                onClick = {
                    if (input.isNotBlank()) {
                        onSave(items.getOrNull(editIndex), input.trim())
                        input = ""
                        editIndex = -1
                    }
                }
            ) {
                Icon(
                    if (editIndex >= 0) painterResource(R.drawable.ic_check) else painterResource(R.drawable.ic_add),
                    contentDescription = if (editIndex >= 0) "确认编辑" else "添加",
                    tint = if (input.isNotBlank()) colors.primary else colors.outline
                )
            }
        }
    }
}

@Composable
private fun FeatureCard(title: String, icon: Painter, content: @Composable ColumnScope.() -> Unit) {
    val colors = LocalAppColors.current
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .border(SpacingTokens.borderWidth, colors.outlineVariant, MaterialTheme.shapes.medium),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerLow),
        elevation = CardDefaults.cardElevation(defaultElevation = SpacingTokens.cardElevation)
    ) {
        Column(
            Modifier.padding(SpacingTokens.screenPadding),
            verticalArrangement = Arrangement.spacedBy(SpacingTokens.smallSpacing)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    painter = icon,
                    contentDescription = null,
                    tint = colors.primary,
                    modifier = Modifier.size(SpacingTokens.iconMedium)
                )
                Spacer(Modifier.width(SpacingTokens.contentSpacing))
                Text(
                    title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Medium
                )
            }
            content()
        }
    }
}
