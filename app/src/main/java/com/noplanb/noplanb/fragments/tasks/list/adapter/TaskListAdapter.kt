package com.noplanb.noplanb.fragments.tasks.list.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.noplanb.noplanb.data.models.TaskWithProject
import com.noplanb.noplanb.databinding.TaskRowBinding
import com.noplanb.noplanb.utils.NpbConstants
import com.noplanb.noplanb.utils.isDueDateOverdue
import java.util.*


class TaskListAdapter(): RecyclerView.Adapter<TaskListAdapter.MyViewHolder>() {
    var tasksWithProject: List<TaskWithProject>? = null
    var fromList: Int = NpbConstants.TASK_LIST_TODAY
    class MyViewHolder(private val binding: TaskRowBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(taskWithProject: TaskWithProject, fromList: Int, isOverdue: Boolean){
            binding.taskWithProject = taskWithProject
            binding.fromList = fromList
            if (isOverdue) {
                binding.overdueTv.visibility = TextView.VISIBLE
            } else {
                binding.overdueTv.visibility = TextView.GONE
            }
        }
        companion object{
            fun from(parent: ViewGroup): MyViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = TaskRowBinding.inflate(layoutInflater, parent, false)
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
            holder.bind(currentItem, fromList, isDueDateOverdue(currentItem.task.dueDate))
        }

    }

    override fun getItemCount(): Int {
        if (tasksWithProject != null) {
            return tasksWithProject!!.size
        } else {
            return 0
        }
    }

    fun setData(tasksWithProject: List<TaskWithProject>, fromList: Int) {
        this.tasksWithProject = tasksWithProject
        this.fromList = fromList
        notifyDataSetChanged()
    }

}