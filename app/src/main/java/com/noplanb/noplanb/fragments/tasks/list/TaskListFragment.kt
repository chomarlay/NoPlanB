package com.noplanb.noplanb.fragments.tasks.list

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.noplanb.noplanb.R
import com.noplanb.noplanb.data.models.Project
import com.noplanb.noplanb.data.viewmodel.ProjectViewModel
import com.noplanb.noplanb.data.viewmodel.TaskViewModel
import com.noplanb.noplanb.databinding.FragmentTaskListBinding
import com.noplanb.noplanb.fragments.tasks.list.adapter.TaskListAdapter
import com.noplanb.noplanb.utils.NpbConstants
import com.noplanb.noplanb.utils.hideKeyboard

class TaskListFragment : Fragment() {
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
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        val menuEditProject: MenuItem = menu.findItem(R.id.menu_edit_project);
        val menuShowCompletedTask: MenuItem = menu.findItem(R.id.menu_show_completed_tasks);
        val menuHideCompletedTask: MenuItem = menu.findItem(R.id.menu_hide_completed_tasks);
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
    private fun setupRecyclerView() {
        val recyclerView = binding.taskRecyclerView
        recyclerView.layoutManager = LinearLayoutManager(requireActivity())
        recyclerView.adapter = taskListAdapter
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null // very important to avoid memory leak
    }

}