package com.adwardstark.dd_mini_assignment.ui.adapters

import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.*
import com.adwardstark.dd_mini_assignment.R
import com.adwardstark.dd_mini_assignment.data.OrderDetail
import com.adwardstark.dd_mini_assignment.databinding.LayoutOrderItemBinding
import com.adwardstark.dd_mini_assignment.utils.Constants.ONE_SECOND
import com.adwardstark.dd_mini_assignment.utils.CountDownRunnable
import com.adwardstark.dd_mini_assignment.utils.getExpiryAndAlertTime
import com.adwardstark.dd_mini_assignment.utils.getTimeFrom

/**
 * Created by Aditya Awasthi on 08/09/21.
 * @author github.com/adwardstark
 */
class OrderListAdapter: RecyclerView.Adapter<OrderListAdapter.OrderItemViewHolder>() {

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

    private val recyclerHandler = Handler(Looper.getMainLooper())
    private var onItemClickListener: ((OrderDetail) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderItemViewHolder {
        return OrderItemViewHolder(LayoutOrderItemBinding
            .inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: OrderItemViewHolder, position: Int) {
        holder.bind(currentList()[position])
    }

    override fun getItemCount(): Int = _differList.currentList.size

    fun removeCallbacks() {
        recyclerHandler.removeCallbacksAndMessages(null)
    }

    fun onItemClicked(listener: (OrderDetail) -> Unit) {
        onItemClickListener = listener
    }

    inner class OrderItemViewHolder(private val viewBinder: LayoutOrderItemBinding)
        : RecyclerView.ViewHolder(viewBinder.root) {

        private var countDownRunnable: CountDownRunnable =
            CountDownRunnable(itemView.context, recyclerHandler)

        internal fun bind(orderDetail: OrderDetail) {
            recyclerHandler.removeCallbacks(countDownRunnable)
            val expiryAlertPair = getExpiryAndAlertTime(orderDetail.expiredAt, orderDetail.alertedAt)
            if(expiryAlertPair.first > 0) {
                viewBinder.autoRejectTxt.visibility = View.VISIBLE
                viewBinder.timeLeftTxt.visibility = View.VISIBLE
                viewBinder.acceptOrderButton.text = itemView.context.getString(R.string.accept_txt)

                countDownRunnable.setViews(viewBinder.timeLeftTxt, viewBinder.autoRejectTxt,
                    viewBinder.acceptOrderButton, viewBinder.progressBar)
                countDownRunnable.setElapsed(expiryAlertPair.first, expiryAlertPair.second)
                recyclerHandler.postDelayed(countDownRunnable, ONE_SECOND)
            } else {
                viewBinder.autoRejectTxt.visibility = View.INVISIBLE
                viewBinder.timeLeftTxt.visibility = View.INVISIBLE
                viewBinder.acceptOrderButton.text = itemView.context.getString(R.string.expired_txt)
            }

            viewBinder.orderIDTxt.text = itemView
                .resources.getString(R.string.order_id_txt, orderDetail.id)
            viewBinder.orderAtTxt.text = itemView
                .resources.getString(R.string.ordered_at_txt, getTimeFrom(orderDetail.createdAt))
            viewBinder.totalItems.text = itemView
                .resources.getQuantityString(R.plurals.total_items, orderDetail.quantity, orderDetail.quantity)
            viewBinder.orderQuantityTxt.text = itemView
                .resources.getString(R.string.quantity_txt, orderDetail.quantity)
            viewBinder.orderTitleTxt.text = orderDetail.title

            val addonItemsLayoutManager = LinearLayoutManager(viewBinder.rvAddonList.context,
                RecyclerView.VERTICAL, false)
            addonItemsLayoutManager.initialPrefetchItemCount = orderDetail.addon.size
            val addonItemAdapter = AddonListAdapter()
            viewBinder.rvAddonList.apply {
                layoutManager = addonItemsLayoutManager
                adapter = addonItemAdapter
                setRecycledViewPool(recyclerViewPool)
            }
            addonItemAdapter.newList(orderDetail.addon)

            viewBinder.acceptOrderButton.setOnClickListener {
                recyclerHandler.removeCallbacks(countDownRunnable)
                onItemClickListener?.let { it(orderDetail) }
            }
        }
    }
}