package com.oops.oops_android.data.db.Database

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.oops.oops_android.ApplicationClass.Companion.applicationContext
import com.oops.oops_android.data.db.Dao.UserDao
import com.oops.oops_android.data.db.Entity.User

/* Room DB */
@Database(entities = [User::class], version = 2)
abstract class AppDatabase: RoomDatabase() {
    // 사용자 정보
    abstract fun userDao(): UserDao

    companion object {
        private var appDatabase: AppDatabase? = null

        fun getUserDB(): AppDatabase? {
            if (appDatabase == null) {
                appDatabase = Room.databaseBuilder(
                    applicationContext(),
                    AppDatabase::class.java,
                    "userDatabase"
                )
                    .allowMainThreadQueries()
                    .fallbackToDestructiveMigration()
                    .build()
            }
            return appDatabase
        }
    }
}