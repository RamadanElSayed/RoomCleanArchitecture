package com.instant.mvi.presentation.view.components

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

@Composable
fun UserInput(
    name: String,
    email: String,
    nameError: Boolean,
    emailError: Boolean,
    onNameChange: (String) -> Unit,
    onEmailChange: (String) -> Unit,
    onAddUser: (Triple<String, String, String?>) -> Unit,
    onClearUsers: () -> Unit,
) {
    val imageBitmap = remember { mutableStateOf<Bitmap?>(null) }
    val context = LocalContext.current

    val isNameValid = remember(name) { name.isNotBlank() }
    val isEmailValid = remember(email) {
        email.isNotBlank() && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }
    val isImageSelected = remember(imageBitmap.value) { imageBitmap.value != null }

    // Camera launcher to take a photo
    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicturePreview()
    ) { bitmap ->
        imageBitmap.value = bitmap
    }

    // Gallery launcher to select an image
    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        uri?.let {
            imageBitmap.value = loadBitmapFromUri(context, it)
        }
    }

    // Permission launcher for CAMERA
    val cameraPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            // Permission granted, launch the camera
            cameraLauncher.launch(null)
        } else {
            // Permission denied, handle accordingly (e.g., show a message)
        }
    }

    // Permission launcher for READ_EXTERNAL_STORAGE (only needed on Android 9 and below)
    val galleryPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            // Permission granted, launch the gallery
            galleryLauncher.launch("image/*")
        } else {
            // Permission denied, handle accordingly (e.g., show a message)
        }
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier.fillMaxWidth()
        ) {
            // Gallery Button
            Button(onClick = {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    // On Android 10 and above, no need for storage permission
                    galleryLauncher.launch("image/*")
                } else {
                    // On Android 9 and below, request READ_EXTERNAL_STORAGE permission
                    if (context.checkSelfPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE) ==
                        android.content.pm.PackageManager.PERMISSION_GRANTED
                    ) {
                        // Permission is already granted, launch the gallery
                        galleryLauncher.launch("image/*")
                    } else {
                        // Request the storage permission
                        galleryPermissionLauncher.launch(android.Manifest.permission.READ_EXTERNAL_STORAGE)
                    }
                }
            }) {
                Text("Select from Gallery")
            }

            // Camera Button
            Button(onClick = {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (context.checkSelfPermission(android.Manifest.permission.CAMERA) ==
                        android.content.pm.PackageManager.PERMISSION_GRANTED
                    ) {
                        // Permission is already granted, launch the camera
                        cameraLauncher.launch(null)
                    } else {
                        // Request the camera permission
                        cameraPermissionLauncher.launch(android.Manifest.permission.CAMERA)
                    }
                } else {
                    // No need to request permission on devices below Android 6.0
                    cameraLauncher.launch(null)
                }
            }) {
                Text("Take a Photo")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Display the selected image or a placeholder with circular clipping
        val imageModifier = Modifier
            .size(120.dp)
            .clip(CircleShape)
            .border(
                2.dp,
                MaterialTheme.colorScheme.primary,
                CircleShape
            )

        imageBitmap.value?.let {
            Image(
                bitmap = it.asImageBitmap(),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = imageModifier
            )
        } ?: Box(
            modifier = Modifier
                .size(120.dp)
                .background(
                    MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f),
                    CircleShape
                )
                .border(
                    2.dp,
                    MaterialTheme.colorScheme.primary,
                    CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            Text("No Image", color = MaterialTheme.colorScheme.onSurface)
        }

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = name,
            onValueChange = onNameChange,
            label = { Text("Name") },
            isError = nameError,
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next),
            colors = TextFieldDefaults.colors(
                focusedTextColor = MaterialTheme.colorScheme.onSurface,
                unfocusedTextColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                errorTextColor = MaterialTheme.colorScheme.error,
                cursorColor = MaterialTheme.colorScheme.primary,
                errorCursorColor = MaterialTheme.colorScheme.error
            )
        )

        OutlinedTextField(
            value = email,
            onValueChange = onEmailChange,
            label = { Text("Email") },
            isError = emailError,
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Done
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            colors = TextFieldDefaults.colors(
                focusedTextColor = MaterialTheme.colorScheme.onSurface,
                unfocusedTextColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                errorTextColor = MaterialTheme.colorScheme.error,
                cursorColor = MaterialTheme.colorScheme.primary,
                errorCursorColor = MaterialTheme.colorScheme.error
            )
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                if (isNameValid && isEmailValid && isImageSelected) {
                    onAddUser(
                        Triple(name, email,
                            imageBitmap.value?.let { saveBitmapToFile(context, it) })
                    )
                    imageBitmap.value = null // Clear the image after adding the user
                }
            },
            enabled = isNameValid && isEmailValid && isImageSelected,
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            )
        ) {
            Text("Add User")
        }

        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = onClearUsers,
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.error,
                contentColor = MaterialTheme.colorScheme.onError
            )
        ) {
            Text("Clear Users")
        }
    }
}


fun loadBitmapFromUri(context: Context, uri: Uri): Bitmap? {
    return try {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            // Use MediaStore to handle images for Android 10+
            context.contentResolver.openInputStream(uri)?.use {
                BitmapFactory.decodeStream(it)
            }
        } else {
            // Handle for Android 9 and below
            context.contentResolver.openInputStream(uri)?.use {
                BitmapFactory.decodeStream(it)
            }
        }
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}


// Helper function to save a bitmap to a file and return the file path
private fun saveBitmapToFile(context: Context, bitmap: Bitmap): String {
    val filename = "user_image_${System.currentTimeMillis()}.png"
    val file = File(context.filesDir, filename)
    return try {
        FileOutputStream(file).use { out ->
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, out)
        }
        file.absolutePath // Return the file path as a string
    } catch (e: IOException) {
        e.printStackTrace()
        ""
    }
}


//fun saveBitmapToFile(context: Context, bitmap: Bitmap): Uri? {
//    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
//        // Scoped Storage approach for Android 10 and above
//        val filename = generateFilename()  // Using the filename generator
//        val contentValues = ContentValues().apply {
//            put(MediaStore.Images.Media.DISPLAY_NAME, "$filename.jpg")
//            put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
//            put(MediaStore.Images.Media.RELATIVE_PATH, Environment.DIRECTORY_PICTURES + "/MyApp")
//        }
//        val uri = context.contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
//        uri?.let {
//            context.contentResolver.openOutputStream(it).use { out ->
//                out?.let { it1 -> bitmap.compress(Bitmap.CompressFormat.JPEG, 100, it1) }
//            }
//        }
//        uri
//    } else {
//        // Legacy external storage approach for Android 9 and below
//        val picturesDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
//        val file = File(picturesDir, "${generateFilename()}.jpg")  // Using the filename generator
//        try {
//            FileOutputStream(file).use { out ->
//                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out)
//            }
//            Uri.fromFile(file)
//        } catch (e: IOException) {
//            e.printStackTrace()
//            null
//        }
//    }
//}
//
//// Filename generator function
//fun generateFilename(): String {
//    val timestamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(Date())
//    return "IMG_$timestamp"
//}
