package com.example.todo.fragments.list

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
    var dataList = emptyList<ToDoData>()

    inner class ViewHolder(private val binding:RowLayoutBinding):RecyclerView.ViewHolder(binding.root){
        fun bind(toDoData: ToDoData){
            binding.titleTxt.text = toDoData.title
            binding.descriptionTxt.text = toDoData.description

            when(toDoData.priority){
                Priority.HIGH->binding.priorityIndicator.setCardBackgroundColor(
                    ContextCompat.getColor(
                        binding.root.context,
                        R.color.red
                    )
                )
                Priority.LOW->binding.priorityIndicator.setCardBackgroundColor(
                    ContextCompat.getColor(
                        binding.root.context,
                        R.color.green
                    )
                )
                Priority.MEDIUM->binding.priorityIndicator.setCardBackgroundColor(
                    ContextCompat.getColor(
                        binding.root.context,
                        R.color.yellow
                    )
                )
            }

            binding.rowBackground.setOnClickListener{
                binding.root.findNavController().navigate(R.id.action_listFragment_to_updateFragment)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(RowLayoutBinding.inflate(LayoutInflater.from(parent.context),parent,false))
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