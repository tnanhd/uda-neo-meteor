package com.udacity.asteroidradar.main

import android.os.Bundle
import android.view.*
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.udacity.asteroidradar.R
import com.udacity.asteroidradar.databinding.FragmentMainBinding
import com.udacity.asteroidradar.viewmodels.AsteroidViewModel

class MainFragment : Fragment() {
    private lateinit var binding: FragmentMainBinding
    private val viewModel: AsteroidViewModel by lazy {
        val activity = requireNotNull(this.activity) {
            "You can only access the viewModel after onViewCreated()"
        }
        ViewModelProvider(
            this,
            AsteroidViewModel.Factory(activity.application)
        ).get(AsteroidViewModel::class.java)
    }

    private var viewModelAdapter: AsteroidAdapter? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.asteroids.observe(viewLifecycleOwner) {
            it.apply {
                viewModelAdapter?.submitList(it)
            }
        }

        binding.activityMainImageOfTheDay.contentDescription = getString(
            R.string.nasa_picture_of_day_content_description_format,
            viewModel.pictureOfDay.value?.title
        )
        viewModel.pictureOfDay.observe(viewLifecycleOwner) {
            it?.let {
                binding.activityMainImageOfTheDay.contentDescription =
                    getString(R.string.nasa_picture_of_day_content_description_format, it.title)
            }
        }

        val menuProvider = object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.main_overflow_menu, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when (menuItem.itemId) {
                    R.id.show_today_menu -> {
                        true
                    }

                    R.id.show_next_week_menu -> {
                        true
                    }

                    R.id.show_saved_menu -> {
                        true
                    }

                    else -> false
                }
            }
        }
        requireActivity().addMenuProvider(menuProvider, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMainBinding.inflate(inflater)
        binding.lifecycleOwner = this

        binding.viewModel = viewModel

        viewModelAdapter = AsteroidAdapter(AsteroidClickListener {
            viewModel.displayAsteroidDetails(it)
        })
        binding.asteroidRecycler.adapter = viewModelAdapter

        viewModel.navigateToSelectedAsteroid.observe(viewLifecycleOwner) {
            it?.let {
                this.findNavController()
                    .navigate(MainFragmentDirections.actionShowDetail(it))
                viewModel.displayAsteroidDetailsComplete()
            }
        }

        return binding.root
    }
}
