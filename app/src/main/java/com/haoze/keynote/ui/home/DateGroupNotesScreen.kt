package com.haoze.keynote.ui.home

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import org.koin.compose.viewmodel.koinViewModel
import com.haoze.keynote.ui.navigation.LocalDrawerScope
import com.haoze.keynote.ui.navigation.LocalDrawerState
import com.haoze.keynote.ui.theme.LocalAppColors
import android.content.Intent
import android.content.ClipData
import android.content.ClipboardManager
import com.haoze.keynote.ui.common.NoteDeleteConfirmDialog
import com.haoze.keynote.ui.common.NoteDetailsDialog
import com.haoze.keynote.ui.common.NoteAddTagDialog
import com.haoze.keynote.ui.common.NoteManageTagsDialog
import com.haoze.keynote.viewmodel.HomeViewModel
import kotlinx.coroutines.launch
import com.haoze.keynote.ui.components.DrawerScaffold

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DateGroupNotesScreen(
    onNavigateToEdit: (Long) -> Unit,
    onNavigateToTagNotes: (Long, String) -> Unit,
    viewModel: HomeViewModel = koinViewModel()
) {
    val drawerState = LocalDrawerState.current
    val scope = LocalDrawerScope.current
    val colors = LocalAppColors.current
    val notes by viewModel.notes.collectAsState()
    var showActionDialogForNote by remember { mutableStateOf<Long?>(null) }
    var showDeleteConfirm by remember { mutableStateOf<Long?>(null) }
    var isAiTagLoadingForNote by remember { mutableStateOf<Long?>(null) }
    var isSummarizingForNote by remember { mutableStateOf<Long?>(null) }
    var isGeneratingTitleForNote by remember { mutableStateOf<Long?>(null) }
    var showAddTagForNote by remember { mutableStateOf<Long?>(null) }
    var showManageTagsForNote by remember { mutableStateOf<Long?>(null) }
    var showNoteDetailsForNote by remember { mutableStateOf<Long?>(null) }

    val context = LocalContext.current

    val groupedNotes = remember(notes) {
        notes.groupBy { noteWithTags ->
            val cal = java.util.Calendar.getInstance().apply {
                timeInMillis = noteWithTags.note.createdAt
            }
            "${cal.get(java.util.Calendar.YEAR)}年${cal.get(java.util.Calendar.MONTH) + 1}月"
        }.toSortedMap(Comparator.reverseOrder())
    }

    DrawerScaffold(
        title = "按日期查看",
        onMenuClick = { scope.launch { drawerState.open() } }
    ) { innerPadding ->
        if (notes.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize().padding(innerPadding),
                contentAlignment = Alignment.Center
            ) {
                Text("暂无笔记", style = MaterialTheme.typography.bodyMedium, color = colors.onSurfaceVariant)
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize().padding(innerPadding),
                verticalArrangement = Arrangement.spacedBy(4.dp),
                contentPadding = PaddingValues(vertical = 8.dp)
            ) {
                groupedNotes.forEach { (month, monthNotes) ->
                    item {
                        Text(
                            text = month,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                        )
                    }
                    items(monthNotes, key = { it.note.id }) { noteWithTags ->
                        NoteCard(
                            noteWithTags = noteWithTags,
                            onClick = { onNavigateToEdit(noteWithTags.note.id) },
                            onTagClick = { tagId, tagName -> onNavigateToTagNotes(tagId, tagName) },
                            onLongClick = { showActionDialogForNote = noteWithTags.note.id },
                            modifier = Modifier.animateItem()
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                    }
                }
            }
        }
    }

    if (showActionDialogForNote != null) {
        val currentNote = notes.find { it.note.id == showActionDialogForNote }
        if (currentNote != null) {
            NoteActionBottomSheet(
                noteTitle = currentNote.note.title ?: "",
                noteContent = currentNote.note.content ?: "",
                isAiTagLoading = isAiTagLoadingForNote == showActionDialogForNote,
                isSummarizing = isSummarizingForNote == showActionDialogForNote,
                isGeneratingTitle = isGeneratingTitleForNote == showActionDialogForNote,
                onEdit = {
                    showActionDialogForNote?.let { onNavigateToEdit(it) }
                    showActionDialogForNote = null
                },
                onShare = {
                    showActionDialogForNote?.let { noteId ->
                        val note = notes.find { it.note.id == noteId }
                        if (note != null) {
                            val text = buildString {
                                if (note.note.title.isNotBlank()) append(note.note.title).append("\n\n")
                                append(note.note.content)
                            }
                            val intent = Intent(Intent.ACTION_SEND).apply {
                                type = "text/plain"
                                putExtra(Intent.EXTRA_TEXT, text)
                            }
                            context.startActivity(Intent.createChooser(intent, "分享笔记"))
                        }
                    }
                    showActionDialogForNote = null
                },
                onAiSummary = {
                    showActionDialogForNote?.let { noteId ->
                        isSummarizingForNote = noteId
                        viewModel.summarizeNote(noteId) { success ->
                            isSummarizingForNote = null
                            if (success) showActionDialogForNote = null
                        }
                    }
                },
                onCopyContent = {
                    showActionDialogForNote?.let { noteId ->
                        val note = notes.find { it.note.id == noteId }
                        if (note != null) {
                            val text = buildString {
                                if (note.note.title.isNotBlank()) append(note.note.title).append("\n\n")
                                append(note.note.content)
                            }
                            val clipboard = context.getSystemService(ClipboardManager::class.java)
                            clipboard?.setPrimaryClip(ClipData.newPlainText("KeyNote", text))
                        }
                    }
                    showActionDialogForNote = null
                },
                onViewDetails = {
                    showNoteDetailsForNote = showActionDialogForNote
                    showActionDialogForNote = null
                },
                onAiGenerateTitle = {
                    showActionDialogForNote?.let { noteId ->
                        isGeneratingTitleForNote = noteId
                        viewModel.aiGenerateTitle(noteId) { success ->
                            isGeneratingTitleForNote = null
                            if (success) showActionDialogForNote = null
                        }
                    }
                },
                onAddTag = {
                    showAddTagForNote = showActionDialogForNote
                    showActionDialogForNote = null
                },
                onManageTags = {
                    showManageTagsForNote = showActionDialogForNote
                    showActionDialogForNote = null
                },
                onAiTag = {
                    showActionDialogForNote?.let { noteId ->
                        isAiTagLoadingForNote = noteId
                        viewModel.aiGenerateTags(noteId) { success ->
                            isAiTagLoadingForNote = null
                            if (success) showActionDialogForNote = null
                        }
                    }
                },
                onDelete = {
                    showDeleteConfirm = showActionDialogForNote
                    showActionDialogForNote = null
                },
                onDismiss = {
                    showActionDialogForNote = null
                    isAiTagLoadingForNote = null
                    isSummarizingForNote = null
                    isGeneratingTitleForNote = null
                }
            )
        }
    }

    NoteDeleteConfirmDialog(
        show = showDeleteConfirm != null,
        noteId = showDeleteConfirm,
        onConfirm = { noteId -> viewModel.deleteNote(noteId) },
        onDismiss = { showDeleteConfirm = null }
    )

    NoteDetailsDialog(
        note = notes.find { it.note.id == showNoteDetailsForNote },
        onDismiss = { showNoteDetailsForNote = null }
    )

    NoteAddTagDialog(
        show = showAddTagForNote != null,
        noteId = showAddTagForNote,
        onAddTag = { noteId, tagName -> viewModel.addTagToNote(noteId, tagName) },
        onDismiss = { showAddTagForNote = null }
    )

    NoteManageTagsDialog(
        note = if (showManageTagsForNote != null) notes.find { it.note.id == showManageTagsForNote } else null,
        onRemoveTag = { noteId, tagId -> viewModel.removeTagFromNote(noteId, tagId) },
        onDismiss = { showManageTagsForNote = null }
    )
}