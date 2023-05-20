package com.esprit.smarktgo.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.esprit.smarktgo.R
import com.esprit.smarktgo.databinding.ActivityOtpBinding
import com.esprit.smarktgo.viewmodel.OtpViewModel
import com.google.android.material.snackbar.Snackbar


class OtpActivity : AppCompatActivity() {

    private lateinit var binding : ActivityOtpBinding
    lateinit var otpViewModel : OtpViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_otp)
        binding = ActivityOtpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.toolbar.setNavigationIcon(R.drawable.ic_baseline_arrow_back_24)
        binding.toolbar.setNavigationOnClickListener { finish() }

        otpViewModel = OtpViewModel(this)
        val storedVerificationId= intent.getStringExtra("storedVerificationId")
        val phoneNumber= intent.getStringExtra("phoneNumber")

        binding.text.setText("a 6-digit code has been sent to +216 $phoneNumber")

        binding.otpButton.setOnClickListener {
            val otp = binding.otp.text?.trim().toString()
            otpViewModel.verifyOTP(otp, storedVerificationId!!)
        }
    }

    fun navigate(result:Boolean) {
        if (result) {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        } else
          Snackbar.make(findViewById(R.id.otpConstraintLayout),"Failed!",Snackbar.LENGTH_LONG).show()
    }

    fun showError(errorText : String)
    {
        binding.otpContainer.error = errorText
    }

}