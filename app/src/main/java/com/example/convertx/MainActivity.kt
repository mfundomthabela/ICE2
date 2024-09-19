package com.example.convertx

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {

    private lateinit var amountEditText: EditText
    private lateinit var fromCurrencyEditText: EditText
    private lateinit var toCurrencyEditText: EditText
    private lateinit var resultTextView: TextView
    private lateinit var convertButton: Button

    private lateinit var currencyService: CurrencyService
    private val API_KEY = "294fbe9db4acd7327c158bb1b5cf753835480bd0"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        amountEditText = findViewById(R.id.amountEditText)
        fromCurrencyEditText = findViewById(R.id.fromCurrencyEditText)
        toCurrencyEditText = findViewById(R.id.toCurrencyEditText)
        resultTextView = findViewById(R.id.resultTextView)
        convertButton = findViewById(R.id.convertButton)

        val retrofit = Retrofit.Builder()
            .baseUrl("https://currency.getgeoapi.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        currencyService = retrofit.create(CurrencyService::class.java)

        convertButton.setOnClickListener {
            convertCurrency()
        }
    }

    private fun convertCurrency() {
        val amountStr = amountEditText.text.toString()
        val fromCurrency = fromCurrencyEditText.text.toString().uppercase()
        val toCurrency = toCurrencyEditText.text.toString().uppercase()

        if (amountStr.isEmpty() || fromCurrency.isEmpty() || toCurrency.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
            return
        }

        val amount = amountStr.toDouble()

        currencyService.convertCurrency(API_KEY, fromCurrency, toCurrency, amount)
            .enqueue(object : Callback<CurrencyResponse> {
                override fun onResponse(
                    call: Call<CurrencyResponse>,
                    response: Response<CurrencyResponse>
                ) {
                    if (response.isSuccessful && response.body() != null) {
                        val rate = response.body()?.rates?.get(toCurrency)?.rate ?: 0.0
                        val result = amount * rate
                        resultTextView.text = String.format("Converted amount: %.2f", result)
                    } else {
                        Toast.makeText(this@MainActivity, "Failed to get conversion rate", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<CurrencyResponse>, t: Throwable) {
                    Toast.makeText(this@MainActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
                }
            })
    }
}
