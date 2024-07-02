package com.example.notionpad.models

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.notionpad.database.NoteRepository

class NoteViewModelFactory(private val application: Application, private val noteRepository: NoteRepository): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return NoteViewModel(application, noteRepository) as T
    }
}
