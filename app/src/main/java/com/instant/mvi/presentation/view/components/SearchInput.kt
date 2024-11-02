package com.instant.mvi.presentation.view.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.material3.MaterialTheme

@Composable
fun SearchInput(
    query: String,
    onQueryChange: (String) -> Unit,
) {
    // Get the keyboard controller
    val softwareKeyboardController = LocalSoftwareKeyboardController.current

    OutlinedTextField(
        value = query,
        onValueChange = {
            onQueryChange(it)
        },
        label = { Text("Search Users") },
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
        keyboardActions = KeyboardActions(onSearch = {
            onQueryChange(query)
            softwareKeyboardController?.hide()
        }),
        colors  = TextFieldDefaults.colors(
            focusedTextColor = MaterialTheme.colorScheme.onSurface,
            unfocusedTextColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
            disabledTextColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f),
            errorTextColor = MaterialTheme.colorScheme.error,

            focusedContainerColor = MaterialTheme.colorScheme.surface,
            unfocusedContainerColor = MaterialTheme.colorScheme.surface,
            disabledContainerColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.05f),
            errorContainerColor = MaterialTheme.colorScheme.errorContainer,

            cursorColor = MaterialTheme.colorScheme.primary,
            errorCursorColor = MaterialTheme.colorScheme.error,

            focusedIndicatorColor = MaterialTheme.colorScheme.primary,
            unfocusedIndicatorColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
            disabledIndicatorColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f),
            errorIndicatorColor = MaterialTheme.colorScheme.error,

            focusedLeadingIconColor = MaterialTheme.colorScheme.primary,
            unfocusedLeadingIconColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
            disabledLeadingIconColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f),
            errorLeadingIconColor = MaterialTheme.colorScheme.error,

            focusedTrailingIconColor = MaterialTheme.colorScheme.primary,
            unfocusedTrailingIconColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
            disabledTrailingIconColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f),
            errorTrailingIconColor = MaterialTheme.colorScheme.error,

            focusedLabelColor = MaterialTheme.colorScheme.primary,
            unfocusedLabelColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
            disabledLabelColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f),
            errorLabelColor = MaterialTheme.colorScheme.error,

            focusedPlaceholderColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
            unfocusedPlaceholderColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
            disabledPlaceholderColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f),
            errorPlaceholderColor = MaterialTheme.colorScheme.error,

            focusedSupportingTextColor = MaterialTheme.colorScheme.onSurface,
            unfocusedSupportingTextColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
            disabledSupportingTextColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f),
            errorSupportingTextColor = MaterialTheme.colorScheme.error,

            focusedPrefixColor = MaterialTheme.colorScheme.onSurface,
            unfocusedPrefixColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
            disabledPrefixColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f),
            errorPrefixColor = MaterialTheme.colorScheme.error,

            focusedSuffixColor = MaterialTheme.colorScheme.onSurface,
            unfocusedSuffixColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
            disabledSuffixColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f),
            errorSuffixColor = MaterialTheme.colorScheme.error
        )
    )
}
