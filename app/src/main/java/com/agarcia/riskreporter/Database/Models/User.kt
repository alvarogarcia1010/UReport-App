package com.agarcia.riskreporter.Database.Models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user")
data class User(
    val email: String = "N/A",
    val url_image: String = "N/A",
    val full_name: String = "N/A",
    val company: String = "N/A",
    val is_admin: Boolean = false
){
    @PrimaryKey(autoGenerate = true)
    var id: Int=0
}