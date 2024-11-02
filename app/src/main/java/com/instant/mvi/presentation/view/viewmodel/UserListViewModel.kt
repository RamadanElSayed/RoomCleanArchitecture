package com.instant.mvi.presentation.view.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.instant.mvi.data.database.UserEntity
import com.instant.mvi.data.model.StateResources
import com.instant.mvi.domain.usecases.thememanagmentusecases.GetThemeUseCase
import com.instant.mvi.domain.usecases.thememanagmentusecases.SaveThemeUseCase
import com.instant.mvi.domain.usecases.usermanagmentusecases.AddUserUseCase
import com.instant.mvi.domain.usecases.usermanagmentusecases.ClearUsersUseCase
import com.instant.mvi.domain.usecases.usermanagmentusecases.DeleteUserUseCase
import com.instant.mvi.domain.usecases.usermanagmentusecases.GetUsersUseCase
import com.instant.mvi.domain.usecases.usermanagmentusecases.SearchUsersUseCase
import com.instant.mvi.presentation.view.uimodel.SnackbarEffect
import com.instant.mvi.presentation.view.uimodel.UserIntent
import com.instant.mvi.presentation.view.uimodel.UserViewState
import com.instant.mvi.utils.isValidEmail
import com.instant.mvi.utils.isValidImage
import com.instant.mvi.utils.isValidName
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class UserListViewModel @Inject constructor(
    private val getUsersUseCase: GetUsersUseCase,
    private val addUserUseCase: AddUserUseCase,
    private val deleteUserUseCase: DeleteUserUseCase,
    private val clearUsersUseCase: ClearUsersUseCase,
    private val searchUsersUseCase: SearchUsersUseCase,
    private val saveThemeUseCase: SaveThemeUseCase,
    private val getThemeUseCase: GetThemeUseCase
) : ViewModel() {

    private val _viewState = MutableStateFlow(UserViewState())
    val viewState: StateFlow<UserViewState> = _viewState

    private val _effectChannel = Channel<SnackbarEffect>()
    val effectFlow: Flow<SnackbarEffect> = _effectChannel.receiveAsFlow()

    private var recentlyDeletedUser: UserEntity? = null

    // Global Job variable to manage search cancellation only
    private var searchJob: Job? = null

    init {
        handleIntent(UserIntent.LoadTheme)
        handleIntent(UserIntent.LoadUsers)
    }

    fun handleIntent(intent: UserIntent) {
        when (intent) {
            is UserIntent.LoadUsers -> loadUsers()
            is UserIntent.AddUser -> validateAndAddUser(intent.name, intent.email, intent.imagePath)
            is UserIntent.DeleteUser -> deleteUser(intent.user)
            is UserIntent.ClearUsers -> clearUsers()
            is UserIntent.SearchUser -> searchUsers(intent.query)
            is UserIntent.UpdateName -> validateName(intent.name)
            is UserIntent.UpdateEmail -> validateEmail(intent.email)
            is UserIntent.UndoDelete -> undoDelete()
            is UserIntent.LoadTheme -> loadTheme()
            is UserIntent.UpdateTheme -> updateTheme(intent.isDarkTheme)
        }
    }

    // Load users from the Room database using Flow and UIResources
    private fun loadUsers() {
        viewModelScope.launch(Dispatchers.IO) {  // Perform loading on the IO thread
            getUsersUseCase().collectLatest { resource ->
                when (resource) {
                    is StateResources.Loading -> withContext(Dispatchers.Main) {
                        _viewState.update { it.copy(isLoading = true) }
                    }
                    is StateResources.Success -> withContext(Dispatchers.Main) {
                        _viewState.update {
                            it.copy(isLoading = false, users = resource.data, filteredUsersList = resource.data)
                        }
                    }
                    is StateResources.Error -> withContext(Dispatchers.Main) {
                        _viewState.update { it.copy(isLoading = false) }
                        _effectChannel.send(SnackbarEffect.ShowSnackbar("Error loading users: ${resource.message}"))
                    }
                }
            }
        }
    }

    // Search users with job cancellation
    private fun searchUsers(query: String) {
        _viewState.update { it.copy(searchQuery = query) }

        if (query.isEmpty()) {
            // Reset to the full user list if the query is empty
            _viewState.update {
                it.copy(
                    filteredUsersList = it.users,
                    isLoading = false
                )
            }
        } else {
            // Cancel the previous job if one is active (to avoid multiple search requests)
            searchJob?.cancel()

            // Start a new search operation with SupervisorScope
            searchJob = viewModelScope.launch(Dispatchers.IO) {  // Perform search on the IO thread
                searchUsersUseCase(query).collectLatest { resource ->
                    when (resource) {
                        is StateResources.Loading -> withContext(Dispatchers.Main) {
                            _viewState.update { it.copy(isLoading = true) }
                        }
                        is StateResources.Success -> withContext(Dispatchers.Main) {
                            _viewState.update {
                                it.copy(
                                    isLoading = false,
                                    filteredUsersList = resource.data
                                )
                            }
                        }
                        is StateResources.Error -> withContext(Dispatchers.Main) {
                            _viewState.update { it.copy(isLoading = false) }
                            _effectChannel.send(SnackbarEffect.ShowSnackbar("Error searching users: ${resource.message}"))
                        }
                    }
                }
            }
        }
    }

    // Validate and add a new user (no job cancellation needed)
    private fun validateAndAddUser(name: String, email: String, imagePath: String?) {
        val nameError = !name.isValidName()
        val emailError = !email.isValidEmail()
        val imageError = !imagePath.isValidImage()

        // Check if the name, email, and image are valid
        if (nameError || emailError || imageError) {
            val errorMessage = buildErrorMessage(nameError, emailError, imageError)
            _viewState.update { it.copy(nameError = nameError, emailError = emailError) }
            viewModelScope.launch(Dispatchers.Main) {
                _effectChannel.send(SnackbarEffect.ShowSnackbar(errorMessage))
            }
            return
        }

        _viewState.update { it.copy(isLoading = true) }
        viewModelScope.launch(Dispatchers.IO) {  // Perform database operations on the IO thread
            try {
                val imageUrl = imagePath  // Save image to internal storage

                // Create a UserEntity and add it to the Room database
                addUserUseCase(UserEntity(name = name, email = email, imageUrl = imageUrl))
                withContext(Dispatchers.Main) {
                    _viewState.update {
                        it.copy(
                            isLoading = false,
                            name = "",
                            email = "",
                            nameError = false,
                            emailError = false
                        )
                    }
                    _effectChannel.send(SnackbarEffect.ShowSnackbar("User added successfully!"))
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    _viewState.update { it.copy(isLoading = false) }
                    _effectChannel.send(SnackbarEffect.ShowSnackbar("Error adding user: ${e.message}"))
                }
            }
        }
    }

    // Delete the selected user (no job cancellation needed)
    private fun deleteUser(user: UserEntity) {
        _viewState.update { it.copy(isLoading = true) }
        viewModelScope.launch(Dispatchers.IO) {  // Perform delete operation on the IO thread
            recentlyDeletedUser = user
            try {
                deleteUserUseCase(user)
                withContext(Dispatchers.Main) {
                    _viewState.update { it.copy(isLoading = false) }
                    _effectChannel.send(SnackbarEffect.ShowSnackbar("User deleted", "Undo"))
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    _viewState.update { it.copy(isLoading = false) }
                    _effectChannel.send(SnackbarEffect.ShowSnackbar("Error deleting user: ${e.message}"))
                }
            }
        }
    }

    // Clear all users (no job cancellation needed)
    private fun clearUsers() {
        _viewState.update { it.copy(isLoading = true) }
        viewModelScope.launch(Dispatchers.IO) {
            try {
                clearUsersUseCase()
                withContext(Dispatchers.Main) {
                    _viewState.update { it.copy(isLoading = false) }
                    _effectChannel.send(SnackbarEffect.ShowSnackbar("All users cleared!"))
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    _viewState.update { it.copy(isLoading = false) }
                    _effectChannel.send(SnackbarEffect.ShowSnackbar("Error clearing users: ${e.message}"))
                }
            }
        }
    }

    // Undo the last deleted user (no job cancellation needed)
    private fun undoDelete() {
        recentlyDeletedUser?.let { deletedUser ->
            viewModelScope.launch(Dispatchers.IO) {
                try {
                    addUserUseCase(deletedUser)
                    withContext(Dispatchers.Main) {
                        _effectChannel.send(SnackbarEffect.ShowSnackbar("User restored successfully!"))
                    }
                } catch (e: Exception) {
                    withContext(Dispatchers.Main) {
                        _effectChannel.send(SnackbarEffect.ShowSnackbar("Error restoring user: ${e.message}"))
                    }
                }
            }
        }
    }

    private fun validateName(name: String) {
        val nameError = !name.isValidName()
        _viewState.update { it.copy(name = name, nameError = nameError) }
    }

    private fun validateEmail(email: String) {
        val emailError = !email.isValidEmail()
        _viewState.update { it.copy(email = email, emailError = emailError) }
    }

    private fun buildErrorMessage(
        nameError: Boolean,
        emailError: Boolean,
        imageError: Boolean
    ): String {
        return when {
            nameError && emailError && imageError -> "Name, email, and image are required."
            nameError && emailError -> "Name and email are required."
            nameError -> "Name is required and must have at least 3 characters."
            emailError -> "Invalid email address."
            imageError -> "Please select or capture an image."
            else -> ""
        }
    }

    private fun loadTheme() {
        viewModelScope.launch(Dispatchers.IO) {
            getThemeUseCase.invoke().collectLatest { isDarkTheme ->
                withContext(Dispatchers.Main) {
                    _viewState.update { it.copy(isDarkTheme = isDarkTheme) }
                }
            }
        }
    }

    private fun updateTheme(isDarkTheme: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            saveThemeUseCase.invoke(isDarkTheme)
            withContext(Dispatchers.Main) {
                _viewState.update { it.copy(isDarkTheme = isDarkTheme) }
            }
        }
    }
}





