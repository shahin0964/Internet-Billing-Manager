package com.example

import android.app.Application
import android.util.Log
import com.google.firebase.FirebaseApp

class IspApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        try {
            if (FirebaseApp.getApps(this).isEmpty()) {
                FirebaseApp.initializeApp(this)
            }
            Log.d("IspApplication", "FirebaseApp initialized successfully")
        } catch (e: Exception) {
            Log.e("IspApplication", "Failed to initialize FirebaseApp: ${e.message}", e)
        }
    }
}
