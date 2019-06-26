package com.agarcia.riskreporter.Repositories

import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import com.agarcia.riskreporter.Database.Models.User
import com.agarcia.riskreporter.Database.UserDAO

class UserRepository(private val userDao: UserDAO) {

    @WorkerThread
    suspend fun insertReport(user: User) = userDao.insertUser(user)

    fun deleteAll() = userDao.deleteAll()

    fun updateReport(user: User) = userDao.updateUser(user)

    fun getAllReports(): LiveData<List<User>> = userDao.getAlluser()

    fun getuserbyname(name: String): User = userDao.getuserbyname(name)
}