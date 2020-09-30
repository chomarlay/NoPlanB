package com.noplanb.noplanb.fragments.projects.list.adapter

import androidx.recyclerview.widget.DiffUtil
import com.noplanb.noplanb.data.models.ProjectWithTasks


class ProjectDiffUtil (private val oldList: List<ProjectWithTasks>, private val newList: List<ProjectWithTasks> ): DiffUtil.Callback() {
    override fun getOldListSize(): Int {
        return oldList.size
    }

    override fun getNewListSize(): Int {
        return newList.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition] === newList[newItemPosition]
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldProject = oldList[oldItemPosition].project
        val newProject = newList[newItemPosition].project
        return (oldProject.id == newProject.id &&
                oldProject.title == newProject.title &&
                oldProject.description == newProject.description &&
                oldList[oldItemPosition].tasks.size == newList[newItemPosition].tasks.size
                )

    }
}