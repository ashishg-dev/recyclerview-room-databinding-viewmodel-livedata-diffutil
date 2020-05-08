package dev.ashish.roomdatabase.viewmodel

import android.app.Application
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import dev.ashish.roomdatabase.entity.UserDetails
import dev.ashish.roomdatabase.repository.UserDetailsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.StringBuilder

class UserDetailsViewModel(application: Application) : AndroidViewModel(application) {

    private val userDetailsRepository: UserDetailsRepository = UserDetailsRepository(application)

//    val allData: LiveData<List<UserDetails>>

    init {
//        allData = userDetailsRepository.getAllUserDetails()
    }

    fun insert(userDetails: UserDetails) = viewModelScope.launch(Dispatchers.IO) {
        userDetailsRepository.insert(userDetails)
    }

    fun update(userDetails: UserDetails) = viewModelScope.launch(Dispatchers.IO) {
        userDetailsRepository.update(userDetails)
    }

    fun delete(userDetails: UserDetails) = viewModelScope.launch(Dispatchers.IO) {
        userDetailsRepository.delete(userDetails)
    }

    fun deleteAll() = viewModelScope.launch(Dispatchers.IO) {
        userDetailsRepository.deleteAll()
    }

    fun getAllDetails() : LiveData<List<UserDetails>> {
        return userDetailsRepository.getAllUserDetails()
    }

}

@BindingAdapter(value =["app:setFirstName", "app:setLastName"], requireAll = true)
fun setFullName(view: TextView, firstName: String, lastName: String) {

    view.text = StringBuilder().append(firstName).append(" ").append(lastName);

}