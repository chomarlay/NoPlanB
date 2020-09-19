package com.noplanb.noplanb.fragments.projects.update

import android.app.AlertDialog
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs

import com.noplanb.noplanb.R
import com.noplanb.noplanb.data.models.Project
import com.noplanb.noplanb.data.viewmodel.ProjectViewModel
import com.noplanb.noplanb.data.viewmodel.SharedViewModel
import com.noplanb.noplanb.databinding.FragmentUpdateProjectBinding
import kotlinx.android.synthetic.main.fragment_update_project.*

class UpdateProjectFragment : Fragment() {
    private val args by navArgs<UpdateProjectFragmentArgs> ()  // see fragment_update_project and main_nav for  currentItem args declartion
    private val projectViewModel: ProjectViewModel by viewModels()
    private val sharedViewModel: SharedViewModel by lazy{ SharedViewModel()}

    var _binding : FragmentUpdateProjectBinding? = null
    val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentUpdateProjectBinding.inflate(inflater, container, false)
        binding.args = args
        setHasOptionsMenu(true)
        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.update_project_fragment_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_update_project -> updateProject()
            R.id.menu_delete_project -> confirmItemDelete()
        }

        return super.onOptionsItemSelected(item)
    }

    private fun confirmItemDelete() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Delete Project")
        builder.setMessage("Do you want to delete project '${args.currentItem.title}' ?")
        builder.setPositiveButton("Yes") {
                //dialogInterface: DialogInterface, i: Int ->
            _,_-> // short form to above
            projectViewModel.deleteProject(args.currentItem)
            findNavController().navigate(R.id.action_updateProjectFragment_to_taskListFragment)
            Toast.makeText(requireContext(), "Project '${args.currentItem.title}' delete successfully.", Toast.LENGTH_SHORT).show()
        }
        builder.setNegativeButton("No") {
//                dialogInterface: DialogInterface, i: Int ->
            _,_-> //short form to above
        }
        builder.show()
    }

    private fun updateProject() {
        val mTitle = current_title_et.text.toString()
        val mDescription = current_description_et.text.toString()
        if (sharedViewModel.validProjectDataFromInput(mTitle)) {
            val project = Project(args.currentItem.id,mTitle,mDescription)
            projectViewModel.updateProject(project)
            val action = UpdateProjectFragmentDirections.actionUpdateProjectFragmentToTaskListFragment(
                args.currentItem.id, mTitle
            )
            findNavController().navigate(action)
            Toast.makeText(requireContext(), "Project '${mTitle}' updated successfully.", Toast.LENGTH_SHORT).show()

        } else {
            Toast.makeText(requireContext(), "Please enter the Title of the project.", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}