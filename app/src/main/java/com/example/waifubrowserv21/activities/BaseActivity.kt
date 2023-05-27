package com.example.waifubrowserv21.activities

import android.app.Dialog
import android.os.Bundle
import android.os.Handler
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.waifubrowserv21.R
import com.example.waifubrowserv21.databinding.DialogProgressBinding
import com.google.android.material.snackbar.Snackbar

open class BaseActivity : AppCompatActivity() {

    private var doubleBackToExitPressedOnce = false

    private var mProgressDialog: Dialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_base)
    }

    /**
     * Base Activity Progress Dialog shows progress dialog with custom loading text
     */
    fun showProgressDialog() {
        val binding : DialogProgressBinding = DialogProgressBinding.inflate(layoutInflater)
        mProgressDialog = Dialog(this)
        mProgressDialog?.setContentView(binding.root)
        mProgressDialog?.show()
    }

    /**
     * Hide progress dialog which was created using base Activity
     */
    fun hideProgressDialog() {
        mProgressDialog?.hide()
    }

    /**
     * Implementation for double back to exit function
     */
    fun doubleBackToExit() {
        if(doubleBackToExitPressedOnce){
            super.onBackPressed()
            return
        }
        showSnackBarMessage("Please press back again to exit.")
        this.doubleBackToExitPressedOnce = true


        //If user presses back only once, we want to reset the counter after a few seconds
        Handler().postDelayed({ doubleBackToExitPressedOnce = false }, 2000)
    }

    fun showSnackBarMessage(message: String){
        val snackBar = Snackbar.make(
            findViewById(android.R.id.content),
            message,
            Snackbar.LENGTH_LONG)
//        val snackbarView = snackBar.view
//        snackbarView.setBackgroundColor(ContextCompat.getColor(this, R.color.white))
        snackBar.show()
    }
}