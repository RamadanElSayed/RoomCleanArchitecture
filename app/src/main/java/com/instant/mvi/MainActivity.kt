package com.instant.mvi

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import com.instant.mvi.presentation.view.viewmodel.UserListViewModel
import com.instant.mvi.presentation.view.screens.UserListScreen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint  // Ensure Hilt is aware of this activity
class MainActivity : ComponentActivity() {
    private val userListViewModel: UserListViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            UserListScreen(userListViewModel = userListViewModel)
        }
    }


}




