package com.example.todo.fragments

import android.app.Application
import android.text.TextUtils
import android.view.View
import android.widget.AdapterView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.todo.R
import com.example.todo.data.models.Priority
import com.example.todo.data.models.ToDoData

class SharedViewModel(application: Application):AndroidViewModel(application) {
    val emptyDatabase: MutableLiveData<Boolean> = MutableLiveData(false)

    fun checkIfDatabaseEmpty(toDoData:List<ToDoData>){
        emptyDatabase.value = toDoData.isEmpty()
    }

    val listener: AdapterView.OnItemSelectedListener = object :
    AdapterView.OnItemSelectedListener{
        override fun onItemSelected(parent: AdapterView<*>?,
                                    view: View?,
                                    position: Int,
                                    id: Long) {
            when(position){
                0->{
                    (parent?.getChildAt(0)as TextView).setTextColor(ContextCompat.getColor(application,
                        R.color.red
                    ))
                }
                1->{
                    (parent?.getChildAt(0)as TextView).setTextColor(ContextCompat.getColor(application,
                        R.color.yellow
                    ))
                }
                2->{
                    (parent?.getChildAt(0)as TextView).setTextColor(ContextCompat.getColor(application,
                        R.color.green
                    ))
                    }
            }
        }

        override fun onNothingSelected(p0: AdapterView<*>?) {

        }

    }

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

    fun parsePriorityToInt(priority: Priority):Int{
        return when(priority){
            Priority.HIGH->0
            Priority.MEDIUM->1
            Priority.LOW->2
        }
    }
}