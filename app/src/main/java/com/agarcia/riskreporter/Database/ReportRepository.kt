package com.agarcia.riskreporter.Database

import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData

class ReportRepository(private val reportDao: ReportDAO) {

    @WorkerThread
    suspend fun insertReport(report: Report) = reportDao.insertReport(report)


    fun deleteAll() = reportDao.deleteAll()

    fun updateReport(report: Report) = reportDao.updateReport(report)

    fun getAllReport(): LiveData<List<Report>> = reportDao.getAllreport()

}