package com.noplanb.noplanb.fragments


import android.provider.SyncStateContract
import android.widget.Spinner
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import androidx.navigation.NavDirections
import androidx.navigation.findNavController
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.noplanb.noplanb.R
import com.noplanb.noplanb.data.models.Project
import com.noplanb.noplanb.data.models.Task
import com.noplanb.noplanb.data.models.TaskWithProject
import com.noplanb.noplanb.data.viewmodel.SharedViewModel
import com.noplanb.noplanb.fragments.projects.list.ProjectListFragmentDirections
import com.noplanb.noplanb.fragments.projects.list.adapter.ProjectAdapter
import com.noplanb.noplanb.fragments.tasks.list.TaskListFragmentDirections
import com.noplanb.noplanb.fragments.tasks.list.TodayTaskListFragmentDirections
import com.noplanb.noplanb.utils.NpbConstants
import com.noplanb.noplanb.utils.isDueDateOverdue
import java.util.*


class BindingAdapters {
    companion object{
        private val sharedViewModel: SharedViewModel by lazy { SharedViewModel() }

        @BindingAdapter("android:navigateToAddProjectFragment")
        @JvmStatic
        fun navigateToAddProjectFragment(view: FloatingActionButton, navigate: Boolean) {
            view.setOnClickListener(){
                if (navigate) {
                    view.findNavController().navigate(R.id.action_projectListFragment_to_addProjectFragment)
                }
            }
        }

        @BindingAdapter("android:sendTaskToUpdateTaskFragment", "android:fromList")
        @JvmStatic
        fun sendDataAndNavigateToUpdateTaskFragment(view: ConstraintLayout, currentItem: TaskWithProject, fromList: Int) {

            if (fromList == NpbConstants.TASK_LIST_TODAY)  {
                val action = TodayTaskListFragmentDirections.actionTodayTaskListFragmentToUpdateTaskFragment(
                    currentItem, fromList
                )
                view.setOnClickListener {
                    view.findNavController().navigate(action)
                }
            } else {
                val action = TaskListFragmentDirections.actionTaskListFragmentToUpdateTaskFragment(
                    currentItem, fromList
                )
                view.setOnClickListener {
                    view.findNavController().navigate(action)
                }
            }
        }

        @BindingAdapter("android:sendDataAndNavigateToTaskListFragment")
        @JvmStatic
        fun sendDataAndNavigateToTaskListFragment(view: ConstraintLayout, project: Project) {
            val action = ProjectListFragmentDirections.actionProjectListFragmentToTaskListFragment(project.id, project.title)
            view.setOnClickListener {
                view.findNavController().navigate(action)
            }
        }

        @BindingAdapter("android:projectsForSpinner", "android:selectedProjectForSpinner")
        @JvmStatic
        fun getProjectsForSpinner(view: Spinner, projects: List<Project>?, selectedProjectId: Int) {
            if (projects == null) {
                return
            }
            val spinnerAdapter = ProjectAdapter(view.context, R.layout.support_simple_spinner_dropdown_item, projects)
            view.adapter = spinnerAdapter
            setCurrentSelection(view, selectedProjectId)

        }

        private fun setCurrentSelection(spinner: Spinner, selectedProjectId: Int?): Boolean {
            for (index in 0 until spinner.adapter.count) {
                val currentItem = spinner.getItemAtPosition(index) as Project
                if (currentItem.id == selectedProjectId) {
                    spinner.setSelection(index)
                    return true
                }
            }
            return false
        }

        @BindingAdapter("android:showDueDate")
        @JvmStatic
        fun showDueDate(view: TextView, dueDate: Date?)  {
            if (dueDate != null) {
                val calendar = Calendar.getInstance()
                calendar.setTime(dueDate)
                view.setText(
                    "${sharedViewModel.formatDate(calendar)}"
                )
            } else {
                view.text = view.context.getString(R.string.schedule)
            }
        }

        @BindingAdapter("android:showProjectStatus")
        @JvmStatic
        fun showProjectStatus(view: TextView, completedDate: Date?)  {
            if (completedDate != null) {
                view.visibility= TextView.VISIBLE
                view.text = view.context.getString(R.string.completed)
            } else {
                view.visibility= TextView.GONE
            }
        }

        @BindingAdapter("android:showTaskStatus")
        @JvmStatic
        fun showTaskStatus(view: TextView, task:Task)  {
            if (task.completedDate != null) {
                view.visibility = TextView.VISIBLE
                view.text = view.context.getString(R.string.completed)
                view.setTextColor( ContextCompat.getColor(view.context, R.color.colorCompleted))
            } else if (isDueDateOverdue(task.dueDate)) {
                view.visibility = TextView.VISIBLE
                view.text = view.context.getString(R.string.overdue)
                view.setTextColor( ContextCompat.getColor(view.context, R.color.colorOverdue))

            } else {
                view.visibility= TextView.GONE
            }
        }

    }
}