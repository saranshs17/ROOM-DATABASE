package com.example.roomdatabase.Room.Dao

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.roomdatabase.Room.Converters.Converters

@Database(entities = [Contact::class], version = 2) // from 1 to 2 version
@TypeConverters(Converters::class)
abstract class ContactDatabase :
    RoomDatabase() {   // contain methods without body, with body or mixture of both
    // They are designed to be used as a base class
    // Abstract classes are used to hide the intricate code implementation details from the user.
    // This is                                       // called Data Abstraction in Object-Oriented Programming

    abstract fun contactDao(): ContactDAO

    // Singelton Pattern thread safe approach prevents creation of more than 1 instance of database
    companion object { // only one companion object of a class can exist

        // Migration from 1 to 2 will only for user which has previous intalled app and has database version 1
        private val migration_1_2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("ALTER TABLE contact ADD COLUMN isActive INTEGER NOT NULL DEFAULT(1)")
            }
        }

        @Volatile // all thread get new updated value as they get to know that value of INSTANCE is updated
        private var INSTANCE: ContactDatabase? = null

        fun getDatabase(context: Context): ContactDatabase {
            if (INSTANCE == null) {
                synchronized(this) { // prevent creation of more than 1 instance if multiple threads are accessing this block simultaneously
                    INSTANCE = Room.databaseBuilder(
                        context.applicationContext,
                        ContactDatabase::class.java,
                        "contactDB"
                    )
                        .addMigrations(migration_1_2)
                        .build()
                }
            }
            return INSTANCE!! // since INSTANCE(nullable but we want non null so used !!) is not null, return it
        }
    }
}