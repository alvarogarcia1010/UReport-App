package com.agarcia.riskreporter.Fragments


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation

import com.agarcia.riskreporter.R
import kotlinx.android.synthetic.main.fragment_report_list.*

class ReportListFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        val view = inflater.inflate(R.layout.fragment_report_list, container, false)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val fab: View = view.findViewById(R.id.fab)
        val btnDetails: View = view.findViewById(R.id.list_btn_next_action)
        val btnDetails1: View = view.findViewById(R.id.list_btn_next_action1)
        val btnDetails2: View = view.findViewById(R.id.list_btn_next_action2)

        fab.setOnClickListener { view ->
            Navigation.findNavController(view).navigate(R.id.new_action)
        }

        btnDetails.setOnClickListener {
            val nextAction = ReportListFragmentDirections.nextAction()
            Navigation.findNavController(it).navigate(nextAction)
        }

        btnDetails1.setOnClickListener {
            val nextAction = ReportListFragmentDirections.nextAction()
            Navigation.findNavController(it).navigate(nextAction)
        }

        btnDetails2.setOnClickListener {
            val nextAction = ReportListFragmentDirections.nextAction()
            Navigation.findNavController(it).navigate(nextAction)
        }
    }
}
