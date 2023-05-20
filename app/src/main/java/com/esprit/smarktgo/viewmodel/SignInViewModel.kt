package com.esprit.smarktgo.viewmodel

import android.content.ContentValues
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.esprit.smarktgo.R
import com.esprit.smarktgo.model.User
import com.esprit.smarktgo.repository.UserRepository
import com.esprit.smarktgo.view.SignInActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

class SignInViewModel(signInActivity: SignInActivity): ViewModel() {

    var mGoogleSignInClient: GoogleSignInClient
    var gso: GoogleSignInOptions

    val mActivity = signInActivity
    var userRepository: UserRepository

    var auth: FirebaseAuth
    var callbacks: PhoneAuthProvider.OnVerificationStateChangedCallbacks
    lateinit var storedVerificationId: String
    lateinit var resendToken: PhoneAuthProvider.ForceResendingToken

    init{
        userRepository = UserRepository()
        gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build()
        mGoogleSignInClient = GoogleSignIn.getClient(signInActivity, gso)
        auth = FirebaseAuth.getInstance()

        callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                Log.d("GFG", "onVerificationCompleted Success")
            }

            override fun onVerificationFailed(e: FirebaseException) {
                mActivity.showSnackBar("Try later!")
                Log.d("GFG", "onVerificationFailed  $e")
            }

            override fun onCodeSent(verificationId: String, token: PhoneAuthProvider.ForceResendingToken)
            {
                Log.d("GFG","onCodeSent: $verificationId")
                storedVerificationId = verificationId
                resendToken = token
                mActivity.navigateToOtpActivity(storedVerificationId)
            }
        }
    }

    fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            val account: GoogleSignInAccount = completedTask.getResult(ApiException::class.java)
            val user = User(id = account.email.toString(), fullName = account.displayName.toString(), wallet = 0.0)

            viewModelScope.launch {
                val signInResult = userRepository.signIn(user)
                if (signInResult==null) {
                    val signUpResult = userRepository.signUp(user)
                    if (signUpResult!=null)
                        mActivity.navigateToMainActivity(true)
                    else
                        mActivity.navigateToMainActivity(false)
                } else {
                    mActivity.navigateToMainActivity(true)
                }
            }
        } catch (e: ApiException) {
            Log.w(ContentValues.TAG, "signInResult:failed code=" + e.statusCode)
            mActivity.navigateToMainActivity(false)
        }
    }

    fun signInWithPhoneNumber(number: String)  : Boolean {
        if(number.isEmpty())
        {
            mActivity.showError(mActivity.getString(R.string.type_your_phone_number))
            return false
        }
        else if(number.length!=8)
        {
            mActivity.showError(mActivity.getString(R.string.type_a_valid_phone_number))
            return false
        }
        else
            sendVerificationCode("+216$number")
        return true
    }

    private fun sendVerificationCode(number: String) {
        val options = PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber(number) // Phone number to verify
            .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
            .setActivity(mActivity) // Activity (for callback binding)
            .setCallbacks(callbacks) // OnVerificationStateChangedCallbacks
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)
        Log.d("GFG" , "Auth started")
    }


}