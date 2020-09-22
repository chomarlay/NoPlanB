package com.noplanb.noplanb.fragments.tasks.list

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.noplanb.noplanb.data.viewmodel.ProjectViewModel
import com.noplanb.noplanb.data.viewmodel.TaskViewModel
import com.noplanb.noplanb.databinding.FragmentTodayTaskListBinding
import com.noplanb.noplanb.fragments.tasks.list.adapter.TaskListAdapter
import com.noplanb.noplanb.utils.NpbConstants
import com.noplanb.noplanb.utils.dueBeforeDate
import com.noplanb.noplanb.utils.hideKeyboard

import java.util.*

class TodayTaskListFragment : Fragment() {
    private var _binding: FragmentTodayTaskListBinding? = null
    private val binding get()=_binding!!
    private val taskViewModel: TaskViewModel by viewModels()
    private val taskListAdapter: TaskListAdapter by lazy { TaskListAdapter() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentTodayTaskListBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this

        setupRecyclerView()
        taskViewModel.getTasksDueBeforeDate(dueBeforeDate(1)).observe(viewLifecycleOwner, { data-> taskListAdapter.setData(data, NpbConstants.TASK_LIST_TODAY)})

        binding.addTaskBtn.setOnClickListener{
            val action = TodayTaskListFragmentDirections.actionTodayTaskListFragmentToAddTaskFragment(0, NpbConstants.TASK_LIST_TODAY) // pass the projectId to addTaskFragment to set the current project in spinner
            findNavController().navigate(action)
        }
        // hide soft keyboard
        hideKeyboard(requireActivity())
        return binding.root
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