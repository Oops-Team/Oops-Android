package com.oops.oops_android.data.db.Database

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.oops.oops_android.ApplicationClass.Companion.applicationContext
import com.oops.oops_android.data.db.Dao.RemindAlarmDao
import com.oops.oops_android.data.db.Dao.UserDao
import com.oops.oops_android.data.db.Entity.RemindAlarm
import com.oops.oops_android.data.db.Entity.User

/* Room DB */
@Database(entities = [User::class, RemindAlarm::class], version = 6)
abstract class AppDatabase: RoomDatabase() {
    abstract fun userDao(): UserDao // 사용자 정보
    abstract fun alarmDao(): RemindAlarmDao // 알람 정보

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
