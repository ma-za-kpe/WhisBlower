package com.maku.whisblower.ui.fragments

import android.Manifest

import android.content.*

import android.content.pm.PackageManager

import android.location.Location

import android.net.Uri

import android.os.Bundle
import android.os.IBinder

import android.provider.Settings

import android.text.Spannable
import android.text.SpannableString
import android.text.TextUtils
import android.text.style.ForegroundColorSpan

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import androidx.databinding.library.BuildConfig
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.google.android.material.snackbar.Snackbar

import com.maku.whisblower.R
import com.maku.whisblower.WhisBlower
import com.maku.whisblower.databinding.MainFragmentBinding
import com.maku.whisblower.data.network.interfaces.services.ForegroundOnlyLocationService
import com.maku.whisblower.ui.ScopedFragment
import com.maku.whisblower.utils.SharedPreferenceUtil
import com.maku.whisblower.utils.showToast
import com.maku.whisblower.utils.toText

import kotlinx.coroutines.launch

import timber.log.Timber

import java.util.*

class MainFragment : ScopedFragment() , SharedPreferences.OnSharedPreferenceChangeListener {
    private var foregroundOnlyLocationServiceBound = false

    // Provides location updates for while-in-use feature.
    private var foregroundOnlyLocationService: ForegroundOnlyLocationService? = null

    // Listens for location broadcasts from ForegroundOnlyLocationService.
    private lateinit var foregroundOnlyBroadcastReceiver: ForegroundOnlyBroadcastReceiver

    private lateinit var sharedPreferences: SharedPreferences


    // Monitors connection to the while-in-use service.
    private val foregroundOnlyServiceConnection = object : ServiceConnection {

        override fun onServiceConnected(name: ComponentName, service: IBinder) {
            val binder = service as ForegroundOnlyLocationService.LocalBinder
            foregroundOnlyLocationService = binder.service
            foregroundOnlyLocationServiceBound = true
        }

        override fun onServiceDisconnected(name: ComponentName) {
            foregroundOnlyLocationService = null
            foregroundOnlyLocationServiceBound = false
        }
    }

    private val REQUEST_FOREGROUND_ONLY_PERMISSIONS_REQUEST_CODE = 34

    val mContext: Context =
        WhisBlower.applicationContext()

    private lateinit var mViewBinding: MainFragmentBinding

    companion object {
        fun newInstance() = MainFragment()
    }

    private lateinit var viewModel: MainViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        mViewBinding = DataBindingUtil.inflate(
            inflater, R.layout.main_fragment, container, false)
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)

        mViewBinding.lifecycleOwner = this
        mViewBinding.mainViewModel = viewModel

        mViewBinding.spouseNumber?.visibility = View.GONE

        /**
         * when the spouse button is clicked, get form data
         * */
        mViewBinding.spouseBtn?.setOnClickListener { view ->
            mViewBinding.spouseNumber?.visibility = View.VISIBLE
            mViewBinding.abuserBtn?.visibility = View.GONE

//            getDataFromUiSpouse(mViewBinding.spouseinput.text)
        }

        /**
         * when the attacker button is clicked, get form data
         * */
        mViewBinding.abuserBtn?.setOnClickListener { view ->
            mViewBinding.spouseBtn?.visibility = View.GONE
            getDataFromUiAttacker()
        }

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

        //location
        foregroundOnlyBroadcastReceiver = ForegroundOnlyBroadcastReceiver()

        sharedPreferences =  requireActivity().getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE)

//        foregroundOnlyLocationButton = findViewById(R.id.foreground_only_location_button)
//        outputTextView = findViewById(R.id.output_text_view)

        mViewBinding.help?.setOnClickListener {
            val enabled = sharedPreferences.getBoolean(
                SharedPreferenceUtil.KEY_FOREGROUND_ENABLED, false)

            if (enabled) {
                foregroundOnlyLocationService?.unsubscribeToLocationUpdates()
            } else {

                // TODO: Step 1.0, Review Permissions: Checks and requests if needed.
                if (foregroundPermissionApproved()) {
                    foregroundOnlyLocationService?.subscribeToLocationUpdates()
                        ?: Timber.d("Service Not Bound")
                } else {
                    requestForegroundPermissions()
                }
            }
        }

        return mViewBinding.root
    }

    /**
     * get form attacker data, and launch from a couroutine scope
     * */
    private fun getDataFromUiAttacker()  = launch {
        viewModel.getFormData()?.observe(viewLifecycleOwner, Observer(){ victim ->
            //check if message is empty
            if (TextUtils.isEmpty(Objects.requireNonNull(victim).message)) {
                mViewBinding.message.error = "Enter a message";
                mViewBinding.message.requestFocus();
            } else {
//                viewModel.victimData(VictimRequest(victim.attacterType, victim.spouseNumber, victim.message, victim.organisation))?.observe(viewLifecycleOwner, Observer {
//
//                })
//                mViewBinding.message.setText(victim.message)

                // TODO: ADD LOGIC TO GET ORGANISATION NAME HERE, DONT FOROGET TO PUT THE ORGANISATIONS IN AN ARRAYLISY FiRST.
            }
        })
    }

//    /**
//     * get spouse form data, and launch from a couroutine scope
//     * */
//    private fun getDataFromUiSpouse(text: Editable) = launch  {
//        viewModel.getFormData()?.observe(viewLifecycleOwner, Observer { victim ->
//            if (TextUtils.isEmpty(Objects.requireNonNull(victim).spouseNumber)) {
//                mViewBinding.spouseinput.error = "Enter a spouse phone number";
//                mViewBinding.spouseinput.requestFocus();
//            } else if (TextUtils.isEmpty(Objects.requireNonNull(victim).message)) {
//                mViewBinding.message.error = "Enter a message";
//                mViewBinding.message.requestFocus();
//            } else {
//                viewModel.victimData(VictimRequest(victim.attacterType, victim.spouseNumber, victim.message, victim.organisation))?.observe(viewLifecycleOwner, Observer {
//
//                })
////                mViewBinding.spouseinput.setText(victim.spouseNumber)
////                mViewBinding.message.setText(victim.message)
//
//                // TODO: ADD LOGIC TO GET ORGANISATION NAME HERE, DONT FROGET TO PUT THE ORGANISATIONS IN AN ARRAYLISY FiRST.
//            }
//        } )
//    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)
        // TODO: Use the ViewModel
    }

    private fun unistallAppFromPhone() {
        val packageUri = Uri.parse("package:${activity?.packageName}");
        val intent = Intent(Intent.ACTION_DELETE, packageUri);
        startActivity(intent);
    }

    override fun onStart() {
        super.onStart()
        updateButtonState(
            sharedPreferences.getBoolean(SharedPreferenceUtil.KEY_FOREGROUND_ENABLED, false)
        )
        sharedPreferences.registerOnSharedPreferenceChangeListener(this)

        val serviceIntent = Intent(requireContext(), ForegroundOnlyLocationService::class.java)
        activity?.bindService(serviceIntent, foregroundOnlyServiceConnection, Context.BIND_AUTO_CREATE)
    }

    override fun onResume() {
        super.onResume()
        LocalBroadcastManager.getInstance(requireContext()).registerReceiver(
            foregroundOnlyBroadcastReceiver,
            IntentFilter(
                ForegroundOnlyLocationService.ACTION_FOREGROUND_ONLY_LOCATION_BROADCAST)
        )
    }

    override fun onPause() {
        LocalBroadcastManager.getInstance(requireContext()).unregisterReceiver(
            foregroundOnlyBroadcastReceiver
        )
        super.onPause()
    }

    override fun onStop() {
        if (foregroundOnlyLocationServiceBound) {
            activity?.unbindService(foregroundOnlyServiceConnection)
            foregroundOnlyLocationServiceBound = false
        }
        sharedPreferences.unregisterOnSharedPreferenceChangeListener(this)

        super.onStop()
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences, key: String) {
        // Updates button states if new while in use location is added to SharedPreferences.
        if (key == SharedPreferenceUtil.KEY_FOREGROUND_ENABLED) {
            updateButtonState(sharedPreferences.getBoolean(
                SharedPreferenceUtil.KEY_FOREGROUND_ENABLED, false)
            )
        }
    }

    // TODO: Step 1.0, Review Permissions: Method checks if permissions approved.
    private fun foregroundPermissionApproved(): Boolean {
        return PackageManager.PERMISSION_GRANTED == ActivityCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.ACCESS_FINE_LOCATION
        )
    }

    // TODO: Step 1.0, Review Permissions: Method requests permissions.
    private fun requestForegroundPermissions() {
        val provideRationale = foregroundPermissionApproved()

        // If the user denied a previous request, but didn't check "Don't ask again", provide
        // additional rationale.
        if (provideRationale) {
            Snackbar.make(
                requireActivity().window.decorView.rootView,
                R.string.permission_rationale,
                Snackbar.LENGTH_LONG
            )
                .setAction(R.string.ok) {
                    // Request permission
                    ActivityCompat.requestPermissions(
                        requireActivity(),
                        arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                        REQUEST_FOREGROUND_ONLY_PERMISSIONS_REQUEST_CODE
                    )
                }
                .show()
        } else {
            Timber.d("Request foreground only permission")
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                REQUEST_FOREGROUND_ONLY_PERMISSIONS_REQUEST_CODE
            )
        }
    }

    // TODO: Step 1.0, Review Permissions: Handles permission result.
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
//        if (requestCode == 100) { //If permission is granted
//            if ( grantResults.isEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                //Displaying a toast
//                activity?.showToast("Permission granted you can now make calls")
//            } else {
//                //Displaying another toast if permission is not granted
//                activity?.showToast("Oops you just denied the permission")
//            }
//        }

        when (requestCode) {
            100 -> {
                if ( grantResults.isEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //Displaying a toast
                    activity?.showToast("Permission granted you can now make calls")
                } else {
                    //Displaying another toast if permission is not granted
                    activity?.showToast("Oops you just denied the permission")
                }
            }
            REQUEST_FOREGROUND_ONLY_PERMISSIONS_REQUEST_CODE -> when {
                grantResults.isEmpty() ->
                    // If user interaction was interrupted, the permission request
                    // is cancelled and you receive empty arrays.
                    activity?.showToast("Oops you just denied the permission")

                grantResults[0] == PackageManager.PERMISSION_GRANTED ->
                    // Permission was granted.
                    foregroundOnlyLocationService?.subscribeToLocationUpdates()

                else -> {
                    // Permission denied.
                    updateButtonState(false)

                    Snackbar.make(
                        requireActivity().window.decorView.rootView,
                        R.string.permission_denied_explanation,
                        Snackbar.LENGTH_LONG
                    )
                        .setAction(R.string.settings) {
                            // Build intent that displays the App settings screen.
                            val intent = Intent()
                            intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                            val uri = Uri.fromParts(
                                "package",
                                BuildConfig.APPLICATION_ID,
                                null
                            )
                            intent.data = uri
                            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                            startActivity(intent)
                        }
                        .show()
                }
            }
        }
    }

    private fun updateButtonState(trackingLocation: Boolean) {
        if (trackingLocation) {
            mViewBinding.help?.text = getString(R.string.stop_location_updates_button_text)
        } else {
            mViewBinding.help?.text = getString(R.string.start_location_updates_button_text)
        }
    }

    private fun logResultsToScreen(output:String) {
        val outputWithPreviousLogs = "$output"
       Timber.d(" location cordinates " + outputWithPreviousLogs)
    }

    /**
     * Receiver for location broadcasts from [ForegroundOnlyLocationService].
     */
    private inner class ForegroundOnlyBroadcastReceiver : BroadcastReceiver() {

        override fun onReceive(context: Context, intent: Intent) {
            val location = intent.getParcelableExtra<Location>(
                ForegroundOnlyLocationService.EXTRA_LOCATION
            )

            if (location != null) {
                logResultsToScreen("Foreground location: ${location.toText()}")
            }
        }
    }
}
