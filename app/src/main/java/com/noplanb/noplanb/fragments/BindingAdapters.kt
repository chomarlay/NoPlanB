package com.noplanb.noplanb.fragments


import android.widget.Spinner
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.BindingAdapter
import androidx.navigation.findNavController
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.noplanb.noplanb.R
import com.noplanb.noplanb.data.models.Project
import com.noplanb.noplanb.data.models.Task
import com.noplanb.noplanb.fragments.projects.list.ProjectListFragmentDirections
import com.noplanb.noplanb.fragments.projects.list.adapter.ProjectAdapter
import com.noplanb.noplanb.fragments.tasks.list.TaskListFragmentDirections
import java.util.*


class BindingAdapters {
    companion object{

        @BindingAdapter("android:navigateToAddProjectFragment")
        @JvmStatic
        fun navigateToAddProjectFragment(view: FloatingActionButton, navigate: Boolean) {
            view.setOnClickListener(){
                if (navigate) {
                    view.findNavController().navigate(R.id.action_projectListFragment_to_addProjectFragment)
                }
            }
        }

        @BindingAdapter(value=["android:sendTaskToUpdateTaskFragment", "android:sendProjectToUpdateTaskFragment"], requireAll = false )
        @JvmStatic
        fun sendDataAndNavigateToUpdateTaskFragment(view: ConstraintLayout, currentItem: Task, currentProject: Project) {
            val action = TaskListFragmentDirections.actionTaskListFragmentToUpdateTaskFragment(currentItem, currentProject)
            view.setOnClickListener {
                view.findNavController().navigate(action)
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

        @BindingAdapter(value = ["android:projectsForSpinner", "android:selectedProjectForSpinner"], requireAll = false)
        @JvmStatic
        fun getProjectsForSpinner(view: Spinner, projects: List<Project>?, selectedProjectId: Int) {
            if (projects == null) {
                return
            }
//            val spinnerAdapter = ArrayAdapter<Project>(view.context, R.layout.support_simple_spinner_dropdown_item, projs)
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
                    "${calendar.get(Calendar.DAY_OF_MONTH)}-${calendar.get(Calendar.MONTH) + 1}-${
                        calendar.get(
                            Calendar.YEAR
                        )
                    }"
                )
            } else {
                view.setText ("Schedule")
            }
        }
    }
}