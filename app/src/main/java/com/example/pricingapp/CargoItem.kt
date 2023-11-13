package com.example.pricingapp

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView

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

    lateinit var classValue : String
    lateinit var weightValue : String
    lateinit var packageValue : String
    lateinit var quantityValue : String
    lateinit var lengthValue : String
    lateinit var widthValue : String
    lateinit var heightValue : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            fragmentNumber = requireArguments().getInt(ARG_NUMBER)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val view = inflater.inflate(R.layout.fragment_cargo_item, container, false)

        val itemNumberTextView = view.findViewById<TextView>(R.id.itemNumberTextView)
        val classSpinner = view.findViewById<Spinner>(R.id.itemClass)
        val packageSpinner = view.findViewById<Spinner>(R.id.itemType)

        itemNumberTextView.text = "Item #$fragmentNumber"
        setupSpinner(classSpinner, R.array.classes)
        setupSpinner(packageSpinner, R.array.package_types)

        val classValueField = view.findViewById<Spinner>(R.id.itemClass)
        classValueField.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                classValue = parent.getItemAtPosition(position).toString()
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // Do nothing or handle 'nothing selected' state
            }
        }

        val weightValueF = view.findViewById<EditText>(R.id.itemWeight)
        weightValueF.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                weightValue = s.toString()
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // Do nothing or handle 'before text changed' state
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // Do nothing or handle 'on text changed' state
            }
        })

        val packageValueF = view.findViewById<Spinner>(R.id.itemType)
        packageValueF.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                packageValue = parent.getItemAtPosition(position).toString()
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // Do nothing or handle 'nothing selected' state
            }
        }

        val quantityValueF = view.findViewById<EditText>(R.id.itemQuantity)
        quantityValueF.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                quantityValue = s.toString()
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // Do nothing or handle 'before text changed' state
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // Do nothing or handle 'on text changed' state
            }
        })

        val lengthValueF = view.findViewById<EditText>(R.id.itemLength)
        lengthValueF.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                lengthValue = s.toString()
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // Do nothing or handle 'before text changed' state
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // Do nothing or handle 'on text changed' state
            }
        })

        val widthValueF = view.findViewById<EditText>(R.id.itemWidth)
        widthValueF.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                widthValue = s.toString()
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // Do nothing or handle 'before text changed' state
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // Do nothing or handle 'on text changed' state
            }
        })

        val heightValueF = view.findViewById<EditText>(R.id.itemHeight)
        heightValueF.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                heightValue = s.toString()
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // Do nothing or handle 'before text changed' state
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // Do nothing or handle 'on text changed' state
            }
        })

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