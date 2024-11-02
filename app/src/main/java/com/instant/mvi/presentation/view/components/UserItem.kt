package com.instant.mvi.presentation.view.components

import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.instant.mvi.data.database.entity.UserEntity
import java.io.File

// A single user item in the list, now using UserEntity with image support
@Composable
fun UserItem(user: UserEntity, onDeleteUser: (UserEntity) -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clip(RoundedCornerShape(16.dp)) // Rounded corners
            .shadow(6.dp, RoundedCornerShape(16.dp)), // Elevation shadow
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface), // Use theme surface color
        elevation = CardDefaults.cardElevation(8.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp) // Increased padding for cleaner layout
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Display the user's image
                user.imageUrl?.let { imageUrl ->
                    LoadImageFromFile(imageUrl)
                }

                Spacer(modifier = Modifier.width(16.dp))

                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = user.name,
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold
                        ) // Bolder font for the name
                    )
                    Spacer(modifier = Modifier.height(4.dp)) // Small space between name and email
                    Text(
                        text = user.email,
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f) // Softer color for email
                        )
                    )
                }

                Button(
                    onClick = { onDeleteUser(user) },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.error, // Use theme error color for delete button
                        contentColor = MaterialTheme.colorScheme.onError // Use onError for contrast
                    ),
                    shape = MaterialTheme.shapes.medium
                ) {
                    Text("Delete")
                }
            }
        }
    }
}

// Helper function to load and display image from file path
@Composable
fun LoadImageFromFile(imagePath: String) {
    val file = File(imagePath)
    if (file.exists()) {
        val bitmap = BitmapFactory.decodeFile(file.absolutePath)
        bitmap?.let {
            Image(
                bitmap = it.asImageBitmap(),
                contentDescription = null,
                contentScale = ContentScale.Crop, // Ensures the image fills the circular container
                modifier = Modifier
                    .size(64.dp) // Adjust size as needed
                    .clip(CircleShape) // Clip the image to a circular shape
            )
        }
    } else {
        // Handle case where image file is missing, optionally show a placeholder
        Box(
            modifier = Modifier
                .size(64.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f)), // Placeholder background
            contentAlignment = Alignment.Center
        ) {
            Text("No Image", color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)) // Placeholder text
        }
    }
}
