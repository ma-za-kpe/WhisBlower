package com.maku.whisblower.ui.fragments

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.maku.whisblower.R
import com.maku.whisblower.WhisBlower
import com.maku.whisblower.databinding.MainFragmentBinding
import com.maku.whisblower.utils.showToast
import timber.log.Timber


class MainFragment : Fragment() {

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

//        //more
//        val more = resources.getString(R.string.supporting_text1);
//
//        val spannableM = SpannableString(more);
//        spannableM.setSpan(
//            ForegroundColorSpan(resources.getColor(R.color.icons_pink)),
//            14, 15,
//            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
//        );
//        mViewBinding.more.text = spannableM

        //locatioon asterics
//        val location = resources.getString(R.string.location);
//
//        val spannableL = SpannableString(location);
//        spannableL.setSpan(
//            ForegroundColorSpan(resources.getColor(R.color.icons_pink)),
//            0, 1,
//            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
//        );
//        mViewBinding.loc?.text = spannableL

        return mViewBinding.root
    }

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
                activity?.showToast("Permission granted you can now make calls")
            } else {
                //Displaying another toast if permission is not granted
                activity?.showToast("Oops you just denied the permission")
            }
        }
    }

}
