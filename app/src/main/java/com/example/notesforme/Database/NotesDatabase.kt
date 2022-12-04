package com.example.notesforme.Database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.notesforme.Models.Note
import com.example.notesforme.Utilities.DATABASE_NAME


@Database(entities = arrayOf(Note::class), version= 1, exportSchema = false)
abstract class NotesDatabase:RoomDatabase() {

    abstract fun getNoteDao(): NoteDao

    //Create a singletone
    companion object{
    @Volatile
    private var INSTANCE :NotesDatabase? = null
        fun getDatabase(context: Context):NotesDatabase{
            return INSTANCE?: synchronized(this){
                val instance = Room.databaseBuilder(
                    context.applicationContext,NotesDatabase::class.java,
                    DATABASE_NAME
                ).build()
                INSTANCE= instance
                instance
            }
        }

    }
}