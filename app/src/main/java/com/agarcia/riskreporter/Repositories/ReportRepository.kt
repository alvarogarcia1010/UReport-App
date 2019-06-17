package com.agarcia.riskreporter.Repositories

import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import com.agarcia.riskreporter.Database.Report
import com.agarcia.riskreporter.Database.ReportDAO

class ReportRepository(private val reportDao: ReportDAO) {

    @WorkerThread
    suspend fun insertReport(report: Report) = reportDao.insertReport(report)


    fun deleteAll() = reportDao.deleteAll()

    fun updateReport(report: Report) = reportDao.updateReport(report)

    fun getAllReports(): LiveData<List<Report>> = reportDao.getAllreport()

}