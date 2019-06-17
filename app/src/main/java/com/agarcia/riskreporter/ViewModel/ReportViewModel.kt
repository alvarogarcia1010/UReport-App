package com.agarcia.riskreporter.ViewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.agarcia.riskreporter.Database.Report
import com.agarcia.riskreporter.Database.RoomDB
import com.agarcia.riskreporter.Repositories.ReportRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ReportViewModel(app: Application): AndroidViewModel(app) {

    private val reportRepository: ReportRepository
    val allReports: LiveData<List<Report>>

    init {
        val reportDao = RoomDB.getDatabase(app).reportDao()
        reportRepository = ReportRepository(reportDao)
        allReports = reportRepository.getAllReports()
    }

    fun insertReport(report: Report) = viewModelScope.launch(Dispatchers.IO){
        reportRepository.insertReport(report)
    }

}