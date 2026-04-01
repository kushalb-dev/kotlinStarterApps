package com.starterKotlin.todolist

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TodoApp()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TodoApp(
    modifier: Modifier = Modifier,
) {
    val todoList = rememberSaveable { mutableStateListOf<TodoData>() }
    val onToggleTodoStatus: (TodoData) -> Unit = { item ->
        val index = todoList.indexOf(item)
        if (index != -1) {
            todoList[index] = item.copy(todoStatus = !item.todoStatus)
        }
    }
    var currentInput by rememberSaveable { mutableStateOf("") }
    val focusManager = LocalFocusManager.current
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("TODO App") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                ),
                modifier = modifier
                    .clip(
                        RoundedCornerShape(
                            bottomStart = 20.dp,
                            bottomEnd = 20.dp,
                        )
                    )
            )
        },
        bottomBar = {
            BottomEditBar(
                inputText = currentInput,
                onValueChange = { currentInput = it },
                onAddClick = {
                    todoList.add(TodoData(currentInput, false))
                    currentInput = ""
                    focusManager.clearFocus()
                },
                onClearClick = {
                    todoList.removeAll { it.todoStatus }
                    focusManager.clearFocus()
                }
            )
        }
    ) {
        innerPadding ->
        Column(
            modifier = modifier
                .padding(innerPadding)
                .fillMaxSize()
                .pointerInput(Unit) {
                    detectTapGestures( onTap = {
                        focusManager.clearFocus()
                    })
                }
        ) {
            RecyclerView(
                todoItems = todoList,
                onToggleStatus = onToggleTodoStatus,
                modifier = Modifier
                    .weight(1f),
            )
        }
    }
}

/**
 * Card for each item in the to-do list
 * used by [RecyclerView], accepting [TodoData] as parameter
 */
@Composable
fun TodoCard(
    todoItem: TodoData,
    onStatusChange: (TodoData) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .padding(2.dp)
            .fillMaxWidth(),
        shape = RoundedCornerShape(CornerSize(20.dp)),
        elevation = CardDefaults.cardElevation(2.dp),
    ) {
        Row(
            modifier = Modifier
                .padding(5.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = todoItem.todoDetail,
                textAlign = TextAlign.Start,
                fontStyle = if (todoItem.todoStatus) FontStyle.Italic else FontStyle.Normal,
                textDecoration = if (todoItem.todoStatus) TextDecoration.LineThrough else TextDecoration.None,
                color = if (todoItem.todoStatus) Color.Gray else Color.Unspecified,
                fontSize = 20.sp,
                modifier = Modifier
                    .weight(0.9f),
            )
            Spacer(
                modifier = Modifier
                    .weight(0.05f)
            )
            Checkbox(
                checked = todoItem.todoStatus,
                onCheckedChange = { onStatusChange(todoItem) },
                modifier = Modifier
                    .weight(0.05f)
                    .padding(end = 6.dp)
            )
        }
    }
}

@Composable
fun RecyclerView (
    todoItems: List<TodoData>,
    onToggleStatus: (TodoData) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        modifier = modifier
    ) {
        items(todoItems) { todoItem ->
            TodoCard(todoItem, { onToggleStatus(todoItem) })
        }
    }
}

@Composable
fun BottomEditBar(
    inputText: String,
    onValueChange: (String) -> Unit,
    onAddClick: () -> Unit,
    onClearClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .padding(8.dp)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        TextField(
            value = inputText,
            onValueChange = onValueChange,
            label = { Text(stringResource(R.string.enter_a_todo)) },
            shape = RoundedCornerShape(20.dp),
            colors = TextFieldDefaults.colors(
                unfocusedIndicatorColor = Color.Transparent
            ),
            modifier = Modifier
                .weight(0.5f)
        )
        Spacer(
            modifier = Modifier
                .weight(0.02f)
        )
        Button(
            onClick = onAddClick,
            modifier = Modifier
                .weight(0.25f)
        ) {
            Text(
                text = stringResource(R.string.add_todo),
                textAlign = TextAlign.Center,
            )
        }
        Spacer(
            modifier = Modifier
                .weight(0.02f)
        )
        Button(
            onClick = onClearClick,
            modifier = Modifier
                .weight(0.25f)
        ) {
            Text(
                text = stringResource(R.string.clear_completed),
                textAlign = TextAlign.Center,
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TodoAppPreview() {
    TodoApp()
}
