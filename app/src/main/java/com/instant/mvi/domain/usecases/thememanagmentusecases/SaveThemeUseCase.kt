package com.instant.mvi.domain.usecases.thememanagmentusecases

import com.instant.mvi.data.sharedprefences.AppPreferences
import com.instant.mvi.domain.usecases.thememanagmentusecases.SharedPrefKeys.THEME_STATUS
import javax.inject.Inject

class SaveThemeUseCase @Inject constructor(
    private val appPreferences: AppPreferences
) {
    fun invoke(isDarkTheme: Boolean) {
        appPreferences.saveBooleanPreference(THEME_STATUS, isDarkTheme)
    }
}

object SharedPrefKeys{
const val THEME_STATUS="isDarkTheme"
}
