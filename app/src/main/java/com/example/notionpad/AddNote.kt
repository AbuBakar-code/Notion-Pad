package com.example.notionpad

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.notionpad.databinding.ActivityAddNoteBinding
import com.example.notionpad.models.Note
import java.text.SimpleDateFormat
import java.util.Date

class AddNote : AppCompatActivity() {
    private lateinit var binding: ActivityAddNoteBinding
    private lateinit var note: Note
    private lateinit var oldNote: Note
    private var isUpdated = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityAddNoteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        try {
            oldNote = intent.getParcelableExtra("current_note")!!
            binding.etTitle.setText(oldNote.title)
            binding.etDescription.setText(oldNote.note)
            isUpdated = true
        }catch (e: Exception){
            e.printStackTrace()
        }

        binding.saveNote.setOnClickListener {
            val title = binding.etTitle.text.toString()
            val description = binding.etDescription.text.toString()
            if (title.isNotEmpty() || description.isNotEmpty()) {
                val formatter = SimpleDateFormat("EEE, d MMM yyyy HH:mm a")
                if (isUpdated) {
                    note = Note(oldNote.id, title, description, formatter.format(Date()))
                } else {
                    note = Note(null, title, description, formatter.format(Date()))
                }
                val intent = Intent()
                intent.putExtra("note", note)
                setResult(Activity.RESULT_OK, intent)
                finish()
            } else {
                Toast.makeText(this, "Please enter some data", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
        }

        binding.backNote.setOnClickListener {
            onBackPressed()
            finish()
        }

    }
}