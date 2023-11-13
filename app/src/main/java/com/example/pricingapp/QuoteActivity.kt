package com.example.pricingapp

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.FragmentManager
import androidx.room.Room
import com.example.pricingapp.databinding.NewQuoteBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.simplexml.SimpleXmlConverterFactory
import java.io.InputStream
import java.util.Calendar
import java.util.Properties

class QuoteActivity : AppCompatActivity() {
    private lateinit var binding: NewQuoteBinding
    private lateinit var db: AppDatabase

    private val fragmentManager = supportFragmentManager
    private var fragmentCount = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = NewQuoteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Today's date
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH) + 1
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        // Company properties
        val properties = Properties()
        val inputStream: InputStream = assets.open("secrets.properties")
        properties.load(inputStream)

        // SQLite database
        db = Room.databaseBuilder(applicationContext,
            AppDatabase::class.java, "QuoteDatabase.db").build()

        // Adding the first cargo fragment
        fragmentCount++
        val fragment = CargoItem.newInstance(fragmentCount)
        fragmentManager.beginTransaction()
            .add(binding.fragmentContainer.id, fragment, "fragment$fragmentCount") // Gives a tag to each fragment so that we can search for it later.
            .commit()

        // Add fragment Button
        binding.addItemButton.setOnClickListener {
            if (fragmentCount < 15) {
                fragmentCount++
                val fragment = CargoItem.newInstance(fragmentCount)
                fragmentManager.beginTransaction()
                    .add(binding.fragmentContainer.id, fragment, "fragment$fragmentCount") // Gives a tag to each fragment so that we can search for it later.
                    .commit()
            }
        }

        // Remove fragment Button
        binding.removeItemButton.setOnClickListener {
            val fragment = fragmentManager.findFragmentById(binding.fragmentContainer.id)
            if (fragment != null && fragmentCount > 1) { // Won't let the app have less than cargo fragment
                fragmentManager.beginTransaction()
                    .remove(fragment)
                    .commit()
                fragmentCount--
            }
        }

        // Dropdown buttons
        setupSpinners()


        // Calculate quote button
        binding.calculateQuote.setOnClickListener {
            if(fragmentCount > 0){ //Solo hace algo si hay más de un item agregado
                val userValues = getUserInput()

                if (userValues.values.any { it.isBlank() }) {
                    showToast(this, "Please fill in all fields")
                } else {
                    CoroutineScope(Dispatchers.IO).launch {
                        try {
                            val params = buildApiParams(userValues, month, day, year, properties)

                            runOnUiThread {
                                showToast(this@QuoteActivity, "Loading...")
                            }
                            val call = getRetrofit().create(XmlPlaceholderApi::class.java).getQuote(params)

                            runOnUiThread {
                                handleApiResponse(call, db)
                            }

                            Log.e("params", params.toString())
                        } catch (e: Exception) {
                            Log.e("QuoteActivity", "Exception during API call: ${e.message}", e)
                        }
                    }
                }
            }
        }

        // Return home button
        binding.returnHome.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }
    }

    // Setup multiple spinners
    private fun setupSpinners() {
        setupSpinner(binding.originState, R.array.states)
        setupSpinner(binding.destinationState, R.array.states)
    }

    // Adapters for spinners
    private fun setupSpinner(spinner: Spinner, arrayId: Int) {
        ArrayAdapter.createFromResource(
            this,
            arrayId,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner.adapter = adapter
        }
    }


    // Extract user input from the activity
    private fun getUserInput(): Map<String, String> {

        var data = mutableMapOf(
            "originZip" to binding.originZip.text.toString(),
            "originCity" to binding.originCity.text.toString(),
            "originState" to binding.originState.selectedItem.toString(),
            "destinationZip" to binding.destinationZip.text.toString(),
            "destinationCity" to binding.destinationCity.text.toString(),
            "destinationState" to binding.destinationState.selectedItem.toString()
        )

        for(i in 1..fragmentCount){
            val fragment = supportFragmentManager.findFragmentByTag("fragment$i") as CargoItem

            data["itemClass$i"] = fragment.classValue
            data["itemWeight$i"] = fragment.weightValue
            data["itemPackage$i"] = fragment.packageValue
            data["itemQuantity$i"] = fragment.quantityValue
            data["itemLength$i"] = fragment.lengthValue
            data["itemWidth$i"] = fragment.widthValue
            data["itemHeight$i"] = fragment.heightValue
        }

        return data
    }

    // API query parameters
    private fun buildApiParams(
        userValues: Map<String, String>,
        month: Int,
        day: Int,
        year: Int,
        properties: Properties
    ): Map<String, String> {
        val packageTypes = mapOf(
            "Bags" to "BAG", "Bundles" to "BX", "Boxes" to "BDL",
            "Cases" to "CS", "Crates" to "CRT", "Cartons" to "CTN",
            "Cylinders" to "CYL", "Drums" to "DR", "Pails" to "PL",
            "Pieces" to "PC", "Pallets" to "PLT", "Flat Racks" to "RK",
            "Reels" to "REL", "Rolls" to "RL", "Skids" to "SKD",
            "Slip Sheets" to "SLP", "Totes" to "TOTE"
        )

        var params = mutableMapOf(
            "ID" to properties.getProperty("ID"),
            "ShipCity" to userValues["originCity"]!!,
            "ShipState" to userValues["originState"]!!,
            "ShipZip" to userValues["originZip"]!!,
            "ShipCountry" to "US",  // Hard coded US as we are only giving service inside the US and won't be using other counties
            "ConsCity" to userValues["destinationCity"]!!,
            "ConsState" to userValues["destinationState"]!!,
            "ConsZip" to userValues["destinationZip"]!!,
            "ConsCountry" to "US",  // Hard coded US as we are only giving service inside the US and won't be using other counties
            "FrtLWHType" to "IN",
            "ShipMonth" to month.toString(),
            "ShipDay" to day.toString(),
            "ShipYear" to year.toString(),
            "TPBAff" to "Y",  // Hard coded as Mint Cargo will always be the third party
            "TPBPay" to "Y",  // Hard coded as Mint Cargo will always be in charge of payment
            "TPBName" to properties.getProperty("TPBName").toString(),
            "TPBAddr" to properties.getProperty("TPBAddr").toString(),
            "TPBCity" to properties.getProperty("TPBCity").toString(),
            "TPBState" to properties.getProperty("TPBState").toString(),
            "TPBZip" to properties.getProperty("TPBZip").toString(),
            "TPBCountry" to properties.getProperty("TPBCountry").toString(),
            "TPBAcct" to properties.getProperty("TPBAcct").toString()
        )

        for(i in 1..fragmentCount){
            params["Wgt$i"] = userValues["itemWeight$i"]
            params["FrtLng$i"] = userValues["itemLength$i"]
            params["FrtWdth$i"] = userValues["itemWidth$i"]
            params["FrtHght$i"] = userValues["itemHeight$i"]
            params["UnitNo$i"] = userValues["itemQuantity$i"]
            params["UnitType$i"] = packageTypes[userValues["itemPackage$i"]]
        }

        return params
    }

    // API response - error or success handling
    private fun handleApiResponse(call: retrofit2.Response<QuoteResponse>, db: AppDatabase) {
        if (call.isSuccessful) {
            val quoteResponseData = call.body()
            Log.d("QuoteActivity", "API Response: $quoteResponseData")

            if (quoteResponseData?.numErrors != 0) {
                handleUserInputError(quoteResponseData?.errors!!)
            } else {
                saveQuoteInDatabase(quoteResponseData, db)
                startResultActivity(quoteResponseData)
            }
        } else {
            Log.e("QuoteActivity", "API Error Response: ${call.errorBody()?.string()}")
        }
    }

    // API response errors
    private fun handleUserInputError(errors: List<Error>) {
        var errorMessage = ""
        for (error in errors) {
            for (message in error.errorMessage!!) {
                errorMessage += "• ${message}\n"
            }
        }
        val builder = AlertDialog.Builder(this@QuoteActivity)
        builder.setTitle("Invalid Quote")
        builder.setMessage(errorMessage)
        builder.setPositiveButton("OK") { dialog, _ ->
            dialog.dismiss()
        }
        val dialog = builder.create()
        dialog.show()
    }

    // API response success
    private fun saveQuoteInDatabase(quoteResponseData: QuoteResponse, db: AppDatabase) {
        GlobalScope.launch(Dispatchers.IO) {
            val quote = Quote(
                id = quoteResponseData.quoteID.toString(),
                fromCity = quoteResponseData.originInfo?.origingTerminalCity.toString(),
                fromState = quoteResponseData.originInfo?.origingTerminalState.toString(),
                fromZip = quoteResponseData.originInfo?.originTerminalZip.toString(),
                toCity = quoteResponseData.destinationInfo?.destinationTerminalCity.toString(),
                toState = quoteResponseData.destinationInfo?.destinationTerminalState.toString(),
                toZip = quoteResponseData.destinationInfo?.destinationTerminalZip.toString(),
                price = quoteResponseData.charge.toString(),
                transitTime = quoteResponseData.transitTime.toString(),
                expirationDate = quoteResponseData.expirationDate.toString()
            )
            db.quoteDao().insert(quote)
        }
    }

    // Result activity with bundle
    private fun startResultActivity(quoteResponseData: QuoteResponse) {
        val intent = Intent(this@QuoteActivity, ResultActivity::class.java)
        val bundle = Bundle()
        bundle.putString("fromCity", quoteResponseData.originInfo?.origingTerminalCity.toString())
        bundle.putString("fromState", quoteResponseData.originInfo?.origingTerminalState.toString())
        bundle.putString("fromZip", quoteResponseData.originInfo?.originTerminalZip.toString())
        bundle.putString("toCity", quoteResponseData.destinationInfo?.destinationTerminalCity.toString())
        bundle.putString("toState", quoteResponseData.destinationInfo?.destinationTerminalState.toString())
        bundle.putString("toZip", quoteResponseData.destinationInfo?.destinationTerminalZip.toString())
        bundle.putString("price", quoteResponseData.charge.toString())
        bundle.putString("transitTime", quoteResponseData.transitTime.toString())
        bundle.putString("expirationDate", quoteResponseData.expirationDate.toString())
        intent.putExtras(bundle)
        startActivity(intent)
    }

    // Retrofit library
    private fun getRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://www.abfs.com/xml/aquotexml.asp/")
            .addConverterFactory(SimpleXmlConverterFactory.create())
            .build()
    }

    // Notification
    private fun showToast(context: Context, message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }
}
