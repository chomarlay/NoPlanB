package com.noplanb.noplanb.fragments.tasks.list

import android.os.Bundle
import android.view.*
import android.widget.Toast
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
import com.noplanb.noplanb.data.models.Project
import com.noplanb.noplanb.data.models.Task
import com.noplanb.noplanb.data.viewmodel.ProjectViewModel
import com.noplanb.noplanb.data.viewmodel.TaskViewModel
import com.noplanb.noplanb.databinding.FragmentTaskListBinding
import com.noplanb.noplanb.fragments.tasks.list.adapter.SwipeToMarkAsCompleted
import com.noplanb.noplanb.fragments.tasks.list.adapter.TaskListAdapter
import com.noplanb.noplanb.utils.NpbConstants
import com.noplanb.noplanb.utils.hideKeyboard
import jp.wasabeef.recyclerview.animators.SlideInUpAnimator
import java.util.*

class TaskListFragment : Fragment(), SearchView.OnQueryTextListener {
    private val args by navArgs<TaskListFragmentArgs> ()
    private var _binding: FragmentTaskListBinding? = null
    private val binding get()=_binding!!

    private val projectViewModel: ProjectViewModel by viewModels()
    private val taskViewModel: TaskViewModel by viewModels()
    private val taskListAdapter: TaskListAdapter by lazy { TaskListAdapter() }
    private var currentProject: Project? = null
    private var showCompletedTaskMenu = true
    private var projectId = 0
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentTaskListBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this

        setupRecyclerView()
        projectId = args.projectId
        showCompletedTaskMenu = true
        setHasOptionsMenu(true)

        projectViewModel.getProjectById(projectId).observe(viewLifecycleOwner, {data-> currentProject = data})

        taskViewModel.getTasksByProject(projectId).observe(viewLifecycleOwner, {data-> taskListAdapter.setData(data, NpbConstants.TASK_LIST_PROJ)})

        binding.addTaskBtn.setOnClickListener{
           val action = TaskListFragmentDirections.actionTaskListFragmentToAddTaskFragment(projectId, NpbConstants.TASK_LIST_PROJ) // pass the projectId to addTaskFragment to set the current project in spinner
           findNavController().navigate(action)
        }
        // hide soft keyboard
        hideKeyboard(requireActivity())
        return binding.root
    }
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.task_list_fragment_menu, menu)
        val search = menu.findItem(R.id.menu_search)
        val searchView = search.actionView as SearchView
        searchView.isSubmitButtonEnabled = true
        searchView.setOnQueryTextListener(this)
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        val menuEditProject: MenuItem = menu.findItem(R.id.menu_edit_project)
        val menuShowCompletedTask: MenuItem = menu.findItem(R.id.menu_show_completed_tasks)
        val menuHideCompletedTask: MenuItem = menu.findItem(R.id.menu_hide_completed_tasks)
        menuEditProject.setVisible(projectId != 1)
        menuShowCompletedTask.setVisible(showCompletedTaskMenu)
        menuHideCompletedTask.setVisible(!showCompletedTaskMenu)
        super.onPrepareOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_edit_project -> editProject()
            R.id.menu_show_completed_tasks -> showHideCompletedTasks()
            R.id.menu_hide_completed_tasks -> showHideCompletedTasks()
        }
        return super.onOptionsItemSelected(item)
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
                    Toast.makeText(requireContext(), "Task '${task.title}' is already marked as completed.", Toast.LENGTH_SHORT).show()
                }


            }
        }
        val itemTouchHelper = ItemTouchHelper(swipeToMarkAsCompletedCallback)
        itemTouchHelper.attachToRecyclerView(recyclerView)
    }

    private fun restoreMarkAsCompleted(view: View, completedTask: Task, position: Int) {
        val snackBar = Snackbar.make(view, "Task '${completedTask.title}' marked as completed.", Snackbar.LENGTH_LONG)
        snackBar.setAction("Undo"){
            completedTask.completedDate = null
            taskViewModel.updateTask(completedTask)
            taskListAdapter.notifyItemChanged(position)
        }
        snackBar.setActionTextColor(ContextCompat.getColor(snackBar.context, R.color.colorPrimary))
        snackBar.show()
    }
    private fun editProject() {
        val action =
            TaskListFragmentDirections.actionTaskListFragmentToUpdateProjectFragment(
                currentProject!!
            )
        findNavController().navigate(action)
    }
    private fun showHideCompletedTasks() {
        if (showCompletedTaskMenu) {
            taskViewModel.getAllTasksByProject(projectId).observe(
                viewLifecycleOwner,
                { data -> taskListAdapter.setData(data, NpbConstants.TASK_LIST_PROJ) })
        } else {
            taskViewModel.getTasksByProject(projectId).observe(
                viewLifecycleOwner,
                { data -> taskListAdapter.setData(data, NpbConstants.TASK_LIST_PROJ) })
        }
        showCompletedTaskMenu = !showCompletedTaskMenu

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

    private fun searchData (query: String) {
        if (!showCompletedTaskMenu) { // show completed task menu is now visible, i.e currently showing all tasks including completed ones then search for all
            taskViewModel.getAllTasksByProjectAndTitle(projectId, "%${query}%").observe(
                viewLifecycleOwner,
                { data -> taskListAdapter.setData(data, NpbConstants.TASK_LIST_PROJ) })
        } else {
            taskViewModel.getTasksByProjectAndTitle(projectId, "%${query}%").observe(
                viewLifecycleOwner,
                { data -> taskListAdapter.setData(data, NpbConstants.TASK_LIST_PROJ) })
        }
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

    override fun onDestroy() {
        super.onDestroy()
        _binding = null // very important to avoid memory leak
    }



}