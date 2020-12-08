package com.maku.whisblower.ui.fragments.emergency

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.maku.whisblower.databinding.EmergencyCardsBinding
import timber.log.Timber

class EmergencyAdapter(private val photosList: ArrayList<String>) :
    RecyclerView.Adapter<EmergencyAdapter.PhotosViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotosViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = EmergencyCardsBinding.inflate(inflater, parent, false)
        return PhotosViewHolder(binding)
    }

    override fun getItemCount() = photosList.size

    override fun onBindViewHolder(holder: PhotosViewHolder, position: Int) {
        holder.bind(photosList[position])
    }

    class PhotosViewHolder(rowBinding: EmergencyCardsBinding) :
        RecyclerView.ViewHolder(rowBinding.root) {
        private val binding = rowBinding

        fun bind(s: String) {

            Timber.d("photo ${s}")
            binding.othertext.text = s
            binding.otherVictims.setOnClickListener { view ->
                Timber.d("photo has been clicked")

                //pass the 'context' here
                val alertDialog = AlertDialog.Builder(view.context)
                alertDialog.setTitle("Rover Name: " + s)
                alertDialog.setPositiveButton("Close") { dialog, which -> dialog.cancel()
                }

                val dialog = alertDialog.create()
                dialog.show()

            }
        }
    }
}