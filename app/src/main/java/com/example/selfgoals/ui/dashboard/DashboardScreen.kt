package com.example.selfgoals.ui.dashboard

import android.content.Intent
import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.compose.ui.res.stringResource
import com.example.selfgoals.R
import com.example.selfgoals.data.entity.Category
import com.example.selfgoals.data.entity.Goal
import com.example.selfgoals.data.entity.GoalDetails
import com.example.selfgoals.data.entity.Milestone
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    viewModel: DashboardViewModel = hiltViewModel()
) {
    val goals by viewModel.goals.collectAsState()
    val categories by viewModel.categories.collectAsState()
    val stats by viewModel.stats.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()
    val showArchived by viewModel.showArchived.collectAsState()
    val selectedCategoryId by viewModel.selectedCategoryId.collectAsState()
    val sortOption by viewModel.sortOption.collectAsState()
    val themeMode by viewModel.themeMode.collectAsState()
    
    var showGoalDialog by remember { mutableStateOf(false) }
    var editingGoal by remember { mutableStateOf<Goal?>(null) }
    var showAddCategoryDialog by remember { mutableStateOf(false) }
    var showSettingsMenu by remember { mutableStateOf(false) }

    val systemDark = isSystemInDarkTheme()
    val isDark = when (themeMode) {
        ThemeMode.SYSTEM -> systemDark
        ThemeMode.LIGHT -> false
        ThemeMode.DARK -> true
    }
    
    val bgColor = MaterialTheme.colorScheme.background
    val context = LocalContext.current
    val haptic = LocalHapticFeedback.current

    Scaffold(
        containerColor = bgColor,
        floatingActionButton = {
            Row(
                modifier = Modifier
                    .padding(16.dp)
                    .clip(RoundedCornerShape(32.dp))
                    .background(MaterialTheme.colorScheme.primary)
                    .clickable { 
                        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                        editingGoal = null
                        showGoalDialog = true 
                    }
                    .padding(horizontal = 24.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(Icons.Default.Add, contentDescription = stringResource(R.string.add), tint = Color.White)
                Spacer(modifier = Modifier.width(8.dp))
                Text(stringResource(R.string.new_goal), color = Color.White, fontWeight = FontWeight.Bold)
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
        ) {
            // iOS Style Large Header
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Bottom
            ) {
                Text(
                    text = stringResource(R.string.app_name),
                    style = MaterialTheme.typography.headlineLarge.copy(
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 34.sp,
                        letterSpacing = (-1).sp
                    )
                )
                Row {
                    IconButton(onClick = { 
                        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                        viewModel.toggleShowArchived() 
                    }) {
                        Icon(
                            if (showArchived) Icons.Default.Unarchive else Icons.Default.Archive,
                            contentDescription = stringResource(if (showArchived) R.string.unarchive else R.string.archive),
                            tint = if (showArchived) MaterialTheme.colorScheme.primary else Color.Gray
                        )
                    }
                    
                    Box {
                        IconButton(onClick = { showSettingsMenu = true }) {
                            Icon(Icons.Default.MoreHoriz, contentDescription = stringResource(R.string.more))
                        }
                        DropdownMenu(
                            expanded = showSettingsMenu,
                            onDismissRequest = { showSettingsMenu = false },
                            modifier = Modifier.background(MaterialTheme.colorScheme.surface)
                        ) {
                            Text(stringResource(R.string.theme), modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp), style = MaterialTheme.typography.labelSmall, color = Color.Gray)
                            ThemeMode.entries.forEach { mode ->
                                DropdownMenuItem(
                                    text = { Text(mode.name.lowercase().replaceFirstChar { it.uppercase() }) },
                                    onClick = { 
                                        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                        viewModel.setThemeMode(mode)
                                        showSettingsMenu = false
                                    },
                                    trailingIcon = { if (themeMode == mode) Icon(Icons.Default.Check, null) }
                                )
                            }
                            Divider()
                            Text(stringResource(R.string.export), modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp), style = MaterialTheme.typography.labelSmall, color = Color.Gray)
                            DropdownMenuItem(
                                text = { Text(stringResource(R.string.share_summary)) },
                                onClick = {
                                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                    val shareText = viewModel.generateExportText()
                                    val sendIntent = Intent().apply {
                                        action = Intent.ACTION_SEND
                                        putExtra(Intent.EXTRA_TEXT, shareText)
                                        type = "text/plain"
                                    }
                                    context.startActivity(Intent.createChooser(sendIntent, null))
                                    showSettingsMenu = false
                                },
                                leadingIcon = { Icon(Icons.Default.Share, null) }
                            )
                            DropdownMenuItem(
                                text = { Text(stringResource(R.string.manage_tags)) },
                                onClick = { 
                                    showAddCategoryDialog = true
                                    showSettingsMenu = false
                                },
                                leadingIcon = { Icon(Icons.Default.Settings, null) }
                            )
                        }
                    }
                }
            }

            // iOS Style Search Bar & Sort Toggle
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 8.dp)
            ) {
                Surface(
                    modifier = Modifier.weight(1f),
                    color = MaterialTheme.colorScheme.secondary.copy(alpha = if (isDark) 0.15f else 0.05f),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(horizontal = 12.dp)
                    ) {
                        Icon(Icons.Default.Search, contentDescription = null, tint = Color.Gray, modifier = Modifier.size(20.dp))
                        TextField(
                            value = searchQuery,
                            onValueChange = { viewModel.updateSearchQuery(it) },
                            placeholder = { Text(stringResource(R.string.search_hint), color = Color.Gray) },
                            modifier = Modifier.weight(1f),
                            colors = TextFieldDefaults.colors(
                                focusedContainerColor = Color.Transparent,
                                unfocusedContainerColor = Color.Transparent,
                                disabledContainerColor = Color.Transparent,
                                focusedIndicatorColor = Color.Transparent,
                                unfocusedIndicatorColor = Color.Transparent
                            ),
                            singleLine = true,
                            textStyle = MaterialTheme.typography.bodyLarge
                        )
                        if (searchQuery.isNotEmpty()) {
                            IconButton(onClick = { 
                                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                viewModel.updateSearchQuery("") 
                            }) {
                                Icon(Icons.Default.Close, contentDescription = stringResource(R.string.clear), tint = Color.Gray, modifier = Modifier.size(20.dp))
                            }
                        }
                    }
                }
                
                Spacer(modifier = Modifier.width(12.dp))
                
                var showSortMenu by remember { mutableStateOf(false) }
                Box {
                    Surface(
                        color = MaterialTheme.colorScheme.secondary.copy(alpha = if (isDark) 0.15f else 0.05f),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.clickable { showSortMenu = true }
                    ) {
                        Icon(Icons.Rounded.SwapVert, null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.padding(10.dp))
                    }
                    DropdownMenu(expanded = showSortMenu, onDismissRequest = { showSortMenu = false }) {
                        SortOption.entries.forEach { option ->
                            DropdownMenuItem(
                                text = { Text(option.name.replace("_", " ").lowercase().replaceFirstChar { it.uppercase() }) },
                                onClick = {
                                    viewModel.setSortOption(option)
                                    showSortMenu = false
                                },
                                trailingIcon = { if (sortOption == option) Icon(Icons.Default.Check, null) }
                            )
                        }
                    }
                }
            }

            AnalyticsOverview(stats = stats, isDark = isDark)
            
            if (categories.isNotEmpty()) {
                CategoryList(
                    categories = categories,
                    selectedCategoryId = selectedCategoryId,
                    isDark = isDark,
                    onCategoryClick = { 
                        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                        viewModel.selectCategory(it) 
                    }
                )
            }
            
            if (goals.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(stringResource(R.string.empty_goals_hint), style = MaterialTheme.typography.bodyLarge, color = Color.Gray)
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(bottom = 100.dp)
                ) {
                    items(goals, key = { it.goal.id }) { goalDetails ->
                        GoalItem(
                            goalDetails = goalDetails,
                            isDark = isDark,
                            onToggleGoal = { 
                                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                viewModel.toggleGoalCompletion(goalDetails.goal) 
                            },
                            onEditGoal = { 
                                editingGoal = goalDetails.goal
                                showGoalDialog = true 
                            },
                            onArchiveGoal = { 
                                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                viewModel.toggleArchive(goalDetails.goal) 
                            },
                            onPriorityToggle = {
                                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                viewModel.togglePriority(goalDetails.goal)
                            },
                            onDeleteGoal = { viewModel.deleteGoal(goalDetails.goal) },
                            onAddMilestone = { title -> 
                                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                viewModel.addMilestone(goalDetails.goal.id, title) 
                            },
                            onToggleMilestone = { 
                                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                viewModel.toggleMilestoneCompletion(it) 
                            },
                            onMoveMilestone = { from, to -> viewModel.reorderMilestone(goalDetails.goal.id, from, to) }
                        )
                    }
                }
            }
        }
    }

    if (showGoalDialog) {
        GoalDialog(
            categories = categories,
            goalToEdit = editingGoal,
            onDismiss = { 
                showGoalDialog = false
                editingGoal = null
            },
            onConfirm = { title, desc, catId, reminder, deadline, priority ->
                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                if (editingGoal == null) {
                    viewModel.addGoal(title, desc, catId, reminder, deadline, priority)
                } else {
                    val updatedGoal = editingGoal!!.copy(
                        title = title,
                        description = desc,
                        categoryId = catId,
                        reminderTime = reminder,
                        deadline = deadline,
                        isPriority = priority
                    )
                    viewModel.editGoal(updatedGoal, reminder != editingGoal!!.reminderTime)
                }
                showGoalDialog = false
                editingGoal = null
            }
        )
    }

    if (showAddCategoryDialog) {
        AddCategoryDialog(
            onDismiss = { showAddCategoryDialog = false },
            onConfirm = { name, color ->
                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                viewModel.addCategory(name, color)
                showAddCategoryDialog = false
            }
        )
    }
}

@Composable
fun CategoryList(
    categories: List<Category>,
    selectedCategoryId: Long?,
    isDark: Boolean,
    onCategoryClick: (Long?) -> Unit
) {
    LazyRow(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        contentPadding = PaddingValues(horizontal = 24.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        items(categories) { category ->
            val isSelected = selectedCategoryId == category.id
            val color = Color(category.color)
            Surface(
                color = if (isSelected) color else if (isDark) color.copy(alpha = 0.2f) else color.copy(alpha = 0.1f),
                shape = RoundedCornerShape(20.dp),
                modifier = Modifier.clickable { onCategoryClick(category.id) }
            ) {
                Text(
                    text = category.name,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp),
                    style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold),
                    color = if (isSelected) Color.White else color
                )
            }
        }
    }
}

@Composable
fun GoalItem(
    goalDetails: GoalDetails,
    isDark: Boolean,
    onToggleGoal: () -> Unit,
    onEditGoal: () -> Unit,
    onArchiveGoal: () -> Unit,
    onPriorityToggle: () -> Unit,
    onDeleteGoal: () -> Unit,
    onAddMilestone: (String) -> Unit,
    onToggleMilestone: (Milestone) -> Unit,
    onMoveMilestone: (Int, Int) -> Unit
) {
    val goal = goalDetails.goal
    val category = goalDetails.category
    val milestones = goalDetails.milestones
    var expanded by remember { mutableStateOf(false) }
    var newMilestoneTitle by remember { mutableStateOf("") }

    val completedMilestones = milestones.count { it.isCompleted }
    val progress = if (milestones.isNotEmpty()) completedMilestones.toFloat() / milestones.size else 0f

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 8.dp),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .clip(CircleShape)
                        .background(
                            if (goal.isCompleted) MaterialTheme.colorScheme.primary 
                            else MaterialTheme.colorScheme.secondary.copy(alpha = 0.2f)
                        )
                        .clickable { onToggleGoal() },
                    contentAlignment = Alignment.Center
                ) {
                    if (goal.isCompleted) {
                        Icon(Icons.Default.Check, contentDescription = null, tint = Color.White, modifier = Modifier.size(20.dp))
                    }
                }
                
                Spacer(modifier = Modifier.width(16.dp))
                
                Column(modifier = Modifier.weight(1f)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        if (goal.isPriority) {
                            Icon(Icons.Default.Star, null, tint = Color(0xFFFFCC00), modifier = Modifier.size(16.dp).padding(end = 4.dp))
                        }
                        if (category != null) {
                            val catColor = Color(category.color)
                            Text(
                                text = category.name.uppercase(),
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    letterSpacing = 1.sp
                                ),
                                color = if (isDark) catColor.copy(alpha = 0.8f) else catColor
                            )
                        }
                    }
                    Text(
                        text = goal.title,
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        textDecoration = if (goal.isCompleted) TextDecoration.LineThrough else null
                    )
                    if (goal.deadline != null) {
                        val isOverdue = goal.deadline < System.currentTimeMillis() && !goal.isCompleted
                        Text(
                            text = stringResource(R.string.due_date, SimpleDateFormat("MMM dd, yyyy", Locale.getDefault()).format(Date(goal.deadline))),
                            style = MaterialTheme.typography.labelSmall,
                            color = if (isOverdue) Color(0xFFFF3B30) else Color.Gray
                        )
                    }
                }

                IconButton(onClick = onEditGoal) {
                    Icon(Icons.Default.Edit, contentDescription = stringResource(R.string.edit), tint = Color.Gray, modifier = Modifier.size(20.dp))
                }
                
                IconButton(onClick = { expanded = !expanded }) {
                    Icon(
                        if (expanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                        contentDescription = stringResource(R.string.expand),
                        tint = Color.Gray
                    )
                }
            }

            if (milestones.isNotEmpty()) {
                Spacer(modifier = Modifier.height(12.dp))
                LinearProgressIndicator(
                    progress = progress,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(6.dp)
                        .clip(RoundedCornerShape(3.dp)),
                    color = category?.let { Color(it.color) } ?: MaterialTheme.colorScheme.primary,
                    trackColor = MaterialTheme.colorScheme.secondary.copy(alpha = 0.2f),
                    strokeCap = StrokeCap.Round
                )
            }

            AnimatedVisibility(visible = expanded) {
                Column {
                    Spacer(modifier = Modifier.height(16.dp))
                    if (goal.description.isNotEmpty()) {
                        Text(text = goal.description, style = MaterialTheme.typography.bodyMedium, color = Color.Gray)
                        Spacer(modifier = Modifier.height(12.dp))
                    }
                    
                    milestones.forEachIndexed { index, milestone ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                        ) {
                            Icon(
                                if (milestone.isCompleted) Icons.Default.CheckCircle else Icons.Default.RadioButtonUnchecked,
                                contentDescription = null,
                                tint = if (milestone.isCompleted) MaterialTheme.colorScheme.primary else Color.Gray,
                                modifier = Modifier
                                    .size(20.dp)
                                    .clickable { onToggleMilestone(milestone) }
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Text(
                                text = milestone.title,
                                style = MaterialTheme.typography.bodyMedium,
                                textDecoration = if (milestone.isCompleted) TextDecoration.LineThrough else null,
                                color = if (milestone.isCompleted) Color.Gray else MaterialTheme.colorScheme.onSurface,
                                modifier = Modifier.weight(1f)
                            )
                            
                            // Simple reorder controls
                            if (index > 0) {
                                IconButton(onClick = { onMoveMilestone(index, index - 1) }, modifier = Modifier.size(24.dp)) {
                                    Icon(Icons.Default.KeyboardArrowUp, null, tint = Color.Gray)
                                }
                            }
                            if (index < milestones.size - 1) {
                                IconButton(onClick = { onMoveMilestone(index, index + 1) }, modifier = Modifier.size(24.dp)) {
                                    Icon(Icons.Default.KeyboardArrowDown, null, tint = Color.Gray)
                                }
                            }
                        }
                    }
                    
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp)
                    ) {
                        TextField(
                            value = newMilestoneTitle,
                            onValueChange = { newMilestoneTitle = it },
                            placeholder = { Text(stringResource(R.string.add_milestone_hint), fontSize = 14.sp) },
                            modifier = Modifier.weight(1f),
                            colors = TextFieldDefaults.colors(
                                focusedContainerColor = Color.Transparent,
                                unfocusedContainerColor = Color.Transparent,
                                disabledContainerColor = Color.Transparent,
                                focusedIndicatorColor = Color.Transparent,
                                unfocusedIndicatorColor = Color.Transparent
                            ),
                            textStyle = MaterialTheme.typography.bodyMedium
                        )
                        IconButton(
                            onClick = {
                                if (newMilestoneTitle.isNotBlank()) {
                                    onAddMilestone(newMilestoneTitle)
                                    newMilestoneTitle = ""
                                }
                            },
                            enabled = newMilestoneTitle.isNotBlank()
                        ) {
                            Icon(Icons.Default.AddCircle, contentDescription = stringResource(R.string.add), tint = MaterialTheme.colorScheme.primary)
                        }
                    }
                    
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        TextButton(
                            onClick = onPriorityToggle,
                            colors = ButtonDefaults.textButtonColors(contentColor = if (goal.isPriority) Color(0xFFFFCC00) else Color.Gray)
                        ) {
                            Text(stringResource(if (goal.isPriority) R.string.remove_priority else R.string.set_priority), style = MaterialTheme.typography.labelSmall)
                        }
                        
                        TextButton(
                            onClick = onArchiveGoal,
                            colors = ButtonDefaults.textButtonColors(contentColor = MaterialTheme.colorScheme.primary),
                        ) {
                            Text(stringResource(if (goal.isArchived) R.string.unarchive else R.string.archive), style = MaterialTheme.typography.labelSmall)
                        }
                        
                        TextButton(
                            onClick = onDeleteGoal,
                            colors = ButtonDefaults.textButtonColors(contentColor = Color(0xFFFF3B30)),
                        ) {
                            Text(stringResource(R.string.delete_goal), style = MaterialTheme.typography.labelSmall)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun AnalyticsOverview(stats: ProgressStats, isDark: Boolean) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 8.dp),
        shape = RoundedCornerShape(28.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(modifier = Modifier.padding(24.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(R.string.activity),
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
                )
                Text(
                    text = stringResource(R.string.done_percent, (stats.goalCompletionRate * 100).toInt()),
                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.primary
                )
            }
            
            Spacer(modifier = Modifier.height(20.dp))
            
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                AnalyticsModule(
                    label = stringResource(R.string.goals),
                    current = stats.completedGoals,
                    total = stats.totalGoals,
                    color = Color(0xFF007AFF),
                    isDark = isDark,
                    modifier = Modifier.weight(1f)
                )
                AnalyticsModule(
                    label = stringResource(R.string.tasks),
                    current = stats.completedMilestones,
                    total = stats.totalMilestones,
                    color = Color(0xFF34C759),
                    isDark = isDark,
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Composable
fun AnalyticsModule(label: String, current: Int, total: Int, color: Color, isDark: Boolean, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(20.dp))
            .background(MaterialTheme.colorScheme.secondary.copy(alpha = if (isDark) 0.15f else 0.05f))
            .padding(16.dp)
    ) {
        Text(text = label, style = MaterialTheme.typography.labelMedium, color = Color.Gray)
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = "$current/$total",
            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold, fontSize = 22.sp)
        )
        Spacer(modifier = Modifier.height(8.dp))
        LinearProgressIndicator(
            progress = if (total > 0) current.toFloat() / total else 0f,
            modifier = Modifier.fillMaxWidth().height(4.dp).clip(CircleShape),
            color = color,
            trackColor = color.copy(alpha = 0.1f)
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GoalDialog(
    categories: List<Category>,
    goalToEdit: Goal? = null,
    onDismiss: () -> Unit,
    onConfirm: (String, String, Long?, Long?, Long?, Boolean) -> Unit
) {
    var title by remember { mutableStateOf(goalToEdit?.title ?: "") }
    var desc by remember { mutableStateOf(goalToEdit?.description ?: "") }
    var selectedCategoryId by remember { mutableStateOf(goalToEdit?.categoryId) }
    var setReminder by remember { mutableStateOf(goalToEdit?.reminderTime != null) }
    var reminderMinutes by remember { mutableStateOf("60") }
    var isPriority by remember { mutableStateOf(goalToEdit?.isPriority ?: false) }
    
    var showDatePicker by remember { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = goalToEdit?.deadline
    )
    val selectedDate = datePickerState.selectedDateMillis
    val dateFormatter = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())

    AlertDialog(
        onDismissRequest = onDismiss,
        properties = androidx.compose.ui.window.DialogProperties(usePlatformDefaultWidth = false),
        modifier = Modifier.padding(24.dp),
        content = {
            Surface(
                shape = RoundedCornerShape(32.dp),
                color = MaterialTheme.colorScheme.surface,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(24.dp)) {
                    Text(
                        text = stringResource(if (goalToEdit == null) R.string.new_goal else R.string.edit_goal), 
                        style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.ExtraBold)
                    )
                    Spacer(modifier = Modifier.height(20.dp))
                    
                    OutlinedTextField(
                        value = title,
                        onValueChange = { title = it },
                        placeholder = { Text(stringResource(R.string.goal_title_hint)) },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp)
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    OutlinedTextField(
                        value = desc,
                        onValueChange = { desc = it },
                        placeholder = { Text(stringResource(R.string.goal_desc_hint)) },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp)
                    )
                    
                    Spacer(modifier = Modifier.height(20.dp))
                    
                    Text(stringResource(R.string.options), style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold))
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    Surface(
                        color = MaterialTheme.colorScheme.secondary.copy(alpha = 0.05f),
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(8.dp)) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { showDatePicker = true }
                                    .padding(12.dp)
                            ) {
                                Icon(Icons.Default.CalendarToday, contentDescription = null, modifier = Modifier.size(20.dp))
                                Spacer(modifier = Modifier.width(12.dp))
                                Text(
                                    text = if (selectedDate != null) dateFormatter.format(Date(selectedDate)) else stringResource(R.string.select_deadline),
                                    modifier = Modifier.weight(1f)
                                )
                                Icon(Icons.Default.ChevronRight, contentDescription = null, tint = Color.Gray, modifier = Modifier.size(20.dp))
                            }
                            Divider(modifier = Modifier.padding(horizontal = 12.dp), color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f))
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(12.dp)
                            ) {
                                Icon(Icons.Default.Notifications, contentDescription = null, modifier = Modifier.size(20.dp))
                                Spacer(modifier = Modifier.width(12.dp))
                                Text(stringResource(R.string.reminder), modifier = Modifier.weight(1f))
                                Switch(checked = setReminder, onCheckedChange = { setReminder = it })
                            }
                            Divider(modifier = Modifier.padding(horizontal = 12.dp), color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f))
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(12.dp)
                                    .clickable { isPriority = !isPriority }
                            ) {
                                Icon(Icons.Default.Star, contentDescription = null, modifier = Modifier.size(20.dp), tint = if (isPriority) Color(0xFFFFCC00) else Color.Gray)
                                Spacer(modifier = Modifier.width(12.dp))
                                Text(stringResource(R.string.high_priority), modifier = Modifier.weight(1f))
                                Checkbox(checked = isPriority, onCheckedChange = { isPriority = it })
                            }
                        }
                    }

                    if (setReminder) {
                        Spacer(modifier = Modifier.height(12.dp))
                        OutlinedTextField(
                            value = reminderMinutes,
                            onValueChange = { if (it.all { char -> char.isDigit() }) reminderMinutes = it },
                            label = { Text(stringResource(R.string.minutes_from_now)) },
                            modifier = Modifier.fillMaxWidth(),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            shape = RoundedCornerShape(16.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(20.dp))
                    Text(stringResource(R.string.category), style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold))
                    Spacer(modifier = Modifier.height(8.dp))
                    LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        item {
                            FilterChip(
                                selected = selectedCategoryId == null,
                                onClick = { selectedCategoryId = null },
                                label = { Text(stringResource(R.string.none)) },
                                shape = RoundedCornerShape(12.dp)
                            )
                        }
                        items(categories) { category ->
                            FilterChip(
                                selected = selectedCategoryId == category.id,
                                onClick = { selectedCategoryId = category.id },
                                label = { Text(category.name) },
                                shape = RoundedCornerShape(12.dp),
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = Color(category.color).copy(alpha = 0.2f),
                                    selectedLabelColor = Color(category.color)
                                )
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(32.dp))
                    
                    Button(
                        onClick = { 
                            if (title.isNotBlank()) {
                                val reminderTime = if (setReminder) {
                                    System.currentTimeMillis() + (reminderMinutes.toLongOrNull() ?: 60L) * 60 * 1000
                                } else null
                                onConfirm(title, desc, selectedCategoryId, reminderTime, selectedDate, isPriority)
                            }
                        },
                        enabled = title.isNotBlank(),
                        modifier = Modifier.fillMaxWidth().height(56.dp),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Text(
                            text = stringResource(if (goalToEdit == null) R.string.create_goal else R.string.save_changes), 
                            fontWeight = FontWeight.Bold, 
                            fontSize = 16.sp
                        )
                    }
                    
                    TextButton(
                        onClick = onDismiss,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(stringResource(R.string.cancel), color = Color.Gray)
                    }
                }
            }
        }
    )

    if (showDatePicker) {
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(onClick = { showDatePicker = false }) { Text(stringResource(R.string.done)) }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) { Text(stringResource(R.string.cancel)) }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }
}

@Composable
fun AddCategoryDialog(
    onDismiss: () -> Unit,
    onConfirm: (String, Int) -> Unit
) {
    var name by remember { mutableStateOf("") }
    val colors = listOf(0xFF007AFF.toInt(), 0xFF34C759.toInt(), 0xFFFF3B30.toInt(), 0xFFFF9500.toInt(), 0xFFAF52DE.toInt())
    var selectedColor by remember { mutableStateOf(colors[0]) }

    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            Button(onClick = { if (name.isNotBlank()) onConfirm(name, selectedColor) }) {
                Text(stringResource(R.string.add))
            }
        },
        title = { Text(stringResource(R.string.new_tag)) },
        text = {
            Column {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    placeholder = { Text(stringResource(R.string.category_name_hint)) },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                )
                Spacer(modifier = Modifier.height(16.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    colors.forEach { color ->
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(Color(color))
                                .clickable { selectedColor = color }
                                .padding(4.dp)
                        ) {
                            if (selectedColor == color) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .background(Color.White.copy(alpha = 0.5f), CircleShape)
                                )
                            }
                        }
                    }
                }
            }
        }
    )
}
