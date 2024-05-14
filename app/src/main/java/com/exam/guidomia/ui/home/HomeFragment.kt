package com.exam.guidomia.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.exam.guidomia.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private lateinit var homeViewModel: HomeViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        homeViewModel = ViewModelProvider(this)[HomeViewModel::class.java]

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textHome
        homeViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }

        observeCarsData()


        return root
    }

    private fun observeCarsData() {
        homeViewModel.cars.observe(viewLifecycleOwner) { cars ->
            //Toast.makeText(binding.root.context, cars.toString(), Toast.LENGTH_LONG).show()
            binding.textHome.text = cars.toString()
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
}