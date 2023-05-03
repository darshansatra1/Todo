package com.example.todo.fragments.list

import android.inputmethodservice.Keyboard.Row
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.todo.R
import com.example.todo.data.models.Priority
import com.example.todo.data.models.ToDoData
import com.example.todo.databinding.RowLayoutBinding

class ListAdapter:RecyclerView.Adapter<ListAdapter.ViewHolder>() {
    private var dataList = emptyList<ToDoData>()

    class ViewHolder(private val binding:RowLayoutBinding):RecyclerView.ViewHolder(binding.root){
        fun bind(toDoData: ToDoData){
            binding.toDoData = toDoData
            binding.executePendingBindings()
        }

        companion object{
            fun from(parent:ViewGroup):ViewHolder{
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = RowLayoutBinding.inflate(layoutInflater,parent,false)
                return ViewHolder(binding)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(dataList[position])
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    fun setData(toDoData:List<ToDoData>){
        this.dataList = toDoData
        notifyDataSetChanged()
    }
}