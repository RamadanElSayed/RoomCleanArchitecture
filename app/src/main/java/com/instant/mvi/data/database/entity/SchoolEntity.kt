package com.instant.mvi.data.database.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "schools", // Sets the table name in the database to "schools".
    foreignKeys = [ForeignKey(
        entity = UserEntity::class, // Establishes a relationship to the UserEntity table.
        parentColumns = ["id"], // Specifies the "id" column in UserEntity as the primary key that this foreign key references.
        childColumns = ["userId"], // Specifies that "userId" in this table (SchoolEntity) is the foreign key column.
        onDelete = ForeignKey.CASCADE // Deletes all associated SchoolEntity records if the referenced UserEntity record is deleted.
    )],
    indices = [Index("userId")] // Creates an index on the "userId" column to optimize queries involving this foreign key.
)
data class SchoolEntity(
    @PrimaryKey(autoGenerate = true) val schoolId: Int = 0, // Primary key for SchoolEntity, auto-generates a unique ID for each record.
    val userId: Int, // Foreign key column linking each school to a user in the UserEntity table.
    val schoolName: String, // Stores the name of the school.
    val address: String // Stores the address of the school.
)

