package gr.atcom.upworkproject


import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import gr.atcom.upworkproject.databinding.ActivityCalculateBmiBinding
import gr.atcom.upworkproject.presenter.BmiCalculationPresenter
import gr.atcom.upworkproject.view.BmiCalculationView


class CalculateBmiActivity : AppCompatActivity(), BmiCalculationView {

    private var mInterstitialAd: InterstitialAd? = null
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

        // Initializing Interstitial ads
        initAdMob()
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

            if (mInterstitialAd != null) {
                mInterstitialAd?.show(this)
            } else {
                Log.d("AdMob", "The interstitial ad wasn't ready yet.")
                calculateBmi()
            }
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

    private fun initAdMob() {
        var adRequest = AdRequest.Builder().build()

        InterstitialAd.load(
            this,
            "ca-app-pub-3940256099942544/1033173712",
            adRequest,
            object : InterstitialAdLoadCallback() {
                override fun onAdFailedToLoad(adError: LoadAdError) {
                    Log.d("AdMob", adError?.message)
                    mInterstitialAd = null
                }

                override fun onAdLoaded(interstitialAd: InterstitialAd) {
                    Log.d("AdMob", "Ad was loaded.")
                    mInterstitialAd = interstitialAd

                    mInterstitialAd?.fullScreenContentCallback = object : FullScreenContentCallback() {
                        override fun onAdDismissedFullScreenContent() {
                            Log.d("AdMob", "Ad was dismissed.")
                            mInterstitialAd = null
                            calculateBmi()
                        }

                        override fun onAdFailedToShowFullScreenContent(adError: AdError?) {
                            Log.d("AdMob", "Ad failed to show.")
                        }

                        override fun onAdShowedFullScreenContent() {
                            Log.d("AdMob", "Ad showed fullscreen content.")
                            mInterstitialAd = null
                        }
                    }
                }
            })



    }

    private fun calculateBmi() {
        if (binding.nameInput.text.toString().isEmpty()) {
            Toast.makeText(applicationContext, "Please add name", Toast.LENGTH_SHORT).show()
            return
        }
        presenter.calculateBmi(
            binding.weightPicker.value,
            binding.heightPicker.value,
            binding.genderPicker.value,
            binding.nameInput.text.toString()
        )
    }

    override fun showBmi(bmi: Double, name: String) {
        val intent = Intent(this, MainActivity::class.java)
        val bundle = Bundle()
        bundle.putDouble(getString(R.string.bmiValueBundle), bmi)
        bundle.putString(getString(R.string.bmiNameBundle), name)
        intent.putExtras(bundle)
        startActivity(intent)
        overridePendingTransition(
            R.anim.animation_slide_up,
            R.anim.animation_zoom_out
        )
    }


}

