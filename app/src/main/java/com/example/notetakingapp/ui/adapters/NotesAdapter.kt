package com.example.notetakingapp.ui.adapters

import android.content.Context
import android.view.ContextMenu
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.notetakingapp.util.NotesDiffUtil
import com.example.notetakingapp.R
import com.example.notetakingapp.databinding.ItemNoteBinding
import com.example.notetakingapp.models.Note
import com.example.notetakingapp.models.Priority
import com.example.notetakingapp.ui.fragments.notes_list.NotesListFragmentDirections
import com.example.notetakingapp.util.TimeUtil

class NotesAdapter : RecyclerView.Adapter<NotesAdapter.ViewHolder>() {

    var listOfNotes = emptyList<Note>()
    var listOfNotesAll = emptyList<Note>()

    class ViewHolder(private val itemNoteBinding: ItemNoteBinding) :
        RecyclerView.ViewHolder(itemNoteBinding.root) {
        fun bind(note: Note, context: Context) {
            itemNoteBinding.titleTv.text = note.title
            itemNoteBinding.contentTv.text = note.content
            itemNoteBinding.dateTv.text = TimeUtil.getDateFormat(note.date)

            when (note.priority) {
                Priority.LOW -> {
                    itemNoteBinding.priorityColorView.backgroundTintList =
                        ContextCompat.getColorStateList(context, R.color.low_priority_color)
                    itemNoteBinding.priorityColorView.text = context.getString(R.string.low)
                }
                Priority.MEDIUM -> {
                    itemNoteBinding.priorityColorView.backgroundTintList =
                        ContextCompat.getColorStateList(context, R.color.medium_priority_color)
                    itemNoteBinding.priorityColorView.text = context.getString(R.string.medium)
                }
                Priority.HIGH -> {
                    itemNoteBinding.priorityColorView.backgroundTintList =
                        ContextCompat.getColorStateList(context, R.color.high_priority_color)
                    itemNoteBinding.priorityColorView.text = context.getString(R.string.high)
                }
            }

            itemNoteBinding.noteItemLayout.setOnClickListener {
                val action =
                    NotesListFragmentDirections.actionNotesListFragmentToUpdateNoteFragment(note)
                it.findNavController().navigate(action)
            }

            itemNoteBinding.noteItemLayout.setOnCreateContextMenuListener { contextMenu, view, _ ->
                contextMenu?.setHeaderTitle("Select a priority")
                contextMenu?.add(0, 1, 0, "Low")?.setOnMenuItemClickListener {
                    note.priority = Priority.LOW
                    val action =
                        NotesListFragmentDirections.actionNotesListFragmentToUpdateNoteFragment(note)
                    view.findNavController().navigate(action)
                    true
                }
                contextMenu?.add(0, 2, 0, "Medium")?.setOnMenuItemClickListener {
                    note.priority = Priority.MEDIUM
                    val action =
                        NotesListFragmentDirections.actionNotesListFragmentToUpdateNoteFragment(note)
                    view.findNavController().navigate(action)
                    true
                }
                contextMenu?.add(0, 2, 0, "High")?.setOnMenuItemClickListener {
                    note.priority = Priority.HIGH
                    val action =
                        NotesListFragmentDirections.actionNotesListFragmentToUpdateNoteFragment(note)
                    view.findNavController().navigate(action)
                    true
                }
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemNoteBinding =
            ItemNoteBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(itemNoteBinding)
    }

    override fun getItemCount(): Int {
        return listOfNotes.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val note = listOfNotes[position]
        holder.bind(note, holder.itemView.context)
    }

    fun setNotesList(newListOfNotes: List<Note>) {
        val notesDiffResult = DiffUtil.calculateDiff(
            NotesDiffUtil(
                oldNotesList = listOfNotes,
                newNotesList = newListOfNotes
            )
        )
        this.listOfNotes = newListOfNotes
        notesDiffResult.dispatchUpdatesTo(this)
    }

    fun setNotesListAll(notesListAll: List<Note>?) {
        listOfNotesAll = notesListAll!!
    }

}