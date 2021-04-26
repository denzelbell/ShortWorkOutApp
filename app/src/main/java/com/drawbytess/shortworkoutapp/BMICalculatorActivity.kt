package com.drawbytess.shortworkoutapp

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_bmi_calculator.*
import java.math.BigDecimal
import java.math.RoundingMode

class BMICalculatorActivity : AppCompatActivity() {

    val METRIC_UNIT_VIEW = "METRIC_UNIT_VIEW"
    val US_UNIT_VIEW = "US_UNIT_VIEW"

    var currentVisibleView: String = US_UNIT_VIEW

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bmi_calculator)

        // Set up toolbar instructions
        setSupportActionBar(toolbar_bmi_activity)
        val actionbar = supportActionBar
        if (actionbar != null) {
            actionbar.setDisplayHomeAsUpEnabled(true)
            // Change action bar text
            actionbar.title = "CALCULATE BMI"
        }

        // Set up toolbar actions
        toolbar_bmi_activity.setNavigationOnClickListener {
            onBackPressed()
        }

        // Setup button
        btnCalculateUnits.setOnClickListener {
            when {
                validateTextFields() -> {
                    val heightFeet: Float = etFeet.text.toString().toFloat()
                    val heightInches: Float = etInches.text.toString().toFloat()

                    val heightTotal: Float = heightFeet * 12 + heightInches
                    val weightValue: Float = etWeight.text.toString().toFloat()

                    val bmi = 703 * (weightValue / (heightTotal * heightTotal))

                    displayBMIResult(bmi)

                }
                validateMetricTextFields() -> {
                    val height: Float = etMetricUnitHeight.text.toString().toFloat() / 100
                    val weight: Float = etMetricUnitWeight.text.toString().toFloat()

                    val bmi = weight / (height * height)

                    displayBMIResult(bmi)

                }
                else -> {
                    Toast.makeText(
                        this,
                        "Please enter valid values.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }

        // Setup radio button listener
        makeUsUnitsVisible()
        rgUnits.setOnCheckedChangeListener { group, checkedId ->
            if (checkedId == R.id.rbUsUnits){
                makeUsUnitsVisible()
            } else {
                makeMetricUnitsVisible()
        }
        }
    }

    private fun makeMetricUnitsVisible(){
        currentVisibleView = METRIC_UNIT_VIEW

        tilUsUnitWeight.visibility = View.GONE
        llUsHeightView.visibility = View.GONE

        etMetricUnitHeight.text!!.clear()
        etMetricUnitWeight.text!!.clear()

        tilMetricUnitHeight.visibility = View.VISIBLE
        tilMetricUnitWeight.visibility = View.VISIBLE

        llDisplayBMIResult.visibility = View.GONE
    }
    private fun makeUsUnitsVisible(){
        currentVisibleView = US_UNIT_VIEW

        tilUsUnitWeight.visibility = View.VISIBLE
        llUsHeightView.visibility = View.VISIBLE

        etWeight.text!!.clear()
        etFeet.text!!.clear()
        etInches.text!!.clear()

        tilMetricUnitHeight.visibility = View.GONE
        tilMetricUnitWeight.visibility = View.GONE

        llDisplayBMIResult.visibility = View.GONE
    }

    private fun displayBMIResult(bmi: Float){
        val bmiLabel: String
        val bmiDescription: String

        if (bmi.compareTo(15f) <= 0) {
            bmiLabel = "Very severely underweight"
            bmiDescription = "Oops! You really need to take better care of yourself! Eat more!"
        } else if (bmi.compareTo(15f) > 0 && bmi.compareTo(16f) <= 0
        ) {
            bmiLabel = "Severely underweight"
            bmiDescription = "Oops!You really need to take better care of yourself! Eat more!"
        } else if (bmi.compareTo(16f) > 0 && bmi.compareTo(18.5f) <= 0
        ) {
            bmiLabel = "Underweight"
            bmiDescription = "Oops! You really need to take better care of yourself! Eat more!"
        } else if (bmi.compareTo(18.5f) > 0 && bmi.compareTo(25f) <= 0
        ) {
            bmiLabel = "Normal"
            bmiDescription = "Congratulations! You are in a good shape!"
        } else if (java.lang.Float.compare(bmi, 25f) > 0 && java.lang.Float.compare(
                bmi,
                30f
            ) <= 0
        ) {
            bmiLabel = "Overweight"
            bmiDescription = "Oops! You really need to take care of your yourself! Workout maybe!"
        } else if (bmi.compareTo(30f) > 0 && bmi.compareTo(35f) <= 0
        ) {
            bmiLabel = "Obese Class | (Moderately obese)"
            bmiDescription = "Oops! You really need to take care of your yourself! Workout maybe!"
        } else if (bmi.compareTo(35f) > 0 && bmi.compareTo(40f) <= 0
        ) {
            bmiLabel = "Obese Class || (Severely obese)"
            bmiDescription = "OMG! You are in a very dangerous condition! Act now!"
        } else {
            bmiLabel = "Obese Class ||| (Very Severely obese)"
            bmiDescription = "OMG! You are in a very dangerous condition! Act now!"
        }

        llDisplayBMIResult.visibility = View.VISIBLE

        /*
        tvBMINumber.visibility = View.VISIBLE
        tvYourBMI.visibility = View.VISIBLE
        tvBMIType.visibility = View.VISIBLE
        tvBMIDescription.visibility = View.VISIBLE

         */

        // Shortens Value
        val bmiValue = BigDecimal(bmi.toDouble()).setScale(2, RoundingMode.HALF_EVEN).toString()

        tvBMINumber.text = bmiValue
        tvBMIType.text = bmiLabel
        tvBMIDescription.text = bmiDescription

    }

    private fun validateTextFields(): Boolean{
        var isValid = true

        if (etWeight.text.toString().isEmpty())
            isValid = false
        else if (etFeet.text.toString().isEmpty())
            isValid = false
        else if (etInches.text.toString().isEmpty())
            isValid = false

        return isValid
    }
    private fun validateMetricTextFields(): Boolean{
        var isValid = true

        if (etMetricUnitHeight.text.toString().isEmpty())
            isValid = false
        else if (etMetricUnitWeight.text.toString().isEmpty())
            isValid = false

        return isValid
    }

}