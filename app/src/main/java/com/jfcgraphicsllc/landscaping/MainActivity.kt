package com.jfcgraphicsllc.landscaping


import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import android.view.animation.AnimationUtils
import android.widget.FrameLayout
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.IdpResponse
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.crashlytics.FirebaseCrashlytics


class MainActivity : AppCompatActivity() {

    private lateinit var firebaseAnalytics: FirebaseAnalytics
    private val crashlytics : FirebaseCrashlytics = FirebaseCrashlytics.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        anime()
        val user = FirebaseAuth.getInstance().currentUser
        Log.e("User","${user}")
        if (user == null)
        {
            val providers = arrayListOf(AuthUI.IdpConfig.EmailBuilder().setAllowNewAccounts(false).build())
            startActivityForResult(
                AuthUI.getInstance()
                    .createSignInIntentBuilder()
                    .setAvailableProviders(providers)
                    .setTheme(R.style.bg)
                    .build(),
                0)
            Log.e("Start Activity","Start Activity")
        }
        val navView: BottomNavigationView = findViewById(R.id.nav_view)
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)
        val navController = findNavController(R.id.nav_host_fragment)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(setOf(
                R.id.navigation_home, R.id.navigation_dashboard))
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
        firebaseAnalytics = FirebaseAnalytics.getInstance(this)


    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        Log.e("Request","${requestCode}")
        if(requestCode == 0)
        {
            val response = IdpResponse.fromResultIntent(data)
            if(resultCode == Activity.RESULT_CANCELED)
            {

                val alertDialog = AlertDialog.Builder(this)
                alertDialog.setTitle("Not Logged In")
                alertDialog.setMessage("You need to log in to use this App")
                alertDialog.setNegativeButton("Cancel"){dialog, which ->
                    finish()
                }
                alertDialog.setPositiveButton("Sign In"){dialog, which ->
                    val providers = arrayListOf(AuthUI.IdpConfig.EmailBuilder().setAllowNewAccounts(false).build())
                    startActivityForResult(AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setTheme(R.style.bg)
                        .setAvailableProviders(providers)
                        .build(),
                        0)
                }
                alertDialog.setCancelable(false)
                alertDialog.show()
            }
            else if (resultCode == Activity.RESULT_OK)
            {
                crashlytics.setUserId(FirebaseAuth.getInstance().currentUser!!.uid)
            }
            else
            {
                if (response != null) {
                    Log.e("LogIn Error","${response.error?.errorCode}")
                }
            }
        }

    }

    fun anime()
    {
        val bgapp: FrameLayout = findViewById(R.id.bgsplash)
        val bganim = AnimationUtils.loadAnimation(this,R.anim.bganim)
        val Y : Float = -2650.toFloat()
        bgapp.animate().translationY(Y).setDuration(1000).setStartDelay(300)
    }


}
