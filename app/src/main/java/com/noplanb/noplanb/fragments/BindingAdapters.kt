package com.noplanb.noplanb.fragments

import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.BindingAdapter
import androidx.navigation.findNavController
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.noplanb.noplanb.R
import com.noplanb.noplanb.data.models.Project
import com.noplanb.noplanb.fragments.projects.list.ProjectListFragmentDirections

class BindingAdapters {
    companion object{
        @BindingAdapter ("android:navigateToAddTaskFragment")
        @JvmStatic
        fun navigateToAddTaskFragment(view: FloatingActionButton, navigate:Boolean) {
            view.setOnClickListener{
                if (navigate) {
                    view.findNavController().navigate(R.id.action_taskListFragment_to_addTaskFragment)
                }
            }
        }

        @BindingAdapter("android:navigateToAddProjectFragment")
        @JvmStatic
        fun navigateToAddProjectFragment(view: FloatingActionButton, navigate: Boolean) {
            view.setOnClickListener(){
                if (navigate) {
                    view.findNavController().navigate(R.id.action_projectListFragment_to_addProjectFragment)
                }
            }
        }

        @BindingAdapter("android:sendDataAndNavigateToUpdateProjectFragment")
        @JvmStatic
        fun sendDataAndNavigateToUpdateProjectFragment(view: ConstraintLayout, currentItem: Project) {
            val action = ProjectListFragmentDirections.actionProjectListFragmentToUpdateProjectFragment(currentItem)
            view.setOnClickListener {
                view.findNavController().navigate(action)
            }

        }
        @BindingAdapter("android:projectsForSpinner")
        @JvmStatic
        fun getProjectsForSpinner(view: Spinner, projs: List<Project>?) {
            if (projs == null) {
                return
            }
            val spinnerAdapter = ArrayAdapter<Project>(view.context, R.layout.support_simple_spinner_dropdown_item, projs)
            view.adapter = spinnerAdapter

        }
    }
}