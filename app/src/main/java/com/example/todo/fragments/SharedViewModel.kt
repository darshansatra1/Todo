package com.example.todo.fragments

import android.app.Application
import android.text.TextUtils
import androidx.lifecycle.AndroidViewModel
import com.example.todo.data.models.Priority

class SharedViewModel(application: Application):AndroidViewModel(application) {
    fun verifyDateFromUser(title:String,description:String):Boolean{
        return !(TextUtils.isEmpty(title) || TextUtils.isEmpty(description))
    }

    fun parsePriority(priority: String): Priority {
        return when(priority){
            "High Priority"-> Priority.HIGH
            "Low Priority"-> Priority.LOW
            "Medium Priority"-> Priority.MEDIUM
            else-> Priority.LOW
        }
    }
}