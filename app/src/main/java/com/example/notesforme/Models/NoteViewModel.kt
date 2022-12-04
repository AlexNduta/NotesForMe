package com.example.notesforme.Models

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.notesforme.Database.NotesDatabase
import com.example.notesforme.Database.NotesRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class NoteViewModel(application: Application):AndroidViewModel(application) {
    private val repository:NotesRepository

    val allNotes:LiveData<List<Note>>

    init {
        val dao= NotesDatabase.getDatabase(application).getNoteDao()
        repository = NotesRepository(dao)
        allNotes= repository.allnotes
    }
    //create functions to interact with our NotesRepository class that intercats with the noteDao method

    fun deleteNote(note:Note)= viewModelScope.launch(Dispatchers.IO){
        repository.delete(note)
    }

    fun insertNote(note:Note)= viewModelScope.launch(Dispatchers.IO){
        repository.insert(note)
    }
    fun updateNote(note:Note)= viewModelScope.launch(Dispatchers.IO){
        repository.update(note)
    }



}