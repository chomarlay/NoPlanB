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
import kotlinx.android.synthetic.main.fragment_add_task.*
import java.util.*

class AddTaskFragment : Fragment() {
    private val args by navArgs<AddTaskFragmentArgs> ()
    private var _binding: FragmentAddTaskBinding? = null
    private val binding get() = _binding!!
    private val projectViewModel: ProjectViewModel by viewModels()
    private val taskViewModel: TaskViewModel by viewModels()
    private val sharedViewModel: SharedViewModel by lazy { SharedViewModel() }
//    private var dueDatePicker: DatePickerDialog? = null
    private var myDay: Int =0
    private var myMonth: Int = 0
    private var myYear: Int =0
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

        val calendar: Calendar = Calendar.getInstance()
        myDay = calendar.get(Calendar.DAY_OF_MONTH)
        myMonth = calendar.get(Calendar.MONTH)
        myYear = calendar.get(Calendar.YEAR)

        binding.dueDateBtn.setOnClickListener{

            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                val dueDatePicker = DatePickerDialog(requireContext())
                dueDatePicker.setOnDateSetListener { view, year, month, dayOfMonth -> setDueDate(year, month, dayOfMonth) }
                dueDatePicker.updateDate(myYear,myMonth,myDay)
                dueDatePicker.show()
            } else {
//                val dueDatePicker = DatePickerDialog(requireContext())
//                dueDatePicker.setOnDateSetListener { view, year, month, dayOfMonth -> binding.dueDateBtn.text = "${dayOfMonth}-${month+1}-${year}" }
//                dueDatePicker.updateDate(year,month,day)
//                dueDatePicker.show()
                Toast.makeText(requireContext(), "PICK DATE", Toast.LENGTH_SHORT).show()
            }
            setHasOptionsMenu(true)
        }
        return binding.root
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
        binding.dueDateBtn.text = "${dayOfMonth}-${month+1}-${year}"
        myDay = dayOfMonth
        myMonth = month
        myYear = year
//        binding.dueDateEt = "${dayOfMonth}-${month+1}-${year}"
        binding.dueDateEt.setText("${myDay}/${myMonth+1}/${myYear}")

    }
    private fun saveTaskToDb() {
        val mTitle = task_title_et.text.toString()
        val mDescription = task_description_et.text.toString()
        val mProject: Project = project_spinner.selectedItem as Project
        if (sharedViewModel.validTaskDataFromInput(mProject, mTitle, mDescription)) {
            val calendar: Calendar = Calendar.getInstance()
            calendar.set(myYear, myMonth, myDay)

            val task = Task(0, mProject.id, mTitle, mDescription, calendar.time)
            taskViewModel.insertTask(task)
            Toast.makeText(
                requireContext(),
                "Successfully saved task '${mTitle}' in project '${mProject.title}'",
                Toast.LENGTH_SHORT
            ).show()
            val action = AddTaskFragmentDirections.actionAddTaskFragmentToTaskListFragment(mProject.id, mProject.title)
            findNavController().navigate(action)

        } else {
            Toast.makeText(requireContext(), "Please enter details of the task", Toast.LENGTH_SHORT)
                .show()
        }

    }

}