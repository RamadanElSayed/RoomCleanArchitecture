package com.instant.mvi.domain.repository

import com.instant.mvi.data.database.UserEntity
import com.instant.mvi.data.model.StateResources

// User repository interface using UserEntity for Room integration
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    fun getUsers(): Flow<StateResources<List<UserEntity>>>
    suspend fun addUser(user: UserEntity)
    suspend fun deleteUser(user: UserEntity)
    suspend fun clearUsers()
    fun searchUsers(query: String): Flow<StateResources<List<UserEntity>>>
}
