package com.haoze.keynote.ui.navigation

import android.net.Uri
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.width
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.haoze.keynote.ui.home.HomeScreen
import com.haoze.keynote.ui.home.FeatureHomeScreen
import com.haoze.keynote.ui.habit.HabitScreen
import com.haoze.keynote.ui.edit.EditNoteScreen
import com.haoze.keynote.ui.settings.SettingsScreen
import com.haoze.keynote.ui.settings.AiProviderManageScreen
import com.haoze.keynote.ui.tag.TagNotesScreen
import com.haoze.keynote.ui.chat.AIChatScreen
import com.haoze.keynote.ui.bill.AaSplitScreen
import com.haoze.keynote.ui.bill.BillScreen
import com.haoze.keynote.ui.bill.BillStatsScreen
import com.haoze.keynote.ui.trash.TrashScreen
import com.haoze.keynote.ui.home.DateGroupNotesScreen
import com.haoze.keynote.ui.home.ExportDataScreen
import com.haoze.keynote.ui.schedule.ScheduleScreen
import com.haoze.keynote.ui.todo.TodoScreen
import com.haoze.keynote.ui.toolbox.KnowledgeVaultScreen
import com.haoze.keynote.ui.theme.MotionTokens
import com.haoze.keynote.viewmodel.SettingsViewModel
import org.koin.compose.viewmodel.koinViewModel
import kotlinx.coroutines.launch

sealed class Screen(val route: String, val title: String) {
    data object FeatureHome : Screen("feature_home", "首页")
    data object Home : Screen("home", "笔记")
    data object EditNote : Screen("edit_note?noteId={noteId}", "编辑笔记") {
        fun createRoute(noteId: Long) = "edit_note?noteId=$noteId"
    }
    data object AIChat : Screen("ai_chat", "AI对话")
    data object Bill : Screen("bill", "记账")
    data object BillStats : Screen("bill_stats", "账单统计")
    data object AaSplit : Screen("aa_split", "AA计算")
    data object Habit : Screen("habit", "习惯打卡")
    data object Settings : Screen("settings", "设置")
    data object AiProviderManage : Screen("ai_provider_manage", "AI厂商管理")
    data object Todo : Screen("todo", "待办事项")
    data object TagNotes : Screen("tag_notes/{tagId}/{tagName}", "标签笔记") {
        fun createRoute(tagId: Long, tagName: String) = "tag_notes/$tagId/${Uri.encode(tagName)}"
    }
    data object KnowledgeVault : Screen("knowledge_vault", "资料库")
    data object DateGroupNotes : Screen("date_group_notes", "按日期查看")
    data object Trash : Screen("trash", "回收站")
    data object DataExport : Screen("data_export", "数据导出")
    data object Schedule : Screen("schedule", "日程")
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    val navBackStackEntry = navController.currentBackStackEntryAsState().value
    val currentRoute = navBackStackEntry?.destination?.route

    CompositionLocalProvider(
        LocalDrawerState provides drawerState,
        LocalDrawerScope provides scope
    ) {
        ModalNavigationDrawer(
            drawerState = drawerState,
            scrimColor = MaterialTheme.colorScheme.scrim.copy(alpha = 0.32f),
            drawerContent = {
                ModalDrawerSheet(
                    drawerContainerColor = MaterialTheme.colorScheme.surfaceContainerLow,
                    drawerTonalElevation = 0.dp,
                    modifier = Modifier.width(260.dp)
                ) {
                    AppDrawerContent(
                        currentRoute = currentRoute,
                        onNavigateToRoute = { route ->
                            navController.navigate(route) {
                                popUpTo(Screen.FeatureHome.route) { saveState = true }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        onNavigateToTag = { tagId, tagName ->
                            navController.navigate(Screen.TagNotes.createRoute(tagId, tagName)) {
                                popUpTo(Screen.FeatureHome.route) { saveState = true }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        onCloseDrawer = {
                            scope.launch { drawerState.close() }
                        }
                    )
                }
            }
        ) {
            NavHost(
                navController = navController,
                startDestination = Screen.FeatureHome.route,
                modifier = Modifier,
                enterTransition = {
                    fadeIn(animationSpec = tween(MotionTokens.DurationMedium, easing = MotionTokens.StandardEasing)) +
                        slideInHorizontally(
                            initialOffsetX = { it / 6 },
                            animationSpec = tween(MotionTokens.DurationMedium, easing = MotionTokens.StandardEasing)
                        )
                },
                exitTransition = {
                    fadeOut(animationSpec = tween(MotionTokens.DurationShort, easing = MotionTokens.StandardEasing)) +
                        slideOutHorizontally(
                            targetOffsetX = { -it / 12 },
                            animationSpec = tween(MotionTokens.DurationShort, easing = MotionTokens.StandardEasing)
                        )
                },
                popEnterTransition = {
                    fadeIn(animationSpec = tween(MotionTokens.DurationMedium, easing = MotionTokens.StandardEasing)) +
                        slideInHorizontally(
                            initialOffsetX = { -it / 6 },
                            animationSpec = tween(MotionTokens.DurationMedium, easing = MotionTokens.StandardEasing)
                        )
                },
                popExitTransition = {
                    fadeOut(animationSpec = tween(MotionTokens.DurationShort, easing = MotionTokens.StandardEasing)) +
                        slideOutHorizontally(
                            targetOffsetX = { it / 6 },
                            animationSpec = tween(MotionTokens.DurationShort, easing = MotionTokens.StandardEasing)
                        )
                }
            ) {
                composable(Screen.FeatureHome.route) {
                    FeatureHomeScreen(
                        onNavigateToRoute = { route ->
                            navController.navigate(route) {
                                popUpTo(Screen.FeatureHome.route) { saveState = true }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    )
                }
                composable(Screen.Home.route) {
                    HomeScreen(
                        onNavigateToEdit = { noteId ->
                            navController.navigate(Screen.EditNote.createRoute(noteId))
                        },
                        onNavigateToTagNotes = { tagId, tagName ->
                            navController.navigate(Screen.TagNotes.createRoute(tagId, tagName))
                        }
                    )
                }
                composable(
                    route = Screen.EditNote.route,
                    arguments = listOf(navArgument("noteId") {
                        type = NavType.LongType
                    })
                ) { backStackEntry ->
                    val noteId = backStackEntry.arguments?.getLong("noteId") ?: return@composable
                    EditNoteScreen(
                        noteId = noteId,
                        onNavigateBack = { navController.popBackStack() }
                    )
                }
                composable(Screen.AIChat.route) {
                    AIChatScreen(
                        onCreateNote = { noteId ->
                            navController.navigate(Screen.EditNote.createRoute(noteId))
                        }
                    )
                }
                composable(Screen.Bill.route) {
                    BillScreen()
                }
                composable(Screen.BillStats.route) {
                    BillStatsScreen()
                }
                composable(Screen.AaSplit.route) {
                    AaSplitScreen()
                }
                composable(Screen.Habit.route) {
                    HabitScreen(viewModel = koinViewModel())
                }
                composable(Screen.DateGroupNotes.route) {
                    DateGroupNotesScreen(
                        onNavigateToEdit = { noteId ->
                            navController.navigate(Screen.EditNote.createRoute(noteId))
                        },
                        onNavigateToTagNotes = { tagId, tagName ->
                            navController.navigate(Screen.TagNotes.createRoute(tagId, tagName))
                        }
                    )
                }
                composable(Screen.Trash.route) {
                    TrashScreen()
                }
                composable(Screen.DataExport.route) {
                    ExportDataScreen()
                }
                composable(Screen.Todo.route) {
                    TodoScreen(viewModel = koinViewModel())
                }
                composable(Screen.Schedule.route) {
                    ScheduleScreen(viewModel = koinViewModel())
                }
                composable(Screen.KnowledgeVault.route) {
                    KnowledgeVaultScreen()
                }
                composable(Screen.Settings.route) {
                    SettingsScreen(
                        onNavigateToProviderManage = { navController.navigate(Screen.AiProviderManage.route) }
                    )
                }
                composable(Screen.AiProviderManage.route) {
                    val settingsViewModel: SettingsViewModel = koinViewModel()
                    val providers = settingsViewModel.providers.collectAsState().value
                    val activeProviderId = settingsViewModel.activeProviderId.collectAsState().value

                    AiProviderManageScreen(
                        onNavigateBack = { navController.popBackStack() },
                        providers = providers,
                        activeProviderId = activeProviderId,
                        onSelectProvider = { settingsViewModel.selectProvider(it) },
                        onUpdateProvider = { settingsViewModel.updateProvider(it) },
                        onDeleteProvider = { settingsViewModel.deleteCustomProvider(it) },
                        onAddProvider = { name, url, model, key -> settingsViewModel.addCustomProvider(name, url, model, key) },
                        sealKey = { settingsViewModel.sealZidaipass(it) },
                        openKey = { settingsViewModel.openZidaipass(it) }
                    )
                }
                composable(
                    route = Screen.TagNotes.route,
                    arguments = listOf(
                        navArgument("tagId") { type = NavType.LongType },
                        navArgument("tagName") { type = NavType.StringType }
                    )
                ) { backStackEntry ->
                    val tagId = backStackEntry.arguments?.getLong("tagId") ?: return@composable
                    val tagName = Uri.decode(backStackEntry.arguments?.getString("tagName") ?: "")
                    TagNotesScreen(
                        tagId = tagId,
                        tagName = tagName,
                        onNavigateToEdit = { noteId ->
                            noteId?.let { navController.navigate(Screen.EditNote.createRoute(it)) }
                        }
                    )
                }
            }
        }
    }
}
