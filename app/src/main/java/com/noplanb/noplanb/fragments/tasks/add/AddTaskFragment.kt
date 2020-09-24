package com.noplanb.noplanb.fragments.tasks.add

import android.app.DatePickerDialog
import android.os.Build
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.annotation.RequiresApi
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
import com.noplanb.noplanb.databinding.FragmentAddTaskBinding
import com.noplanb.noplanb.utils.NpbConstants
import com.noplanb.noplanb.utils.dueDateToSave
import kotlinx.android.synthetic.main.fragment_add_task.*
import java.util.*

class AddTaskFragment : Fragment() {
    private val args by navArgs<AddTaskFragmentArgs> ()
    private var _binding: FragmentAddTaskBinding? = null
    private val binding get() = _binding!!
    private val projectViewModel: ProjectViewModel by viewModels()
    private val taskViewModel: TaskViewModel by viewModels()
    private val sharedViewModel: SharedViewModel by lazy { SharedViewModel() }

    private var saveDay: Int =0
    private var saveMonth: Int = 0
    private var saveYear: Int =0

    private var mDay: Int =0
    private var mMonth: Int = 0
    private var mYear: Int =0
    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View? {
        _binding = FragmentAddTaskBinding.inflate(inflater, container, false)
        binding.args = args
        binding.lifecycleOwner = this
        binding.projectViewModel = projectViewModel
        binding.taskViewModel = taskViewModel

        setShowCalendarDate()
        binding.dueDateBtn.setOnClickListener{
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                val dueDatePicker = DatePickerDialog(requireContext())
                dueDatePicker.setOnDateSetListener { view, year, month, dayOfMonth -> setDueDate(year, month, dayOfMonth) }
                dueDatePicker.updateDate(mYear, mMonth, mDay)
                dueDatePicker.show()
            } else { // find a way for my phone  api <=21
                val dateSetListener  = DatePickerDialog.OnDateSetListener{ view, year, month, dayOfMonth -> setDueDate(year, month, dayOfMonth) }
                val dueDatePicker = DatePickerDialog(requireContext(),dateSetListener, mYear, mMonth, mDay)
                dueDatePicker.show()
            }
        }
        binding.clearDueDateBtn.setOnClickListener{
            clearDueDate()
        }

        setHasOptionsMenu(true)
        return binding.root
    }

    private fun setShowCalendarDate() {
        val calendar: Calendar = Calendar.getInstance()
        mYear = calendar.get(Calendar.YEAR)
        mMonth = calendar.get(Calendar.MONTH)
        mDay = calendar.get(Calendar.DAY_OF_MONTH)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.add_task_fragment_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId === R.id.menu_save_task) {
            saveTaskToDb()
        }

        return super.onOptionsItemSelected(item)
    }
    private fun setDueDate(year: Int, month: Int, dayOfMonth: Int) {
        binding.dueDateBtn.text = "${sharedViewModel.formatDate(year,month+1,dayOfMonth)}"
        saveDay = dayOfMonth
        saveMonth = month
        saveYear = year
        mDay = dayOfMonth
        mMonth = month
        mYear = year
    }

    private fun clearDueDate() {
        binding.dueDateBtn.text = "Schedule"
        saveDay=0
        saveMonth=0
        saveYear=0
        setShowCalendarDate()

    }
    private fun saveTaskToDb() {
        val mTitle = task_title_et.text.toString()
        val mDescription = task_description_et.text.toString()
        val mProject: Project = project_spinner.selectedItem as Project
        if (sharedViewModel.validTaskDataFromInput(mTitle)) {

            val task = Task(0, mProject.id, mTitle, mDescription, dueDateToSave(saveYear, saveMonth, saveDay), null)
            taskViewModel.insertTask(task)
            Toast.makeText(
                requireContext(),
                "Successfully saved task '${mTitle}' in project '${mProject.title}'",
                Toast.LENGTH_SHORT
            ).show()
            if (args.fromList == NpbConstants.TASK_LIST_TODAY) {
                val action = AddTaskFragmentDirections.actionAddTaskFragmentToTodayTaskListFragment()
                findNavController().navigate(action)
            } else {
                val action = AddTaskFragmentDirections.actionAddTaskFragmentToTaskListFragment(mProject.id, mProject.title)
                findNavController().navigate(action)
            }


        } else {
            Toast.makeText(requireContext(), "Please enter the Title of the task", Toast.LENGTH_SHORT).show()
        }

    }
    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}