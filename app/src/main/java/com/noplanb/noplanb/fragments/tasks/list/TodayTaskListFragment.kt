package com.noplanb.noplanb.fragments.tasks.list

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.noplanb.noplanb.R
import com.noplanb.noplanb.data.models.Task
import com.noplanb.noplanb.data.viewmodel.TaskViewModel
import com.noplanb.noplanb.databinding.FragmentTodayTaskListBinding
import com.noplanb.noplanb.fragments.tasks.list.adapter.SwipeToMarkAsCompleted
import com.noplanb.noplanb.fragments.tasks.list.adapter.TaskListAdapter
import com.noplanb.noplanb.utils.NpbConstants
import com.noplanb.noplanb.utils.dueBeforeDate
import com.noplanb.noplanb.utils.hideKeyboard
import jp.wasabeef.recyclerview.animators.SlideInUpAnimator
import java.util.*


class TodayTaskListFragment : Fragment(), SearchView.OnQueryTextListener {
    private val args by navArgs<TodayTaskListFragmentArgs> ()
    private var _binding: FragmentTodayTaskListBinding? = null
    private val binding get()=_binding!!
    private val taskViewModel: TaskViewModel by viewModels()
    private val taskListAdapter: TaskListAdapter by lazy { TaskListAdapter() }
    private var fromList = NpbConstants.TASK_LIST_TODAY
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentTodayTaskListBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this
        binding.args = args

        setupRecyclerView()

        val context = context as AppCompatActivity
        if (args.noDays==1) {
            fromList = NpbConstants.TASK_LIST_TODAY
            context.supportActionBar!!.title =  context.getString(R.string.today)
        } else {
            fromList = NpbConstants.TASK_LIST_7DAYS
            context.supportActionBar!!.title =  context.getString(R.string.next_7_days)
        }

        taskViewModel.getTasksDueBeforeDate(dueBeforeDate(args.noDays)).observe(
            viewLifecycleOwner,
            { data ->
                taskListAdapter.setData(
                    data,
                    fromList
                )
            })

        binding.addTaskBtn.setOnClickListener{
            val action = TodayTaskListFragmentDirections.actionTodayTaskListFragmentToAddTaskFragment(
                0,
                fromList
            ) // pass the projectId to addTaskFragment to set the current project in spinner
            findNavController().navigate(action)
        }
        // hide soft keyboard
        hideKeyboard(requireActivity())
        setHasOptionsMenu(true)

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.today_task_list_fragment_menu, menu)
        val search = menu.findItem(R.id.menu_search)
        val searchView = search.actionView as SearchView
        searchView.isSubmitButtonEnabled = true
        searchView.setOnQueryTextListener(this)
    }

    private fun setupRecyclerView() {
        val recyclerView = binding.taskRecyclerView
        recyclerView.layoutManager = LinearLayoutManager(requireActivity())
        recyclerView.adapter = taskListAdapter
//        recyclerView.itemAnimator = SlideInUpAnimator().apply {
//            addDuration= 300
//        }
        swipeToMarkAsCompleted(recyclerView)
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        if (query != null) {
            searchData(query)
        }
        return true
    }

    override fun onQueryTextChange(query: String?): Boolean {
        if (query != null) {
            searchData(query)
        }
        return true
    }

    private fun searchData(query: String) {
            taskViewModel.getTasksDueBeforeDateAndTitle(dueBeforeDate(args.noDays), "%${query}%").observe(
                viewLifecycleOwner,
                { data ->
                    taskListAdapter.setData(
                        data,
                        fromList
                    )
                })
    }

    private fun swipeToMarkAsCompleted(recyclerView: RecyclerView) {
        val swipeToMarkAsCompletedCallback = object: SwipeToMarkAsCompleted() {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val itemToMarkAsCompleted = taskListAdapter.tasksWithProject[viewHolder.adapterPosition]
                val task = itemToMarkAsCompleted.task
                if (task.completedDate == null) {
                    task.completedDate = Date()
                    taskViewModel.updateTask(task)
                    taskListAdapter.notifyItemChanged(viewHolder.adapterPosition)
                    restoreMarkAsCompleted(viewHolder.itemView, task, viewHolder.adapterPosition)
                } else {
                    taskListAdapter.notifyItemChanged(viewHolder.adapterPosition)
                    Toast.makeText(
                        requireContext(),
                        "Task '${task.title}' is already marked as completed.",
                        Toast.LENGTH_SHORT
                    ).show()
                }


            }
        }
        val itemTouchHelper = ItemTouchHelper(swipeToMarkAsCompletedCallback)
        itemTouchHelper.attachToRecyclerView(recyclerView)
    }

    private fun restoreMarkAsCompleted(view: View, completedTask: Task, position: Int) {
        val snackBar = Snackbar.make(
            view,
            "Task '${completedTask.title}' marked as completed.",
            Snackbar.LENGTH_LONG
        )
        snackBar.setAction("Undo"){
            completedTask.completedDate = null
            taskViewModel.updateTask(completedTask)
            taskListAdapter.notifyItemChanged(position)
        }
        snackBar.setActionTextColor(ContextCompat.getColor(snackBar.context, R.color.colorPrimary))
        snackBar.show()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null // very important to avoid memory leak
    }
}