package com.demo.PocketStore.ui.dashboard

import android.Manifest

import android.view.View
import android.view.ViewGroup
import android.view.LayoutInflater

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.demo.PocketStore.ui.home.HomeViewModel
import androidx.lifecycle.ViewModelProvider

import com.demo.PocketStore.databinding.FragmentDashboardBinding
import java.util.Comparator
import java.util.Collections

class DashboardFragment : Fragment() {
    private var binding: FragmentDashboardBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val dashboardViewModel = ViewModelProvider(this).get(
            DashboardViewModel::class.java
        )
        binding = FragmentDashboardBinding.inflate(inflater, container, false)
        val root: View = binding!!.root

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

}