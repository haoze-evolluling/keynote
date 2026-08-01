package com.haoze.keynote.ui.navigation

import androidx.compose.material3.DrawerState
import androidx.compose.runtime.staticCompositionLocalOf
import kotlinx.coroutines.CoroutineScope

/**
 * CompositionLocal 用于向子组件提供 DrawerState，避免逐层参数传递。
 * 在 AppNavigation 中通过 CompositionLocalProvider 注入。
 */
val LocalDrawerState = staticCompositionLocalOf<DrawerState> {
    error("No DrawerState provided — must be inside ModalNavigationDrawer")
}

/**
 * CompositionLocal 用于向子组件提供用于启动 drawer 操作的 CoroutineScope。
 */
val LocalDrawerScope = staticCompositionLocalOf<CoroutineScope> {
    error("No DrawerScope provided — must be inside ModalNavigationDrawer")
}
