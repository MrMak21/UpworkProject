package gr.atcom.upworkproject.presenter

import android.util.Log
import gr.atcom.upworkproject.view.BmiCalculationView
import kotlin.math.round

class BmiCalculationPresenter(view: BmiCalculationView) {

    private val view = view

    fun calculateBmi(weight: Int, height: Int, gender: Int, name: String) {
        val bmi = (weight.toDouble().div(height.toDouble()).div(height.toDouble())).times(10000)
        Log.d("Bmi", bmi.round(2).toString())
        view.showBmi(bmi.round(2), name)
    }

    fun Double.round(decimals: Int): Double {
        var multiplier = 1.0
        repeat(decimals) { multiplier *= 10 }
        return round(this * multiplier) / multiplier
    }
}