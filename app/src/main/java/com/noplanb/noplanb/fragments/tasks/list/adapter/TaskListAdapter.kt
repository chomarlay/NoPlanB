package com.noplanb.noplanb.fragments.tasks.list.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.noplanb.noplanb.data.models.Project
import com.noplanb.noplanb.data.models.ProjectWithTasks
import com.noplanb.noplanb.data.models.Task
import com.noplanb.noplanb.databinding.ProjectRowBinding
import com.noplanb.noplanb.databinding.TaskRowBinding
import com.noplanb.noplanb.fragments.projects.list.adapter.ProjectListAdapter

class TaskListAdapter(): RecyclerView.Adapter<TaskListAdapter.MyViewHolder>() {
    var projectWithTasks: ProjectWithTasks? = null
    class MyViewHolder(private val binding: TaskRowBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(project: Project?, task: Task){
            binding.project = project
            binding.task = task
        }
        companion object{
            fun from (parent: ViewGroup): MyViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = TaskRowBinding.inflate(layoutInflater)
                return MyViewHolder(binding)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskListAdapter.MyViewHolder {
        return TaskListAdapter.MyViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: TaskListAdapter.MyViewHolder, position: Int) {
        var tasks = projectWithTasks?.tasks
        if (tasks != null) {
            var currentItem = tasks[position]
            holder.bind(projectWithTasks?.project, currentItem)
        }

    }

    override fun getItemCount(): Int {
        if (projectWithTasks != null && projectWithTasks!!.tasks!= null) {
            return projectWithTasks!!.tasks.size
        }
        return 0
    }

    fun setData(projectWithTasks: ProjectWithTasks?) {
        this.projectWithTasks = projectWithTasks
        notifyDataSetChanged()
    }

}