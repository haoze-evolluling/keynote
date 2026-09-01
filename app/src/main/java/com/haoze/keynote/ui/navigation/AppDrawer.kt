package com.haoze.keynote.ui.navigation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.clickable
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.unit.dp
import com.haoze.keynote.R
import com.haoze.keynote.data.db.KeyNoteDatabase
import com.haoze.keynote.data.db.entity.TagEntity
import com.haoze.keynote.ui.theme.LocalAppColors
import com.haoze.keynote.ui.theme.MotionTokens

@Stable
internal data class DrawerItem(
    val label: String,
    val iconRes: Int,
    val route: String,
)

@Stable
private data class DrawerGroup(
    val label: String,
    val iconRes: Int,
    val items: List<DrawerItem>,
)

@Composable
internal fun AppDrawerContent(
    currentRoute: String?,
    onNavigateToRoute: (String) -> Unit,
    onNavigateToTag: (Long, String) -> Unit,
    onCloseDrawer: () -> Unit,
) {
    val context = androidx.compose.ui.platform.LocalContext.current
    val tags by remember { KeyNoteDatabase.getDatabase(context).tagDao().getActiveTags() }.collectAsState(initial = emptyList())

    val drawerGroups = remember {
        listOf(
            DrawerGroup("首页", R.drawable.ic_dashboard, listOf(
                DrawerItem("首页", R.drawable.ic_dashboard, Screen.FeatureHome.route),
            )),
            DrawerGroup("笔记", R.drawable.ic_description, listOf(
                DrawerItem("笔记列表", R.drawable.ic_description, Screen.Home.route),
                DrawerItem("按日期查看", R.drawable.ic_date_range, Screen.DateGroupNotes.route),
                DrawerItem("资料库", R.drawable.ic_local_library, Screen.KnowledgeVault.route),
            )),
            DrawerGroup("日程", R.drawable.ic_event, listOf(
                DrawerItem("日程列表", R.drawable.ic_event, Screen.Schedule.route),
                DrawerItem("待办事项", R.drawable.ic_check_box, Screen.Todo.route),
                DrawerItem("习惯打卡", R.drawable.ic_fitness_center, Screen.Habit.route),
            )),
            DrawerGroup("记账", R.drawable.ic_receipt, listOf(
                DrawerItem("记账", R.drawable.ic_receipt, Screen.Bill.route),
                DrawerItem("统计", R.drawable.ic_assessment, Screen.BillStats.route),
                DrawerItem("AA计算", R.drawable.ic_people, Screen.AaSplit.route),
            )),
            DrawerGroup("AI对话", R.drawable.ic_psychology, listOf(
                DrawerItem("AI对话", R.drawable.ic_psychology, Screen.AIChat.route),
            )),
            DrawerGroup("系统", R.drawable.ic_settings, listOf(
                DrawerItem("导出数据", R.drawable.ic_file_download, Screen.DataExport.route),
                DrawerItem("回收站", R.drawable.ic_delete, Screen.Trash.route),
                DrawerItem("设置", R.drawable.ic_settings, Screen.Settings.route),
            ))
        )
    }

    var expandedGroups by remember(currentRoute) { mutableStateOf(defaultExpandedGroups(currentRoute, drawerGroups)) }
    val isTagSelected = remember(currentRoute) { currentRoute == Screen.TagNotes.route }

    Column(
        modifier = Modifier.verticalScroll(rememberScrollState())
    ) {
        DrawerHeader()

        drawerGroups.forEach { group ->
            val isExpanded = group.label in expandedGroups
            val isSelected = group.items.any { it.route == currentRoute } ||
                (group.label == "笔记" && isTagSelected)

            if (group.items.size == 1) {
                val item = group.items.first()
                DrawerGroupRow(
                    label = item.label,
                    iconRes = item.iconRes,
                    isExpanded = false,
                    isSelected = isSelected,
                    showTrailingIcon = false,
                    onClick = {
                        onNavigateToRoute(item.route)
                        onCloseDrawer()
                    }
                )
            } else {
                DrawerGroupRow(
                    label = group.label,
                    iconRes = group.iconRes,
                    isExpanded = isExpanded,
                    isSelected = isSelected,
                    onClick = {
                        expandedGroups = if (isExpanded) {
                            expandedGroups - group.label
                        } else {
                            expandedGroups + group.label
                        }
                    }
                )

                AnimatedDrawerVisibility(visible = isExpanded) {
                    Column {
                        group.items.forEach { item ->
                            DrawerNavRow(
                                itemLabel = item.label,
                                iconRes = item.iconRes,
                                isSelected = currentRoute == item.route,
                                onClick = {
                                    onNavigateToRoute(item.route)
                                    onCloseDrawer()
                                }
                            )
                        }

                        if (group.label == "笔记") {
                            DrawerTagsSection(
                                tags = tags,
                                isExpanded = expandedGroups.contains("标签"),
                                isSelected = isTagSelected,
                                onToggle = {
                                    expandedGroups = if (expandedGroups.contains("标签")) {
                                        expandedGroups - "标签"
                                    } else {
                                        expandedGroups + "标签"
                                    }
                                },
                                onTagClick = { tag ->
                                    onNavigateToTag(tag.id, tag.name)
                                    onCloseDrawer()
                                }
                            )
                        }
                    }
                }
            }
        }

        Spacer(Modifier.height(8.dp))
    }
}

private fun defaultExpandedGroups(currentRoute: String?, drawerGroups: List<DrawerGroup>): Set<String> {
    val selectedGroup = drawerGroups.firstOrNull { group ->
        group.items.any { it.route == currentRoute }
    }?.label

    return setOfNotNull("笔记", "标签", selectedGroup)
}

@Composable
private fun AnimatedDrawerVisibility(
    visible: Boolean,
    content: @Composable () -> Unit,
) {
    AnimatedVisibility(
        visible = visible,
        enter = expandVertically(
            animationSpec = tween(MotionTokens.DurationMedium, easing = MotionTokens.StandardEasing)
        ) + fadeIn(
            animationSpec = tween(MotionTokens.DurationShort, easing = MotionTokens.StandardEasing)
        ),
        exit = shrinkVertically(
            animationSpec = tween(MotionTokens.DurationMedium, easing = MotionTokens.StandardEasing)
        ) + fadeOut(
            animationSpec = tween(MotionTokens.DurationShort, easing = MotionTokens.StandardEasing)
        )
    ) {
        content()
    }
}

@Composable
private fun DrawerHeader() {
    val colors = LocalAppColors.current
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painterResource(R.drawable.ic_logo),
            contentDescription = "KeyNote Logo",
            modifier = Modifier.size(40.dp),
            tint = colors.unspecified
        )
        Spacer(Modifier.width(12.dp))
        Column {
            Text(
                "KeyNote",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold
            )
            Text(
                "个人管理工具",
                style = MaterialTheme.typography.bodySmall,
                color = colors.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun DrawerGroupRow(
    label: String,
    iconRes: Int,
    isExpanded: Boolean,
    isSelected: Boolean,
    showTrailingIcon: Boolean = true,
    onClick: () -> Unit,
) {
    val colors = LocalAppColors.current
    val contentColor by animateColorAsState(
        targetValue = if (isSelected) colors.primary else colors.onSurface,
        animationSpec = tween(MotionTokens.DurationShort, easing = MotionTokens.StandardEasing),
        label = "drawerGroupContentColor"
    )
    val backgroundColor by animateColorAsState(
        targetValue = if (isSelected) MaterialTheme.colorScheme.secondaryContainer else colors.transparent,
        animationSpec = tween(MotionTokens.DurationShort, easing = MotionTokens.StandardEasing),
        label = "drawerGroupBackgroundColor"
    )
    val trailingRotation by animateFloatAsState(
        targetValue = if (isExpanded) 90f else 0f,
        animationSpec = tween(MotionTokens.DurationMedium, easing = MotionTokens.StandardEasing),
        label = "drawerGroupTrailingRotation"
    )
    val icon = painterResource(iconRes)
    val trailingIcon = painterResource(R.drawable.ic_keyboard_arrow_right)

    Row(
        modifier = Modifier
            .padding(horizontal = 8.dp, vertical = 2.dp)
            .fillMaxWidth()
            .heightIn(min = 44.dp)
            .background(
                color = backgroundColor,
                shape = MaterialTheme.shapes.extraLarge
            )
            .clickable(onClick = onClick)
            .padding(horizontal = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = icon,
            contentDescription = label,
            tint = contentColor,
            modifier = Modifier.size(23.dp)
        )
        Spacer(Modifier.width(12.dp))
        Text(
            label,
            color = contentColor,
            fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Medium,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.weight(1f)
        )
        if (showTrailingIcon) {
            Icon(
                painter = trailingIcon,
                contentDescription = if (isExpanded) "收起" else "展开",
                modifier = Modifier
                    .size(20.dp)
                    .rotate(trailingRotation),
                tint = colors.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun DrawerNavRow(
    itemLabel: String,
    iconRes: Int,
    isSelected: Boolean,
    onClick: () -> Unit,
) {
    val colors = LocalAppColors.current
    val contentColor by animateColorAsState(
        targetValue = if (isSelected) colors.primary else colors.onSurface,
        animationSpec = tween(MotionTokens.DurationShort, easing = MotionTokens.StandardEasing),
        label = "drawerNavContentColor"
    )
    val backgroundColor by animateColorAsState(
        targetValue = if (isSelected) MaterialTheme.colorScheme.secondaryContainer else colors.transparent,
        animationSpec = tween(MotionTokens.DurationShort, easing = MotionTokens.StandardEasing),
        label = "drawerNavBackgroundColor"
    )
    val icon = painterResource(iconRes)

    Row(
        modifier = Modifier
            .padding(horizontal = 8.dp, vertical = 2.dp)
            .fillMaxWidth()
            .heightIn(min = 44.dp)
            .background(
                color = backgroundColor,
                shape = MaterialTheme.shapes.extraLarge
            )
            .clickable(onClick = onClick)
            .padding(start = 36.dp, end = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = icon,
            contentDescription = itemLabel,
            tint = contentColor,
            modifier = Modifier.size(23.dp)
        )
        Spacer(Modifier.width(12.dp))
        Text(
            itemLabel,
            color = contentColor,
            fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
private fun DrawerTagsSection(
    tags: List<TagEntity>,
    isExpanded: Boolean,
    isSelected: Boolean,
    onToggle: () -> Unit,
    onTagClick: (TagEntity) -> Unit,
) {
    val colors = LocalAppColors.current
    val contentColor by animateColorAsState(
        targetValue = if (isSelected) colors.primary else colors.onSurface,
        animationSpec = tween(MotionTokens.DurationShort, easing = MotionTokens.StandardEasing),
        label = "drawerTagsContentColor"
    )
    val backgroundColor by animateColorAsState(
        targetValue = if (isSelected) MaterialTheme.colorScheme.secondaryContainer else colors.transparent,
        animationSpec = tween(MotionTokens.DurationShort, easing = MotionTokens.StandardEasing),
        label = "drawerTagsBackgroundColor"
    )
    val trailingRotation by animateFloatAsState(
        targetValue = if (isExpanded) 90f else 0f,
        animationSpec = tween(MotionTokens.DurationMedium, easing = MotionTokens.StandardEasing),
        label = "drawerTagsTrailingRotation"
    )
    val trailingIcon = painterResource(R.drawable.ic_keyboard_arrow_right)

    Row(
        modifier = Modifier
            .padding(horizontal = 8.dp, vertical = 2.dp)
            .fillMaxWidth()
            .heightIn(min = 44.dp)
            .background(
                color = backgroundColor,
                shape = MaterialTheme.shapes.extraLarge
            )
            .clickable(onClick = onToggle)
            .padding(start = 36.dp, end = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painterResource(R.drawable.ic_label_mirrored),
            contentDescription = "标签分类",
            tint = contentColor,
            modifier = Modifier.size(23.dp)
        )
        Spacer(Modifier.width(12.dp))
        Text(
            "标签分类",
            color = contentColor,
            fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.weight(1f)
        )
        Icon(
            painter = trailingIcon,
            contentDescription = if (isExpanded) "收起" else "展开",
            modifier = Modifier
                .size(18.dp)
                .rotate(trailingRotation),
            tint = colors.onSurfaceVariant
        )
    }

    AnimatedDrawerVisibility(visible = isExpanded) {
        Column {
            if (tags.isEmpty()) {
                Text(
                    "暂无标签",
                    style = MaterialTheme.typography.bodyMedium,
                    color = colors.onSurfaceVariant,
                    modifier = Modifier.padding(start = 68.dp, end = 16.dp, top = 8.dp, bottom = 10.dp)
                )
            } else {
                tags.forEach { tag ->
                    Row(
                        modifier = Modifier
                            .padding(horizontal = 8.dp, vertical = 2.dp)
                            .fillMaxWidth()
                            .heightIn(min = 44.dp)
                            .background(
                                color = colors.transparent,
                                shape = MaterialTheme.shapes.extraLarge
                            )
                            .clickable { onTagClick(tag) }
                            .padding(start = 68.dp, end = 12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            "#${tag.name}",
                            style = MaterialTheme.typography.bodyMedium,
                            color = colors.onSurface,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }
        }
    }
}
