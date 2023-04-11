package com.example.todo.data

import androidx.lifecycle.LiveData

class ToDoRepository(private val toDoDao:ToDoDao) {

    val getAllData:LiveData<List<ToDoData>> = toDoDao.getAllData()

    suspend fun insertData(toDoData: ToDoData){
        toDoDao.insertData(toDoData)
    }
}