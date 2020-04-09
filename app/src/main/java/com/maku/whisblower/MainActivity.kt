package com.maku.whisblower

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.content.Context
import android.content.res.Configuration
import android.graphics.Typeface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatDelegate
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
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
        mViewBinding = DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main)

        //handle newtwork changes
        handleNetworkChanges()

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
                return true
            }
            R.id.action_call -> {
                showToast("Share App with friends")
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
    }
}
