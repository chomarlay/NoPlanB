package com.noplanb.noplanb.fragments.projects.add

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.noplanb.noplanb.R
import com.noplanb.noplanb.data.models.Project
import com.noplanb.noplanb.data.viewmodel.ProjectViewModel
import com.noplanb.noplanb.data.viewmodel.SharedViewModel
import kotlinx.android.synthetic.main.fragment_add_project.*

class AddProjectFragment : Fragment() {
    private val projectViewModel: ProjectViewModel by viewModels()
    private val sharedViewModel:SharedViewModel by lazy { SharedViewModel() }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_add_project, container, false)
        setHasOptionsMenu(true)
        return view

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.add_project_fragment_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.menu_save_project) {
            insertProjectToDb()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun insertProjectToDb() {
        val mTitle = title_et.text.toString()
        val mDescription = description_et.text.toString()
        if (sharedViewModel.validProjectDataFromInput(mTitle)) {
            val project = Project(0,mTitle,mDescription)
            projectViewModel.insertProject(project)
            Toast.makeText(requireContext(),"Project saved successfully", Toast.LENGTH_SHORT).show()
            findNavController().navigate(R.id.action_addProjectFragment_to_projectListFragment)
        } else {
            Toast.makeText(requireContext(),"Please enter the Title of the project", Toast.LENGTH_SHORT).show()
        }

    }

}