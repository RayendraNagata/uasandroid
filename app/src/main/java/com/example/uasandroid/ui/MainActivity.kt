package com.example.uasandroid.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.uasandroid.adapter.NoteAdapter
import com.example.uasandroid.data.RetrofitClient
import com.example.uasandroid.databinding.ActivityMainBinding
import com.example.uasandroid.model.Note
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var noteAdapter: NoteAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Setup RecyclerView
        noteAdapter = NoteAdapter()
        binding.notesRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.notesRecyclerView.adapter = noteAdapter

        // Load notes
        loadNotes()

        // Add note button
        binding.addButton.setOnClickListener {
            startActivity(Intent(this, AddNoteActivity::class.java))
        }

        // Handle item clicks
        noteAdapter.setOnItemClickListener(object : NoteAdapter.OnItemClickListener {
            override fun onUpdateClick(note: Note) {
                val intent = Intent(this@MainActivity, UpdateNoteActivity::class.java).apply {
                    putExtra("note_id", note.id)
                    putExtra("note_title", note.title)
                    putExtra("note_content", note.content)
                }
                startActivity(intent)
            }

            override fun onDeleteClick(note: Note) {
                deleteNote(note.id!!)
            }
        })
    }

    private fun loadNotes() {
        RetrofitClient.instance.getAllNotes().enqueue(object : Callback<List<Note>> {
            override fun onResponse(call: Call<List<Note>>, response: Response<List<Note>>) {
                if (response.isSuccessful) {
                    noteAdapter.setNotes(response.body() ?: emptyList())
                }
            }

            override fun onFailure(call: Call<List<Note>>, t: Throwable) {
                Toast.makeText(this@MainActivity, "Failed to load notes", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun deleteNote(id: Int) {
        RetrofitClient.instance.deleteNote(id).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    Toast.makeText(this@MainActivity, "Note deleted", Toast.LENGTH_SHORT).show()
                    loadNotes()
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Toast.makeText(this@MainActivity, "Failed to delete note", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
