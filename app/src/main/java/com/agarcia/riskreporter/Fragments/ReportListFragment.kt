package com.agarcia.riskreporter.Fragments


import android.app.Activity
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
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
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.fragment_report_list.view.*

class ReportListFragment : Fragment() {

    lateinit var reportViewModel: ReportViewModel
    lateinit var adapter: ReportAdapter
    private lateinit var reportsRef: DatabaseReference
    private lateinit var uid : String
    private var user : FirebaseUser? = null
    private var calledAlready = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        setHasOptionsMenu(true)

        val view = inflater.inflate(R.layout.fragment_report_list, container, false)

        reportsRef = FirebaseDatabase.getInstance().getReference("reports")
        reportsRef.keepSynced(true)

        user = FirebaseAuth.getInstance().currentUser
        uid = ""
        user?.let{
            uid = user?.uid!!
        }

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
        adapter = object : ReportAdapter(view.context){
            override fun setClickListenerToReport(holder: ViewHolder, item: Report) {
                holder.itemView.setOnClickListener {
                    val nextAction = ReportListFragmentDirections.nextAction(item)
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
            getReport()
            swipeRefreshLayout.isRefreshing = false

        }

        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary)

//        reportViewModel.allReports.observe(this, Observer { reports ->
//            reports?.let{ adapter.changeDataSet(it)}
//        })

        getReport()

    }

    private fun getReport(){

        val reportsListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                loadReportList(dataSnapshot)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.d("Reports", "loadPost:onCancelled", databaseError.toException())
            }
        }

        reportsRef
            .orderByChild("reporter_id")
            .equalTo(uid)
            .addValueEventListener(reportsListener)

    }

    private fun loadReportList(dataSnapshot: DataSnapshot){

        val reportsList = mutableListOf<Report>()

        for (postSnapshot in dataSnapshot.children) {
            val report = postSnapshot.getValue(Report::class.java)
            report?.let { reportsList.add(it) }
        }

        Log.d("Reports", reportsList.toString())

        adapter.changeDataSet(reportsList)
    }

    companion object{
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1
    }

}
