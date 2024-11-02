package com.instant.mvi.data.database.dao


import androidx.room.*
import com.instant.mvi.data.database.entity.UserEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {
    @Query("SELECT * FROM users")
    fun getAllUsers(): Flow<List<UserEntity>> // Return Flow instead of List for live data updates

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: UserEntity)

    @Delete
    suspend fun deleteUser(user: UserEntity)

    @Query("DELETE FROM users")
    suspend fun clearUsers()

    @Query("SELECT * FROM users WHERE name LIKE '%' || :searchQuery || '%' OR email LIKE '%' || :searchQuery || '%'")
    fun searchUsers(searchQuery: String): Flow<List<UserEntity>> // Return Flow for live search results


    // New update function based on id
    @Query("UPDATE users SET name = :name, email = :email WHERE id = :id")
    suspend fun updateUserById(id: Int, name: String, email: String)


    @Update
    suspend fun updateUser(user: UserEntity)
}
