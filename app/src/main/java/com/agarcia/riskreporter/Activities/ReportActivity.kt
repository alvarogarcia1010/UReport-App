package com.agarcia.riskreporter.Activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
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

        val adapter = ViewPagerAdapter(supportFragmentManager)
        //adapter.addFragment(ImageFragment(), "Image")
        adapter.addFragment(SummaryFragment(), "Paso 1")
        adapter.addFragment(LocationFragment(), "Paso 2")
        //adapter.addFragment(RiskFragment(), "Risk")
        //adapter.addFragment(MeasuresFragment(), "Measures")
        viewPager.adapter = adapter
        tabs.setupWithViewPager(viewPager)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

}
