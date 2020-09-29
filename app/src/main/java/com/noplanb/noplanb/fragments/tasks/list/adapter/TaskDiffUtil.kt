package com.noplanb.noplanb.fragments.tasks.list.adapter

import androidx.recyclerview.widget.DiffUtil
import com.noplanb.noplanb.data.models.TaskWithProject

class TaskDiffUtil(private val oldList: List<TaskWithProject>, private val newList: List<TaskWithProject> ): DiffUtil.Callback() {
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
        val oldTask = oldList[oldItemPosition].task
        val newTask = newList[newItemPosition].task
        return (oldTask.id == newTask.id &&
                oldTask.title == newTask.title &&
                oldTask.description == newTask.description &&
                oldTask.projectId == newTask.projectId &&
                oldTask.completedDate == newTask.completedDate &&
                oldTask.dueDate == newTask.dueDate)

    }
}