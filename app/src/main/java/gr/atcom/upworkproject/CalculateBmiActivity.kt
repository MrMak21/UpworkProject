package gr.atcom.upworkproject


import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import gr.atcom.upworkproject.databinding.ActivityCalculateBmiBinding
import gr.atcom.upworkproject.presenter.BmiCalculationPresenter
import gr.atcom.upworkproject.view.BmiCalculationView


class CalculateBmiActivity : AppCompatActivity(), BmiCalculationView {

    lateinit var binding: ActivityCalculateBmiBinding
    lateinit var presenter: BmiCalculationPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCalculateBmiBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()
        presenter = BmiCalculationPresenter(this)

        initResources()
        initLayout()
    }


    private fun initResources() {
        binding.appToolbar.toolbarTitle.setText(R.string.bmiToolbarTitle)
        binding.bmiInputBtn.setText(R.string.bmiBtnText)
        binding.bmiInputSubtitle.setText(R.string.bmiSubtitle)
        binding.bmiInputTitle.setText(R.string.bmiTitle)
        binding.inputHeightLabel.setText(R.string.bmiHeightLabel)
        binding.inputGenderLabel.setText(R.string.bmiGenderLabel)
        binding.inputWeightLabel.setText(R.string.bmiWeightLabel)
    }

    private fun initLayout() {
        binding.bmiInputBtn.setOnClickListener {
            if (binding.nameInput.text.toString().isEmpty()) {
                Toast.makeText(this,"Please add name", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            presenter.calculateBmi(
                binding.weightPicker.value,
                binding.heightPicker.value,
                binding.genderPicker.value,
                binding.nameInput.text.toString()
            )
        }

        binding.appToolbar.toolbarBackImageView.setOnClickListener {
            onBackPressed()
        }

        binding.weightPicker.minValue = 40
        binding.weightPicker.maxValue = 120
        binding.weightPicker.value = 80
        binding.weightPicker.wrapSelectorWheel = false

        binding.heightPicker.minValue = 140
        binding.heightPicker.maxValue = 220
        binding.heightPicker.value = 180
        binding.heightPicker.wrapSelectorWheel = false

        val data = arrayOf("Male", "Female")
        binding.genderPicker.minValue = 1
        binding.genderPicker.maxValue = data.size
        binding.genderPicker.displayedValues = data
        binding.genderPicker.value = 0
    }

    override fun showBmi(bmi: Double, name: String) {
        val intent = Intent(this, MainActivity::class.java)
        val bundle = Bundle()
        bundle.putDouble(getString(R.string.bmiValueBundle), bmi)
        bundle.putString(getString(R.string.bmiNameBundle), name)
        intent.putExtras(bundle)
        startActivity(intent)
    }


}

