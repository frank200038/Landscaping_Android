package com.jfcgraphicsllc.landscaping


import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import android.view.animation.Animation
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
        val navView: BottomNavigationView = findViewById(R.id.nav_view)
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)
        val navController = findNavController(R.id.nav_host_fragment)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destination.
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
        bgapp.startAnimation(bganim)

        val bgapp2: FrameLayout = findViewById(R.id.bgsplash2)
        val bganim2 = AnimationUtils.loadAnimation(this,R.anim.bganim2)
        bganim2.setAnimationListener(object:Animation.AnimationListener{
            override fun onAnimationRepeat(animation: Animation?) {

            }

            override fun onAnimationEnd(animation: Animation?) {
                FirebaseAuth.getInstance().addAuthStateListener {
                    val user = FirebaseAuth.getInstance().currentUser
                    Log.e("User","${user}")
                    Log.e("Animation Finish","Finish")
                    if(user == null) {
                        if (!this@MainActivity.isFinishing) {
                            val alertDialog = AlertDialog.Builder(this@MainActivity)
                            alertDialog.setTitle("Not Logged In")
                            alertDialog.setMessage("You Need to Sign In")
                            alertDialog.setNegativeButton("Cancel") { dialog, which ->
                                finish()
                            }
                            alertDialog.setPositiveButton("Sign In") { dialog, which ->
                                val providers = arrayListOf(
                                    AuthUI.IdpConfig.EmailBuilder().setAllowNewAccounts(false)
                                        .build()
                                )
                                startActivityForResult(
                                    AuthUI.getInstance()
                                        .createSignInIntentBuilder()
                                        .setTheme(R.style.bg)
                                        .setAvailableProviders(providers)
                                        .build(),
                                    0
                                )
                            }
                            alertDialog.setCancelable(false)
                            alertDialog.show()
                        }
                    }
                }
            }

            override fun onAnimationStart(animation: Animation?) {

            }

        })
        bgapp2.startAnimation(bganim2)

    }


}
