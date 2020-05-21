package com.maku.whisblower.ui.activities

import android.Manifest
import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.IntentSender
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.annotation.Nullable
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import com.google.android.material.snackbar.Snackbar
import com.google.android.play.core.appupdate.AppUpdateInfo
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.install.InstallStateUpdatedListener
import com.google.android.play.core.install.model.ActivityResult
import com.google.android.play.core.install.model.ActivityResult.RESULT_IN_APP_UPDATE_FAILED
import com.google.android.play.core.install.model.AppUpdateType.FLEXIBLE
import com.google.android.play.core.install.model.InstallStatus
import com.google.android.play.core.install.model.UpdateAvailability
import com.maku.whisblower.R
import com.maku.whisblower.WhisBlower
import com.maku.whisblower.databinding.ActivityMainBinding
import com.maku.whisblower.utils.*
import com.shreyaspatil.MaterialDialog.MaterialDialog
import timber.log.Timber


class MainActivity : AppCompatActivity() {

    //databinding
    private lateinit var mViewBinding: ActivityMainBinding

    val mContext: Context =
        WhisBlower.applicationContext()

    // Creates instance of the manager.
    var appUpdateManager = AppUpdateManagerFactory.create(mContext)

    // Returns an intent object that you use to check for an update.
    val appUpdateInfoTask = appUpdateManager.appUpdateInfo
    private val MY_REQUEST_CODE = 20154

    override fun onCreate(savedInstanceState: Bundle?) {

        setTheme(R.style.AppTheme)  // Set AppTheme before setting content view.

        super.onCreate(savedInstanceState)

        //initialize the binding
        mViewBinding = DataBindingUtil.setContentView<ActivityMainBinding>(this,
            R.layout.activity_main
        )

        mViewBinding.setLifecycleOwner(this);

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration: AppBarConfiguration = AppBarConfiguration.Builder(
                R.id.mainFragment
            )
            .build()
        val navController: NavController =
            Navigation.findNavController(this, R.id.nav_host_fragment)
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration)

        //handle newtwork changes
        handleNetworkChanges()
        checkUpdate()

    }

    /**
     * Observe network changes i.e. Internet Connectivity
     */
    private fun handleNetworkChanges() {
        NetworkUtils.getNetworkLiveData(applicationContext).observe(this, Observer { isConnected ->
            if (!isConnected) {

                mViewBinding.textViewNetworkStatus.text = getString(R.string.text_no_connectivity)
                mViewBinding.networkStatusLayout.apply {
                    alpha = 0f
                    show()
                    setBackgroundColor(getColorRes(R.color.colorStatusNotConnected))
                    animate()
                        .alpha(1f)
                        .setDuration(ANIMATION_DURATION)
                        .setListener(null)
                }
            } else {
                mViewBinding.textViewNetworkStatus.text = getString(R.string.text_connectivity)
                mViewBinding.networkStatusLayout.apply {
                    setBackgroundColor(getColorRes(R.color.colorStatusConnected))

                    animate()
                        .alpha(0f)
                        .setStartDelay(ANIMATION_DURATION)
                        .setDuration(ANIMATION_DURATION)
                        .setListener(object : AnimatorListenerAdapter() {
                            override fun onAnimationEnd(animation: Animator) {
                                hide()
                            }
                        })
                }
            }
        })
    }

    /**
     * Check for any updates
     */
    private fun checkUpdate() {

        // Before starting an update, register a listener for updates.
        appUpdateManager.registerListener(listener)

        appUpdateInfoTask.addOnSuccessListener { appUpdateInfo ->
            Timber.d(
                "appUpdateInfo : packageName :" + appUpdateInfo.packageName() + ", " + "availableVersionCode :" + appUpdateInfo.availableVersionCode() + ", " + "updateAvailability :" + appUpdateInfo.updateAvailability() + ", " + "installStatus :" + appUpdateInfo.installStatus()
            )
            if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
                && appUpdateInfo.isUpdateTypeAllowed(FLEXIBLE)
            ) {
                requestUpdate(appUpdateInfo)
                Timber.d("UpdateAvailable update is there ")
            } else if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_NOT_AVAILABLE) {
                Timber.d("Update 3")
//                popupSnackbarForCompleteUpdate()
            } else {
                Toast.makeText(this@MainActivity, "No Update Available", Toast.LENGTH_SHORT)
                    .show()
                Timber.d("NoUpdateAvailable Update is not there ")
            }

        }
    }

    /**
     * request for any updates
     */
    private fun requestUpdate(appUpdateInfo: AppUpdateInfo): Unit {
        try {
            appUpdateManager.startUpdateFlowForResult(
                appUpdateInfo,
                FLEXIBLE,
                this,
                MY_REQUEST_CODE
            )
            onResume()
        } catch (e: IntentSender.SendIntentException) {
            e.printStackTrace()
        }
    }

    /**
     * get a callback update status
     */
    override fun onActivityResult(requestCode: Int, resultCode: Int, @Nullable data: Intent?
    ) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == MY_REQUEST_CODE) {
            when (resultCode) {
                Activity.RESULT_OK -> if (resultCode != Activity.RESULT_OK) {
                    Toast.makeText(this, "RESULT_OK$resultCode", Toast.LENGTH_LONG).show()
                    Timber.d("RESULT_OK  :"  + resultCode)
                }
                Activity.RESULT_CANCELED -> if (resultCode != Activity.RESULT_CANCELED) {
                    Toast.makeText(this, "RESULT_CANCELED$resultCode", Toast.LENGTH_LONG).show()
                    Timber.d("RESULT_CANCELED  :" + resultCode)
                }
                ActivityResult.RESULT_IN_APP_UPDATE_FAILED -> if (resultCode != RESULT_IN_APP_UPDATE_FAILED) {
                    Toast.makeText(
                        this,
                        "RESULT_IN_APP_UPDATE_FAILED$resultCode",
                        Toast.LENGTH_LONG
                    ).show()
                    Timber.d("RESULT_IN_APP_FAILED:" + resultCode)
                }
            }
        }
    }

    /**
     * handling the flexible ui
     */
    var listener =
        InstallStateUpdatedListener { state ->
            Timber.d("installState"+ state.toString())
            if (state.installStatus() == InstallStatus.DOWNLOADED) { // After the update is downloaded, show a notification
                // and request user confirmation to restart the app.
                popupSnackbarForCompleteUpdate()
            }
        }

    /**
     * pop up snack bar for when the update is completed
     */
    private fun popupSnackbarForCompleteUpdate() {
        val snackbar = Snackbar.make(
            findViewById(android.R.id.content),
            "An update has just been downloaded.",
            Snackbar.LENGTH_INDEFINITE
        )
        snackbar.apply {
            setAction("RESTART") { appUpdateManager.completeUpdate() }
            setActionTextColor(resources.getColor(R.color.colorAccent))
            show()
        }

        snackbar.setActionTextColor(
            getResources().getColor(R.color.colorAccent));
        snackbar.show()

    }

    override fun onResume() {
        super.onResume()
        appUpdateManager
            .appUpdateInfo
            .addOnSuccessListener { appUpdateInfo ->
                // If the update is downloaded but not installed,
                // notify the user to complete the update.
                if (appUpdateInfo.installStatus() == InstallStatus.DOWNLOADED) {
                    popupSnackbarForCompleteUpdate()
                }
            }
    }

    override fun onDestroy() {
        super.onDestroy()
        appUpdateManager.unregisterListener(listener);
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_theme -> {
                // Get new mode.
                val mode =
                    if ((resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) ==
                        Configuration.UI_MODE_NIGHT_NO
                    ) {
                        AppCompatDelegate.MODE_NIGHT_YES
                    } else {
                        AppCompatDelegate.MODE_NIGHT_AUTO_BATTERY
                    }

                // Call for help change UI Mode
                AppCompatDelegate.setDefaultNightMode(mode)
                true
            }
            R.id.action_share -> {
                showToast("Share App with friends")
                val sendIntent: Intent = Intent().apply {
                    action = Intent.ACTION_SEND
                    putExtra(Intent.EXTRA_TEXT, "Click this link to share this app.")
                    type = "text/plain"
                }

                val shareIntent = Intent.createChooser(sendIntent, null)
                startActivity(shareIntent)
                return true
            }
            R.id.action_call -> {
                showToast("Call For help")
                val intentCall =
                    Intent(Intent.ACTION_CALL, Uri.parse("tel:" + "+256703818546"))
                if (ActivityCompat.checkSelfPermission(
                        this,
                        Manifest.permission.CALL_PHONE
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    ActivityCompat.requestPermissions(
                        this,
                        arrayOf(Manifest.permission.CALL_PHONE),
                        100
                    );

                } else {
                    //You already have permission
                    startActivity(intentCall);
                }

                return true
            }

            else -> true
        }
    }



    /**
     * Handle backpressed
     */
    override fun onBackPressed() {
        MaterialDialog.Builder(this)
            .setTitle("Exit?")
            .setMessage("Are you sure you want to exit?")
            .setPositiveButton("Yes") { dialogInterface, _ ->
                dialogInterface.dismiss()
                super.onBackPressed()
            }
            .setNegativeButton("No") { dialogInterface, _ ->
                dialogInterface.dismiss()
            }
            .build()
            .show()
    }

    companion object {
        const val ANIMATION_DURATION = 1000.toLong()
        const val UNINSTALL_REQUEST_CODE = 1;

    }

}
