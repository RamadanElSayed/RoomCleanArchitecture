package com.instant.mvi.presentation.view.uimodel

// SnackbarEvent for one-time snackbar events
sealed class SnackbarEffect {
    data class ShowSnackbar(val message: String, val actionLabel: String? = null) : SnackbarEffect()
}