package com.example.notesforme.Database

import androidx.lifecycle.LiveData
import com.example.notesforme.Models.Note
//Our repository class acts as an intermediate to our main activity

class NotesRepository(private val notedao:NoteDao) {

    val allnotes:LiveData<List<Note>> = notedao.getAllNotes()

    suspend fun insert(note:Note){
        notedao.insert(note)
    }

    suspend fun delete(note: Note){
        notedao.delete()
    }
    suspend fun update(note: Note){
        notedao.update(note.id, note.title,note.note)
    }

}