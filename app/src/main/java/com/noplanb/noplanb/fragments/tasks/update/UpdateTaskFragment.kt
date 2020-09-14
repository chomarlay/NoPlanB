package com.noplanb.noplanb.fragments.tasks.update

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
import com.noplanb.noplanb.data.models.Task
import com.noplanb.noplanb.data.viewmodel.ProjectViewModel
import com.noplanb.noplanb.data.viewmodel.SharedViewModel
import com.noplanb.noplanb.data.viewmodel.TaskViewModel
import com.noplanb.noplanb.databinding.FragmentUpdateTaskBinding
import kotlinx.android.synthetic.main.fragment_add_task.*

class UpdateTaskFragment : Fragment() {
    private val args by navArgs<UpdateTaskFragmentArgs> ()
    private val projectViewModel: ProjectViewModel by viewModels()
    private val sharedViewModel: SharedViewModel by lazy{ SharedViewModel() }
    private val taskViewModel: TaskViewModel by viewModels()

    var _binding : FragmentUpdateTaskBinding? = null
    val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentUpdateTaskBinding.inflate(inflater, container, false)
        binding.args = args
        binding.lifecycleOwner = this
        binding.projectViewModel = projectViewModel
        binding.taskViewModel = taskViewModel
        setHasOptionsMenu(true)
        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.update_task_fragment_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_update_task -> updateTask()
            R.id.menu_delete_task -> confirmItemDelete()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun updateTask() {
        val mTitle = task_title_et.text.toString()
        val mDescription = task_description_et.text.toString()
        val mProject: Project = project_spinner.selectedItem as Project
        if (sharedViewModel.validTaskDataFromInput(mProject, mTitle, mDescription)) {
            val task = Task(args.currentItem.id, mProject.id, mTitle, mDescription)
            taskViewModel.updateTask(task)
            Toast.makeText(
                requireContext(),
                "Successfully updated task '${mTitle}' in project '${mProject.title}'",
                Toast.LENGTH_SHORT
            ).show()
            val action = UpdateTaskFragmentDirections.actionUpdateTaskFragmentToTaskListFragment(mProject.id, mProject.title)
            findNavController().navigate(action)

        } else {
            Toast.makeText(requireContext(), "Please enter details of the task", Toast.LENGTH_SHORT)
                .show()
        }
    }

    private fun confirmItemDelete() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Delete Task")
        builder.setMessage("Do you want to delete task '${args.currentItem.title}' from ${args.currentProject.title} ?")
        builder.setPositiveButton("Yes") {
            //dialogInterface: DialogInterface, i: Int ->
                _,_-> // short form to above
            taskViewModel.deleteTask(args.currentItem)
            val action = UpdateTaskFragmentDirections.actionUpdateTaskFragmentToTaskListFragment(args.currentItem.projectId, args.currentProject.title)
            findNavController().navigate(action)
            Toast.makeText(requireContext(), "Task '${args.currentItem.title}' deleted successfully from ${args.currentProject.title}", Toast.LENGTH_SHORT).show()
        }
        builder.setNegativeButton("No") {
//                dialogInterface: DialogInterface, i: Int ->
                _,_-> //short form to above
        }
        builder.show()
    }


}