package com.haoze.keynote.ui.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import com.haoze.keynote.R
import com.haoze.keynote.ui.components.SettingsDivider
import com.haoze.keynote.ui.components.SettingsGroup
import com.haoze.keynote.ui.components.SettingsGroupTitle
import com.haoze.keynote.ui.components.SettingsInfoText
import com.haoze.keynote.ui.components.SettingsNavigationItem
import com.haoze.keynote.ui.components.SettingsScaffold
import com.haoze.keynote.ui.navigation.LocalDrawerScope
import com.haoze.keynote.ui.navigation.LocalDrawerState
import com.haoze.keynote.ui.navigation.Screen
import kotlinx.coroutines.launch

@Composable
fun FeatureHomeScreen(
    onNavigateToRoute: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val drawerState = LocalDrawerState.current
    val scope = LocalDrawerScope.current
    val scrollState = rememberScrollState()

    SettingsScaffold(
        title = "首页",
        modifier = modifier,
        onMenuClick = { scope.launch { drawerState.open() } }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(scrollState)
        ) {
            SettingsInfoText("KeyNote 将笔记、日程、账单和 AI 对话放在同一个工作台中。")

            SettingsGroupTitle("笔记")
            SettingsGroup {
                SettingsNavigationItem(
                    title = "笔记列表",
                    subtitle = "查看、搜索和编辑全部笔记",
                    leadingIcon = painterResource(R.drawable.ic_description),
                    onClick = { onNavigateToRoute(Screen.Home.route) }
                )
                SettingsDivider()
                SettingsNavigationItem(
                    title = "按日期查看",
                    subtitle = "按更新时间回看笔记内容",
                    leadingIcon = painterResource(R.drawable.ic_date_range),
                    onClick = { onNavigateToRoute(Screen.DateGroupNotes.route) }
                )
                SettingsDivider()
                SettingsNavigationItem(
                    title = "资料库",
                    subtitle = "沉淀长期资料和知识条目",
                    leadingIcon = painterResource(R.drawable.ic_local_library),
                    onClick = { onNavigateToRoute(Screen.KnowledgeVault.route) }
                )
            }

            SettingsGroupTitle("日程")
            SettingsGroup {
                SettingsNavigationItem(
                    title = "日程列表",
                    subtitle = "管理日期、地点和提醒",
                    leadingIcon = painterResource(R.drawable.ic_event),
                    onClick = { onNavigateToRoute(Screen.Schedule.route) }
                )
                SettingsDivider()
                SettingsNavigationItem(
                    title = "待办事项",
                    subtitle = "跟踪任务状态和截止时间",
                    leadingIcon = painterResource(R.drawable.ic_check_box),
                    onClick = { onNavigateToRoute(Screen.Todo.route) }
                )
                SettingsDivider()
                SettingsNavigationItem(
                    title = "习惯打卡",
                    subtitle = "记录长期习惯和完成情况",
                    leadingIcon = painterResource(R.drawable.ic_fitness_center),
                    onClick = { onNavigateToRoute(Screen.Habit.route) }
                )
            }

            SettingsGroupTitle("财务")
            SettingsGroup {
                SettingsNavigationItem(
                    title = "记账",
                    subtitle = "记录收入、支出和分类",
                    leadingIcon = painterResource(R.drawable.ic_receipt),
                    onClick = { onNavigateToRoute(Screen.Bill.route) }
                )
                SettingsDivider()
                SettingsNavigationItem(
                    title = "统计",
                    subtitle = "查看账单趋势和分类占比",
                    leadingIcon = painterResource(R.drawable.ic_assessment),
                    onClick = { onNavigateToRoute(Screen.BillStats.route) }
                )
                SettingsDivider()
                SettingsNavigationItem(
                    title = "AA 计算",
                    subtitle = "管理多人分摊记录",
                    leadingIcon = painterResource(R.drawable.ic_people),
                    onClick = { onNavigateToRoute(Screen.AaSplit.route) }
                )
            }

            SettingsGroupTitle("AI")
            SettingsGroup {
                SettingsNavigationItem(
                    title = "AI 对话",
                    subtitle = "整理内容、生成想法和创建笔记",
                    leadingIcon = painterResource(R.drawable.ic_psychology),
                    onClick = { onNavigateToRoute(Screen.AIChat.route) }
                )
            }
        }
    }
}
