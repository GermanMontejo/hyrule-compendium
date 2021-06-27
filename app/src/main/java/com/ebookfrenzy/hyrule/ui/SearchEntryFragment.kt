package com.ebookfrenzy.hyrule.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.ebookfrenzy.hyrule.R
import com.ebookfrenzy.hyrule.adapter.SearchAdapter
import com.ebookfrenzy.hyrule.databinding.SearchEntryFragmentBinding
import com.ebookfrenzy.hyrule.utils.Constants
import com.ebookfrenzy.hyrule.utils.Converter
import com.ebookfrenzy.hyrule.utils.EntryResource
import com.ebookfrenzy.hyrule.viewmodel.HyruleViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SearchEntryFragment : Fragment() {
    private val args: SearchEntryFragmentArgs by navArgs()
    private lateinit var binding: SearchEntryFragmentBinding
    private val viewModel: HyruleViewModel by viewModels()
    private var query: String = ""
    private var entryFrom = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = SearchEntryFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val adapter = SearchAdapter()

        binding.apply {
            args.query?.let {
                query = it
            }
            if (query != "none") {
                editTextSearch.setText(query)
                searchProcessor(query)
            }

            var job: Job? = null
            editTextSearch.addTextChangedListener { editable ->
                job?.cancel()
                job = MainScope().launch {
                    delay(500)
                    editable?.let {
                        if (it.toString().isNotBlank()) {
                            searchProcessor(it.toString())
                            entryFrom = "search"
                        }
                    }
                }
            }

            recyclerViewSearch.layoutManager = LinearLayoutManager(requireContext())
            recyclerViewSearch.adapter = adapter
        }

        viewModel.entriesLiveData.observe(viewLifecycleOwner, {
            run {
                it.consume { entries ->
                    when (entries) {
                        is EntryResource.Loading -> {
                            showProgressBar()
                        }
                        is EntryResource.Success -> {
                            hideProgressBar()
                            binding.textViewNotFound.visibility = View.INVISIBLE
                            entries.data?.let {
                                adapter.submitList(Converter.convertEntriesToCategoryItems(it))
                            }
                        }
                        is EntryResource.Failure -> {
                            binding.textViewNotFound.visibility = View.VISIBLE
                            hideProgressBar()
                            Snackbar.make(
                                binding.root,
                                entries.message.toString(),
                                Snackbar.LENGTH_LONG
                            )
                                .show()
                        }
                    }
                }

            }
        })

        viewModel.creaturesLiveData.observe(viewLifecycleOwner, {
            run {
                it.consume { creatures ->
                    when (creatures) {
                        is EntryResource.Loading -> {
                            showProgressBar()
                        }
                        is EntryResource.Success -> {
                            hideProgressBar()
                            binding.textViewNotFound.visibility = View.INVISIBLE
                            creatures.data?.let {
                                adapter.submitList(Converter.convertCreaturesToCategoryItems(it.data))
                            }
                        }
                        is EntryResource.Failure -> {
                            hideProgressBar()
                            binding.textViewNotFound.visibility = View.VISIBLE

                            Snackbar.make(
                                binding.root,
                                creatures.message.toString(),
                                Snackbar.LENGTH_LONG
                            ).show()
                        }
                    }
                }

            }
        })

        adapter.setItemClickListener { entry ->
            entryFrom = "click"
            viewModel.getEntry(entry.replace(" ", "_"))
        }

        viewModel.entryLiveData.observe(viewLifecycleOwner, {
            run {
                it.consume { entry ->

                    when (entry) {
                        is EntryResource.Loading -> {
                            showProgressBar()
                            binding.textViewNotFound.visibility = View.INVISIBLE
                        }
                        is EntryResource.Success -> {
                            hideProgressBar()
                            if (entryFrom == "click") {
                                val bundle = Bundle().apply {
                                    putParcelable("entry", entry.data)
                                    putString("launchedFrom", Constants.SEARCH_ENTRY_FRAGMENT)
                                }

                                findNavController().navigate(
                                    R.id.action_searchEntryFragment_to_entryFragment,
                                    bundle
                                )
                            } else {
                                if (entry.data?.data?.category == null && entry.data?.data?.id == 0) {
                                    // no results
                                    binding.textViewNotFound.visibility = View.VISIBLE
                                    adapter.submitList(mutableListOf())
                                } else {
                                    // there was a result.
                                    binding.textViewNotFound.visibility = View.INVISIBLE
                                    adapter.submitList(Converter.convertEntryToCategoryItems(entry.data?.data!!))
                                }
                            }
                        }
                        is EntryResource.Failure -> {
                            hideProgressBar()
                            Snackbar.make(binding.root, "An error occurred: ${entry.message}", Snackbar.LENGTH_LONG).show()
                        }
                    }
                }
            }
        })
    }

    private fun searchProcessor(query: String) {
        val categories = resources.getStringArray(R.array.categories)
        query.let {
            if (categories.contains(it.capitalize()))
                viewModel.getEntriesByCategory(it)
            else
                viewModel.getEntry(it)
        }
    }

    private fun showProgressBar() {
        binding.progressBar.visibility = View.VISIBLE

        requireActivity().window.setFlags(
            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
        )
    }

    private fun hideProgressBar() {
        binding.progressBar.visibility = View.INVISIBLE
        requireActivity().window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
    }
}