package com.noplanb.noplanb.fragments.projects.list.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.noplanb.noplanb.data.models.ProjectWithTasks
import com.noplanb.noplanb.databinding.ProjectRowBinding

class ProjectListAdapter(): RecyclerView.Adapter<ProjectListAdapter.MyViewHolder>() {
    var projectList = emptyList<ProjectWithTasks>()
    class MyViewHolder(private val binding: ProjectRowBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(project:ProjectWithTasks){
            binding.projectWithTasks = project
            val tasksCount = project.tasks.filter { t->t.completedDate == null }.count()
            binding.tasksCount = tasksCount.toString()
        }

        companion object{
            fun from (parent: ViewGroup): MyViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ProjectRowBinding.inflate(layoutInflater, parent, false)
                return MyViewHolder(binding)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = projectList[position]
        holder.bind(currentItem)
    }

    override fun getItemCount(): Int {
        return projectList.size
    }

    fun setData(projectList: List<ProjectWithTasks>) {
        val projectDiffUtil = ProjectDiffUtil(this.projectList, projectList)
        val projectDiffResult = DiffUtil.calculateDiff(projectDiffUtil)
        this.projectList = projectList
//        notifyDataSetChanged()  // this will not work with the wasabeef animation,  changing to diffUtils works
        projectDiffResult.dispatchUpdatesTo(this)

    }
}