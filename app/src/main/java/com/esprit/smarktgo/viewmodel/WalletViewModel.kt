package com.esprit.smarktgo.viewmodel

import android.Manifest
import android.app.Activity
import android.app.ProgressDialog
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import android.view.Menu
import android.widget.PopupMenu
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.esprit.smarktgo.R
import com.esprit.smarktgo.model.UpdateTicket
import com.esprit.smarktgo.repository.TicketRepository
import com.esprit.smarktgo.view.WalletActivity
import com.google.android.gms.common.api.ApiException
import com.google.mlkit.vision.common.InputImage
import kotlinx.coroutines.launch
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.TextRecognizer
import com.google.mlkit.vision.text.latin.TextRecognizerOptions

const val CAMERA_REQUEST_CODE = 100
const val STORAGE_REQUEST_CODE = 101

class WalletViewModel(mActivity: WalletActivity): ViewModel() {

    private val mActivity = mActivity

    var imageUri: Uri?=null
    var cameraPermissions:Array<String>
    var storagePermissions:Array<String>
    private val ticketRepository: TicketRepository = TicketRepository()
    private var progressDialog: ProgressDialog
    private val textRecognizer: TextRecognizer

    init{
        cameraPermissions = arrayOf(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)
        storagePermissions = arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        progressDialog = ProgressDialog(mActivity)
        textRecognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)

    }



    fun fill(code: String) : Boolean {
        if(code.isEmpty())
        {
            showError(mActivity.getString(R.string.type_the_ticket_code))
            return false
        }
        else if(code.length!=6)
        {
            showError(mActivity.getString(R.string.type_a_valid_code))
            return false
        }
        else{
            viewModelScope.launch {
                var found = false
                val result = ticketRepository.getAll()
                result.let {
                    for(ticket in result!!)
                    {
                        if(code==ticket.code.toString()&&!ticket.used)
                        {
                            found = true
                            update(code)
                            break
                        }
                        else if(code==ticket.code.toString())
                        {
                            showError(mActivity.getString(R.string.invalid_ticket))
                            found = true
                            break
                        }
                    }
                    if(!found)
                        showError(mActivity.getString(R.string.ticket_not_found))
                }
            }
        }
        return true
    }

    private fun update(code: String)
    {
        try {
        viewModelScope.launch {
            val updateTicket = UpdateTicket(code.toInt(),mActivity.id,mActivity.wallet)
            val result = ticketRepository.update(updateTicket)
            result?.let {
                mActivity.finish()
            }
        }
        } catch (e: ApiException) {
            Log.w(ContentValues.TAG, e.statusCode.toString())
        }
    }

    fun showToast(message:String){
        Toast.makeText(mActivity,message, Toast.LENGTH_SHORT).show()
    }

    private fun showError(errorText: String) {
        mActivity.binding.codeContainer.error = errorText
    }

     private fun checkGalleryPermission():Boolean =  ContextCompat.checkSelfPermission(mActivity, Manifest.permission.WRITE_EXTERNAL_STORAGE)== PackageManager.PERMISSION_GRANTED

     private fun checkCameraPermission():Boolean {
        val storagePermission =   ContextCompat.checkSelfPermission(mActivity,
            Manifest.permission.WRITE_EXTERNAL_STORAGE)== PackageManager.PERMISSION_GRANTED
        val cameraPermission= ContextCompat.checkSelfPermission(mActivity,
            Manifest.permission.CAMERA)== PackageManager.PERMISSION_GRANTED
        return storagePermission && cameraPermission
    }

     private fun requestGalleryPermission(){
        ActivityCompat.requestPermissions(mActivity,storagePermissions,STORAGE_REQUEST_CODE)
    }
     private fun requestCameraPermission(){
        ActivityCompat.requestPermissions(mActivity,cameraPermissions,CAMERA_REQUEST_CODE)
    }

     fun pickImageGallery(){
        val intent= Intent(Intent.ACTION_PICK)
        intent.type="image/*"
        galleryActivityResultLauncher.launch(intent)
     }

     fun pickImageCamera(){
        val values= ContentValues()
        values.put(MediaStore.Images.Media.TITLE,"SIMPLE")
        values.put(MediaStore.Images.Media.DESCRIPTION,"DESCRIPTION")
        imageUri=mActivity.contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,values)
        val intent= Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        intent.putExtra(MediaStore.EXTRA_OUTPUT,imageUri)
        cameraActivityResultLauncher.launch(intent)

     }

    private val cameraActivityResultLauncher =
        mActivity.registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                recognizeTextFromImage()
            }
        }

    private val galleryActivityResultLauncher =
        mActivity.registerForActivityResult(ActivityResultContracts.StartActivityForResult()){ result->
            if (result.resultCode== Activity.RESULT_OK)
            {
                val data=result.data
                imageUri=data!!.data
                recognizeTextFromImage()
            }

            else {
                showToast(mActivity.getString(R.string.selection_failed))
            }
        }

    fun recognizeTextFromImage() {
        progressDialog.setTitle("Please wait")
        progressDialog.setCanceledOnTouchOutside(false)
        progressDialog.setMessage(mActivity.getString(R.string.preparing_image))
        progressDialog.show()
        try {
            val inputImage = InputImage.fromFilePath(mActivity,imageUri!!)
            progressDialog.setMessage(mActivity.getString(R.string.recognizing_text))
            val textTaskResult = textRecognizer.process(inputImage)
                .addOnSuccessListener { text ->
                    progressDialog.dismiss()
                    val recognizedText = text.text
                    mActivity.binding.code.setText(recognizedText)
                }
                .addOnFailureListener { e ->
                    progressDialog.dismiss()
                    Log.e("", e.message.toString())
                    showToast(mActivity.getString(R.string.recognizing_failed)+"${e.message}")
                }
        } catch (e: Exception) {
            progressDialog.dismiss()
            showToast(mActivity.getString(R.string.preparing_failed)+"${e.message}")
        }
    }

    fun showInputImageDialog() {
        val popupMenu = PopupMenu(mActivity, mActivity.binding.scanButton)
        popupMenu.menu.add(Menu.NONE, 1, 1, "CAMERA")
        popupMenu.menu.add(Menu.NONE, 2, 2, "GALLERY")
        popupMenu.show()
        popupMenu.setOnMenuItemClickListener { menuItem ->
            val id = menuItem.itemId
            if (id == 1) {
                if (checkCameraPermission()) {
                    pickImageCamera()
                } else {
                    requestCameraPermission()
                }
            } else if (id == 2) {
                if (checkGalleryPermission()) {
                    pickImageGallery()
                } else {
                    requestGalleryPermission()
                }
            }
            return@setOnMenuItemClickListener true
        }
    }


}