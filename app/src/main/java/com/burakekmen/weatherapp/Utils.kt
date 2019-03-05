package com.burakekmen.weatherapp

import android.Manifest
import android.content.Context
import android.content.DialogInterface
import android.content.pm.PackageManager
import android.net.ConnectivityManager
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AlertDialog
import com.kaopiz.kprogresshud.KProgressHUD
import com.google.firebase.iid.FirebaseInstanceId



class Utils(var context: Context) {


    private var requestDialog: KProgressHUD? = null

    fun isOnline(): Boolean {
        val connectivityManager = context
            .getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        val activeNetworkInfo = connectivityManager.activeNetworkInfo
        return activeNetworkInfo != null
    }



    fun dialogGoster() {

        if (requestDialog == null) {
            requestDialog = KProgressHUD.create(context!!)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("Bilgiler alınıyor")
                .setDetailsLabel("Lütfen Bekleyiniz")
                .setCancellable(true)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f)
                .show()
        }

    }

    fun dialogGosterme() {

        if (requestDialog != null) {
            if (requestDialog!!.isShowing) {
                requestDialog!!.dismiss()
                requestDialog = null
            }
        }
    }


//    fun onTokenRefresh() {
//        // Get updated InstanceID token.
//        val refreshedToken = FirebaseInstanceId.getInstance()
//
//        // If you want to send messages to this application instance or
//        // manage this apps subscriptions on the server side, send the
//        // Instance ID token to your app server.
//        sendRegistrationToServer(refreshedToken)
//    }
//



}