package com.instant.mvi.presentation.view.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.instant.mvi.presentation.view.components.SearchInput
import com.instant.mvi.presentation.view.components.UserInput
import com.instant.mvi.presentation.view.components.UserItem
import com.instant.mvi.presentation.view.theme.MVITheme
import com.instant.mvi.presentation.view.theme.Shapes
import com.instant.mvi.presentation.view.uimodel.SnackbarEffect
import com.instant.mvi.presentation.view.uimodel.UserIntent
import com.instant.mvi.presentation.view.viewmodel.UserListViewModel
import kotlinx.coroutines.flow.collectLatest

@Composable
fun UserListScreen(userListViewModel: UserListViewModel) {

    val viewState by userListViewModel.viewState.collectAsStateWithLifecycle()
    val listState = rememberLazyListState()

    // State to manage Snackbar
    val snackbarHostState = remember { SnackbarHostState() }

    // Collect snackbar events and show snackbar
    LaunchedEffect(userListViewModel) {
        userListViewModel.effectFlow.collectLatest { effect ->
            if (effect is SnackbarEffect.ShowSnackbar) {
                val result = snackbarHostState.showSnackbar(
                    message = effect.message,
                    actionLabel = effect.actionLabel
                )
                if (result == SnackbarResult.ActionPerformed && effect.actionLabel == "Undo") {
                    userListViewModel.handleIntent(UserIntent.UndoDelete)
                }
            }
        }
    }

    // UI Content wrapped with a dynamic theme
    MVITheme(darkTheme = viewState.isDarkTheme) {
        Scaffold(
            snackbarHost = { SnackbarHost(snackbarHostState) },
            content = { paddingValues ->
                Column(
                    modifier = Modifier
                        .padding(paddingValues)
                        .fillMaxSize()
                        .background(MaterialTheme.colorScheme.background, shape = Shapes.medium)
                ) {
                    // Theme toggle button
                    Button(
                        onClick = {
                            userListViewModel.handleIntent(UserIntent.UpdateTheme(!viewState.isDarkTheme))

                        }, // Toggle theme
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                    ) {
                        Text(text = if (viewState.isDarkTheme) "Switch to Light Theme" else "Switch to Dark Theme"
                        )
                    }

                    SearchInput(
                        query = viewState.searchQuery,
                        onQueryChange = {
                            userListViewModel.handleIntent(UserIntent.SearchUser(it))
                        },
                    )

                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth(),
                        contentAlignment = Alignment.TopCenter
                    ) {
                        when {
                            viewState.isLoading -> CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
                            viewState.users.isEmpty() && !viewState.isLoading -> Text("No users available")
                            else -> {
                                LazyColumn(
                                    state = listState,
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .padding(horizontal = 16.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    items(viewState.filteredUsersList, key = { user -> user.id }) { user ->
                                        UserItem(user = user, onDeleteUser = {
                                            userListViewModel.handleIntent(UserIntent.DeleteUser(it))
                                        })
                                    }
                                }

                            }
                        }
                    }

                    UserInput(
                        name = viewState.name,
                        email = viewState.email,
                        nameError = viewState.nameError,
                        emailError = viewState.emailError,
                        onNameChange = { userListViewModel.handleIntent(UserIntent.UpdateName(it)) },
                        onEmailChange = { userListViewModel.handleIntent(UserIntent.UpdateEmail(it)) },
                        onAddUser = {
                            userListViewModel.handleIntent(
                                UserIntent.AddUser(it.first, it.second, it.third)
                            )
                        },
                        onClearUsers = { userListViewModel.handleIntent(UserIntent.ClearUsers) }
                    )
                }
            }
        )
    }
}
