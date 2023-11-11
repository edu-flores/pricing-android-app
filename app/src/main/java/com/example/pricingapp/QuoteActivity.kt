package com.example.pricingapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.room.Room
import com.example.pricingapp.databinding.NewQuoteBinding
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.simplexml.SimpleXmlConverterFactory
import java.io.InputStream
import java.util.Calendar
import java.util.Properties

class QuoteActivity : AppCompatActivity() {
    private lateinit var binding: NewQuoteBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = NewQuoteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Getting Current Day
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH) + 1
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        //Getting the Secret Properties
        val properties = Properties()
        val inputStream: InputStream = assets.open("secrets.properties")
        properties.load(inputStream)

        // Database
        val db = Room.databaseBuilder(applicationContext,
            AppDatabase::class.java, "QuoteDatabase.db").build()

        // Calculate quote button action
        val button = binding.calculateQuote
        button.setOnClickListener {
            // Get user values
            val originZip   = binding.originZip.text.toString()
            val originCity  = binding.originCity.text.toString()
            val originState = binding.originState.text.toString()

            val destinationZip   = binding.destinationZip.text.toString()
            val destinationCity  = binding.destinationCity.text.toString()
            val destinationState = binding.destinationState.text.toString()

            val itemClass    = binding.itemClass.text.toString()
            val itemWeight   = binding.itemWeight.text.toString()
            val itemType     = binding.itemType.text.toString()
            val itemQuantity = binding.itemQuantity.text.toString()

            val itemLength = binding.itemLength.text.toString()
            val itemWidth  = binding.itemWidth.text.toString()
            val itemHeight = binding.itemHeight.text.toString()

            // Check if missing parameters
            if (originZip.isBlank() || originCity.isBlank() || originState.isBlank() ||
                destinationZip.isBlank() || destinationCity.isBlank() || destinationState.isBlank() ||
                itemClass.isBlank() || itemWeight.isBlank() || itemType.isBlank() || itemQuantity.isBlank() || // itemClass will remain unused for this iteration
                itemLength.isBlank() || itemWidth.isBlank() || itemHeight.isBlank()
            ) {
                showToast("Please fill in all fields")
            } else {
                // API call
                CoroutineScope(Dispatchers.IO).launch {
                    try {

                        //Creating a map with the url parameters
                        val params = hashMapOf<String, String>(
                            "ID" to properties.getProperty("ID"),
                            "ShipCity" to originCity,
                            "ShipState" to originState,
                            "ShipZip" to originZip,
                            "ShipCountry" to "US", // Hard Coded US as we are only giving service inside the US and won't be using other counties
                            "ConsCity" to destinationCity,
                            "ConsState" to destinationState,
                            "ConsZip" to destinationZip,
                            "ConsCountry" to "US", // Hard Coded US as we are only giving service inside the US and won't be using other counties
                            "Wgt1" to itemWeight,
                            "FrtLng1" to itemLength,
                            "FrtWdth1" to itemWidth,
                            "FrtHght1" to itemHeight,
                            "FrtLWHType" to "IN", // The API support IN and CM, we will only be using IN for this iteration
                            "UnitNo1" to itemQuantity,
                            "UnitType1" to itemType,
                            "ShipMonth" to month.toString(),
                            "ShipDay" to day.toString(),
                            "ShipYear" to year.toString(),
                            "TPBAff" to "Y", // Hard Coded as Mint Cargo will always be the Third Party
                            "TPBPay" to "Y", // Hard Coded as Mint Cargo will always be in charge of payment
                            "TPBName" to properties.getProperty("TPBName").toString(),
                            "TPBAddr" to properties.getProperty("TPBAddr").toString(),
                            "TPBCity" to properties.getProperty("TPBCity").toString(),
                            "TPBState" to properties.getProperty("TPBState").toString(),
                            "TPBZip" to properties.getProperty("TPBZip").toString(),
                            "TPBCountry" to properties.getProperty("TPBCountry").toString(),
                            "TPBAcct" to properties.getProperty("TPBAcct").toString()
                        )

                        val call = getRetrofit().create(XmlPlaceholderApi::class.java).getQuote(params)

                        runOnUiThread {
                            if (call.isSuccessful) {
                                val quoteResponseData = call.body()
                                Log.d("QuoteActivity", "API Response: $quoteResponseData")

                                // Save in SQLite
                                GlobalScope.launch(Dispatchers.IO) {
                                    val quote = Quote(
                                        id = quoteResponseData?.quoteID.toString(),
                                        fromCity = quoteResponseData?.originInfo?.origingTerminalCity.toString(),
                                        fromState = quoteResponseData?.originInfo?.origingTerminalState.toString(),
                                        fromZip = quoteResponseData?.originInfo?.originTerminalZip.toString(),
                                        toCity = quoteResponseData?.destinationInfo?.origingTerminalCity.toString(),
                                        toState = quoteResponseData?.destinationInfo?.origingTerminalState.toString(),
                                        toZip = quoteResponseData?.destinationInfo?.originTerminalZip.toString(),
                                        price = quoteResponseData?.charge.toString(),
                                        transitTime = quoteResponseData?.transitTime.toString(),
                                        expirationDate = quoteResponseData?.expirationDate.toString()
                                    )
                                    db.quoteDao().insert(quote)
                                }

                                // Show result activity
                                val intent = Intent(this@QuoteActivity, ResultActivity::class.java)
                                val bundle = Bundle()
                                bundle.putString("from", "Boston, Massachusetts")
                                bundle.putString("to", "Las Vegas, Nevada")
                                bundle.putInt("price", 8000)
                                bundle.putString("transit", "90 days")
                                intent.putExtras(bundle)
                                startActivity(intent)
                            } else {
                                Log.e("QuoteActivity", "API Error Response: ${call.errorBody()?.string()}")
                            }
                        }
                    } catch (e: Exception) {
                        Log.e("QuoteActivity", "Exception during API call: ${e.message}", e)
                    }
                }
            }
        }

        // Menu button
        val returnHome = binding.returnHome
        returnHome.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }

    // Retrofit library for API responses
    private fun getRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://testt.free.beeceptor.com/")
            .addConverterFactory(SimpleXmlConverterFactory.create())
            .build()
    }

    // Notifications
    fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
