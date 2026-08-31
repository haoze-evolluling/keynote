# Keynote × 谛听（DITING）UI 统一设计规范

> 本文档是「Keynote 界面向谛听视觉风格对齐」的唯一实施依据。所有改造智能体必须遵守。
> 铁律：**只改视觉与交互皮肤，不改任何业务逻辑、回调签名、数据流、文案**。长按、弹层、分享等行为全部保留。

## 0. 设计总纲

两个项目都是 Material 3 + Android 12 动态取色，因此「统一」的关键不在静态色值，而在**用法一致**：卡片层级、圆角体系、间距节奏、排版层级、组件形态。谛听的风格关键词：低饱和表面色、12dp 万能圆角、极浅描边代替阴影、几乎无海拔、onSurfaceVariant 次要文字、克制动效。

## 1. 形状体系（改 Theme.kt 的 AppShapes）

```
extraSmall = 8.dp   small = 12.dp   medium = 12.dp   large = 16.dp   extraLarge = 28.dp
```

使用规则：
- **12dp**：所有信息卡、按钮、输入框、芯片、小面板、气泡
- **28dp**：分组卡（iOS 式）、AlertDialog（M3 默认即是）、搜索框
- **4–6dp**：小徽章
- 所有自定义 `AlertDialog(... shape = RoundedCornerShape(16.dp))` 一律改为 28dp 或直接删掉 shape 参数用 M3 默认
- `ModalBottomSheet` 用 M3 默认（顶部 28dp），不要自定义

## 2. 卡片样式（三档标准，替换全部 OutlinedCard/BorderStroke 写法）

**① 标准信息卡**（笔记卡、习惯卡、待办卡、功能卡、日程卡等一切内容卡）：
```kotlin
Card(
    shape = RoundedCornerShape(12.dp),
    colors = CardDefaults.cardColors(
        containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
    ),
    // 不要 border、不要 elevation 覆盖
)
```
内边距 14–16dp，内部纵向 `spacedBy(6.dp)`～`spacedBy(12.dp)`。

**② 统计/仪表卡**（SummaryCard、HabitStatCard 等）：
```kotlin
Surface(
    shape = RoundedCornerShape(12.dp),
    color = MaterialTheme.colorScheme.surface,
    shadowElevation = 0.dp, tonalElevation = 0.dp,
    modifier = Modifier.border(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.45f), RoundedCornerShape(12.dp))
)
```
内边距 16dp；数值 **28sp ExtraBold**；标签 `labelMedium` + onSurfaceVariant。

**③ 列表行 / 排行项**：`surfaceVariant.copy(alpha = 0.3f)`、12dp 圆角、内边距 12dp。

分隔线：组内用 `HorizontalDivider(thickness = 1.dp, color = outline.copy(alpha = 0.25f))`；卡内可用 `outlineVariant.copy(alpha = 0.3f)`。

## 3. 颜色体系

- 动态取色（Android 12+）**保持不变**；非动态时的 Fallback 双色板改为谛听同款 M3 baseline：
  - 浅色 primary `0xFF6650A4`、primaryContainer `0xFFEADDFF`、onPrimaryContainer `0xFF21005D`、secondary `0xFF625B71`、tertiary `0xFF7D5260`，其余槽位用 M3 基线默认
  - 深色 primary `0xFFD0BCFF`、primaryContainer `0xFF4F378B`、onPrimaryContainer `0xFFEADDFF`、secondary `0xFFCCC2DC`、tertiary `0xFFEFB8C8`
- 次要/说明文字一律 `onSurfaceVariant`（不用 outline）；禁用态 alpha 0.38
- 危险/删除：`error`（警示条背景 `error.copy(alpha = 0.12f)` + 边框 `error.copy(alpha = 0.35f)`）；成功/正常强调：`primary`
- `AppColors.chartColors` 改为 10 色序列：`[primary, tertiary, secondary, error, primaryContainer, tertiaryContainer, secondaryContainer, outline, outlineVariant, surfaceVariant]`；DonutChart 改用它（删除自建色表）
- Habit 6 色板（0xFF4CAF50 等）是**存入 DB 的数据**，保留不动
- AIChatScreen 的 `GeminiBlue/GeminiCanvas` 硬编码删除：聊天背景改为主题化——浅色 = `colorScheme.background` 上叠加以 `primary.copy(alpha ≤ 0.12f)` 为源的柔和径向光晕（保留呼吸动画），深色 = 纯 background 不画光晕
- 启动窗口背景 `res/values/colors.xml` 的 `app_startup_background`：浅色 `#FFFBFE`、夜间 `#1C1B1F`（对齐谛听）
- scrim、阴影保持现状即可

## 4. 间距节奏

- 屏幕水平内边距：**16dp**（列表/设置/统计页）；**24dp**（首页/宫格类）
- 区块间距：**12dp**（LazyColumn spacedBy、卡片与卡片之间）
- 卡片内边距：14–16dp；行内图标与文字间距 12dp
- 列表项最小行高 44dp
- **FAB 底部 padding 统一 16dp**（废除 64dp 旧值）
- 设置项行：`horizontal = 24.dp, vertical = 16.dp`（纯导航行 vertical 12dp）

## 5. 排版层级

- 主屏 TopAppBar：M3 默认 titleLarge；二级页标题 `titleMedium + SemiBold`
- 大数字：**28sp ExtraBold**（统计卡、汇总）
- 正文 bodyLarge(16sp)/bodyMedium(14sp)；次要说明 bodySmall + onSurfaceVariant；徽章/标签 labelSmall
- ModalTokens 现有四级 TextStyle 保持不变

## 6. 共享组件（基础层一次性改造）

- `SettingsCornerShape`：10dp → **12dp**
- `SettingsGroup`：`shape = RoundedCornerShape(28.dp)`，`containerColor = surfaceVariant.copy(alpha = 0.72f)`，elevation 0；组内分隔线 `outline.copy(alpha = 0.25f)`
- `SettingsItem` 系列行：minHeight 44dp，`padding(horizontal = 24.dp, vertical = 16.dp)`；leading 图标 tint `primary`；值文字 bodyMedium onSurfaceVariant + `KeyboardArrowRight`（与值间 4dp）
- **新增 `DrawerScaffold`**（放 SettingsComponents.kt），供 10 个「抽屉+菜单」屏替换自绘 Scaffold：
```kotlin
@Composable
fun DrawerScaffold(
    title: String,
    onMenuClick: () -> Unit,
    modifier: Modifier = Modifier,
    actions: @Composable RowScope.() -> Unit = {},
    floatingActionButton: @Composable () -> Unit = {},
    containerColor: Color = MaterialTheme.colorScheme.background,
    content: @Composable (PaddingValues) -> Unit
)
```
  内部：Scaffold + TopAppBar（navigationIcon = IconButton(ic_menu)，containerColor = containerColor、scrolledContainerColor 同色，title = Text(title, style = titleMedium, fontWeight = SemiBold)），FAB 置 BottomEnd 且 padding 16dp。
- `ActionMenuDialog`：shape 28dp
- `ActionRow`：默认值改为 `rowHeight = 48.dp`、`horizontalPadding = 16.dp`（调用方不再覆盖）
- `NoteDialogs` 4 个对话框：16dp → 28dp

## 7. 动效与交互

- 状态颜色/尺寸过渡：`tween(250)`；出入场：`fadeIn(tween(180)) + scaleIn(initialScale = 0.95f)`（反向对应）
- 内容高度变化：`animateContentSize(tween(180))`
- MotionTokens（150/250/350 + FastOutSlowIn）保持；弹簧 `spring(1f, 1000f)` 仅用于跟手位移
- 按压反馈：M3 默认 ripple；可点击卡片保持 `combinedClickable`（长按功能不能丢）

## 8. 分模块要点（与并行智能体分工对应，文件互不重叠）

| 模块 | 文件 | 要点 |
|---|---|---|
| home+tag | HomeScreen、DateGroupNotesScreen、TagNotesScreen、NoteCard、SearchBar、NoteActionBottomSheet | 改用 DrawerScaffold；NoteCard 改标准信息卡①；空状态文案色 onSurfaceVariant + bodyMedium、居中；SearchBar shape 28dp |
| navigation | AppDrawer、AppNavigation | 抽屉行：12dp 圆角行、minHeight 44dp、选中 = secondaryContainer 底 + primary 文字；分组标题 titleSmall onSurfaceVariant；sheet 保持 surfaceContainerLow/宽 260/scrim 0.32 |
| bill | BillScreen、BillStatsScreen、AaSplitScreen、CategoryChipRow、BillActionBottomSheet、BarChart、LineChart、DonutChart | 三种卡片各归其位；SummaryCard 改样式②；DonutChart 用 AppColors.chartColors；AlertDialog 28dp；图表颜色保持语义（primary 主色、outlineVariant 网格、outline 文字） |
| todo+schedule | TodoScreen、ScheduleScreen | TodoCard/ScheduleCard 改卡①；优先级色点保留（AppColors.priority*）；`Color(category.color)` 数据色保留；AlertDialog 28dp；FAB 16dp |
| habit+toolbox+trash | HabitScreen、KnowledgeVaultScreen、TrashScreen | HabitCard/HabitStatCard/FeatureCard/VaultListCard 改标准卡①②；HabitEmptyState 作为全项目空状态范本（图标 primary + 双行文案 onSurfaceVariant）；RemainingDaysBadge 保持 errorContainer |
| edit+chat | EditNoteScreen、MarkdownPreview、TagChipRow、AIChatScreen | 气泡：用户 primaryContainer / AI surfaceVariant α0.5、12dp；输入条 Surface 12dp + 细描边；ChatBackground 主题化（见 §3）；EditNote SummaryCard 保持 primaryContainer 语义；菜单图标 ic_menu 统一（弃 ic_menu_outlined） |
| settings系 | SettingsScreen、AiProviderManageScreen、FeatureHomeScreen、ExportDataScreen | 全部吃 §6 的新 SettingsGroup/Item；ProviderRow 对话框 28dp；ExportDataScreen 的 ModalBottomSheet 保持默认形状、按钮 12dp |

## 9. 验收口径

- 全项目不再出现 `RoundedCornerShape(16.dp)`（对话框 28dp、卡片 12dp）；`SettingsCornerShape` = 12dp
- 全项目不再出现 `OutlinedCard` + `BorderStroke` 的卡片写法（改为 §2 三档之一）
- 调用方不再覆盖 ActionRow 的 48.dp/16.dp
- GeminiBlue/GeminiCanvas 硬编码色删除
- 编译通过（:app:compileDebugKotlin），所有长按/弹层/导航行为不变
