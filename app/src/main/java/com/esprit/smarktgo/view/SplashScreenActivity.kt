package com.esprit.smarktgo.view

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.esprit.smarktgo.R
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.firebase.auth.FirebaseAuth
import java.util.*

const val PREF_NAME = "PREF_SMARKT_GO"
const val LANGUAGE = "LANGUAGE"

class SplashScreenActivity : AppCompatActivity() {

    private lateinit var handler: Handler
    lateinit var preference : SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        preference=getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        setLocate(preference.getString(LANGUAGE,"en")!!)

        val googleAccount = GoogleSignIn.getLastSignedInAccount(this)
        val phoneAccount = FirebaseAuth.getInstance().currentUser

        handler = Handler()
        handler.postDelayed({
            if (phoneAccount!=null|| googleAccount?.email !=null)
            {
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            }
            else{
                val intent = Intent(this, SignInActivity::class.java)
                startActivity(intent)
            }
            finish()
        },3000)
    }

    private fun setLocate(lang: String)
    {
        val locale = Locale(lang)
        Locale.setDefault(locale)
        val config = Configuration()
        config.locale = locale
        this.resources.updateConfiguration(config,this.resources.displayMetrics)
    }

}



