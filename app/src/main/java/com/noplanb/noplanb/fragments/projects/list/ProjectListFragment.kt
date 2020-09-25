package com.noplanb.noplanb.fragments.projects.list

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.noplanb.noplanb.R
import com.noplanb.noplanb.data.viewmodel.ProjectViewModel
import com.noplanb.noplanb.databinding.FragmentProjectListBinding
import com.noplanb.noplanb.fragments.projects.list.adapter.ProjectListAdapter
import com.noplanb.noplanb.utils.hideKeyboard

class ProjectListFragment : Fragment() {
    private var _binding: FragmentProjectListBinding? = null
    private val binding get() = _binding!!

    private val projectViewModel: ProjectViewModel by viewModels()
    private val projectListAdapter: ProjectListAdapter by lazy { ProjectListAdapter() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentProjectListBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this
        setupRecyclerView()
        projectViewModel.getProjectsWithTasks.observe(viewLifecycleOwner, {
                data->projectListAdapter.setData(data)
            }
        )
        // hide soft keyboard
        hideKeyboard(requireActivity())
        return binding.root
    }

    private fun setupRecyclerView() {
        val recyclerView = binding.projectRecyclerView
        recyclerView.layoutManager = LinearLayoutManager(requireActivity())
        recyclerView.adapter = projectListAdapter
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null // very important to avoid memory leak
    }
}