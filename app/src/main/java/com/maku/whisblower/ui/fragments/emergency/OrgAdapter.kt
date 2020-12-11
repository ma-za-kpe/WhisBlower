package com.maku.whisblower.ui.fragments.emergency

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.maku.whisblower.databinding.EmergencyCardsBinding
import com.maku.whisblower.databinding.OrgrowBinding
import timber.log.Timber

class OrgAdapter(private val photosList: ArrayList<String>) :
    RecyclerView.Adapter<OrgAdapter.PhotosViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotosViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = OrgrowBinding.inflate(inflater, parent, false)
        return PhotosViewHolder(binding)
    }

    override fun getItemCount() = photosList.size

    override fun onBindViewHolder(holder: PhotosViewHolder, position: Int) {
        holder.bind(photosList[position])
    }

    class PhotosViewHolder(rowBinding: OrgrowBinding) :
        RecyclerView.ViewHolder(rowBinding.root) {
        private val binding = rowBinding

        fun bind(s: String) {

            Timber.d("photo ${s}")
            binding.orgtitle.text = s
            binding.otherVictims.setOnClickListener { view ->
                Timber.d("photo has been clicked")
            }
        }
    }
}