package com.esprit.smarktgo.view


import android.content.pm.PackageManager
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.appcompat.app.AppCompatActivity
import com.esprit.smarktgo.R
import com.esprit.smarktgo.databinding.ActivityWalletBinding
import com.esprit.smarktgo.viewmodel.CAMERA_REQUEST_CODE
import com.esprit.smarktgo.viewmodel.STORAGE_REQUEST_CODE
import com.esprit.smarktgo.viewmodel.WalletViewModel

class WalletActivity : AppCompatActivity() {

    lateinit var binding: ActivityWalletBinding
    lateinit var walletViewModel: WalletViewModel
    lateinit var id: String
    var wallet: Double = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_wallet)
        binding = ActivityWalletBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.toolbar.setNavigationIcon(R.drawable.ic_baseline_arrow_back_24)
        binding.toolbar.setNavigationOnClickListener { finish() }

        walletViewModel = WalletViewModel(this)
        id = intent.getStringExtra("id")!!
        wallet = intent.getDoubleExtra("wallet", 0.0)

        binding.scanButton.setOnClickListener {
            walletViewModel.showInputImageDialog()
        }


        binding.code.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {
                walletViewModel.fill(binding.code.text.toString())
            }
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            }
        })

    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when (requestCode) {
            CAMERA_REQUEST_CODE -> {
                if (grantResults.isNotEmpty()) {
                    val cameraAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED
                    val storageAccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED

                    if (cameraAccepted && storageAccepted) {
                        walletViewModel.pickImageCamera()
                    } else {
                        walletViewModel.showToast(this.getString(R.string.camera_required))
                    }
                }
            }
            STORAGE_REQUEST_CODE -> {
                if (grantResults.isNotEmpty()) {
                    val storageAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED
                    if (storageAccepted) {
                        walletViewModel.pickImageGallery()

                    } else {
                        walletViewModel.showToast(this.getString(R.string.storage_required))
                    }
                }
            }
        }
    }



}