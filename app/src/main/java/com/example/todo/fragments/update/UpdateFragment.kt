package com.example.todo.fragments.update

import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.todo.R
import com.example.todo.data.models.Priority
import com.example.todo.data.models.ToDoData
import com.example.todo.data.viewmodel.ToDoViewModel
import com.example.todo.databinding.FragmentUpdateBinding
import com.example.todo.fragments.SharedViewModel

class UpdateFragment : Fragment(R.layout.fragment_update) {
    private lateinit var binding:FragmentUpdateBinding
    private val  args: UpdateFragmentArgs by navArgs<UpdateFragmentArgs>()
    private val sharedViewModel: SharedViewModel by viewModels()
    private val todoViewModel:ToDoViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentUpdateBinding.bind(view)


        val currentItem = args.currentItem

        binding.currentTitleEt.setText(currentItem.title)
        binding.currentDescriptionEt.setText(currentItem.description)

        binding.currentPrioritiesSpinner.setSelection(sharedViewModel.parsePriorityToInt(currentItem.priority))

        binding.currentPrioritiesSpinner.onItemSelectedListener = sharedViewModel.listener

        setupMenu()
    }

    private fun setupMenu(){
        (requireActivity() as MenuHost).addMenuProvider(object :MenuProvider{
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.update_fragment_menu,menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                when(menuItem.itemId){
                    R.id.menu_save->{
                        updateItem()
                    }
                    R.id.menu_delete->{}
                    else->{
                        findNavController().navigateUp()
                    }
                }
                return true
            }

        },viewLifecycleOwner,Lifecycle.State.RESUMED)
    }

    private fun updateItem(){
        val title = binding.currentTitleEt.text.toString()
        val description = binding.currentDescriptionEt.text.toString()
        val priority = binding.currentPrioritiesSpinner.selectedItem.toString()

        val validation = sharedViewModel.verifyDateFromUser(title,description)

        if(!validation){
            Toast.makeText(requireContext(),"Validation Failed",Toast.LENGTH_SHORT).show()
            return;
        }

        val updateItem = ToDoData(
            args.currentItem.id,
            title,
            sharedViewModel.parsePriority(priority),
            description,
        )

        todoViewModel.updateData(updateItem)

        Toast.makeText(requireContext(),"Successfully Updated",Toast.LENGTH_SHORT).show()

        findNavController().navigate(R.id.action_updateFragment_to_listFragment)
    }

}