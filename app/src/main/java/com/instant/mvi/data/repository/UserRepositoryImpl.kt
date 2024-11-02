package com.instant.mvi.data.repository
import com.instant.mvi.data.database.dao.UserDao
import com.instant.mvi.data.database.entity.UserEntity
import com.instant.mvi.data.model.StateResources
import com.instant.mvi.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(private val userDao: UserDao) : UserRepository {

    override fun getUsers(): Flow<StateResources<List<UserEntity>>> = flow {
        emit(StateResources.Loading)
        userDao.getAllUsers().collect { users ->
            emit(StateResources.Success(users))
        }
    }.catch { e ->
        emit(StateResources.Error(e.localizedMessage ?: "Unknown error occurred"))
    }

    override suspend fun addUser(user: UserEntity) {
        try {
            userDao.insertUser(user)
        } catch (e: Exception) {
            throw e // Let the ViewModel handle any caught exception
        }
    }

    override suspend fun deleteUser(user: UserEntity) {
        try {
            userDao.deleteUser(user)
        } catch (e: Exception) {
            throw e
        }
    }

    override suspend fun clearUsers() {
        try {
            userDao.clearUsers()
        } catch (e: Exception) {
            throw e
        }
    }

    override fun searchUsers(query: String): Flow<StateResources<List<UserEntity>>> = flow {
        emit(StateResources.Loading)
        userDao.searchUsers(query).collect { users ->
            emit(StateResources.Success(users))
        }
    }.catch { e ->
        emit(StateResources.Error(e.localizedMessage ?: "Unknown error occurred"))
    }
}


