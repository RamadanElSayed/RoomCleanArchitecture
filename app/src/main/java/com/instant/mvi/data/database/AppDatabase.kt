package com.instant.mvi.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.instant.mvi.data.database.dao.SchoolDao
import com.instant.mvi.data.database.dao.UserDao
import com.instant.mvi.data.database.entity.SchoolEntity
import com.instant.mvi.data.database.entity.UserEntity

@Database(entities = [UserEntity::class, SchoolEntity::class], version = 2)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun schoolDao(): SchoolDao
}

