package com.agarcia.riskreporter.Database

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface ReportDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertReport(report: Report)

    @Query("DELETE from report")
    fun deleteAll()

    @Update
    fun updateReport(report: Report)

    @Query("Select * from report")
    fun getAllreport(): LiveData<List<Report>>


}