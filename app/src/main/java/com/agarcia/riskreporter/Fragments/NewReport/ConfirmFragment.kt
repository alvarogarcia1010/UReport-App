package com.agarcia.riskreporter.Fragments.NewReport


import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import com.agarcia.riskreporter.Database.Models.Report

import com.agarcia.riskreporter.R
import com.agarcia.riskreporter.ViewModel.ReportViewModel
import kotlinx.android.synthetic.main.fragment_confirm.*

class ConfirmFragment : Fragment() {

    lateinit var title : String
    lateinit var risk : String
    lateinit var description : String
    lateinit var date : String
    lateinit var image: String
    lateinit var location : String

    lateinit var reportViewModel : ReportViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view =inflater.inflate(R.layout.fragment_confirm, container, false)

        (activity as AppCompatActivity).supportActionBar?.subtitle = getString(R.string.step_4)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        reportViewModel = ViewModelProviders.of(this).get(ReportViewModel::class.java)


        arguments?.let {
            val safeArgs = ConfirmFragmentArgs.fromBundle(it)
            Log.d("hola", safeArgs.toString())
            title = safeArgs.title
            risk = safeArgs.riskLevel
            description = safeArgs.remark
            date = safeArgs.date
            image = safeArgs.urlImage
            location = safeArgs.location
        }

        val report = Report(
            title,
            image,
            description,
            location,
            risk,
            fr_confirm_location.text.toString(),
            "Luis Castillo",
            date,
            "Pendiente"
            )

            try{
                reportViewModel.insertReport(report)
                Navigation.findNavController(view).navigate(R.id.save_report)
            }catch (e: Exception){
                Log.d("Codigo", e.message)
            }

        fr_confirm_bt_save.setOnClickListener {
            Navigation.findNavController(view).navigate(R.id.save_report)
            activity?.finish()
        }

    }

}
