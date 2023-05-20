package com.esprit.smarktgo.view

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.esprit.smarktgo.R
import com.esprit.smarktgo.adapter.ProfileAdapter
import com.esprit.smarktgo.databinding.ActivitySettingsBinding
import com.esprit.smarktgo.viewmodel.SettingsViewModel

class SettingsActivity : AppCompatActivity() {

    private lateinit var binding : ActivitySettingsBinding
    private lateinit var settingsViewModel: SettingsViewModel
    private lateinit var profileAdapter: ProfileAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.toolbar.setNavigationIcon(R.drawable.ic_baseline_arrow_back_24)
        binding.toolbar.setNavigationOnClickListener { finish() }

        settingsViewModel = SettingsViewModel(this)
        prepareRecyclerView()

        profileAdapter.setOnItemClickListener(object : ProfileAdapter.onItemClickListener {
            override fun onItemClick(position: Int) {
                when (position) {
                    0 -> LanguagesDialog(this@SettingsActivity).show()
                    1 -> ContactDialog(this@SettingsActivity).show()
                    2 -> logOutDialog()
                }
            }
        })

    }

    private fun prepareRecyclerView() {
        profileAdapter = ProfileAdapter(settingsViewModel.getList())
        binding.rvItems.apply {
            adapter = profileAdapter
            layoutManager = LinearLayoutManager(this.context, LinearLayoutManager.VERTICAL ,false)
        }
    }

    private fun logOut() {
        settingsViewModel.logOut()
        val intent = Intent(this, SignInActivity::class.java)
        startActivity(intent)
    }

    private fun logOutDialog() {
        val dialogBuilder = AlertDialog.Builder(this)
        dialogBuilder.setMessage("")
            .setCancelable(false)
            .setPositiveButton(this.getString(R.string.yes), DialogInterface.OnClickListener { dialog, id ->
                logOut()
            })
            .setNegativeButton(this.getString(R.string.no), DialogInterface.OnClickListener { dialog, id ->
                dialog.cancel()
            })

        val alert = dialogBuilder.create()
        alert.setTitle("${this.getString(R.string.log_out)}?")
        alert.show()
    }

}