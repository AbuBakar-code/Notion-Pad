package com.example.notionpad.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.notionpad.models.Note

@Dao
interface NoteDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNote(note: Note)

    @Update
    suspend fun updateNote(note: Note)

    @Delete
    suspend fun deleteNote(note: Note)

    @Query("SELECT * FROM notes_table ORDER BY id DESC")
    fun getAllNotes(): LiveData<List<Note>>
}
