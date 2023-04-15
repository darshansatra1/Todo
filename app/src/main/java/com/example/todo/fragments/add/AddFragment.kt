package com.example.todo.fragments.add

import android.os.Bundle
import android.text.TextUtils
import android.view.*
import android.widget.Toast
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.navigation.fragment.findNavController
import com.example.todo.R
import com.example.todo.data.models.Priority
import com.example.todo.data.models.ToDoData
import com.example.todo.data.viewmodel.ToDoViewModel
import com.example.todo.databinding.FragmentAddBinding
import com.example.todo.fragments.SharedViewModel

class AddFragment : Fragment(R.layout.fragment_add) {

    private lateinit var binding: FragmentAddBinding
    private val todoViewModel: ToDoViewModel by viewModels()
    private val sharedViewModel: SharedViewModel by viewModels()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentAddBinding.bind(view)
        binding.prioritiesSpinner.onItemSelectedListener = sharedViewModel.listener

        setupMenu()
    }

    private fun setupMenu(){
        (requireActivity() as MenuHost).addMenuProvider(object :MenuProvider{
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.add_fragment_menu,menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                when(menuItem.itemId){
                    R.id.menu_add->{
                        insertDataToDb()
                    }
                    else->{
                        findNavController().navigateUp()
                    }
                }
                return true
            }

        },viewLifecycleOwner,Lifecycle.State.RESUMED)
    }

    private fun insertDataToDb(){
        val title =binding.titleEt.text.toString()
        val description = binding.descriptionEt.text.toString()
        val priority = binding.prioritiesSpinner.selectedItem.toString()

        if(!sharedViewModel.verifyDateFromUser(title,description)){
            Toast.makeText(requireActivity(),"Please fill out all the fields!",Toast.LENGTH_SHORT).show()
            return
        }
        val data:ToDoData = ToDoData(
            0,
             title = title,
            description = description,
            priority = sharedViewModel.parsePriority(priority),
        )

        todoViewModel.insertData(data)

        Toast.makeText(requireActivity(),"Successfully added!",Toast.LENGTH_SHORT).show()
        findNavController().navigate(R.id.action_addFragment_to_listFragment)
    }


}