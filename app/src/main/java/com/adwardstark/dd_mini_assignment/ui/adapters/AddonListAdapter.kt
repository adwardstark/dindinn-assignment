package com.adwardstark.dd_mini_assignment.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.adwardstark.dd_mini_assignment.R
import com.adwardstark.dd_mini_assignment.data.AddonDetail
import com.adwardstark.dd_mini_assignment.databinding.LayoutAddonItemBinding

/**
 * Created by Aditya Awasthi on 08/09/21.
 * @author github.com/adwardstark
 */
class AddonListAdapter: RecyclerView.Adapter<AddonListAdapter.AddonItemViewHolder>() {

    inner class AddonItemViewHolder(val viewBinder: LayoutAddonItemBinding)
        : RecyclerView.ViewHolder(viewBinder.root)

    private val differCallback = object: DiffUtil.ItemCallback<AddonDetail>() {
        override fun areItemsTheSame(oldItem: AddonDetail, newItem: AddonDetail): Boolean {
            return oldItem.id == newItem.id
        }
        override fun areContentsTheSame(oldItem: AddonDetail, newItem: AddonDetail): Boolean {
            return oldItem == newItem
        }
    }

    private val _differList = AsyncListDiffer(this, differCallback)
    fun currentList(): List<AddonDetail> = _differList.currentList
    fun newList(list: List<AddonDetail>) = _differList.submitList(list)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AddonItemViewHolder {
        return AddonItemViewHolder(LayoutAddonItemBinding
            .inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: AddonItemViewHolder, position: Int) {
        with(holder) {
            with(currentList()[position]) {
                val addon = "$quantity $title"
                viewBinder.addonItemTitle.text = addon
            }
        }
    }

    override fun getItemCount(): Int = currentList().size
}