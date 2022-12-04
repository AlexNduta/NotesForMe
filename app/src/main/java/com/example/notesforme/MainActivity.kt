package com.example.notesforme

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Adapter
import android.widget.LinearLayout
import android.widget.PopupMenu
import android.widget.SearchView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.cardview.widget.CardView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.notesforme.Adapter.NotesAdapter
import com.example.notesforme.Database.NotesDatabase
import com.example.notesforme.Models.Note
import com.example.notesforme.Models.NoteViewModel
import com.example.notesforme.databinding.ActivityMainBinding

class  MainActivity : AppCompatActivity(), NotesAdapter.NotesClickListener, PopupMenu.OnMenuItemClickListener {

    //Initialise all our methods
    private  lateinit var _binding:ActivityMainBinding
    lateinit var database:NotesDatabase
    lateinit var viewModel: NoteViewModel
    lateinit var adapter:NotesAdapter
    lateinit var selectedNote:Note


//If the user taps on a note on our recyclerview, We call this initialised function
    //what the function basicly does is launch the AddNote activity so as to update the existing record of our note
    private  val updateNote= registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
        result->
        if (result.resultCode == Activity.RESULT_OK){
            val note =result.data?.getSerializableExtra("note") as? Note
            if (note!= null){
                //if the note is not null then we reference the viewmodel's update method
                //the updateNote fuction uses coroutines to update our note on the background thread
                viewModel.updateNote(note)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding= ActivityMainBinding.inflate(layoutInflater)
        setContentView(_binding.root)
    //Initializing the UI

        initUI()

        //initialise the viemodel
        viewModel= ViewModelProvider(this,
            ViewModelProvider.AndroidViewModelFactory.getInstance(application)).get(NoteViewModel::class.java)
//livedata observer
        viewModel.allNotes.observe(this){
            list -> list?.let {
                adapter.updateList(list)
        }
        }
    }
//Initialize our recyclerview and set the layout
    private fun initUI() {
        _binding.recyclerView.setHasFixedSize(true)
        _binding.recyclerView.layoutManager= StaggeredGridLayoutManager(2, LinearLayout.VERTICAL)
adapter = NotesAdapter(this, this)
_binding.recyclerView.adapter= adapter


    //Code to resgister content using out FAB
    //This isntance is used to create an new note
    //NB: these are inbuilt methods
    val getContent= registerForActivityResult(ActivityResultContracts.StartActivityForResult()){

        result ->
//if the result code is ok
        if(result.resultCode== Activity.RESULT_OK){

            val note = result.data?.getSerializableExtra("note") as Note
            if (note !=null){
                //We call the viewModel's insertNote
                //The insertNote uses coroutines to insert a note
    viewModel.insertNote(note)

            }
        }

    }

    //When our user clicks on our FAB, We launch the addNote activity
    _binding.fbAddNote.setOnClickListener{

        val intent= Intent(this, AddNote::class.java)
        //We reference the getCote content variable that was used to initialise the insertNote code
            //We launch our new activity
        getContent.launch(intent)
    }
//Our searchview code
    _binding.searchView2.setOnQueryTextListener(
        object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            //This method is called whenever a user types in anything in the search view
            override fun onQueryTextChange(newText: String?): Boolean {
//We call our filterList() created in our Adapter class
                if (newText != null) {
                    adapter.filter(newText)
                }
                return true
            }
        })

    }
    //these 2 implemented methods will be used to eneble us to make out notes clickable  for update

    override fun onItemClicked(note: Note) {
//Navigate to the AddNote Activity
        val intent= Intent(this@MainActivity, AddNote::class.java)
        //We will pass the contents of the note to the nest activity
        intent.putExtra("current_note", note)
        //Launch our intent by referencing out our initialised class
        updateNote.launch(intent)

    }
//If the itemis clicked for long, a popup menu promting us to delete out note will appear
    override fun onLongItemClicked(note: Note, cardview: CardView) {

        selectedNote= note
    //if it is selected for long,  call our popUp method
        popUpDisplay(cardview)
    }

    //Our method creates a popup menue
    private fun popUpDisplay(cardview: CardView) {
        //inititialise our inbuilt popupMenue method
    val popup= PopupMenu(this, cardview)
        popup.setOnMenuItemClickListener(this@MainActivity)
        //it i nflates our view
        popup.inflate(R.menu.pop_up_menu)
    }
//
    override fun onMenuItemClick(item: MenuItem?): Boolean {
       // if the item id is the smae as the selected noe, delete it
        if (item?.itemId== R.id.delete_note){
            //We call our viewmodel's delete method and pass the selected note
            //the delete note refences the delete query in our noteDao
            viewModel.deleteNote(selectedNote)
            return true
        }
        return false
    }
}