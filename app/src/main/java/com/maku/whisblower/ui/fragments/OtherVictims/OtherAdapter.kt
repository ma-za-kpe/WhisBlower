package com.maku.whisblower.ui.fragments.OtherVictims

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.navigation.fragment.NavHostFragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.maku.whisblower.R
import com.maku.whisblower.databinding.EmergencyCardsBinding
import com.maku.whisblower.databinding.OtherviewrowBinding
import com.maku.whisblower.ui.fragments.emergency.EmergencyAdapter
import timber.log.Timber

class OtherAdapter (private val photosList: ArrayList<String>) :
    RecyclerView.Adapter<OtherAdapter.PhotosViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotosViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = OtherviewrowBinding.inflate(inflater, parent, false)
        return PhotosViewHolder(binding)
    }

    override fun getItemCount() = photosList.size

    override fun onBindViewHolder(holder: PhotosViewHolder, position: Int) {
        holder.bind(photosList[position])
    }

    class PhotosViewHolder(rowBinding: OtherviewrowBinding) :
        RecyclerView.ViewHolder(rowBinding.root) {
        private val binding = rowBinding

        fun bind(s: String) {

            Timber.d("photo ${s}")
            binding.titleeme.text = s
            binding.otherVictims.setOnClickListener { view ->
                Timber.d("photo has been clicked")

            }
        }
    }
}