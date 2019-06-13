package com.agarcia.riskreporter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.report_item.view.*

abstract class ReportAdapter internal constructor(context: Context) : RecyclerView.Adapter<ReportAdapter.ViewHolder>(){

    private val inflater = LayoutInflater.from(context)
    private var items = emptyList<Report>()

    abstract fun setClickListenerToReport(holder: ViewHolder, item: Report)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = inflater.inflate(R.layout.report_item, parent,false)

        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position])
        setClickListenerToReport(holder, items[position])
    }

    internal fun changeDataSet(newDataSet : ArrayList<Report>){
        this.items = newDataSet
        notifyDataSetChanged()
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        fun bind (item: Report) = with(itemView){
            rv_reportname.text = item.title
            rv_reportdate.text = item.date
            rv_reportplace.text = item.place
            rv_reportuser.text = item.reporter
        }
    }
}