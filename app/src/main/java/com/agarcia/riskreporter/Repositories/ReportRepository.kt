package com.agarcia.riskreporter.Repositories

import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import com.agarcia.riskreporter.Database.Models.Report
import com.agarcia.riskreporter.Database.ReportDAO

class ReportRepository(private val reportDao: ReportDAO) {

    /*Report*/
    @WorkerThread
    suspend fun insertReport(report: Report) = reportDao.insertReport(report)


    fun deleteAll() = reportDao.deleteAll()

    fun updateReport(report: Report) = reportDao.updateReport(report)

    fun getAllReports(): LiveData<List<Report>> = reportDao.getAllreport()



}