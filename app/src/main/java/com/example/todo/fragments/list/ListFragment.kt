package com.example.todo.fragments.list

import android.app.AlertDialog
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.todo.R
import com.example.todo.data.viewmodel.ToDoViewModel
import com.example.todo.databinding.FragmentListBinding
import com.example.todo.fragments.SharedViewModel

class ListFragment : Fragment(R.layout.fragment_list) {

    private lateinit var binding: FragmentListBinding
    private val adapter:ListAdapter by lazy { ListAdapter() }
    private val todoViewModel:ToDoViewModel by viewModels()
    private val sharedViewModel:SharedViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentListBinding.bind(view)



        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(requireActivity())

        todoViewModel.getAllData.observe(viewLifecycleOwner,Observer{data->
            sharedViewModel.checkIfDatabaseEmpty(data)
            adapter.setData(data)
        })

        binding.floatingActionButton.setOnClickListener{
            findNavController().navigate(R.id.action_listFragment_to_addFragment)
        }

        sharedViewModel.emptyDatabase.observe(viewLifecycleOwner,Observer{
            showEmptyDatabaseViews(it)
        })

        setupMenu()

    }

    private fun showEmptyDatabaseViews(emptyDatabase:Boolean) {
        if(emptyDatabase){
            binding.noDataImageView.visibility = View.VISIBLE
            binding.noDataTextView.visibility = View.VISIBLE
        }else{
            binding.noDataImageView.visibility = View.GONE
            binding.noDataTextView.visibility = View.GONE
        }
    }

    private fun setupMenu() {
        (requireActivity() as MenuHost).addMenuProvider(object : MenuProvider {

            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.list_fragment_menu, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                when(menuItem.itemId){
                    R.id.menu_search->{}
                    R.id.menu_delete_all->{
                        confirmRemoval()
                    }
                    else->{
                        findNavController().navigateUp()
                    }
                }
                return true
            }

        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

    private fun confirmRemoval() {
        val builder = AlertDialog.Builder(requireContext())

        builder.setPositiveButton("Yes"){_,_->
            todoViewModel.deleteAll()
            Toast.makeText(requireContext(),"Successfully deleted all tasks", Toast.LENGTH_SHORT).show()
        }
        builder.setNegativeButton("No"){_,_->}

        builder.setTitle("Delete all tasks")
        builder.setMessage("Are you sure you want to delete all the tasks?")

        builder.create().show()
    }

}