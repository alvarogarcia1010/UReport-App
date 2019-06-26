package com.agarcia.riskreporter.Fragments.NewReport


import android.Manifest.permission.CAMERA
import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.navigation.Navigation


import com.agarcia.riskreporter.R
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.fragment_summary.*
import kotlinx.android.synthetic.main.fragment_summary.view.*
import java.text.SimpleDateFormat
import java.util.*

class SummaryFragment : Fragment() {

    lateinit var today: Calendar

    lateinit var date: String
    lateinit var format: SimpleDateFormat

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_summary, container, false)

        (activity as AppCompatActivity).supportActionBar?.subtitle = getString(R.string.step_1)

        val risks = arrayOf("Bajo", "Medio", "Alto")

        val adapter = ArrayAdapter<String>(view.context, android.R.layout.simple_dropdown_item_1line, risks)

        view.fr_summary_autocomplete.setAdapter(adapter)

        today = Calendar.getInstance()

        format = SimpleDateFormat("dd-MM-yyyy")

        date = format.format(today.time)

        view.fr_summary_date.text = date

        view.fr_summary_next.setOnClickListener {
            if(validate()){
                val nextAction = SummaryFragmentDirections.nextAction(
                    view.fr_summary_et_title.text.toString(),
                    view.fr_summary_et_description.text.toString(),
                    view.fr_summary_autocomplete.text.toString(),
                    view.fr_summary_date.text.toString()
                )
                Navigation.findNavController(it).navigate(nextAction)
            } else{
             failedNext()
            }
        }

        view.fr_summary_autocomplete.threshold = 1

        view.fr_summary_autocomplete.onItemClickListener =
            AdapterView.OnItemClickListener { parent, view, position, id ->
                val selectedItem = parent.getItemAtPosition(position).toString()
            }

        view.fr_summary_autocomplete.onFocusChangeListener = View.OnFocusChangeListener { view, b ->
            if (b) {
                view.fr_summary_autocomplete.showDropDown()
            }
        }

        return view
    }
    private fun validate() : Boolean{
        var valid = true

        if(fr_summary_et_title.text.toString().isEmpty()){
            fr_summary_et_title.error = "Campo vacío"
            valid = false
        } else{
            fr_summary_et_title.error = null
        }

        if(fr_summary_et_description.text.toString().isEmpty()){
            fr_summary_et_description.error = "Campo vacío"
            valid = false
        } else{
            fr_summary_et_description.error = null
        }

        if(fr_summary_autocomplete.text.toString().isEmpty()){
            fr_summary_autocomplete.error = "Campo vacío"
            valid = false
        }else{
            fr_summary_autocomplete.error = null
        }
        return valid
    }

    private fun failedNext(){
        Snackbar.make(this.view!!, "Favor llenar todos los campos.", Snackbar.LENGTH_SHORT).show()
        fr_summary_next.isEnabled = true
    }


}

