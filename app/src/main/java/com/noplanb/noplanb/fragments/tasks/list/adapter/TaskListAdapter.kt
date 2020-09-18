package com.noplanb.noplanb.fragments.tasks.list.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.noplanb.noplanb.data.models.Project
import com.noplanb.noplanb.data.models.ProjectWithTasks
import com.noplanb.noplanb.data.models.Task
import com.noplanb.noplanb.data.models.TaskWithProject
import com.noplanb.noplanb.databinding.TaskRowBinding


class TaskListAdapter(): RecyclerView.Adapter<TaskListAdapter.MyViewHolder>() {
//    var projectWithTasks: ProjectWithTasks? = null
        var tasksWithProject: List<TaskWithProject>? = null
    class MyViewHolder(private val binding: TaskRowBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind( taskWithProject:TaskWithProject){
            binding.project = taskWithProject.project
            binding.task = taskWithProject.task
        }
        companion object{
            fun from (parent: ViewGroup): MyViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = TaskRowBinding.inflate(layoutInflater)
                return MyViewHolder(binding)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val tasks = tasksWithProject
        if (tasks != null) {
            val currentItem = tasks[position]
            holder.bind(currentItem)
        }

    }

    override fun getItemCount(): Int {
        if (tasksWithProject != null) {
            return tasksWithProject!!.size
        } else {
            return 0
        }
    }

//    fun setData(projectWithTasks: ProjectWithTasks?) {
//        this.projectWithTasks = projectWithTasks
//        notifyDataSetChanged()
//    }

    fun setData(tasksWithProject: List<TaskWithProject>) {
        this.tasksWithProject = tasksWithProject
        notifyDataSetChanged()
    }

}