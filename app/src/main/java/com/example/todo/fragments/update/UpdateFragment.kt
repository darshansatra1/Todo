package com.example.todo.fragments.update

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.todo.R
import com.example.todo.data.models.Priority
import com.example.todo.data.models.ToDoData
import com.example.todo.databinding.FragmentUpdateBinding

class UpdateFragment : Fragment(R.layout.fragment_update) {
    private lateinit var binding:FragmentUpdateBinding
    private val  args: UpdateFragmentArgs by navArgs<UpdateFragmentArgs>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentUpdateBinding.bind(view)


        val currentItem = args.currentItem

        binding.currentTitleEt.setText(currentItem.title)
        binding.currentDescriptionEt.setText(currentItem.description)

        binding.currentPrioritiesSpinner.setSelection(parsePriority(currentItem.priority))

        setupMenu()
    }

    private fun setupMenu(){
        (requireActivity() as MenuHost).addMenuProvider(object :MenuProvider{
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.update_fragment_menu,menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                when(menuItem.itemId){
                    R.id.menu_save->{}
                    R.id.menu_delete->{}
                    else->{
                        findNavController().navigateUp()
                    }
                }
                return true
            }

        },viewLifecycleOwner,Lifecycle.State.RESUMED)
    }

    private fun parsePriority(priority: Priority):Int{
        return when(priority){
            Priority.HIGH->0
            Priority.MEDIUM->1
            Priority.LOW->2
        }
    }
}