package com.example.uasandroid.ui

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.uasandroid.data.RetrofitClient
import com.example.uasandroid.databinding.ActivityAddNoteBinding
import com.example.uasandroid.model.Note
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AddNoteActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddNoteBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddNoteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.saveButton.setOnClickListener {
            val title = binding.titleEditText.text.toString()
            val content = binding.contentEditText.text.toString()

            if (title.isNotBlank() && content.isNotBlank()) {
                addNote(Note(title = title, content = content))
            } else {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun addNote(note: Note) {
        RetrofitClient.instance.createNote(note).enqueue(object : Callback<Note> {
            override fun onResponse(call: Call<Note>, response: Response<Note>) {
                if (response.isSuccessful) {
                    Toast.makeText(this@AddNoteActivity, "Note added", Toast.LENGTH_SHORT).show()
                    finish()
                }
            }

            override fun onFailure(call: Call<Note>, t: Throwable) {
                Toast.makeText(this@AddNoteActivity, "Failed to add note", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
