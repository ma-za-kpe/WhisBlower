package com.maku.whisblower.ui.fragments.emergency

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.maku.whisblower.databinding.OrgrowBinding
import com.maku.whisblower.firebaseData.model.Organisations
import com.maku.whisblower.utils.setImageFromUrlWithProgressBar
import timber.log.Timber
import java.util.*

class OrgAdapter(private val photosList: ArrayList<Organisations>) :
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

        fun bind(s: Organisations) {

            Timber.d("photo ${s.name}")
            binding.orgtitle.text = s.image
            binding.otherVictims.setImageFromUrlWithProgressBar(
                s.name,
                binding.progressBar
            )
        }
    }
}