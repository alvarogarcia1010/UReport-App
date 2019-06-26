package com.agarcia.riskreporter.Fragments

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import com.agarcia.riskreporter.Database.Models.User

import com.agarcia.riskreporter.R
import com.agarcia.riskreporter.ViewModel.UserViewModel
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.fragment_my_perfil.*
import kotlinx.android.synthetic.main.fragment_my_perfil.view.*


class MyPerfilFragment : Fragment() {

    private lateinit var userViewModel: UserViewModel
    lateinit var user: FirebaseUser
    lateinit var currentuser: User
    lateinit var name: String
    lateinit var uid: String

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        val view = inflater.inflate(R.layout.fragment_my_perfil, container, false)

        view.change_password_profile.setOnClickListener {
            val nextAction = MyPerfilFragmentDirections.nextAction()
            Navigation.findNavController(it).navigate(nextAction)
        }

        return view

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)

        currentuser = User()
        user = FirebaseAuth.getInstance().currentUser!!

        user?.let {
            uid = user.uid
        }

        getUserData()

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_toolbar, menu)

        super.onCreateOptionsMenu(menu, inflater)
    }

    /*override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val view = this.getView()
        val navController = Navigation.findNavController(this, R.id.nav_host_fragment)
        val navigated:Boolean = NavigationUI.onNavDestinationSelected(item!!, navController)
        return navigated || super.onOptionsItemSelected(item)
    }*/

    fun getUserData(){
        userViewModel = ViewModelProviders.of(this).get(UserViewModel::class.java)

        val ref = FirebaseDatabase.getInstance().getReference("/users").child(user!!.uid)
        Log.d("hola", ref.toString())
        ref.addListenerForSingleValueEvent(object : ValueEventListener {

            override fun onCancelled(p0: DatabaseError) {
            }

            override fun onDataChange(p0: DataSnapshot) {
                Log.d("hola2", currentuser.toString())
                currentuser = p0.getValue(User::class.java)!!
                Log.d("hola3", currentuser.toString())
                bindData(currentuser)

                /*userViewModel.getuserbyname(name){
                    var item = it.value?: User()
                    bindData(item)

                }*/
            }
        })


    }

    fun bindData(item: User){
        profile_fullname.text = item.full_name
        profile_email.text = item.email
        profile_company.text = item.company
        Glide.with(this).load(item.url_image).into(profile_image)
        profile_institucion.text = item.company
        profile_fullname_principal.text = item.full_name
    }
}
