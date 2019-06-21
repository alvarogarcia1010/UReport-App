package com.agarcia.riskreporter.Fragments


import android.app.Activity
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager

import com.agarcia.riskreporter.R
import com.agarcia.riskreporter.Adapters.ReportAdapter
import com.agarcia.riskreporter.Database.Models.Report
import com.agarcia.riskreporter.ViewModel.ReportViewModel
import kotlinx.android.synthetic.main.fragment_report_list.view.*

class ReportListFragment : Fragment() {

    lateinit var reportViewModel: ReportViewModel


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        setHasOptionsMenu(true)

        val view = inflater.inflate(R.layout.fragment_report_list, container, false)

        init(view)

        return view
    }

    fun init(view:View){

        if(ActivityCompat.checkSelfPermission(activity as Activity,
                android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(activity as Activity,
                arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), LOCATION_PERMISSION_REQUEST_CODE)
        }

        reportViewModel = ViewModelProviders.of(this).get(ReportViewModel::class.java)
        var adapter = object : ReportAdapter(view.context){
            override fun setClickListenerToReport(holder: ViewHolder, item: Report) {
                holder.itemView.setOnClickListener {
                    val nextAction = ReportListFragmentDirections.nextAction()
                    Navigation.findNavController(it).navigate(nextAction)
                }
            }
        }

        val recyclerView = view.recyclerview
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(view.context)

        val fab : View = view.findViewById(R.id.fab)

        fab.setOnClickListener {
            Navigation.findNavController(view).navigate(R.id.new_action)
//
//            val intent = Intent(it.context, ReportActivity::class.java)
//            startActivity(intent)
        }

        reportViewModel.allReports.observe(this, Observer { reports ->
            reports?.let{ adapter.changeDataSet(it)}
        })
    }

    companion object{
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1
    }

}
