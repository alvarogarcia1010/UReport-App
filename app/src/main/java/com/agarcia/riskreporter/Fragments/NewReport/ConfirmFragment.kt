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
import com.agarcia.riskreporter.Database.Models.User

import com.agarcia.riskreporter.R
import com.agarcia.riskreporter.ViewModel.ReportViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.fragment_confirm.*

class ConfirmFragment : Fragment() {

    lateinit var title : String
    lateinit var risk : String
    lateinit var description : String
    lateinit var date : String
    lateinit var image: String
    lateinit var location : String
    lateinit var latitude : String
    lateinit var longitude : String

    lateinit var reportViewModel : ReportViewModel

    lateinit var user : FirebaseUser

    lateinit var name : String
    lateinit var uid : String

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view =inflater.inflate(R.layout.fragment_confirm, container, false)

        (activity as AppCompatActivity).supportActionBar?.subtitle = getString(R.string.step_4)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        reportViewModel = ViewModelProviders.of(this).get(ReportViewModel::class.java)

        user = FirebaseAuth.getInstance().currentUser!!

        user?.let{
            //name = user.displayName!!
            uid = user.uid
        }

        getUserName()

        arguments?.let {
            val safeArgs = ConfirmFragmentArgs.fromBundle(it)
            Log.d("hola", safeArgs.toString())
            title = safeArgs.title
            risk = safeArgs.riskLevel
            description = safeArgs.remark
            date = safeArgs.date
            image = safeArgs.urlImage
            location = safeArgs.location
            latitude = safeArgs.latitude
            longitude = safeArgs.longitude
        }

        fr_confirm_date.text = date
        fr_confirm_title.text = title
        fr_confirm_location.text = location
        fr_confirm_risk.text = risk

        fr_confirm_bt_save.setOnClickListener {
            val report = Report(title,image,description,location,longitude,latitude,risk,fr_confirm_et_measures.text.toString(),name,uid,date,"false")
            Log.d("SIIIUU", report.toString())
            try{
                saveOnFirebase(report)
            }catch (e: Exception){
                Log.d("Codigo", e.message)
            }
        }
    }

    private fun getUserName(){
        val ref = FirebaseDatabase.getInstance().getReference("/users").child(user!!.uid)
        ref.addListenerForSingleValueEvent(object :ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {
            }

            override fun onDataChange(p0: DataSnapshot) {
                    name = p0.getValue(User::class.java)!!.full_name
                    Log.d("NAMEEEE", name)
            }
        })
    }

    private fun saveOnFirebase(report: Report){
        val reference = FirebaseDatabase.getInstance().getReference("/reports")
        val key = reference.push()
        reportViewModel.insertReport(report)
        key.setValue(report)
            .addOnSuccessListener {
                Navigation.findNavController(view!!).navigate(R.id.save_report)
                activity?.finish()
            }
    }
}


