package com.agarcia.riskreporter.ViewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.agarcia.riskreporter.Database.Models.User
import com.agarcia.riskreporter.Database.RoomDB
import com.agarcia.riskreporter.Repositories.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class UserViewModel(app: Application): AndroidViewModel(app) {

    private val userRepository: UserRepository
    val allUsers: LiveData<List<User>>
    lateinit var user: LiveData<User>

    init {
        val userDao = RoomDB.getDatabase(app).userDao()
        userRepository = UserRepository(userDao)
        allUsers = userRepository.getAllReports()
    }

     fun insertUser(user: User) = viewModelScope.launch(Dispatchers.IO) {
         userRepository.insertReport(user)
     }

    fun updateuser(user: User) = viewModelScope.launch(Dispatchers.IO) {
        userRepository.updateReport(user)
    }

    fun getuserbyname(name: String, callback: (data: LiveData<User>) -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            user = MutableLiveData(userRepository.getuserbyname(name))
            callback(user)
        }
    }


}