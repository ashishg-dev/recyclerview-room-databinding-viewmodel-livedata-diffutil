package dev.ashish.roomdatabase.dao

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.room.*
import dev.ashish.roomdatabase.entity.UserDetails

@Dao
interface UserDetailsDao {

    @Query("SELECT * from user_details ORDER BY firstName ASC")
    fun getAlphabetizedWords(): LiveData<List<UserDetails>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(userDetails: UserDetails)

    @Query("DELETE FROM user_details")
    suspend fun deleteAll()

    @Delete
    suspend fun delete(userDetails: UserDetails)

    @Update
    suspend fun update(userDetails: UserDetails)

}