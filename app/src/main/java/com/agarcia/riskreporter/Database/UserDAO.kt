package com.agarcia.riskreporter.Database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.agarcia.riskreporter.Database.Models.User


@Dao
interface UserDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertUser(user: User)

    @Query("Delete from user")
    fun deleteAll()

    @Update
    fun updateUser(user: User)

    @Query("Select * from user")
    fun getAlluser(): LiveData<List<User>>
}