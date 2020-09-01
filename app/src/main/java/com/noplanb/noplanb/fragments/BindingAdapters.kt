package com.noplanb.noplanb.fragments

import androidx.databinding.BindingAdapter
import androidx.navigation.findNavController
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.noplanb.noplanb.R

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
    }
}