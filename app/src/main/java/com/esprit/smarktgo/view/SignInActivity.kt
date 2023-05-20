package com.esprit.smarktgo.view

import android.content.Intent
import android.os.Bundle
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.esprit.smarktgo.R
import com.esprit.smarktgo.databinding.ActivitySignInBinding
import com.esprit.smarktgo.viewmodel.SignInViewModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.tasks.Task
import com.google.android.material.snackbar.Snackbar


const val RC_SIGN_IN = 1001

class SignInActivity : AppCompatActivity() {

    lateinit var signInViewModel : SignInViewModel
    private lateinit var binding : ActivitySignInBinding
    val loading = LoadingDialog(this)
    var phoneNumber: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)
        binding = ActivitySignInBinding.inflate(layoutInflater)
        setContentView(binding.root)

        signInViewModel = SignInViewModel(this)

        binding.googleButton.setOnClickListener {
            signInWithGoogle()
        }

        binding.phoneButton.setOnClickListener {
            signInWithPhoneNumber()
        }
    }

    private fun signInWithGoogle() {
        val signInIntent = signInViewModel.mGoogleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    @Override
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            val task: Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(data)
            loading.startLoading()
            window.setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
            signInViewModel.handleSignInResult(task)
        }
    }

    fun navigateToMainActivity(result:Boolean) {
        loading.dismiss()
        window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)

        if (result) {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        } else
            showSnackBar("Failed!")
    }

    private fun signInWithPhoneNumber() {
        phoneNumber= binding.phone.text?.trim().toString()
        val result = signInViewModel.signInWithPhoneNumber(phoneNumber)
        if(result)
        {
            loading.startLoading()
            window.setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
        }
    }

    fun navigateToOtpActivity(storedVerificationId : String) {
        loading.dismiss()
        window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)

        val intent = Intent(applicationContext, OtpActivity::class.java)
        intent.putExtra("storedVerificationId",storedVerificationId)
        intent.putExtra("phoneNumber", phoneNumber)
        startActivity(intent)
    }

    fun showSnackBar(text: String)
    {
        if(text=="Try later!")
        {
            loading.dismiss()
            window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
        }
        Snackbar.make(findViewById(R.id.signInConstraintLayout),text, Snackbar.LENGTH_LONG).show()
    }

    fun showError(errorText : String)
    {
        binding.phoneContainer.error = errorText
    }

}