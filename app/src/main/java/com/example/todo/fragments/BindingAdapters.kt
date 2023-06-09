package com.example.todo.fragments

import android.os.Build
import android.view.View
import androidx.annotation.RequiresApi
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import androidx.lifecycle.MutableLiveData
import androidx.navigation.findNavController
import com.example.todo.R
import com.example.todo.data.models.Priority
import com.example.todo.data.models.ToDoData
import com.example.todo.fragments.list.ListFragmentDirections
import com.google.android.material.floatingactionbutton.FloatingActionButton

class BindingAdapters {
    companion object {
        @BindingAdapter("android:navigateToAddFragment")
        @JvmStatic
        fun navigateToAddFragment(view:FloatingActionButton, navigate: Boolean){
            view.setOnClickListener{
                if(navigate){
                    view.findNavController().navigate(
                        R.id.action_listFragment_to_addFragment
                    )
                }
            }
        }

        @BindingAdapter("android:emptyDatabase")
        @JvmStatic
        fun emptyDatabase(view:View,emptyDatabase: MutableLiveData<Boolean>){
            when(emptyDatabase.value){
                true->view.visibility = View.VISIBLE
                false -> view.visibility = View.INVISIBLE
                else -> {}
            }
        }

        @RequiresApi(Build.VERSION_CODES.M)
        @BindingAdapter("android:parsePriorityColor")
        @JvmStatic
        fun parsePriorityColor(cardView:CardView, priority: Priority){
            when(priority){
                Priority.HIGH -> {cardView.setCardBackgroundColor(ContextCompat.getColor(cardView.context,R.color.red))}
                Priority.LOW -> {cardView.setCardBackgroundColor(ContextCompat.getColor(cardView.context,R.color.green))}
                Priority.MEDIUM -> {cardView.setCardBackgroundColor(ContextCompat.getColor(cardView.context,R.color.yellow))}
            }
        }

        @BindingAdapter("android:sendDataToUpdateFragment")
        @JvmStatic
        fun sendDataToUpdateFragment(view:ConstraintLayout, currentItem: ToDoData){
            view.setOnClickListener{
                val action = ListFragmentDirections.actionListFragmentToUpdateFragment(currentItem)
                view.findNavController().navigate(action)
            }
        }
    }
}