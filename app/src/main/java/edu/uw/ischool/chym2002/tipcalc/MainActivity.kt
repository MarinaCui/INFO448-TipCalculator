package edu.uw.ischool.chym2002.tipcalc

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import android.widget.EditText
import android.widget.RadioGroup
import android.widget.Toast

class MainActivity : AppCompatActivity() {
    private var current: String = ""
    private var perc: Double = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val amountInput: EditText = findViewById(R.id.amount)
        val percSelect: RadioGroup = findViewById(R.id.percGroup)
        val tipButton: Button = findViewById(R.id.tip)

        val textWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(s: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (s.toString() != current) {
                    amountInput.removeTextChangedListener(this)
                    amountInput.hint = ""

                    val numStr = s.toString().replace("\\D".toRegex(), "")
                    if (numStr.isNotEmpty()) {
                        val parsed = numStr.toDouble()
                        val formatted = String.format("$%5.2f", parsed / 100)

                        current = formatted
                        amountInput.setText(formatted)
                        amountInput.setSelection(formatted.length)
                    }
                }

                amountInput.addTextChangedListener(this)
            }

            override fun afterTextChanged(s: Editable?) {
                enableTipButton()
                amountInput.clearFocus()
            }
        }

        amountInput.addTextChangedListener(textWatcher)

        percSelect.setOnCheckedChangeListener { group, checkedId ->
            perc = when (checkedId) {
                R.id.ten -> 0.1
                R.id.fifteen -> 0.15
                R.id.eighteen -> 0.18
                else -> 0.2
            }

            enableTipButton()
        }

        tipButton.setOnClickListener {
            tip(amountInput.text.toString(), perc)
        }
    }

    private fun tip(amount: String, perc: Double) {
        val parsed: Double = amount.replace("\\D".toRegex(), "").toDouble() / 100
        val tip: Double = parsed * perc
        val formatted: String = String.format("$%.2f", tip)

        Toast.makeText(this, "Tip: $formatted", Toast.LENGTH_LONG).show()
    }

    private fun enableTipButton() {
        val amountInput: EditText = findViewById(R.id.amount)
        val tipButton: Button = findViewById(R.id.tip)
        tipButton.isEnabled = amountInput.hint.toString() != "Amount" && current != "$0.00" && perc > 0
    }
}