package com.noplanb.noplanb.fragments.tasks.list

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.noplanb.noplanb.R
import com.noplanb.noplanb.data.models.ProjectWithTasks
import com.noplanb.noplanb.data.viewmodel.ProjectViewModel
import com.noplanb.noplanb.databinding.FragmentTaskListBinding
import com.noplanb.noplanb.fragments.tasks.list.adapter.TaskListAdapter

class TaskListFragment : Fragment() {
    private val args by navArgs<TaskListFragmentArgs> ()
    private var _binding: FragmentTaskListBinding? = null
    private val binding get()=_binding!!

    private val projectViewModel: ProjectViewModel by viewModels()
    private val taskListAdapter: TaskListAdapter by lazy { TaskListAdapter() }
    private var currentProject: ProjectWithTasks? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentTaskListBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this

        setupRecyclerView()
        var projectId = args.projectId

        setHasOptionsMenu(projectId != 1)

        projectViewModel.getProjectWithTasks(projectId).observe(viewLifecycleOwner, { data ->
            taskListAdapter.setData(data)
            currentProject = data
             }
        )
        return binding.root
    }
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.task_list_fragment_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.menu_edit_project) {
            val action = TaskListFragmentDirections.actionTaskListFragmentToUpdateProjectFragment(
                currentProject!!.project
            )
            findNavController().navigate(action)

        }
        return super.onOptionsItemSelected(item)
    }
    private fun setupRecyclerView() {
        var recyclerView = binding.taskRecyclerView
        recyclerView.layoutManager = LinearLayoutManager(requireActivity())
        recyclerView.adapter = taskListAdapter
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null // very important to avoid memory leak
    }

}