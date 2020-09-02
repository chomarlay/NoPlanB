package com.noplanb.noplanb.fragments.projects.list.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.noplanb.noplanb.data.models.Project
import com.noplanb.noplanb.databinding.ProjectRowBinding

class ProjectListAdapter(): RecyclerView.Adapter<ProjectListAdapter.MyViewHolder>() {
    var projectList = emptyList<Project>()
    class MyViewHolder(private val binding: ProjectRowBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(project:Project){
            binding.project = project
        }
        companion object{
            fun from (parent: ViewGroup): MyViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ProjectRowBinding.inflate(layoutInflater)
                return MyViewHolder(binding)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        var currentItem = projectList[position]
        holder.bind(currentItem)
    }

    override fun getItemCount(): Int {
        return projectList.size
    }

    fun setData(projectList: List<Project>) {
        this.projectList = projectList
        notifyDataSetChanged()
    }
}