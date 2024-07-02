package com.example.notionpad

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.PopupMenu
import android.widget.SearchView
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.notionpad.adapter.NoteAdapter
import com.example.notionpad.database.NoteDatabase
import com.example.notionpad.database.NoteRepository
import com.example.notionpad.databinding.ActivityMainBinding
import com.example.notionpad.models.Note
import com.example.notionpad.models.NoteViewModel
import com.example.notionpad.models.NoteViewModelFactory

class MainActivity : AppCompatActivity(), NoteAdapter.NotesClickListener, PopupMenu.OnMenuItemClickListener {
    private lateinit var binding: ActivityMainBinding
    private lateinit var noteViewModel: NoteViewModel
    private lateinit var noteAdapter: NoteAdapter
    private lateinit var selectedNote: Note

    private val updateNote = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val note = result.data?.getParcelableExtra<Note>("note")
            if (note != null) {
                noteViewModel.updateNote(note)
            }
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initUI()

        setUpViewModel()

        noteViewModel.getAllNotes().observe(this, Observer {list ->
            noteAdapter.updateList(list)
        })
    }

    private fun setUpViewModel(){
        val noteRepository = NoteRepository(NoteDatabase(this))
        val viewModelProviderFactory = NoteViewModelFactory(application, noteRepository)
        noteViewModel = ViewModelProvider(this, viewModelProviderFactory)[NoteViewModel::class.java]
    }

    private fun initUI() {
        binding.recyclerView.setHasFixedSize(true)
        binding.recyclerView.layoutManager = StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL)
        noteAdapter = NoteAdapter(this, this)
        binding.recyclerView.adapter = noteAdapter

        val getContent = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val note = result.data?.getParcelableExtra<Note>("note")
                if (note != null) {
                    noteViewModel.addNote(note)
                }
            }
        }
        binding.fabAddNote.setOnClickListener {
            val intent = Intent(this, AddNote::class.java)
            getContent.launch(intent)
        }
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(p0: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText != null) {
                    noteAdapter.filterList(newText)
                }
                return true
            }
        })
    }

    override fun onItemClicked(note: Note) {
        val intent = Intent(this@MainActivity, AddNote::class.java)
        intent.putExtra("current_note", note)
        updateNote.launch(intent)
    }

    override fun onLongItemClicked(note: Note, cardView: CardView) {
        selectedNote = note
        popupDisplay(cardView)
    }

    private fun popupDisplay(cardView: CardView) {
        val popupMenu = PopupMenu(this, cardView)
        popupMenu.setOnMenuItemClickListener(this@MainActivity)
        popupMenu.inflate(R.menu.pop_up_menu)
        popupMenu.show()
    }

    override fun onMenuItemClick(item: MenuItem?): Boolean {
        if (item?.itemId == R.id.deleteNote) {
            noteViewModel.deleteNote(selectedNote)
            return true
        }
        return false
    }

}