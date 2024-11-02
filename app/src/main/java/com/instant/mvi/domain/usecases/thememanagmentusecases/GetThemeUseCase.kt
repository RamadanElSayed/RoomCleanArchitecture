package com.instant.mvi.domain.usecases.thememanagmentusecases
import com.instant.mvi.data.sharedprefences.AppPreferences
import com.instant.mvi.domain.usecases.thememanagmentusecases.SharedPrefKeys.THEME_STATUS
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetThemeUseCase @Inject constructor(
    private val appPreferences: AppPreferences
) {
    fun invoke(): Flow<Boolean> = flow {
        val isDarkTheme = appPreferences.getBooleanPreference(THEME_STATUS, false)
        emit(isDarkTheme) // Emit the current theme state
    }
}