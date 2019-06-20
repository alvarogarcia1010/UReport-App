package com.agarcia.riskreporter.Database

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.Json

@Entity(tableName = "report")
data class Report(
    @field:Json(name = "title")
    val title: String = "N/A",

    @field:Json(name = "url_image")
    val url_image: String = "N/A",

    @field:Json(name = "remark")
    val remark: String = "N/A",

    @field:Json(name = "detailed_location")
    val detailed_location: String = "N/A",

    @field:Json(name = "risk_level")
    val risk_level: String = "N/A",

    @field:Json(name = "measures_proposed")
    val measures_proposed: String = "N/A",

    @field:Json(name = "reporter")
    val reporter: String = "N/A",

    @field:Json(name = "datetime")
    val datetime: String = "N/A",

    @field:Json(name = "status")
    val status: String = "N/A"
){
    @PrimaryKey(autoGenerate = true)
    @field:Json(name = "id")
    var id: Int=0
}