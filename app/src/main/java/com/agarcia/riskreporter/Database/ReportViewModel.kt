package com.agarcia.riskreporter.Database

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ReportViewModel(app:Application):AndroidViewModel(app) {

    private var repository:ReportRepository?=null

    init {
        val reportDAO = RoomDB.getInstance(getApplication()).reportDao()
        repository = ReportRepository(reportDAO)
    }

    fun insertReport(report: Report) = viewModelScope.launch(Dispatchers.IO) {
        repository!!.insertReport(report)
    }

    fun deletaAll() = viewModelScope.launch(Dispatchers.IO) {
        repository!!.deleteAll()
    }

    fun getAllReport():LiveData<List<Report>> = repository!!.getAllReport()


}