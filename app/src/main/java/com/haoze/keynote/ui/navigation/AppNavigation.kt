package com.haoze.keynote.ui.navigation

import androidx.activity.compose.BackHandler
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.haoze.keynote.ui.bill.AaSplitScreen
import com.haoze.keynote.ui.bill.BillScreen
import com.haoze.keynote.ui.bill.BillStatsScreen
import com.haoze.keynote.ui.chat.AIChatScreen
import com.haoze.keynote.ui.edit.EditNoteScreen
import com.haoze.keynote.ui.habit.HabitScreen
import com.haoze.keynote.ui.home.DateGroupNotesScreen
import com.haoze.keynote.ui.home.ExportDataScreen
import com.haoze.keynote.ui.home.FeatureCenterScreen
import com.haoze.keynote.ui.home.FeatureHomeScreen
import com.haoze.keynote.ui.home.HomeScreen
import com.haoze.keynote.ui.schedule.ScheduleScreen
import com.haoze.keynote.ui.settings.AiProviderManageScreen
import com.haoze.keynote.ui.settings.SettingsScreen
import com.haoze.keynote.ui.tag.TagNotesScreen
import com.haoze.keynote.ui.todo.TodoScreen
import com.haoze.keynote.ui.toolbox.KnowledgeVaultScreen
import com.haoze.keynote.ui.trash.TrashScreen
import com.haoze.keynote.viewmodel.SettingsViewModel
import kotlinx.coroutines.launch
import org.koin.compose.viewmodel.koinViewModel

sealed class Screen(val route: String, val title: String) {
    data object FeatureHome : Screen("feature_home", "首页")
    data object Home : Screen("home", "笔记")
    data object EditNote : Screen("edit_note", "编辑笔记")
    data object AIChat : Screen("ai_chat", "AI对话")
    data object Bill : Screen("bill", "记账")
    data object BillStats : Screen("bill_stats", "账单统计")
    data object AaSplit : Screen("aa_split", "AA计算")
    data object Habit : Screen("habit", "习惯打卡")
    data object Settings : Screen("settings", "设置")
    data object AiProviderManage : Screen("ai_provider_manage", "AI厂商管理")
    data object Todo : Screen("todo", "待办事项")
    data object TagNotes : Screen("tag_notes", "标签笔记")
    data object KnowledgeVault : Screen("knowledge_vault", "资料库")
    data object DateGroupNotes : Screen("date_group_notes", "按日期查看")
    data object Trash : Screen("trash", "回收站")
    data object DataExport : Screen("data_export", "数据导出")
    data object Schedule : Screen("schedule", "日程")
}

@Composable
fun AppPage(
    route: String,
    noteId: Long? = null,
    tagId: Long? = null,
    tagName: String? = null,
    onNavigate: (String, Long?, Long?, String?, Boolean) -> Unit,
    onBack: () -> Unit,
) {
    // 页面顶栏 menu 按钮：回到主界面（底部导航栏所在的首页）
    CompositionLocalProvider(
        LocalOpenMainNav provides { onNavigate(Screen.FeatureHome.route, null, null, null, true) }
    ) {
        when (route) {
            Screen.FeatureHome.route -> MainHomeContent(
                onNavigateToRoute = { target -> onNavigate(target, null, null, null, true) },
                onNavigateToTag = { id, name -> onNavigate(Screen.TagNotes.route, null, id, name, true) }
            )
            Screen.Home.route -> HomeScreen(
                onNavigateToEdit = { id -> onNavigate(Screen.EditNote.route, id, null, null, false) },
                onNavigateToTagNotes = { id, name -> onNavigate(Screen.TagNotes.route, null, id, name, false) }
            )
            Screen.EditNote.route -> noteId?.let { EditNoteScreen(it, onNavigateBack = onBack) }
            Screen.AIChat.route -> AIChatScreen { id -> onNavigate(Screen.EditNote.route, id, null, null, false) }
            Screen.Bill.route -> BillScreen()
            Screen.BillStats.route -> BillStatsScreen()
            Screen.AaSplit.route -> AaSplitScreen()
            Screen.Habit.route -> HabitScreen(viewModel = koinViewModel())
            Screen.DateGroupNotes.route -> DateGroupNotesScreen(
                onNavigateToEdit = { id -> onNavigate(Screen.EditNote.route, id, null, null, false) },
                onNavigateToTagNotes = { id, name -> onNavigate(Screen.TagNotes.route, null, id, name, false) }
            )
            Screen.Trash.route -> TrashScreen()
            Screen.DataExport.route -> ExportDataScreen()
            Screen.Todo.route -> TodoScreen(viewModel = koinViewModel())
            Screen.Schedule.route -> ScheduleScreen(viewModel = koinViewModel())
            Screen.KnowledgeVault.route -> KnowledgeVaultScreen()
            Screen.Settings.route -> SettingsScreen(
                onNavigateToProviderManage = {
                    onNavigate(Screen.AiProviderManage.route, null, null, null, false)
                }
            )
            Screen.AiProviderManage.route -> ProviderManagePage(onBack)
            Screen.TagNotes.route -> if (tagId != null) TagNotesScreen(
                tagId = tagId,
                tagName = tagName.orEmpty(),
                onNavigateToEdit = { id -> id?.let { onNavigate(Screen.EditNote.route, it, null, null, false) } }
            )
        }
    }
}

/**
 * 主界面：与谛听一致的双页结构（首页 / 功能中心），
 * HorizontalPager 承载两页，底部悬浮 FloatingNavigationBar 联动切换。
 */
@Composable
private fun MainHomeContent(
    onNavigateToRoute: (String) -> Unit,
    onNavigateToTag: (Long, String) -> Unit,
) {
    val pagerState = rememberPagerState(initialPage = 0) { 2 }
    val coroutineScope = rememberCoroutineScope()

    BackHandler(enabled = pagerState.currentPage == 1) {
        coroutineScope.launch {
            pagerState.animateScrollToPage(
                page = 0,
                animationSpec = tween(durationMillis = 280)
            )
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxSize()
        ) { page ->
            if (page == 0) {
                FeatureHomeScreen(onNavigateToRoute = onNavigateToRoute)
            } else {
                FeatureCenterScreen(
                    onNavigateToRoute = onNavigateToRoute,
                    onNavigateToTag = onNavigateToTag
                )
            }
        }

        FloatingNavigationBar(
            currentPage = pagerState.currentPage,
            onPageSelected = { targetPage ->
                if (pagerState.currentPage != targetPage) {
                    coroutineScope.launch {
                        pagerState.animateScrollToPage(
                            page = targetPage,
                            animationSpec = tween(durationMillis = 280)
                        )
                    }
                }
            },
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .navigationBarsPadding()
                .padding(bottom = 16.dp),
            pagerProgress = { pagerState.currentPage + pagerState.currentPageOffsetFraction }
        )
    }
}

@Composable
private fun ProviderManagePage(onBack: () -> Unit) {
    val viewModel: SettingsViewModel = koinViewModel()
    val providers = viewModel.providers.collectAsState().value
    val activeProviderId = viewModel.activeProviderId.collectAsState().value
    AiProviderManageScreen(
        onNavigateBack = onBack,
        providers = providers,
        activeProviderId = activeProviderId,
        onSelectProvider = viewModel::selectProvider,
        onUpdateProvider = viewModel::updateProvider,
        onDeleteProvider = viewModel::deleteCustomProvider,
        onAddProvider = { name, url, model, key -> viewModel.addCustomProvider(name, url, model, key) },
        sealKey = viewModel::sealZidaipass,
        openKey = viewModel::openZidaipass
    )
}
