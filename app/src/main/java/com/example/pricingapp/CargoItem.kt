package com.example.pricingapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Spinner

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [CargoItem.newInstance] factory method to
 * create an instance of this fragment.
 */
class CargoItem : Fragment() {

    private var fragmentNumber = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            fragmentNumber = requireArguments().getInt(ARG_NUMBER)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_cargo_item, container, false)
        val classSpinner = view.findViewById<Spinner>(R.id.itemClass)
        val packageSpinner = view.findViewById<Spinner>(R.id.itemType)

        setupSpinner(classSpinner, R.array.classes)
        setupSpinner(packageSpinner, R.array.package_types)

        return view
    }

    companion object {
        private const val ARG_NUMBER = "number"

        fun newInstance(number: Int): CargoItem {
            val fragment = CargoItem()
            val args = Bundle()
            args.putInt(ARG_NUMBER, number)
            fragment.arguments = args
            return fragment
        }
    }

    private fun setupSpinner(spinner: Spinner, arrayId: Int) {
        ArrayAdapter.createFromResource(
            requireContext(),
            arrayId,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner.adapter = adapter
        }
    }
}