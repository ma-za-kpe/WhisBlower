package com.maku.whisblower.ui.fragments.emergency

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.maku.whisblower.R
import com.maku.whisblower.databinding.FragmentEmergencyBinding


class EmergencyFragment : Fragment() {

    private lateinit var binding: FragmentEmergencyBinding
    private lateinit var source: ArrayList<String>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_emergency, container, false
        )
        binding.lifecycleOwner = requireActivity()
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        initUiBindings()
//        initViewModels()
        initObservers()
    }

    private fun initObservers() {
       fetchOtherVictims()
    }

    private fun fetchOtherVictims() {
        // Adding items to RecyclerView.
        AddItemsToRecyclerViewArrayList()
            with(binding.orgrec) {
                adapter = OrgAdapter(source)
            }
            with(binding.others) {
                adapter = EmergencyAdapter(source) { item ->
                    goToOtherFragment(item)
                }
            }

    }

    private fun goToOtherFragment(item: Any) {
//        findNavController().navigate(R.id.otherVictimsFragment)
        //pass the 'context' here
        val alertDialog = AlertDialog.Builder(requireContext())
        alertDialog.setTitle("Rover Name: $item")
        alertDialog.setPositiveButton("Close") {
                dialog, which -> dialog.cancel()
        }

        val dialog = alertDialog.create()
        dialog.show()
    }

    private fun initViewModels() {
        TODO("Not yet implemented")
    }

    private fun initUiBindings() {
        val linearLayoutManagerOrg = LinearLayoutManager(
            activity, LinearLayoutManager.HORIZONTAL,
            false
        )
        binding.orgrec.run {
            setHasFixedSize(true)
            layoutManager = linearLayoutManagerOrg
        }

        val linearLayoutManager = LinearLayoutManager(
            activity, LinearLayoutManager.HORIZONTAL,
            false
        )
            binding.others.run {
            setHasFixedSize(true)
            layoutManager = linearLayoutManager
        }

    }

    // Function to add items in RecyclerView.
    fun AddItemsToRecyclerViewArrayList() {
        // Adding items to ArrayList
        source = ArrayList()
        source!!.add("xmonster")
        source!!.add("redbull")
        source!!.add("cowthirst")
        source!!.add("milkyway")
        source!!.add("gsong")
        source!!.add("hinote")
        source!!.add("preparation")
    }

    companion object {

        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            EmergencyFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }
}