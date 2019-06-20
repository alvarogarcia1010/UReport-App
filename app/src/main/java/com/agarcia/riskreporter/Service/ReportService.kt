package com.agarcia.riskreporter.Service

import com.agarcia.riskreporter.Database.Models.Report
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import kotlinx.coroutines.Deferred
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.create
import retrofit2.http.GET
import retrofit2.http.Path

const val REPORT_API_BASE_URI = "https://ureport-93a7b.firebaseapp.com/"

interface ReportService {

    @GET("api/reports")
    fun getReports(@Path("reports") report: Report): Deferred<Response<List<Report>>>

    companion object{
        fun getReportService():ReportService{
            return Retrofit.Builder()
                .baseUrl(REPORT_API_BASE_URI)
                .addConverterFactory(MoshiConverterFactory.create())
                .addCallAdapterFactory(CoroutineCallAdapterFactory())
                .build().create(ReportService::class.java)
        }
    }
}