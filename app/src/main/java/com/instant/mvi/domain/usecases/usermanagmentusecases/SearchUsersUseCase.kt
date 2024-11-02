package com.instant.mvi.domain.usecases.usermanagmentusecases

import com.instant.mvi.data.database.entity.UserEntity
import com.instant.mvi.data.model.StateResources
import com.instant.mvi.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SearchUsersUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    operator fun invoke(query: String): Flow<StateResources<List<UserEntity>>> {
        return userRepository.searchUsers(query)
    }
}
