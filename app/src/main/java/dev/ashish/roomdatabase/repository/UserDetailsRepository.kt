package dev.ashish.roomdatabase.repository

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import dev.ashish.roomdatabase.dao.UserDetailsDao
import dev.ashish.roomdatabase.database.MyDatabase
import dev.ashish.roomdatabase.entity.UserDetails

class UserDetailsRepository(application: Application) {

    private var userDetailsDao: UserDetailsDao =
        MyDatabase.getDatabase(application).userDetailsDao()

//    private val userDetailsList : LiveData<List<UserDetails>>

    init {
//        userDetailsList = userDetailsDao.getAlphabetizedWords()
    }

    fun getAllUserDetails(): LiveData<List<UserDetails>> {
        return userDetailsDao.getAlphabetizedWords()
    }

    suspend fun insert(userDetails: UserDetails) {
        userDetailsDao.insert(userDetails)
    }

    suspend fun update(userDetails: UserDetails){
        userDetailsDao.update(userDetails)
    }

    suspend fun delete(userDetails: UserDetails){
        userDetailsDao.delete(userDetails)
    }

    suspend fun deleteAll(){
        userDetailsDao.deleteAll()
    }

}