package com.instant.mvi.data.repository

import com.instant.mvi.data.database.dao.SchoolDao
import com.instant.mvi.data.database.entity.SchoolEntity
import com.instant.mvi.domain.repository.SchoolRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SchoolRepositoryImpl @Inject constructor(
    private val schoolDao: SchoolDao
) : SchoolRepository {
    override suspend fun insertSchool(school: SchoolEntity) {
        schoolDao.insertSchool(school)
    }

    override suspend fun updateSchool(school: SchoolEntity) {
        schoolDao.updateSchool(school)
    }

    override suspend fun deleteSchool(school: SchoolEntity) {
        schoolDao.deleteSchool(school)
    }

    override fun getSchoolsForUser(userId: Int): Flow<List<SchoolEntity>> {
        return schoolDao.getSchoolsForUser(userId)
    }
}

