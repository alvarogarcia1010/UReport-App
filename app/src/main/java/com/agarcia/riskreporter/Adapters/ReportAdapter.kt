package com.agarcia.riskreporter.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.agarcia.riskreporter.Database.Report
import com.agarcia.riskreporter.R
import com.bumptech.glide.Glide
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

    internal fun changeDataSet(newDataSet : List<Report>){
        this.items = newDataSet
        notifyDataSetChanged()
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        fun bind (item: Report) = with(itemView){
            Glide.with(itemView).load(item.url_image).into(rv_image)
            //rv_image.setImageResource(R.drawable.logo_a)
            rv_reportname.text = item.title
            rv_reportdate.text = item.datetime
            rv_reportplace.text = item.detailed_location
            rv_status.text = item.status
            rv_reportrisk.text = item.risk_level

            if(item.risk_level == "Bajo"){
                rv_reportrisk_logo.setColorFilter(ContextCompat.getColor(context, R.color.greenRisk))
                rv_reportrisk.setTextColor(ContextCompat.getColor(context, R.color.greenRisk))
            }

            if(item.risk_level == "Medio"){
                rv_reportrisk_logo.setColorFilter(ContextCompat.getColor(context, R.color.yellowRisk))
                rv_reportrisk.setTextColor(ContextCompat.getColor(context, R.color.yellowRisk))
            }

            if(item.risk_level == "Alto"){
                rv_reportrisk_logo.setColorFilter(ContextCompat.getColor(context, R.color.redRisk))
                rv_reportrisk.setTextColor(ContextCompat.getColor(context, R.color.redRisk))
            }

            rv_status.setTextColor(ContextCompat.getColor(context, R.color.greyStatus))
        }
    }
}