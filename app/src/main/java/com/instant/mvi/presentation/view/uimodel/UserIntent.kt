package com.instant.mvi.presentation.view.uimodel

import android.graphics.Bitmap
import android.net.Uri
import com.instant.mvi.data.database.UserEntity

// Intent represents user interactions
sealed class UserIntent {
    data object LoadUsers : UserIntent()
    data class AddUser(val name: String, val email: String, val imagePath:String?) : UserIntent()
    data class DeleteUser(val user: UserEntity) : UserIntent() // Use UserEntity instead of User
    data object ClearUsers : UserIntent()
    data class SearchUser(val query: String) : UserIntent()
    data class UpdateName(val name: String) : UserIntent()  // Intent for name changes
    data class UpdateEmail(val email: String) : UserIntent()  // Intent for email changes
    data object UndoDelete : UserIntent()

    data object LoadTheme : UserIntent() // Intent to load the theme state
    data class UpdateTheme(val isDarkTheme: Boolean) : UserIntent() // Intent to update the theme
}
