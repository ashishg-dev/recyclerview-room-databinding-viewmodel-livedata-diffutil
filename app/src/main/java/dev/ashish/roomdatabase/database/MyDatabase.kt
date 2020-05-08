package dev.ashish.roomdatabase.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import dev.ashish.roomdatabase.dao.UserDetailsDao
import dev.ashish.roomdatabase.entity.UserDetails

@Database(entities = [UserDetails::class], version = 1, exportSchema = false)
public abstract class MyDatabase : RoomDatabase() {

    abstract fun userDetailsDao(): UserDetailsDao

    companion object {
        // Singleton prevents multiple instances of database opening at the
        // same time.
        @Volatile
        private var INSTANCE: MyDatabase? = null

         fun getDatabase(context: Context): MyDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(context.applicationContext,
                    MyDatabase::class.java, "example_database")
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                return instance
            }
        }
    }
}