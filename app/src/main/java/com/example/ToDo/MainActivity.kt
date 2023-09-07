package com.example.ToDo

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.*
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ToDo.ui.theme.*

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ToDoTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.primary
                ){
                    ToDoScreen((application as ToDoList).ToDoView)
                }
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ToDoScreen(viewModel: ToDoView){
    val darkModeEnabled by LocalTheme.current.darkMode.collectAsState()
    val bgColour = if (darkModeEnabled) Color.Black else Color.White
    val themeTodo = LocalTheme.current
    val context = LocalContext.current
    val message = "Please enter your Task"
    var text by remember { mutableStateOf("") }

    Column(modifier = Modifier.fillMaxSize()) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 0.dp, bottom = 10.dp)
                .clip(RoundedCornerShape(bottomEnd = 75.dp, bottomStart = 75.dp)),
            contentAlignment = Alignment.TopCenter,
        ) {
            Box(
                modifier = Modifier
                    .wrapContentWidth()
                    .wrapContentHeight()
                    .background(Skyy)
            ) {
                Column {
                    Row(
                        modifier = Modifier
                            .wrapContentWidth()
                            .wrapContentHeight()
                            .align(Alignment.CenterHorizontally)
                            .padding(2.dp)
                    ) {
                        Text(
                            text = "My ToDo App",
                            style = TextStyle(fontSize = 28.sp),
                            color = Color.White,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(87.dp)
                                .padding(15.dp)
                                .clickable { themeTodo.toggleTheme() },
                        )
                    }
                    Box (modifier = Modifier
                        .wrapContentWidth()
                        .wrapContentHeight())
                    {
                        Row(
                            modifier = Modifier
                                .padding(horizontal = 30.dp, vertical = 15.dp)
                                .clip(RoundedCornerShape(75.dp))
                                .background(bgColour),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            TextField(
                                value = text,
                                onValueChange = { text = it },
                                placeholder = { Text(text = "Enter your Task", fontSize = 14.sp) },
                                modifier = Modifier
                                    .weight(.80f)
                                    .clip(RoundedCornerShape(75.dp)),
                            )
                            Spacer(modifier = Modifier.padding(1.dp))
                            Icon(
                                imageVector = Icons.Default.Done,
                                contentDescription = null,
                                modifier = Modifier
                                    .padding(end = 8.dp)
                                    .align(Alignment.CenterVertically)
                                    .clickable {
                                        if (text.isNotEmpty()) {
                                            viewModel.addItem(text)
                                        } else Toast
                                            .makeText(context, message, Toast.LENGTH_LONG)
                                            .show()
                                    },
                                tint = Skyy,
                            )
                        }
                    }
                }
            }
        }
        LazyColumn(
            content = {
                items(viewModel.toDoItems) { item ->
                    ListedView(
                        text = item.replaceFirstChar { it.uppercase() },
                        onRename = { newItem ->
                            val oldItem = item
                            viewModel.renameItem(oldItem, newItem)
                        },
                        onDelete = {
                            viewModel.removeItem(item)
                        },
                    )
                }
            }
        )
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListedView(text: String,onRename: (newStr: String) -> Unit, onDelete: () -> Unit) {
    val darkModeEnabled by LocalTheme.current.darkMode.collectAsState()
    val textColor = if (darkModeEnabled) Color.White else Color.Black
    val listBG = if (darkModeEnabled) Listdark else Listlight
    var expanded by remember { mutableStateOf(false) }
    var showDialog by remember { mutableStateOf(false) }
    var newName by remember { mutableStateOf(text) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp)
            .clip(RoundedCornerShape(75.dp))
    ) {
        Row(
            modifier = Modifier
                .background(listBG)
                .fillMaxWidth()
                .padding(2.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = text,
                modifier = Modifier
                    .weight(1f)
                    .padding(10.dp),
                fontSize = 20.sp,
                color = textColor
            )
            Box(
                modifier = Modifier.clickable { expanded = true },
            ) {
                IconButton(
                    onClick = { expanded = true },
                    modifier = Modifier.clickable { expanded = true }
                ) {
                    Icon(
                        imageVector = Icons.Default.MoreVert,
                        contentDescription = "More Options",
                        tint = Skyy
                    )
                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = {
                            expanded = false
                        },
                    ) {
                        DropdownMenuItem(
                            text = {
                                Text(
                                    text = "Edit",
                                    modifier = Modifier.padding(2.dp),
                                    fontSize = 14.sp,
                                    color = textColor
                                )
                            },
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Outlined.Edit,
                                    contentDescription = "Edit",
                                    tint = Skyy
                                )
                            },
                            onClick = {
                                showDialog = true
                                expanded = false
                            },
                        )

                        DropdownMenuItem(
                            text = {
                                Text(
                                    text = "Delete",
                                    modifier = Modifier.padding(2.dp),
                                    fontSize = 14.sp,
                                    color = textColor
                                )
                            },
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Outlined.Delete,
                                    contentDescription = "Delete",
                                    tint = Skyy
                                )
                            },
                            modifier = Modifier.padding(2.dp),
                            onClick = {
                                onDelete()
                                expanded = false
                            }
                        )
                    }
                }
            }
        }
    }
    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text(text = "Rename Task", fontSize = 18.sp) },
            text = {
                TextField(
                    value = newName,
                    onValueChange = { newName = it },
                    modifier = Modifier.fillMaxWidth() .clip(RoundedCornerShape(75.dp)),
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        onRename(newName)
                        showDialog = false
                    }
                ) {
                    Text(text = "Rename",
                        color = textColor,
                        fontSize = 14.sp)
                }
            },
            dismissButton = {
                Button(
                    onClick = {
                        showDialog = false
                    }
                ) {
                    Text(text = "Cancel",
                        color = textColor,
                        fontSize = 14.sp)
                }
            }
        )
    }
}

@Preview()
@Composable
fun ToDoScreenPreview() {
    ToDoTheme {
        ToDoScreen(ToDoView())
    }
}
