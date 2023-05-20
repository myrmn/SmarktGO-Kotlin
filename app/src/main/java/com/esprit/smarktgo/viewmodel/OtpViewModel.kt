package com.esprit.smarktgo.viewmodel

import android.content.ContentValues
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.esprit.smarktgo.R
import com.esprit.smarktgo.model.User
import com.esprit.smarktgo.repository.UserRepository
import com.esprit.smarktgo.view.OtpActivity
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import kotlinx.coroutines.launch
import java.util.Random


class OtpViewModel( otpActivity: OtpActivity): ViewModel() {

    var auth: FirebaseAuth
    val mActivity = otpActivity
    var userRepository: UserRepository

    init {
        userRepository = UserRepository()
        auth = FirebaseAuth.getInstance()
    }

    fun verifyOTP(otp: String, storedVerificationId: String) {
        if (otp.isEmpty())
            mActivity.showError(mActivity.getString(R.string.type_the_otp))
        else if (otp.length != 6)
            mActivity.showError(mActivity.getString(R.string.type_a_digit_6_code))
        else {
            val credential: PhoneAuthCredential = PhoneAuthProvider.getCredential(storedVerificationId, otp)
            signInWithPhoneAuthCredential(credential)
        }
    }

    // verifies if the code matches sent by firebase
    fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        auth.signInWithCredential(credential)
            .addOnCompleteListener(mActivity) { task ->
                if (task.isSuccessful) {
                    handleSignInResult()
                } else {
                    if (task.exception is FirebaseAuthInvalidCredentialsException) {
                        mActivity.showError(mActivity.getString(R.string.invalid_otp))
                    }
                }
            }
    }

    fun handleSignInResult() {
        try {
            val account = auth.currentUser
            val user = User(account?.phoneNumber.toString(), "", 0.0)
            val random = Random()
            val randomNumber: Int = random.nextInt(900) + 100
            viewModelScope.launch {
                val signInResult = userRepository.signIn(user)
                if (signInResult==null) {
                    val signUpResult = userRepository.signUp(User(account?.phoneNumber.toString(), "RandomUser$randomNumber", 0.0))
                    if (signUpResult!=null)
                        mActivity.navigate(true)
                    else
                        mActivity.navigate(false)
                } else {
                    mActivity.navigate(true)
                }
            }

        } catch (e: ApiException) {
            Log.w(ContentValues.TAG, "signInResult:failed code=" + e.statusCode)
            mActivity.navigate(false)
        }
    }

}
