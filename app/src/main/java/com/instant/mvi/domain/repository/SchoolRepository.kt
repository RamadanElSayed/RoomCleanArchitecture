package com.instant.mvi.domain.repository

import com.instant.mvi.data.database.entity.SchoolEntity
import kotlinx.coroutines.flow.Flow

interface SchoolRepository {
    suspend fun insertSchool(school: SchoolEntity)
    suspend fun updateSchool(school: SchoolEntity)
    suspend fun deleteSchool(school: SchoolEntity)
    fun getSchoolsForUser(userId: Int): Flow<List<SchoolEntity>>
}
