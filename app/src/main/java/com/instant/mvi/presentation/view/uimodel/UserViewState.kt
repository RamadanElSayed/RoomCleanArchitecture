package com.instant.mvi.presentation.view.uimodel

import com.instant.mvi.data.database.entity.UserEntity

// ViewState to manage the UI state
data class UserViewState(
    val isLoading: Boolean = false,
    val users: List<UserEntity> = emptyList(), // Use UserEntity instead of User
    val filteredUsersList: List<UserEntity> = emptyList(),  // The filtered list of users
    val name: String = "",
    val email: String = "",
    val nameError: Boolean = false,
    val emailError: Boolean = false,
    val searchQuery: String = "",
    val isDarkTheme: Boolean=false,
    val recentlyDeletedUser: UserEntity? = null, // Use UserEntity instead of User
    val selectedImageUri: String? = null
)
