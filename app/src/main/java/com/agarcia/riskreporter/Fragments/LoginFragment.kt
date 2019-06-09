package com.agarcia.riskreporter.Fragments


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation

import com.agarcia.riskreporter.R
import kotlinx.android.synthetic.main.fragment_login.*
import com.agarcia.riskreporter.Activities.MainActivity
import android.content.Intent



class LoginFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        login_btn_login.setOnClickListener {
            val mIntent = Intent(activity, MainActivity::class.java)
            startActivity(mIntent)
        }

        login_btn_register.setOnClickListener {
            val nextAction = LoginFragmentDirections.nextAction()
            Navigation.findNavController(it).navigate(nextAction)
        }
    }

}
