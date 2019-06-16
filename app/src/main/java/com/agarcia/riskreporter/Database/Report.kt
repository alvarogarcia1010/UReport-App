package com.agarcia.riskreporter.Database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "report")
data class Report(
    val title: String = "N/A",
    val url_image: String = "N/A",
    val remark: String = "N/A",
    val detailed_location: String = "N/A",
    val risk_level: String = "N/A",
    val measures_proposed: String = "N/A",
    val reporter: String = "N/A",
    val datetime: String = "N/A",
    val status: String = "N/A"
) {
    @PrimaryKey(autoGenerate = true)
    var id: Int=0
}