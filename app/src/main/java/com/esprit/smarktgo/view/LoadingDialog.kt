package com.esprit.smarktgo.view

import android.app.Activity
import android.app.AlertDialog
import com.esprit.smarktgo.R

class LoadingDialog(val mActivity:Activity) {

    private lateinit var dialog:AlertDialog

    fun startLoading(){
        /**set View*/
        val infalter = mActivity.layoutInflater
        val dialogView = infalter.inflate(R.layout.loading_item,null)
        /**set Dialog*/
        val bulider = AlertDialog.Builder(mActivity)
        bulider.setView(dialogView)
        bulider.setCancelable(false)
        dialog = bulider.create()
        dialog.show()
    }
    fun dismiss(){
        dialog.dismiss()
    }
}