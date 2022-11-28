package com.example.ubiquitihomework.ui.preview

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.ubiquitihomework.databinding.ItemHorizontalBinding
import com.example.ubiquitihomework.model.api.response.AirStatusResponse

class HorizontalRecyclerViewAdapter :
    RecyclerView.Adapter<HorizontalRecyclerViewAdapter.ViewHolder>() {
    private val data = ArrayList<AirStatusResponse>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemHorizontalBinding.inflate(
            LayoutInflater.from(parent.context),
            parent, false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = data[position]
        holder.onBind(item)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    class ViewHolder(
        private val binding: ItemHorizontalBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun onBind(item: AirStatusResponse) {
            binding.tvIndex.text = item.siteId
            binding.tvCounty.text = item.county
            binding.tvSiteName.text = item.siteName
            binding.tvPm.text = item.pm2_5
            binding.tvStatus.text = item.status
        }
    }
}