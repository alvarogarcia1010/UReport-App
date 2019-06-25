package com.agarcia.riskreporter.Database.Models

import android.os.Parcel
import android.os.Parcelable
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

    @field:Json(name = "longitude")
    val longitude: String = "N/A",

    @field:Json(name = "latitude")
    val latitude: String = "N/A",

    @field:Json(name = "risk_level")
    val risk_level: String = "N/A",

    @field:Json(name = "measures_proposed")
    val measures_proposed: String = "N/A",

    @field:Json(name = "reporter")
    val reporter: String = "N/A",

    @field:Json(name = "reporter_id")
    val reporter_id: String = "N/A",

    @field:Json(name = "datetime")
    val datetime: String = "N/A",

    @field:Json(name = "status")
    val status: String = "N/A"
) : Parcelable {
    @PrimaryKey(autoGenerate = true)
    @field:Json(name = "id")
    var id: Int = 0

    constructor(source: Parcel) : this(
        source.readString(),
        source.readString(),
        source.readString(),
        source.readString(),
        source.readString(),
        source.readString(),
        source.readString(),
        source.readString(),
        source.readString(),
        source.readString(),
        source.readString(),
        source.readString()
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeString(title)
        writeString(url_image)
        writeString(remark)
        writeString(detailed_location)
        writeString(longitude)
        writeString(latitude)
        writeString(risk_level)
        writeString(measures_proposed)
        writeString(reporter)
        writeString(reporter_id)
        writeString(datetime)
        writeString(status)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<Report> = object : Parcelable.Creator<Report> {
            override fun createFromParcel(source: Parcel): Report = Report(source)
            override fun newArray(size: Int): Array<Report?> = arrayOfNulls(size)
        }
    }
}