package com.ebookfrenzy.hyrule.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.ebookfrenzy.hyrule.MainActivity
import com.ebookfrenzy.hyrule.R
import com.ebookfrenzy.hyrule.adapter.SearchAdapter
import com.ebookfrenzy.hyrule.databinding.SavedEntryFragmentBinding
import com.ebookfrenzy.hyrule.model.Entries
import com.ebookfrenzy.hyrule.model.Entry
import com.ebookfrenzy.hyrule.utils.Constants
import com.ebookfrenzy.hyrule.utils.Converter
import com.ebookfrenzy.hyrule.viewmodel.HyruleViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SavedEntryFragment : Fragment() {
    private val viewModel: HyruleViewModel by viewModels()
    private lateinit var binding: SavedEntryFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = SavedEntryFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = SearchAdapter()
        viewModel.getSavedEntries().observe(viewLifecycleOwner, { entries ->
            adapter.submitList(Converter.convertEntriesToCategoryItems(Entries(data = entries)))

            (activity as MainActivity).supportActionBar?.title = if (entries.size > 1) "Saved Entries" else "Saved Entry"
        })
        binding.apply {
            recyclerEntries.adapter = adapter
            recyclerEntries.layoutManager = LinearLayoutManager(requireContext())
        }
        adapter.setItemClickListener { name ->
            viewModel.getSavedEntry(name)
        }

        viewModel.savedEntry.observe(viewLifecycleOwner, {
            it.consume { entry ->
                val bundle = Bundle().apply {
                    putParcelable("entry", Entry(entry))
                    putString("launchedFrom", Constants.SAVED_ENTRY_FRAGMENT)
                }
                findNavController().navigate(
                    R.id.action_savedEntryFragment_to_entryFragment,
                    bundle
                )
            }
        })
    }
}