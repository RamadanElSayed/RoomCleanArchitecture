package com.instant.mvi.domain.usecases.usermanagmentusecases

import com.instant.mvi.domain.repository.UserRepository
import javax.inject.Inject

class ClearUsersUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke() {
        userRepository.clearUsers()
    }
}
