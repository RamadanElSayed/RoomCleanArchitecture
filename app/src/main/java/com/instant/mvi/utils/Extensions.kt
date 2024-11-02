package com.instant.mvi.utils


// String Extension to validate a name
fun String.isValidName(): Boolean {
    return this.isNotBlank() && this.length >= 3 && this.all { it.isLetterOrDigit() || it.isWhitespace() }
}

// String Extension to validate an email
fun String.isValidEmail(): Boolean {
    return this.isNotBlank() && android.util.Patterns.EMAIL_ADDRESS.matcher(this).matches()
}

// String? Extension to check if an image path is valid (non-null and not blank)
fun String?.isValidImage(): Boolean {
    return !this.isNullOrBlank()
}
