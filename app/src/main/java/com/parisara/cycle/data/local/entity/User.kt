package com.parisara.cycle.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class User(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val name: String,
    val email: String,
    val mobile: String,
    val passwordHash: String // Storing as plaintext for now as discussed, naming it hash for future-proofing
)
