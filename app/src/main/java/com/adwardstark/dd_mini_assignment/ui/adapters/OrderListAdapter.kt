package com.adwardstark.dd_mini_assignment.ui.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.*
import com.adwardstark.dd_mini_assignment.R
import com.adwardstark.dd_mini_assignment.data.OrderDetail
import com.adwardstark.dd_mini_assignment.databinding.LayoutOrderItemBinding
import com.adwardstark.dd_mini_assignment.ui.getTimeFrom

/**
 * Created by Aditya Awasthi on 08/09/21.
 * @author github.com/adwardstark
 */
class OrderListAdapter: RecyclerView.Adapter<OrderListAdapter.OrderItemViewHolder>() {

    inner class OrderItemViewHolder(val viewBinder: LayoutOrderItemBinding)
        : RecyclerView.ViewHolder(viewBinder.root)

    private val differCallback = object: DiffUtil.ItemCallback<OrderDetail>() {
        override fun areItemsTheSame(oldItem: OrderDetail, newItem: OrderDetail): Boolean {
            return oldItem.id == newItem.id
        }
        override fun areContentsTheSame(oldItem: OrderDetail, newItem: OrderDetail): Boolean {
            return oldItem == newItem
        }
    }

    private val _differList = AsyncListDiffer(this, differCallback)
    fun currentList(): List<OrderDetail> = _differList.currentList
    fun newList(list: List<OrderDetail>) = _differList.submitList(list)

    private val recyclerViewPool: RecyclerView.RecycledViewPool by lazy {
        RecyclerView.RecycledViewPool()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderItemViewHolder {
        return OrderItemViewHolder(LayoutOrderItemBinding
            .inflate(LayoutInflater.from(parent.context), parent, false))
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: OrderItemViewHolder, position: Int) {
        with(holder) {
            with(currentList()[position]) {
                viewBinder.orderIDTxt.text = itemView
                    .resources.getString(R.string.order_id_txt, id)
                viewBinder.orderAtTxt.text = itemView
                    .resources.getString(R.string.ordered_at_txt, getTimeFrom(createdAt))
                viewBinder.totalItems.text = itemView
                    .resources.getQuantityString(R.plurals.total_items, quantity, quantity)
                viewBinder.orderQuantityTxt.text = itemView
                    .resources.getString(R.string.quantity_txt, quantity)
                viewBinder.orderTitleTxt.text = title

                val addonItemsLayoutManager = LinearLayoutManager(viewBinder.rvAddonList.context,
                    RecyclerView.VERTICAL, false)
                addonItemsLayoutManager.initialPrefetchItemCount = addon.size
                val addonItemAdapter = AddonListAdapter()
                viewBinder.rvAddonList.apply {
                    layoutManager = addonItemsLayoutManager
                    adapter = addonItemAdapter
                    setRecycledViewPool(recyclerViewPool)
                }
                addonItemAdapter.newList(addon)
            }
        }
    }

    override fun getItemCount(): Int = _differList.currentList.size
}