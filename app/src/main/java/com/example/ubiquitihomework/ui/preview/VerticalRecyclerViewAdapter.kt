package com.example.ubiquitihomework.ui.preview

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.ubiquitihomework.R
import com.example.ubiquitihomework.databinding.ItemVerticalBinding
import com.example.ubiquitihomework.listener.AdapterInteractionListener
import com.example.ubiquitihomework.model.api.response.AirStatusRecord

class VerticalRecyclerViewAdapter(private val interactionListener: AdapterInteractionListener) :
    RecyclerView.Adapter<VerticalRecyclerViewAdapter.ViewHolder>() {
    companion object {
        const val STATUS_GREAT: String = "良好"
    }

    private val data = ArrayList<AirStatusRecord>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemVerticalBinding.inflate(
            LayoutInflater.from(parent.context),
            parent, false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = data[position]
        holder.onBind(item, interactionListener)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    fun setData(data: List<AirStatusRecord>) {
        this.data.addAll(data)
        notifyDataSetChanged()
    }

    class ViewHolder(
        private val binding: ItemVerticalBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun onBind(item: AirStatusRecord, listener: AdapterInteractionListener) {
            binding.tvIndex.text = item.siteId
            binding.tvCounty.text = item.county
            binding.tvSiteName.text = item.siteName
            binding.tvPm.text = item.pm2_5
            if (item.status == STATUS_GREAT) {
                binding.root.setOnClickListener(null)
                binding.ivNext.visibility = View.GONE
                binding.tvStatus.text =
                    binding.tvStatus.context.getString(R.string.preview_page_status_great_hint)
            } else {
                binding.root.setOnClickListener {
                    listener.onClick(item.siteName)
                }
                binding.ivNext.visibility = View.VISIBLE
                binding.tvStatus.text = item.status
            }
        }
    }
}