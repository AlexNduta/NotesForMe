package com.example.notesforme

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract.CommonDataKinds.Note
import android.widget.Toast
import com.example.notesforme.databinding.ActivityAddNoteBinding
import com.example.notesforme.databinding.ActivityMainBinding
import java.text.SimpleDateFormat
import java.util.Date
import java.util.logging.SimpleFormatter

class AddNote : AppCompatActivity() {
    private lateinit var _binding:ActivityAddNoteBinding
    private lateinit var note:com.example.notesforme.Models.Note // This is the new new updted note
    private lateinit var old_note :com.example.notesforme.Models.Note //This the old note
    var isUpdated = false //checks if our note is updated



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding= ActivityAddNoteBinding.inflate(layoutInflater)
        setContentView(_binding.root)

        //We put the code in the try catch block so that incase isUpdated is true,
        // we know that the activity was called to update the note
        try{
            old_note= intent.getSerializableExtra("current_note") as Note
            _binding.etTitle.setText(old_note.title)
            _binding.etName.setText(old_note.note)
            isUpdated= true

        } catch ( e:Exception){
            e.printStackTrace()
        }
//Whenever we clickon the check icon, they either want o save the note or create a new note
        _binding.imgCheck.setOnClickListener{

            val title= _binding.etTitle.toString()
            val note_desc = _binding.etName.toString()
            //if our fields are not empty,set the date to the form,at given
            if (title.isNotEmpty() || note_desc.isNotEmpty()){
                val formatter = SimpleDateFormat("EEE, d MMM yyyy HH:mm a")

                if (isUpdated){
                    note= com.example.notesforme.Models.Note(old_note.id, title, note, formatter.format(Date())
                    )
                }else{
                    note = com.example.notesforme.Models.Note(null, title, note_desc, formatter.format(Date())
                    )
                }

           val intent= Intent()
                intent.putExtra("note", note)
                setResult(Activity.RESULT_OK, intent)
                finish()
            } else {
                Toast.makeText(this@AddNote, "please enter some data", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

        }

        //If the back icon is pressed, call the onBackPressed()
        _binding.imgBackButton.setOnClickListener {
            onBackPressed()
        }

    }
}