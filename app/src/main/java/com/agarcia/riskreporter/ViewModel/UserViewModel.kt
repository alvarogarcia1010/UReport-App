package com.agarcia.riskreporter.ViewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.agarcia.riskreporter.Database.Models.User
import com.agarcia.riskreporter.Database.RoomDB
import com.agarcia.riskreporter.Repositories.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class UserViewModel(app: Application): AndroidViewModel(app) {

    private val userRepository: UserRepository
    val allUsers: LiveData<List<User>>

    init {
        val userDao = RoomDB.getDatabase(app).userDao()
        userRepository = UserRepository(userDao)
        allUsers = userRepository.getAllReports()
    }

     fun insertUser(user: User) = viewModelScope.launch(Dispatchers.IO) {
         userRepository.insertReport(user)
     }

}