package com.maku.whisblower.ui.fragments.emergency

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.maku.whisblower.databinding.EmergencyCardsBinding
import timber.log.Timber

class EmergencyAdapter(private val photosList: ArrayList<String>, val goToVictimList: (Any) -> Unit,) :
    RecyclerView.Adapter<EmergencyAdapter.PhotosViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotosViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = EmergencyCardsBinding.inflate(inflater, parent, false)
        return PhotosViewHolder(binding)
    }

    override fun getItemCount() = photosList.size

    override fun onBindViewHolder(holder: PhotosViewHolder, position: Int) {
        holder.bind(photosList[position], goToVictimList)
    }

    class PhotosViewHolder(rowBinding: EmergencyCardsBinding) :
        RecyclerView.ViewHolder(rowBinding.root) {
        private val binding = rowBinding

        fun bind(s: String, goToVictimList: (Any) -> Unit) {

            Timber.d("photo ${s}")
            binding.othertext.text = s
            binding.otherVictims.setOnClickListener { view ->
                Timber.d("photo has been clicked")
                goToVictimList(s)
            }
        }
    }
}