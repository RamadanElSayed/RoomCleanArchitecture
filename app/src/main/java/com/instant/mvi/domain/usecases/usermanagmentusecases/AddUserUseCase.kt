package com.instant.mvi.domain.usecases.usermanagmentusecases

import com.instant.mvi.data.database.UserEntity
import com.instant.mvi.domain.repository.UserRepository
import javax.inject.Inject

class AddUserUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(user: UserEntity) {
        userRepository.addUser(user)
    }
}
