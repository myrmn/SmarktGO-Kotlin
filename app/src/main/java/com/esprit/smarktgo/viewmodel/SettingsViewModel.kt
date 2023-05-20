package com.esprit.smarktgo.viewmodel

import androidx.lifecycle.ViewModel
import com.esprit.smarktgo.R
import com.esprit.smarktgo.model.ProfileItem
import com.esprit.smarktgo.view.SettingsActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import kotlin.collections.ArrayList

class SettingsViewModel(settingsActivity: SettingsActivity) : ViewModel() {


    private val mActivity = settingsActivity

    fun getList(): ArrayList<ProfileItem> {
        val list = ArrayList<ProfileItem>()
        list.add(ProfileItem(mActivity.getString(R.string.languages), R.drawable.ic_baseline_language_24))
        list.add(ProfileItem(mActivity.getString(R.string.contact), R.drawable.ic_baseline_contact_support_24))
        list.add(ProfileItem(mActivity.getString(R.string.log_out), R.drawable.ic_baseline_logout_24))
        return list
    }

    fun logOut() {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build()
        val mGoogleSignInClient = GoogleSignIn.getClient(mActivity, gso)
        mGoogleSignInClient.signOut()
        FirebaseAuth.getInstance().signOut()
    }
}