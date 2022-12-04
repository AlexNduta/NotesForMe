package com.example.notesforme.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.notesforme.Models.Note
import com.example.notesforme.R
import kotlin.random.Random

//This is our recyclerview Adapter
//Also pass our created interface in  our Adapter
class NotesAdapter(private val context:Context, val listener:NotesClickListener):RecyclerView.Adapter<noteViewHolder>() {

    private val NoteList= ArrayList<Note>() //This list should display all the items in our recyclerview
    private val FullList = ArrayList<Note>()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): noteViewHolder {
        //return the object of the viewHolder
        return noteViewHolder(
            LayoutInflater.from(context).inflate(R.layout.list_item,parent, false))
    }

    override fun onBindViewHolder(holder: noteViewHolder, position: Int) {
       val currentNotesList = NoteList[position]
        holder.title.text= currentNotesList.title //This binds our title content to the textview referenced in the view holder
        holder.title.isSelected = true
        holder.Note_tv.text= currentNotesList.note
        holder.date.text = currentNotesList.date
        holder.date.isSelected = true
        //change the card background color to a random color
        holder.notes_Item.setCardBackgroundColor(holder.itemView.resources.getColor(RandomColor()))

        //set the click event on our cardview
        holder.notes_Item.setOnClickListener{

            //Set the listener on the item that has been clicked
            listener.onItemClicked(NoteList[holder.adapterPosition])

        }

        //set the clickListener on the Long Item clicked
        holder.notes_Item.setOnLongClickListener {
            listener.onLongItemClicked(NoteList[holder.adapterPosition], holder.notes_Item)

            true
        }
    }

    override fun getItemCount(): Int {
   return  NoteList.size
    }
//Create a method to update the list
    fun updateList(newList: List<Note>){
        FullList.clear()
        FullList.addAll(NoteList)
        NoteList.clear()
        NoteList.addAll(FullList)
    }

    //Create a method to filter out options during serching
 fun filter(search:String){

     FullList.clear()
        for (item in FullList)// we Iterate in the fulllist
        {
            if (item.title?.lowercase()?.contains(search.lowercase()) == true ||
                        item.note?.lowercase()?.contains(search.lowercase()) == true){
                NoteList.add(item)
            }

        }
        notifyDataSetChanged()
 }


    //Create a random color generator
    fun RandomColor():Int{
        val list= ArrayList<Int>()
        list.add(R.color.Note1)
        list.add(R.color.Note2)
        list.add(R.color.Note3)
        list.add(R.color.Note4)
        list.add(R.color.Note5)
        list.add(R.color.Note6)
        list.add(R.color.Note7)
        list.add(R.color.Note8)
        list.add(R.color.Note9)

    val seed = System.currentTimeMillis().toInt()
        val randomIndex= Random(seed).nextInt(list.size)
        return list[randomIndex]

    }

    //create an interface we will use to interact with the MainActity
    //This function will assist us in creating or updating a note when the note is clicked
    interface NotesClickListener{
        fun onItemClicked(note:Note)
        fun onLongItemClicked(note: Note, cardview:CardView)
    }
}

class noteViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){

    //Reference our cardView and all the elements that are in it
    val notes_Item = itemView.findViewById<CardView>(R.id.card_layout)
    val title = itemView.findViewById<TextView>(R.id.tv_title)
    val Note_tv= itemView.findViewById<TextView>(R.id.tv_note)
    val date = itemView.findViewById<TextView>(R.id.tv_date)

}