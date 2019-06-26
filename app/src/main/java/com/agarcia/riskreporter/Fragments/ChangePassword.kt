package com.agarcia.riskreporter.Fragments


import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.Navigation
import com.agarcia.riskreporter.R
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.fragment_change_password.*
import kotlinx.android.synthetic.main.fragment_change_password.view.*

class ChangePassword : Fragment() {

    lateinit var user: FirebaseUser

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_change_password, container, false)

        view.btn_change_password.setOnClickListener {
            if (validate()) {
                val user = FirebaseAuth.getInstance().currentUser!!
                val newPassword = new_passwordconfirm.text.toString()

                user.updatePassword(newPassword)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Log.d("pass3", "Contraseña actualizada")
                            val nextAction = ChangePasswordDirections.nextActionPassword()
                            Navigation.findNavController(it).navigate(nextAction)
                        }else{
                            Toast.makeText(context, "Error Interno. Por favor reinicie sesion", Toast.LENGTH_LONG).show()
                            Log.d("pass4",user.toString())
                            Log.d("pass5","Contraseña no actualizada")
                        }
                    }

            }

        }

        return view
    }

    private fun validate(): Boolean{
        var valid = true
        if(new_password_1.text.toString().isEmpty()){
            new_password_1.error = "Campo vacío"
            valid = false
        } else{
            new_password_1.error = null
        }

        if(new_passwordconfirm.text.toString().isEmpty()){
            new_passwordconfirm.error = "Campo vacío"
            valid = false
        } else{
            new_passwordconfirm.error = null
        }

        if(new_password_1.text.toString() != new_passwordconfirm.text.toString()){
            Snackbar.make(this.view!!, "Contraseñas no coinciden", Snackbar.LENGTH_SHORT).show()
            valid = false
        }

        return valid
    }



}
