package com.noplanb.noplanb.fragments.tasks.update

import android.app.AlertDialog
import android.app.DatePickerDialog
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
import com.noplanb.noplanb.utils.NpbConstants
import com.noplanb.noplanb.utils.dueDateToSave
import kotlinx.android.synthetic.main.fragment_add_task.*
import java.util.*

class UpdateTaskFragment : Fragment() {
    private val args by navArgs<UpdateTaskFragmentArgs> ()
    private val projectViewModel: ProjectViewModel by viewModels()
    private val sharedViewModel: SharedViewModel by lazy{ SharedViewModel() }
    private val taskViewModel: TaskViewModel by viewModels()
    private var saveDay: Int =0
    private var saveMonth: Int = 0
    private var saveYear: Int =0

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
        var calendar: Calendar = Calendar.getInstance()

        if (args.currentItem.task.dueDate != null) {
            calendar.setTime(args.currentItem.task.dueDate!!)
            saveDay = calendar.get(Calendar.DAY_OF_MONTH)
            saveMonth = calendar.get(Calendar.MONTH)
            saveYear = calendar.get(Calendar.YEAR)
            setDueDate(
                saveYear,
                saveMonth,
                saveDay,
            )
        }

        binding.dueDateBtn.setOnClickListener {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                val dueDatePicker = DatePickerDialog(requireContext())
                dueDatePicker.setOnDateSetListener { view, year, month, dayOfMonth ->
                    setDueDate(
                        year,
                        month,
                        dayOfMonth
                    )
                }
                dueDatePicker.updateDate(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH))
                dueDatePicker.show()
            } else {
                val dateSetListener  = DatePickerDialog.OnDateSetListener{ view, year, month, dayOfMonth -> setDueDate(year, month, dayOfMonth) }
                val dueDatePicker = DatePickerDialog(requireContext(),dateSetListener, calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH))
                dueDatePicker.show()
            }
        }
        binding.clearDueDateBtn.setOnClickListener{
            clearDueDate()
            calendar = Calendar.getInstance()
        }
        setHasOptionsMenu(true)
        return binding.root
    }
    private fun setDueDate(year: Int, month: Int, dayOfMonth: Int) {
        binding.dueDateBtn.text = "${sharedViewModel.formatDate(year,month+1,dayOfMonth)}"
        saveDay = dayOfMonth
        saveMonth = month
        saveYear = year
    }

    private fun clearDueDate() {
        binding.dueDateBtn.text = "Schedule"
        saveDay=0
        saveMonth=0
        saveYear=0
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.update_task_fragment_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_update_task -> updateTask()
            R.id.menu_delete_task -> confirmItemDelete()
            R.id.menu_complete_task-> confirmCompleteTask()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun confirmCompleteTask() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Mark Task as completed")
        builder.setMessage("Do you want to mark task '${args.currentItem.task.title}' from ${args.currentItem.project.title} as completed?")
        builder.setPositiveButton("Yes") {
            //dialogInterface: DialogInterface, i: Int ->
                _,_-> // short form to above
            run {val task = args.currentItem.task
            task.completedDate = Date()
            taskViewModel.updateTask(task)}

            if(args.fromList == NpbConstants.TASK_LIST_TODAY) {
                val action = UpdateTaskFragmentDirections.actionUpdateTaskFragmentToTodayTaskListFragment()
                findNavController().navigate(action)
            } else {
                val action =
                    UpdateTaskFragmentDirections.actionUpdateTaskFragmentToTaskListFragment(
                        args.currentItem.task.projectId,
                        args.currentItem.project.title
                    )
                findNavController().navigate(action)
            }
            Toast.makeText(requireContext(), "Task '${args.currentItem.task.title}' from ${args.currentItem.project.title} has been mark as completed ", Toast.LENGTH_SHORT).show()
        }
        builder.setNegativeButton("No") {
//                dialogInterface: DialogInterface, i: Int ->
                _,_-> //short form to above
        }
        builder.show()
    }

    private fun updateTask() {
        val mTitle = task_title_et.text.toString()
        val mDescription = task_description_et.text.toString()
        val mProject: Project = project_spinner.selectedItem as Project

        if (sharedViewModel.validTaskDataFromInput(mTitle)) {
            val task = Task(args.currentItem.task.id, mProject.id, mTitle, mDescription, dueDateToSave(saveYear, saveMonth, saveDay), args.currentItem.task.completedDate)
            taskViewModel.updateTask(task)
            Toast.makeText(
                requireContext(),
                "Successfully updated task '${mTitle}' in project '${mProject.title}'",
                Toast.LENGTH_SHORT
            ).show()
            if(args.fromList == NpbConstants.TASK_LIST_TODAY) {
                val action = UpdateTaskFragmentDirections.actionUpdateTaskFragmentToTodayTaskListFragment()
                findNavController().navigate(action)
            } else {
                val action =
                    UpdateTaskFragmentDirections.actionUpdateTaskFragmentToTaskListFragment(
                        mProject.id,
                        mProject.title
                    )
                findNavController().navigate(action)
            }

        } else {
            Toast.makeText(requireContext(), "Please enter the Title of the task", Toast.LENGTH_SHORT)
                .show()
        }
    }

    private fun confirmItemDelete() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Delete Task")
        builder.setMessage("Do you want to delete task '${args.currentItem.task.title}' from ${args.currentItem.project.title} ?")
        builder.setPositiveButton("Yes") {
            //dialogInterface: DialogInterface, i: Int ->
                _,_-> // short form to above
            taskViewModel.deleteTask(args.currentItem.task)

            if(args.fromList == NpbConstants.TASK_LIST_TODAY) {
                val action = UpdateTaskFragmentDirections.actionUpdateTaskFragmentToTodayTaskListFragment()
                findNavController().navigate(action)
            } else {
                val action =
                    UpdateTaskFragmentDirections.actionUpdateTaskFragmentToTaskListFragment(
                        args.currentItem.task.projectId,
                        args.currentItem.project.title
                    )
                findNavController().navigate(action)
            }
            Toast.makeText(requireContext(), "Task '${args.currentItem.task.title}' deleted successfully from ${args.currentItem.project.title}", Toast.LENGTH_SHORT).show()
        }
        builder.setNegativeButton("No") {
//                dialogInterface: DialogInterface, i: Int ->
                _,_-> //short form to above
        }
        builder.show()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}