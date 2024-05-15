package com.exam.guidomia.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.exam.guidomia.R
import com.exam.guidomia.databinding.FragmentHomeBinding


class HomeFragment : Fragment(), OnCardItemClick {

    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private lateinit var homeViewModel: HomeViewModel

    private val carsListAdapter = CarListAdapter(arrayListOf(), this)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        homeViewModel = ViewModelProvider(this)[HomeViewModel::class.java]

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val itemDecorator = DividerItemDecoration(context, DividerItemDecoration.VERTICAL)
        itemDecorator.setDrawable(ContextCompat.getDrawable(binding.root.context, R.drawable.divider_layer)!!)

        binding.expandableList.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = carsListAdapter
            addItemDecoration(itemDecorator)
        }

        observeCarsData()
        return root
    }

    private fun observeCarsData() {
        homeViewModel.cars.observe(viewLifecycleOwner) { cars ->
            cars.mapIndexed { index, car ->
                car.id = index.toLong()
            }
            carsListAdapter.updateCars(cars)
        }
    }

    override fun onResume() {
        super.onResume()
        homeViewModel.getAllCars()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onCardItemClick(id: Long) {
        TODO("Not yet implemented")
    }
}

