package com.test.edvora.ui

import android.annotation.SuppressLint
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.google.android.material.tabs.TabLayout
import com.test.edvora.R
import com.test.edvora.databinding.FragmentFilterModalBinding
import com.test.edvora.viewmodel.EdvoraViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FilterModal(val tabPosition: Int) : DialogFragment(), AdapterView.OnItemSelectedListener {


    interface OnFilterSelectedListener {
        fun selectState(selectedState: String)
        fun selectCity(selectedCity: String)
    }

    var filterSelectedListener: OnFilterSelectedListener? = null

    override fun setStyle(style: Int, theme: Int) {
        super.setStyle(STYLE_NO_FRAME, R.style.Theme_Dialog)
    }

    val viewmodel: EdvoraViewModel by viewModels()

    lateinit var binding: FragmentFilterModalBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewmodel.publishUser()

        viewmodel.publishAllRides(requireActivity())
        viewmodel.publishRides(tabPosition, requireActivity())
        viewmodel.publishState(tabPosition, requireActivity())
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    var allStates: List<String> = listOf()

    var stateCities: List<String> = listOf()

    @SuppressLint("RestrictedApi")
    override fun setupDialog(dialog: Dialog, style: Int) {

        val view = View.inflate(context, R.layout.fragment_filter_modal, null)
        binding = FragmentFilterModalBinding.bind(view)

        dialog.setContentView(binding.root)
        (view.parent as View).setBackgroundColor(resources.getColor(android.R.color.transparent))

        binding.citySpinner.onItemSelectedListener = this
        binding.stateSpinner.onItemSelectedListener = this

        viewmodel.state.observe(requireActivity()) { state_ride ->
            val states = state_ride.map { it.state }
            allStates = states

            binding.stateSpinner.adapter = ArrayAdapter(
                requireContext(),
                android.R.layout.simple_dropdown_item_1line, states
            )


        }

        viewmodel.cities.observe(requireActivity()) {
            stateCities = it
            it.forEach {
                Log.d("CITIES", it)
            }

            binding.citySpinner.adapter = ArrayAdapter(
                requireContext(),
                android.R.layout.simple_dropdown_item_1line, it
            )

        }




        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

    }


    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {

        p1 as TextView
        p1.setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
        when (p0) {
            binding.stateSpinner -> {

                viewmodel.publishCities(
                    lifecycleOwner = requireActivity(),
                    currentState = allStates[p2]
                )

                filterSelectedListener?.selectState(allStates[p2])

            }

            binding.citySpinner -> {
                filterSelectedListener?.selectCity(stateCities[p2])


            }
        }
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {

    }
}