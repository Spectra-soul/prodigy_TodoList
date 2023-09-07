package com.example.ToDo

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import android.app.Application
import androidx.compose.runtime.mutableStateListOf

class ToDoList: Application() {
    val ToDoView = ToDoView()
}

class ToDoView: ViewModel() {
    val toDoItems = mutableStateListOf<String>()

    fun addItem(item: String) {
        toDoItems.add(item)
    }

    fun renameItem(oldItem: String, newItem: String) {
        val index = toDoItems.indexOf(oldItem)
        if (index != -1) {
            toDoItems[index] = newItem
        }
    }

    fun removeItem(item: String) {
        toDoItems.remove(item)
    }

}

