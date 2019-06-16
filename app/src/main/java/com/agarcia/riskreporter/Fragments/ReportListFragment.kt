package com.agarcia.riskreporter.Fragments


import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager

import com.agarcia.riskreporter.R
import com.agarcia.riskreporter.Report
import com.agarcia.riskreporter.Activities.ReportActivity
import com.agarcia.riskreporter.Adapters.ReportAdapter
import kotlinx.android.synthetic.main.fragment_report_list.view.*

class ReportListFragment : Fragment() {

    var report = Report("Panal de Abejas","Juan Castro","A-33","20/10/2019")
    var report1 = Report("Gotera en Cielo Falso" , "Karla Acevedo", "B-13", "09/04/19")

    val mem : ArrayList<Report> = ArrayList()


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        setHasOptionsMenu(true)

        val view = inflater.inflate(R.layout.fragment_report_list, container, false)

        init(view)

        return view
    }

    fun init(view:View){
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

        addReports()

        adapter.changeDataSet(mem)

    }

    fun addReports(){
        mem.add(report)
        mem.add(report1)
    }

}
