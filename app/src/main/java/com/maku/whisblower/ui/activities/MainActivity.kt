package com.maku.whisblower.ui.activities

import android.Manifest
import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.net.Uri
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.maku.whisblower.R
import com.maku.whisblower.WhisBlower
import com.maku.whisblower.databinding.ActivityMainBinding
import com.maku.whisblower.utils.*
import com.shreyaspatil.MaterialDialog.MaterialDialog


class MainActivity : AppCompatActivity() {

    //databinding
    private lateinit var mViewBinding: ActivityMainBinding

    val mContext: Context =
        WhisBlower.applicationContext()


    override fun onCreate(savedInstanceState: Bundle?) {

        setTheme(R.style.AppTheme)  // Set AppTheme before setting content view.

        super.onCreate(savedInstanceState)

        //initialize the binding
        mViewBinding = DataBindingUtil.setContentView<ActivityMainBinding>(this,
            R.layout.activity_main
        )

        //handle newtwork changes
        handleNetworkChanges()

        //unistall app from the users phone
        mViewBinding.unistall?.setOnClickListener { view ->
            unistallAppFromPhone()
        }

        //hightlihging
        //build the spannable String for 50 shillings
        val optional = resources.getString(R.string.supporting_text);

        val spannableO = SpannableString(optional);
        spannableO.setSpan(
            ForegroundColorSpan(resources.getColor(R.color.icons_pink)),
            9, 10,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        );
        mViewBinding.optional.text = spannableO

        //more
        val more = resources.getString(R.string.supporting_text1);

        val spannableM = SpannableString(more);
        spannableM.setSpan(
            ForegroundColorSpan(resources.getColor(R.color.icons_pink)),
            14, 15,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        );
        mViewBinding.more.text = spannableM

        //locatioon asterics
        val location = resources.getString(R.string.location);

        val spannableL = SpannableString(location);
        spannableL.setSpan(
            ForegroundColorSpan(resources.getColor(R.color.icons_pink)),
            0, 1,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        );
        mViewBinding.loc.text = spannableL

    }

    private fun unistallAppFromPhone() {
        val packageUri = Uri.parse("package:$packageName");
        val intent = Intent(Intent.ACTION_DELETE, packageUri);
        startActivity(intent);
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


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_theme -> {
                // Get new mode.
                val mode =
                    if ((resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK ) ==
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

                }else{
                     //You already have permission
                        startActivity(intentCall);
                }

                return true
            }

            else -> true
        }
    }


    // Handle backpressed
    override fun onBackPressed() {
        MaterialDialog.Builder(this)
            .setTitle("Exit?")
            .setMessage("Are you sure want to exit?")
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

    //permission
    //This method will be called when the user will tap on allow or deny
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>,
        grantResults: IntArray
    ): Unit { //Checking the request code of our request
        if (requestCode == 100) { //If permission is granted
            if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //Displaying a toast
                showToast("Permission granted you can now make calls")
            } else {
                //Displaying another toast if permission is not granted
             showToast("Oops you just denied the permission")
            }
        }
    }
}
