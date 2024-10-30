package com.example.currency
import android.annotation.SuppressLint
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var editTextSourceAmount: EditText
    private lateinit var editTextDestinationAmount: EditText
    private lateinit var spinnerSourceCurrency: Spinner
    private lateinit var spinnerDestinationCurrency: Spinner

    // Tạo tỷ giá hối đoái (giả sử)
    private val exchangeRates = mapOf(
        "USD" to 1.0,
        "EUR" to 0.85,
        "JPY" to 110.0,
        "GBP" to 0.75,
        "VND" to 24000.0,
        "AUD" to 1.3,
        "CAD" to 1.2,
        "CHF" to 0.9,
        "CNY" to 6.5,
        "INR" to 74.0,
        "SGD" to 1.35,
        "KRW" to 1150.0,
        "THB" to 33.0
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        editTextSourceAmount = findViewById(R.id.editTextSourceAmount)
        editTextDestinationAmount = findViewById(R.id.editTextDestinationAmount)
        spinnerSourceCurrency = findViewById(R.id.spinnerSourceCurrency)
        spinnerDestinationCurrency = findViewById(R.id.spinnerDestinationCurrency)

        // Cài đặt dữ liệu cho Spinner
        val currencies = exchangeRates.keys.toList()
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, currencies)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerSourceCurrency.adapter = adapter
        spinnerDestinationCurrency.adapter = adapter

        // Thêm sự kiện khi thay đổi số tiền đầu vào
        editTextSourceAmount.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                convertCurrency()
            }
            override fun afterTextChanged(s: Editable?) {}
        })

        // Thêm sự kiện khi chọn đơn vị tiền tệ nguồn hoặc đích
        spinnerSourceCurrency.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: android.view.View, position: Int, id: Long) {
                convertCurrency()
            }
            override fun onNothingSelected(parent: AdapterView<*>) {}
        }

        spinnerDestinationCurrency.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: android.view.View, position: Int, id: Long) {
                convertCurrency()
            }
            override fun onNothingSelected(parent: AdapterView<*>) {}
        }
    }

    @SuppressLint("DefaultLocale")
    private fun convertCurrency() {
        val sourceAmountText = editTextSourceAmount.text.toString()
        if (sourceAmountText.isEmpty()) {
            editTextDestinationAmount.setText("")
            return
        }

        val sourceAmount = sourceAmountText.toDoubleOrNull() ?: return
        val sourceCurrency = spinnerSourceCurrency.selectedItem.toString()
        val destinationCurrency = spinnerDestinationCurrency.selectedItem.toString()

        val sourceRate = exchangeRates[sourceCurrency] ?: return
        val destinationRate = exchangeRates[destinationCurrency] ?: return

        // Chuyển đổi tiền tệ
        val destinationAmount = sourceAmount * (destinationRate / sourceRate)
        editTextDestinationAmount.setText(String.format("%.2f", destinationAmount))
    }
}
