package com.compendium.hyrule.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.compendium.hyrule.R
import com.compendium.hyrule.databinding.EntryFragmentBinding
import com.compendium.hyrule.utils.Constants
import com.compendium.hyrule.utils.Formatter
import com.compendium.hyrule.viewmodel.HyruleViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

@AndroidEntryPoint
class EntryFragment : Fragment() {
    private lateinit var binding: EntryFragmentBinding
    private val args: EntryFragmentArgs by navArgs()
    private val viewModel: HyruleViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = EntryFragmentBinding.inflate(inflater, container, false)
        setup()
        return binding.root
    }

    private fun setup() {
        val data = args.entry.data
        data.apply {
            Glide.with(requireActivity()).load(image).into(binding.imageViewEntry)
            binding.textViewInfo2.text =
                if (common_locations == null) "N/A" else Formatter.removeBrackets(common_locations)
            binding.textViewInfoLabel2.text = getString(R.string.locations)
            binding.textViewDescription.text = description
            binding.textViewCategory.text = Formatter.capitalizeFirstLetters(category)
            binding.textViewName.text = Formatter.capitalizeFirstLetters(name)

            when (category) {
                "creatures" -> {
                    var info1 = "N/A"
                    if (!cooking_effect.isNullOrEmpty())
                        info1 = cooking_effect
                    else if (!drops.isNullOrEmpty())
                        info1 = Formatter.removeBrackets(drops)

                    binding.textViewInfo1.text = info1
                    var infoLabel = "Drops"
                    if (!cooking_effect.isNullOrEmpty())
                        infoLabel = getString(R.string.cooking_effect)
                    else if (!drops.isNullOrEmpty())
                        infoLabel = getString(R.string.drops)

                    binding.textViewInfoLabel.text = infoLabel

                    var extraInfo = "N/A"
                    if (!cooking_effect.isNullOrEmpty())
                        extraInfo = hearts_recovered.toString()

                    binding.textViewExtraInfo.text = extraInfo
                    var extraInfoLabel = getString(R.string.extra_info)
                    if (!cooking_effect.isNullOrEmpty())
                        extraInfoLabel = getString(R.string.hearts_recovered)

                    binding.textViewExtraLabel.text = extraInfoLabel
                }
                "equipment" -> {
                    binding.textViewInfoLabel.text = getString(R.string.attack_defense)
                    binding.textViewInfo1.text = "$attack, $defense"
                    binding.textViewExtraInfo.text = "N/A"
                }
                "materials" -> {
                    binding.textViewInfoLabel.text = getString(R.string.cooking_effect)
                    binding.textViewInfo1.text = cooking_effect
                    binding.textViewExtraInfo.text = hearts_recovered.toString()
                }
                "monsters" -> {
                    binding.textViewInfoLabel.text = getString(R.string.drops)
                    binding.textViewInfo1.text = Formatter.removeBrackets(drops)
                    binding.textViewExtraInfo.text = "N/A"
                }
                "treasure" -> {
                    binding.textViewInfoLabel.text = getString(R.string.drops)
                    binding.textViewInfo1.text = Formatter.removeBrackets(drops)
                    binding.textViewExtraInfo.text = "N/A"
                }
            }

            if (args.launchedFrom == Constants.SAVED_ENTRY_FRAGMENT) {
                binding.fab.setImageDrawable(
                    ResourcesCompat.getDrawable(resources, android.R.drawable.ic_menu_delete, null)
                )
            }

            binding.fab.setOnClickListener {
                if (args.launchedFrom == Constants.SAVED_ENTRY_FRAGMENT) {
                    // this means that the user has already saved this entry so fab should be
                    // used for deleting the entry.
                    viewModel.deleteEntry(data)
                    Snackbar.make(it, "Removed from saved entries.", Snackbar.LENGTH_LONG).show()
                    MainScope().launch {
                        val result =
                            findNavController().popBackStack(R.id.savedEntryFragment, false)
                    }
                } else {
                    // first let's check if this entry has already been saved or not.
                    val entry = viewModel.isEntrySaved(data.id)

                    viewModel.insertEntry(data)
                    viewModel.result.observe(viewLifecycleOwner, { result ->
                        if (result == 1) {
                            // this is from an update operation
                            Snackbar.make(it, "Entry has been updated.", Snackbar.LENGTH_LONG).show()
                        } else {
                            Snackbar.make(it, "Added to saved entries.", Snackbar.LENGTH_LONG).show()
                        }
                    })
                }
            }
        }
    }
}