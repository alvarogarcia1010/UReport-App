package com.agarcia.riskreporter.Fragments

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.agarcia.riskreporter.Activities.MainActivity

import com.agarcia.riskreporter.R
import kotlinx.android.synthetic.main.fragment_register.*

class RegisterFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        return inflater.inflate(R.layout.fragment_register, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        register_btn_register.setOnClickListener {
            val mIntent = Intent(activity, MainActivity::class.java)
            startActivity(mIntent)
        }

        register_btn_login.setOnClickListener {
            val backAction = RegisterFragmentDirections.backAction()
            Navigation.findNavController(it).navigate(backAction)
        }
    }
}
