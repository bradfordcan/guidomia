package com.exam.guidomia.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.exam.guidomia.R
import com.exam.guidomia.databinding.FragmentHomeBinding
import com.exam.guidomia.util.DEFAULT_FILTER_MAKE
import com.exam.guidomia.util.DEFAULT_FILTER_MODEL


class HomeFragment : Fragment(), ListUpdate {

    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private lateinit var homeViewModel: HomeViewModel

    private val carsListAdapter = CarListAdapter(arrayListOf(), this)

    private var filterMake = "Any Make"
    private var filterModel = "Any Model"

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        homeViewModel = ViewModelProvider(this)[HomeViewModel::class.java]

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val itemDecorator = DividerItemDecoration(context, DividerItemDecoration.VERTICAL)
        itemDecorator.setDrawable(
            ContextCompat.getDrawable(
                binding.root.context,
                R.drawable.divider_layer
            )!!
        )

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

            val makeDefault = listOf(DEFAULT_FILTER_MAKE)
            val modelDefault = listOf(DEFAULT_FILTER_MODEL)

            val makes = makeDefault + cars.map { it.make }
            val models = modelDefault + cars.map { it.model }

            // update filter layout
            // populate make filter data
            val makeAdapter = ArrayAdapter(
                binding.root.context,
                android.R.layout.simple_dropdown_item_1line,
                makes
            )
            binding.carFilter.filterMake.setAdapter(makeAdapter)
            binding.carFilter.filterMake.setText(filterMake, false)
            binding.carFilter.filterMake.setOnItemClickListener { _, _, position, _ ->
                filterMake = makes[position]
                carsListAdapter.getFilter().filter("${filterMake}-${filterModel}")
            }

            // populate model filter data
            val modelAdapter = ArrayAdapter(
                binding.root.context,
                android.R.layout.simple_dropdown_item_1line,
                models
            )
            binding.carFilter.filterModel.setAdapter(modelAdapter)
            binding.carFilter.filterModel.setText(filterModel, false)
            binding.carFilter.filterModel.setOnItemClickListener { _, _, position, _ ->
                filterModel = models[position]
                carsListAdapter.getFilter().filter("${filterMake}-${filterModel}")
            }
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

    override fun onUpdateList(isEmpty: Boolean) {
        if(isEmpty) {
            binding.expandableList.visibility = View.GONE
            binding.emptyView.visibility = View.VISIBLE
        } else {
            binding.expandableList.visibility = View.VISIBLE
            binding.emptyView.visibility = View.GONE
        }
    }
}

