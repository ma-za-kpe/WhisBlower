package com.maku.whisblower.ui.fragments.main

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.maku.whisblower.R
import com.maku.whisblower.WhisBlower
import com.maku.whisblower.databinding.MainFragmentBinding
import com.maku.whisblower.ui.ScopedFragment
import com.maku.whisblower.ui.fragments.emergency.OrgAdapter

class MainFragment : Fragment() {

    val mContext: Context =
        WhisBlower.applicationContext()

    private lateinit var source: ArrayList<String>

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

        mViewBinding.lifecycleOwner = requireActivity()
        mViewBinding.mainViewModel = viewModel

        /**
         * send to server and unistall app from the users phone
         * */
//        mViewBinding.unistall?.setOnClickListener { view ->
//            //send to server
//            launch {
//                viewModel.onClick()
//            }
//            // TODO : ACTIVATE THIS METHOD WHEN YOU WANT A USER TO UNISTALL THYE APP IMMEDIATELY
//            unistallAppFromPhone()
//        }

        //hightlihging
        //build the spannable String for 50 shillings
        val optional = resources.getString(R.string.supporting_text);

        val spannableO = SpannableString(optional);
        spannableO.setSpan(
            ForegroundColorSpan(resources.getColor(R.color.icons_pink)),
            9, 10,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        );
        mViewBinding.optional?.text = spannableO

        return mViewBinding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)
        initUiBindings()
//        initViewModels()
        initObservers()
    }

    private fun initObservers() {
        fetchOrg()
    }

    private fun fetchOrg() {
        // Adding items to RecyclerView.
        AddItemsToRecyclerViewArrayList()
        with(mViewBinding.recorg) {
            this?.adapter = OrgAdapter(source)
        }
    }

    private fun initViewModels() {
        TODO("Not yet implemented")
    }

    private fun initUiBindings() {
        val linearLayoutManagerOrg = LinearLayoutManager(
            activity, LinearLayoutManager.HORIZONTAL,
            false
        )
        mViewBinding.recorg?.run {
            setHasFixedSize(true)
            layoutManager = linearLayoutManagerOrg
        }

    }

    // Function to add items in RecyclerView.
    fun AddItemsToRecyclerViewArrayList() {
        // Adding items to ArrayList
        source = ArrayList()
        source.add("xmonster")
        source.add("redbull")
        source.add("cowthirst")
        source.add("milkyway")
        source.add("gsong")
        source.add("hinote")
        source.add("preparation")
    }


    private fun unistallAppFromPhone() {
        val packageUri = Uri.parse("package:${activity?.packageName}");
        val intent = Intent(Intent.ACTION_DELETE, packageUri);
        startActivity(intent);
    }

}
