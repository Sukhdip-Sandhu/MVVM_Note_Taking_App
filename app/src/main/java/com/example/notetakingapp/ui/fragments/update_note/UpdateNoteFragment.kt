package com.example.notetakingapp.ui.fragments.update_note

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.core.view.get
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.notetakingapp.R
import com.example.notetakingapp.databinding.FragmentUpdateNoteBinding
import com.example.notetakingapp.models.Note
import com.example.notetakingapp.models.Priority
import com.example.notetakingapp.ui.viewmodels.NotesViewModel
import com.example.notetakingapp.util.TimeUtil
import com.google.android.material.chip.Chip
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class UpdateNoteFragment : Fragment() {

    private val notesViewModel: NotesViewModel by viewModels()

    private var _binding: FragmentUpdateNoteBinding? = null
    private val binding get() = _binding!!

    private val args by navArgs<UpdateNoteFragmentArgs>()

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentUpdateNoteBinding.inflate(inflater, container, false)
        val view = binding.root

        setHasOptionsMenu(true)

        setUpdateViewsFromArgs()

        return view
    }

    private fun setUpdateViewsFromArgs() {
        binding.updateTitleEt.setText(args.currentNote.title)
        binding.updateContentEt.setText(args.currentNote.content)

        when (args.currentNote.priority) {
            Priority.LOW -> {
                binding.updateChipGroup.check(binding.updateChipGroup[0].id)
            }
            Priority.MEDIUM -> {
                binding.updateChipGroup.check(binding.updateChipGroup[1].id)
            }
            Priority.HIGH -> {
                binding.updateChipGroup.check(binding.updateChipGroup[2].id)
            }
        }

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.update_note_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_update_note -> {
                updateNote()
            }
            R.id.menu_delete_note -> {
                deleteNote()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun deleteNote() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setIcon(R.drawable.ic_delete_forever)
        builder.setTitle("Delete this note?")
        builder.setMessage("Are you sure you want to remove this note? This cannot be undone.")
        builder.setPositiveButton("Yes") { _: DialogInterface, _: Int ->
            notesViewModel.deleteNote(args.currentNote)
            findNavController().navigate(R.id.action_updateNoteFragment_to_notesListFragment)
            Toast.makeText(requireContext(), "Successfully deleted note!", Toast.LENGTH_SHORT)
                    .show()
        }
        builder.setNegativeButton("No") { _, _ -> }
        builder.create().show()

    }

    private fun updateNote() {
        val titleText = binding.updateTitleEt.text.toString()
        val contentText = binding.updateContentEt.text.toString()
        if (notesViewModel.noteIsValid(titleText, contentText)) {
            val note = Note(
                    args.currentNote.id,
                    titleText,
                    contentText,
                    TimeUtil.getCurrentTime(),
                    getChipPriority()
            )
            notesViewModel.updateNote(note)
            Toast.makeText(context, "Successfully updated note", Toast.LENGTH_SHORT).show()
            findNavController().navigate(R.id.action_updateNoteFragment_to_notesListFragment)
        } else {
            Toast.makeText(context, "Please fill in the fields!", Toast.LENGTH_SHORT).show()
        }
    }

    private fun getChipPriority(): Priority {
        val chipsCount = binding.updateChipGroup.childCount
        var i = 0
        while (i < chipsCount) {
            val chip = binding.updateChipGroup.getChildAt(i) as Chip
            if (chip.isChecked) {
                return Priority.valueOf(chip.text.toString().toUpperCase(Locale.getDefault()))
            }
            i++
        }
        return Priority.LOW
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}