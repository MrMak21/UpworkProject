package gr.atcom.upworkproject

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import gr.atcom.upworkproject.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding

    var bmiValue: Double = -1.0
    lateinit var name: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        getPassData()
        initResources()
        initLayout()
    }

    private fun initResources() {
        binding.appToolbar.toolbarTitle.setText(R.string.toolbarTitle)
        binding.titlePlaceholder.setText(R.string.titlePlaceholder)
        binding.contentTitle.setText(R.string.contentTitle)
        binding.contentSubtitle.setText(R.string.contentSubtitle)
        binding.shareBtn.setText(R.string.shareBtn)
        binding.rateBtn.setText(R.string.rateBtn)
    }

    private fun initLayout() {
        setBmiResults(bmiValue)

        binding.appToolbar.toolbarBackImageView.setOnClickListener {
            onBackPressed()
        }
    }

    private fun getPassData() {
        if (intent.extras != null) {
            bmiValue = intent.extras!!.getDouble(getString(R.string.bmiValueBundle))
            name = intent.extras!!.getString(getString(R.string.bmiNameBundle))!!
        }
    }

    private fun setBmiResults(bmi: Double) {
        if (bmi == -1.0)
            return

        val firstPart = bmi.toString().substringBefore(".")
        val secondPart = bmi.toString().substringAfter(".")
        binding.bodyMassValue.text = firstPart
        binding.bodyMassValueSecond.text = ".$secondPart"


        var weightStatus = ""
        when {
            bmi < 16 -> {
                // Severely Underweight
                weightStatus = "SEVERELY UNDERWEIGHT"
            }
            bmi in 16.0..18.4 -> {
                // Underweight
                weightStatus = "UNDERWEIGHT"
            }
            bmi in 18.5..24.9 -> {
                // Normal
                weightStatus = "NORMAL"
            }
            bmi in 25.0..29.9 -> {
                // Overweight
                weightStatus = "OVERWEIGHT"
            }
            bmi in 30.0..34.9 -> {
                // Moderately Obese
                weightStatus = "MODERATELY OBESE"
            }
            bmi in 35.0..39.9 -> {
                // Severely Obese
                weightStatus = "SEVERELY OBESE"
            }
            else -> {
                // Morbidly Obese
                weightStatus = "MORBIDLY OBESE"
            }
        }

        val shownText = "HELLO ${name.toUpperCase()}, YOU ARE $weightStatus"
        binding.contentTitle.text = shownText
    }
}