package com.example.uasandroid.ui

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.uasandroid.data.RetrofitClient
import com.example.uasandroid.databinding.ActivityUpdateNoteBinding
import com.example.uasandroid.model.Note
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UpdateNoteActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUpdateNoteBinding
    private var noteId: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Inisialisasi View Binding
        binding = ActivityUpdateNoteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Ambil data dari Intent
        noteId = intent.getIntExtra("note_id", 0)
        val title = intent.getStringExtra("note_title") ?: ""
        val content = intent.getStringExtra("note_content") ?: ""

        // Set data ke EditText
        binding.updateTitleEditText.setText(title)
        binding.updateContentEditText.setText(content)

        // Handle klik tombol update
        binding.updateSaveButton.setOnClickListener {
            val updatedTitle = binding.updateTitleEditText.text.toString()
            val updatedContent = binding.updateContentEditText.text.toString()

            if (updatedTitle.isNotBlank() && updatedContent.isNotBlank()) {
                updateNote(Note(id = noteId, title = updatedTitle, content = updatedContent))
            } else {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun updateNote(note: Note) {
        RetrofitClient.instance.updateNote(note.id, note).enqueue(object : Callback<Note> {
            override fun onResponse(call: Call<Note>, response: Response<Note>) {
                if (response.isSuccessful) {
                    Toast.makeText(this@UpdateNoteActivity, "Note updated", Toast.LENGTH_SHORT).show()
                    finish()
                }
            }

            override fun onFailure(call: Call<Note>, t: Throwable) {
                Toast.makeText(this@UpdateNoteActivity, "Failed to update note", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
