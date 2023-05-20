package com.esprit.smarktgo.view

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.util.Log
import android.widget.Button
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import com.esprit.smarktgo.R
import com.esprit.smarktgo.model.Item
import java.util.*

class LanguagesDialog(val mActivity: SettingsActivity) {

    private lateinit var dialog: AlertDialog
    lateinit var radioGroup: RadioGroup
    lateinit var englishRadioButton: RadioButton
    lateinit var frenchRadioButton: RadioButton
    lateinit var preference : SharedPreferences

    fun show(){
        /**set View*/
        val infalter = mActivity.layoutInflater
        val dialogView = infalter.inflate(R.layout.languages_dialog,null)
        /**set Dialog*/
        val bulider = AlertDialog.Builder(mActivity)
        bulider.setView(dialogView)
        bulider.setCancelable(true)
        dialog = bulider.create()
        dialog.show()

        radioGroup = dialog.findViewById<RadioGroup>(R.id.radioGroup)
        englishRadioButton = dialog.findViewById<RadioButton>(R.id.englishRadioButton)
        frenchRadioButton = dialog.findViewById<RadioButton>(R.id.frenchRadioButton)

        preference= mActivity.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)

        if(preference.getString(LANGUAGE,"en")=="en")
        englishRadioButton.isChecked = true
        else frenchRadioButton.isChecked = true

        radioGroup.setOnCheckedChangeListener { _, checkedId ->
            if(englishRadioButton.isChecked)
                setLocate("en")
            else setLocate("fr")
        }
    }

    private fun setLocate(lang: String)
    {
        val locale = Locale(lang)
        Locale.setDefault(locale)
        val config = Configuration()
        config.locale = locale
        mActivity.resources.updateConfiguration(config,mActivity.resources.displayMetrics)

        val editor=preference.edit()
        editor.apply{
            editor.putString(LANGUAGE,lang)
        }.apply()
        dialog.dismiss()
        reStartApp()
    }

    private fun reStartApp()
    {
        val ctx = mActivity
        val pm: PackageManager = ctx.getPackageManager()
        val intent = pm.getLaunchIntentForPackage(ctx.getPackageName())
        val mainIntent = Intent.makeRestartActivityTask(intent!!.component)
        ctx.startActivity(mainIntent)
        Runtime.getRuntime().exit(0)
    }

}