package com.esprit.smarktgo.view

import android.app.AlertDialog
import com.esprit.smarktgo.R

class ContactDialog(val mActivity: SettingsActivity) {

    private lateinit var dialog: AlertDialog

    fun show(){
        /**set View*/
        val infalter = mActivity.layoutInflater
        val dialogView = infalter.inflate(R.layout.contact_dialog,null)
        /**set Dialog*/
        val bulider = AlertDialog.Builder(mActivity)
        bulider.setView(dialogView)
        bulider.setCancelable(true)
        dialog = bulider.create()
        dialog.show()
    }

}