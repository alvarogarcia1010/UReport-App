package com.agarcia.riskreporter.Activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.viewpager.widget.ViewPager
import com.agarcia.riskreporter.Adapters.ViewPagerAdapter
import com.agarcia.riskreporter.Fragments.*
import com.agarcia.riskreporter.R
import kotlinx.android.synthetic.main.activity_report.*

class ReportActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_report)

        setSupportActionBar(toolbar)

        val adapter = ViewPagerAdapter(supportFragmentManager)
        adapter.addFragment(ImageFragment(), "Image")
        adapter.addFragment(LocationFragment(), "Location")
        adapter.addFragment(RiskFragment(), "Risk")
        adapter.addFragment(MeasuresFragment(), "Measures")
        adapter.addFragment(SummaryFragment(), "Summary")
        viewPager.adapter = adapter
        tabs.setupWithViewPager(viewPager)
    }
}
