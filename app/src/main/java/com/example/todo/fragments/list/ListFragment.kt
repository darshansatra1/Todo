package com.example.todo.fragments.list

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.SearchView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.todo.R
import com.example.todo.data.models.ToDoData
import com.example.todo.data.viewmodel.ToDoViewModel
import com.example.todo.databinding.FragmentListBinding
import com.example.todo.fragments.SharedViewModel
import com.example.todo.fragments.list.adapter.ListAdapter
import com.google.android.material.snackbar.Snackbar
import jp.wasabeef.recyclerview.animators.SlideInUpAnimator

class ListFragment : Fragment(R.layout.fragment_list), SearchView.OnQueryTextListener {

    private var _binding: FragmentListBinding? = null
    private val binding get() = _binding!!
    private val adapter: ListAdapter by lazy { ListAdapter() }
    private val todoViewModel:ToDoViewModel by viewModels()
    private val sharedViewModel:SharedViewModel by viewModels()



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding = FragmentListBinding.bind(view)
        binding.lifecycleOwner = this
        binding.sharedViewModel = sharedViewModel


        // Set up recycler view
        setupRecyclerView()

        todoViewModel.getAllData.observe(viewLifecycleOwner,Observer{data->
            sharedViewModel.checkIfDatabaseEmpty(data)
            adapter.setData(data)
        })

        setupMenu()

    }

    private fun setupRecyclerView() {
        val recyclerView = binding.recyclerView
        recyclerView.adapter = adapter
        recyclerView.layoutManager = StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL)
        recyclerView.itemAnimator = SlideInUpAnimator().apply {
            addDuration = 300
        }

        // Swipe to Delete
        swipeToDelete(binding.recyclerView)
    }

    private fun swipeToDelete(recyclerView: RecyclerView){
        val swipeToDeleteCallback = object: SwipeToDelete(){
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val deletedItem = adapter.dataList[viewHolder.adapterPosition]

                // Delete Item
                todoViewModel. deleteData(deletedItem)
                adapter.notifyItemRemoved(viewHolder.adapterPosition)
                Toast.makeText(requireContext(),"Successfully Remove: '${deletedItem.title}'"
                , Toast.LENGTH_SHORT).show()

                // Restore deleted data
                restoreDeletedData(viewHolder.itemView, deletedItem, viewHolder.adapterPosition)
            }
        }

        val itemTouchHelper = ItemTouchHelper(swipeToDeleteCallback)
        itemTouchHelper.attachToRecyclerView(recyclerView)
    }

    private fun restoreDeletedData(view:View, deletedItem:ToDoData, position:Int){
        val snackBar = Snackbar.make(
            view, "Deleted '${deletedItem.title}",
            Snackbar.LENGTH_LONG
        )

        snackBar.setAction("Undo"){
            todoViewModel.insertData(deletedItem)
            adapter.notifyItemChanged(position)
        }

        snackBar.show()
    }


    private fun setupMenu() {
        (requireActivity() as MenuHost).addMenuProvider(object : MenuProvider {

            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.list_fragment_menu, menu)

                val search = menu.findItem(R.id.menu_search)
                val searchView = search.actionView as? SearchView
                searchView?.isSubmitButtonEnabled = true
                searchView?.setOnQueryTextListener(this@ListFragment)
            }


            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                when(menuItem.itemId){

                    R.id.menu_delete_all->{
                        confirmRemoval()
                    }
                    R.id.menu_priority_high->{
                        todoViewModel.sortByHighPriority.observe(viewLifecycleOwner,Observer{
                                adapter.setData(it)
                        })
                    }
                    R.id.menu_priority_low->{
                        todoViewModel.sortByLowPriority.observe(viewLifecycleOwner,Observer{
                            adapter.setData(it)
                        })
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

    override fun onQueryTextSubmit(query: String?): Boolean {
        if(query!=null){
            searchThroughDatabase(query)
        }
        return true
    }

    override fun onQueryTextChange(query: String?): Boolean {
        if(query!=null){
            searchThroughDatabase(query)
        }
        return true
    }

    private fun searchThroughDatabase(query:String) {
        val searchQuery = "%$query%"

        todoViewModel.searchDatabase(searchQuery).observe(this,Observer{list->
            list?.let {
                adapter.setData(it)
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }



}