package com.agarcia.riskreporter.Activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import androidx.viewpager.widget.ViewPager
import com.agarcia.riskreporter.Adapters.ViewPagerAdapter
import com.agarcia.riskreporter.Fragments.*
import com.agarcia.riskreporter.R
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_report.*
import kotlinx.android.synthetic.main.activity_report.toolbar

class ReportActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_report)

        setSupportActionBar(toolbar)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)

        val navController = Navigation.findNavController(this, R.id.nav_host_fragment)
        setUpBottomNavMenu(navController)
        setUpSideNavigationMenu(navController)
        setUpActionBar(navController)

    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

//    override fun onSupportNavigateUp(): Boolean {
//        return NavigationUI.navigateUp(Navigation.findNavController(this, R.id.nav_host_fragment),drawer_layout)
//    }

    private fun setUpBottomNavMenu(navController: NavController){
        bottom_nav?.let {
            NavigationUI.setupWithNavController(it, navController)
        }
    }

    private fun setUpSideNavigationMenu(navController: NavController){
        nav_view?.let {
            NavigationUI.setupWithNavController(it, navController)
        }
    }

    private fun setUpActionBar(navController: NavController){
        NavigationUI.setupActionBarWithNavController(this, navController, drawer_layout)
    }

}
