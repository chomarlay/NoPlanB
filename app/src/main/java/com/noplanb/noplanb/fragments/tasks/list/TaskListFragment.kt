package com.noplanb.noplanb.fragments.tasks.list

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.noplanb.noplanb.R
import com.noplanb.noplanb.data.viewmodel.ProjectViewModel
import com.noplanb.noplanb.databinding.FragmentTaskListBinding

import com.noplanb.noplanb.fragments.tasks.list.adapter.TaskListAdapter
import kotlinx.android.synthetic.main.task_row.*

class TaskListFragment : Fragment() {
    private val args by navArgs<TaskListFragmentArgs> ()
    private var _binding: FragmentTaskListBinding? = null
    private val binding get()=_binding!!

    private val projectViewModel: ProjectViewModel by viewModels()
    private val taskListAdapter: TaskListAdapter by lazy { TaskListAdapter() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentTaskListBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this

        setupRecyclerView()
        var projectId = args.projectId

        projectViewModel.getProjectWithTasks(projectId).observe(viewLifecycleOwner, { data ->
            taskListAdapter.setData(data)
             }
        )
        return binding.root
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