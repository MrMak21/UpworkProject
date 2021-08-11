package gr.atcom.upworkproject

import android.Manifest
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.app.ShareCompat
import androidx.core.content.FileProvider
import com.google.android.gms.ads.*
import com.google.android.gms.ads.nativead.NativeAd
import com.google.android.gms.ads.nativead.NativeAdOptions
import gr.atcom.upworkproject.databinding.ActivityMainBinding
import java.io.File
import java.io.FileOutputStream


class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding

    private val PERMISSION_ALL = 25
    private var bmiValue: Double = -1.0
    lateinit var name: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        MobileAds.initialize(this) {}
        initAdMob()

//        throw RuntimeException("Test Crash") // Force a crash

        getPassData()
        initResources()
        initLayout()
//        checkPermissions()
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

        binding.shareBtn.setOnClickListener {
            val bitmap = getScreenShot(window.decorView.rootView)
            if (bitmap != null) {
                store(bitmap)
            }
        }

        binding.rateBtn.setOnClickListener {
            try {
                startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.appovo.bmicalculator")))
            } catch (e: ActivityNotFoundException) {
                startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.appovo.bmicalculator")))
            }
        }
    }

    private fun initAdMob() {
        val adLoader = AdLoader.Builder(this, "ca-app-pub-3940256099942544/2247696110") //test id
            .forNativeAd { ad : NativeAd ->
                // Show the ad.
                ad.headline?.let {
                    binding.addTitle.text = it
                }

                ad.body?.let {
                    binding.addBody.text = it
                }

                ad.icon?.let {
                    binding.addImage.setImageDrawable(it.drawable)
                }

            }
            .withAdListener(object : AdListener() {
                override fun onAdFailedToLoad(adError: LoadAdError) {
                    // Handle the failure by logging, altering the UI, and so on.
                }
            })
            .withNativeAdOptions(
                NativeAdOptions.Builder()
                // Methods in the NativeAdOptions.Builder class can be
                // used here to specify individual options settings.
                .build())
            .build()

        adLoader.loadAd(AdRequest.Builder().build())
    }

    private fun getPassData() {
        if (intent.extras != null) {
            bmiValue = intent.extras!!.getDouble(getString(R.string.bmiValueBundle))
            name = intent.extras!!.getString(getString(R.string.bmiNameBundle))!!
        }
    }

    fun getScreenShot(view: View): Bitmap? {
        val screenView: View = view.getRootView()
        screenView.setDrawingCacheEnabled(true)
        val bitmap: Bitmap = Bitmap.createBitmap(screenView.getDrawingCache())
        screenView.setDrawingCacheEnabled(false)
        return bitmap
    }

    fun store(bm: Bitmap) {
        val filename = "screenshot.png"
//        val sd = Environment.getExternalStorageDirectory()
        val sd = getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS)
        val dest = File(sd, filename)

        try {
            val out = FileOutputStream(dest)
            bm.compress(Bitmap.CompressFormat.PNG, 90, out)
            out.flush()
            out.close()

            shareFile(dest)
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(this, "Cannot share image", Toast.LENGTH_SHORT).show()
        }
    }


    private fun shareFile(file: File) {


        // Write data in your file
        val uri = FileProvider.getUriForFile(this, BuildConfig.APPLICATION_ID + ".provider", file)

        val intent = ShareCompat.IntentBuilder.from(this)
            .setStream(uri) // uri from FileProvider
            .setType("text/html")
            .intent
            .setAction(Intent.ACTION_VIEW) //Change if needed
            .setDataAndType(uri, "image/*")
            .addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)

        startActivity(intent)
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

    private fun checkPermissions() {
        val PERMISSIONS = arrayOf(
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE
        )

        if (!hasPermissions(this, PERMISSIONS)) {
            ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_ALL)
        } else {
            Log.d("Permissions","Has permissions")
        }
    }

    private fun hasPermissions(context: Context?, permissions: Array<String>): Boolean {
        if (context != null) {
            for (perm in permissions) {
                if (ActivityCompat.checkSelfPermission(
                        context,
                        perm
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    return false
                }
            }
        }
        return true
    }

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(R.anim.animation_still, R.anim.animation_slide_out_right)
    }
}