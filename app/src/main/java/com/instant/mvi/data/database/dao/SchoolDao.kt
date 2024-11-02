package com.instant.mvi.data.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.instant.mvi.data.database.entity.SchoolEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface SchoolDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSchool(school: SchoolEntity)

    @Update
    suspend fun updateSchool(school: SchoolEntity)

    @Delete
    suspend fun deleteSchool(school: SchoolEntity)

    @Query("SELECT * FROM schools WHERE userId = :userId")
    fun getSchoolsForUser(userId: Int): Flow<List<SchoolEntity>>
}
