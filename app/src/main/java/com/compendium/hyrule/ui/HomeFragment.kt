package com.compendium.hyrule.ui

import android.content.res.TypedArray
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.compendium.hyrule.R
import com.compendium.hyrule.adapter.CategoryAdapter
import com.compendium.hyrule.databinding.MainFragmentBinding
import com.compendium.hyrule.model.CarouselCategory
import dagger.hilt.android.AndroidEntryPoint
import org.imaginativeworld.whynotimagecarousel.listener.CarouselOnScrollListener
import org.imaginativeworld.whynotimagecarousel.model.CarouselItem

@AndroidEntryPoint
class HomeFragment : Fragment() {
    private lateinit var binding: MainFragmentBinding

    private var descriptions = arrayOf<String>()

    private lateinit var carouselImages: TypedArray

    private lateinit var categoryImages: TypedArray

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = MainFragmentBinding.inflate(inflater, container, false)
        descriptions = resources.getStringArray(R.array.descriptions)
        carouselImages = resources.obtainTypedArray(R.array.carousel_images)
        categoryImages = resources.obtainTypedArray(R.array.category_images)

        binding.apply {
            textViewDescription.text = descriptions[0]
            imageCarousel.registerLifecycle(lifecycle)
            imageCarousel.autoPlay = true
            imageCarousel.autoPlayDelay = 8000
            val carouselItems = mutableListOf<CarouselItem>()
            for (i in 0 until carouselImages.length()) {
                carouselItems.add(CarouselItem(imageDrawable = carouselImages.getResourceId(i, 0)))
            }

            imageCarousel.setData(carouselItems)
            imageCarousel.imageScaleType = ImageView.ScaleType.CENTER_CROP
            imageCarousel.showIndicator = true
            imageCarousel.showNavigationButtons = false
            imageCarousel.onScrollListener = object : CarouselOnScrollListener {
                override fun onScrolled(
                    recyclerView: RecyclerView,
                    dx: Int,
                    dy: Int,
                    position: Int,
                    carouselItem: CarouselItem?
                ) {
                    super.onScrolled(recyclerView, dx, dy, position, carouselItem)
                    textViewDescription.text = descriptions[position]
                }
            }
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val categories = mutableListOf<CarouselCategory>()

        for (i in 0..4) {
            categories.add(
                CarouselCategory(
                    id = i,
                    name = getCategoryNames()[i],
                    imageResourceId = categoryImages.getResourceId(i, 0),
                    description = getCategoryNames()[i]
                )
            )
        }
        binding.apply {
            val adapter = CategoryAdapter()
            adapter.setOnItemClickListener { category ->
                val bundle = Bundle().apply {
                    putString("query", category.lowercase())
                }
                findNavController().navigate(
                    R.id.action_homeFragment_to_searchEntryFragment,
                    bundle
                )
            }

            adapter.submitList(categories)
            recyclerViewCategories.adapter = adapter
            recyclerViewCategories.layoutManager = GridLayoutManager(requireContext(), 2)
        }
    }

    private fun getCategoryNames(): Array<String> {
        return resources.getStringArray(R.array.categories)
    }
}