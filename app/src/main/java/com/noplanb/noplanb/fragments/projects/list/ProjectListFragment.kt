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
import com.noplanb.noplanb.utils.NpbConstants
import com.noplanb.noplanb.utils.hideKeyboard
import jp.wasabeef.recyclerview.animators.SlideInUpAnimator

class ProjectListFragment : Fragment() {
    private var _binding: FragmentProjectListBinding? = null
    private val binding get() = _binding!!

    private val projectViewModel: ProjectViewModel by viewModels()
    private val projectListAdapter: ProjectListAdapter by lazy { ProjectListAdapter() }
    private var showCompletedProjectsMenu = true
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentProjectListBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this
        showCompletedProjectsMenu = true
        projectViewModel.getProjectsWithTasks.observe(viewLifecycleOwner, {
                data->projectListAdapter.setData(data)
            }
        )
        setupRecyclerView()
        setHasOptionsMenu(true)
        // hide soft keyboard
        hideKeyboard(requireActivity())
        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.project_list_fragment_menu, menu)

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_show_completed_projects -> showHideCompletedProjects()
            R.id.menu_hide_completed_projects -> showHideCompletedProjects()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        val menuShowCompletedProject: MenuItem = menu.findItem(R.id.menu_show_completed_projects)
        val menuHideCompletedProject: MenuItem = menu.findItem(R.id.menu_hide_completed_projects)
        menuShowCompletedProject.setVisible(showCompletedProjectsMenu)
        menuHideCompletedProject.setVisible(!showCompletedProjectsMenu)
        super.onPrepareOptionsMenu(menu)
    }

    private fun showHideCompletedProjects() {
        if (showCompletedProjectsMenu) {
            projectViewModel.getAllProjectsWithTasks().observe(
                viewLifecycleOwner,
                { data -> projectListAdapter.setData(data) })
        } else {
            projectViewModel.getProjectsWithTasks.observe(
                viewLifecycleOwner,
                { data -> projectListAdapter.setData(data) })
        }
        showCompletedProjectsMenu = !showCompletedProjectsMenu

    }
    private fun setupRecyclerView() {
        val recyclerView = binding.projectRecyclerView
        recyclerView.layoutManager = LinearLayoutManager(requireActivity())
        recyclerView.adapter = projectListAdapter
        recyclerView.itemAnimator = SlideInUpAnimator().apply {
            addDuration= 300
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null // very important to avoid memory leak
    }
}