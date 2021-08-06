package gr.atcom.upworkproject

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import gr.atcom.upworkproject.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

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

    }
}