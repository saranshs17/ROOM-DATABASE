package com.example.roomdatabase.Room.Dao

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "contact")
data class Contact(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val name: String,
    val phone: String,
    val date: Date,
    // in version 2 have to add another field isActive
    val isActive: Int
)