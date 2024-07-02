package com.example.notionpad.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.notionpad.R
import com.example.notionpad.databinding.ListItemsBinding
import com.example.notionpad.models.Note

class NoteAdapter(private val context: Context, private val listener: NotesClickListener): RecyclerView.Adapter<NoteAdapter.NoteViewHolder>() {

    private val notesList = ArrayList<Note>()
    private val notesListFull = ArrayList<Note>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val binding = ListItemsBinding.inflate(LayoutInflater.from(context), parent, false)
        return NoteViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return notesList.size
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        val note = notesList[position]
        holder.binding.tvTitle.text = note.title
        holder.binding.tvTitle.isSelected = true
        holder.binding.tvNote.text = note.note
        holder.binding.tvDate.text = note.date
        holder.binding.tvDate.isSelected = true

        holder.binding.cardView.setOnClickListener {
            listener.onItemClicked(notesList[holder.adapterPosition])
        }
        holder.binding.cardView.setOnLongClickListener {
            listener.onLongItemClicked(notesList[holder.adapterPosition], holder.binding.cardView)
            true
        }

    }

    fun updateList(newList: List<Note>){
        notesListFull.clear()
        notesListFull.addAll(newList)
        notesList.clear()
        notesList.addAll(newList)
        notifyDataSetChanged()
    }

    fun filterList(search: String){
        notesList.clear()
        for (item in notesListFull){
            if (item.title?.lowercase()?.contains(search.lowercase()) == true || item.note?.lowercase()?.contains(search.lowercase()) == true){
                notesList.add(item)
            }
        }
        notifyDataSetChanged()
    }

    inner class NoteViewHolder(val binding: ListItemsBinding): RecyclerView.ViewHolder(binding.root)

    interface NotesClickListener{
        fun onItemClicked(note: Note)
        fun onLongItemClicked(note: Note, cardView: CardView)
    }

}