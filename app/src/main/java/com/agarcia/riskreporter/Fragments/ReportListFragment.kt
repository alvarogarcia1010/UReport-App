package com.agarcia.riskreporter.Fragments


import android.app.Activity
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout

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

        val fab : View = view.findViewById(R.id.fab)
        val swipeRefreshLayout: SwipeRefreshLayout = view.findViewById(R.id.swipe_refresh_layout)
        val recyclerView = view.recyclerview

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

        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(view.context)


        fab.setOnClickListener {
            Navigation.findNavController(view).navigate(R.id.new_action)
        }

        swipeRefreshLayout.setOnRefreshListener{
            Toast.makeText(activity, "Estoy refrescando los datos", Toast.LENGTH_SHORT).show()
            swipeRefreshLayout.isRefreshing = false

        }

        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary)

        reportViewModel.allReports.observe(this, Observer { reports ->
            reports?.let{ adapter.changeDataSet(it)}
        })
    }

    companion object{
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1
    }

}
