package com.noplanb.noplanb.fragments.projects.update

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.noplanb.noplanb.R
import com.noplanb.noplanb.databinding.FragmentUpdateProjectBinding

class UpdateProjectFragment : Fragment() {
    var _binding : FragmentUpdateProjectBinding? = null
    val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentUpdateProjectBinding.inflate(inflater, container, false)
        return binding.root
        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_update_project, container, false)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}