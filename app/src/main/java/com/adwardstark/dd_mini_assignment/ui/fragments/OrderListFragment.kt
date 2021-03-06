package com.adwardstark.dd_mini_assignment.ui.fragments

import android.content.res.Configuration
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.adwardstark.dd_mini_assignment.R
import com.adwardstark.dd_mini_assignment.databinding.FragmentOrderListBinding
import com.adwardstark.dd_mini_assignment.ui.OrderServiceViewModel
import com.adwardstark.dd_mini_assignment.ui.adapters.OrderListAdapter
import com.adwardstark.dd_mini_assignment.utils.showHomeUp
import com.adwardstark.dd_mini_assignment.utils.showToast
import dagger.hilt.android.AndroidEntryPoint

/**
 * Created by Aditya Awasthi on 08/09/21.
 * @author github.com/adwardstark
 */
@AndroidEntryPoint
class OrderListFragment : Fragment() {

    private lateinit var viewBinder: FragmentOrderListBinding
    private val orderServiceViewModel: OrderServiceViewModel by viewModels()
    private val orderListAdapter: OrderListAdapter by lazy { OrderListAdapter() }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        setHasOptionsMenu(true)
        showHomeUp(false)
        // Inflate the layout for this fragment
        viewBinder = FragmentOrderListBinding
            .inflate(layoutInflater, container, false)
        return viewBinder.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.toolbar_menu, menu)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        viewBinder.swipeRefreshLayout.isRefreshing = true
        viewBinder.swipeRefreshLayout.setOnRefreshListener {
            orderServiceViewModel.getOrders()
        }

        orderListAdapter.onItemClicked { (id) ->
            orderListAdapter.newList(orderListAdapter.currentList().filter { it.id != id })
        }

        orderServiceViewModel.orderList.observe(viewLifecycleOwner) {
            viewBinder.swipeRefreshLayout.isRefreshing = false
            if(it.isNotEmpty()) {
                orderListAdapter.newList(it)
            } else {
                showToast("No orders found")
            }
        }
        orderServiceViewModel.getOrders()
    }

    private fun setupRecyclerView() {
        viewBinder.rvOrderList.apply {
            layoutManager = if(orderListAdapter.isPortrait) {
                LinearLayoutManager(this.context, RecyclerView.VERTICAL, false)
            } else {
                LinearLayoutManager(this.context, RecyclerView.HORIZONTAL, false)
            }
            adapter = orderListAdapter
            itemAnimator = DefaultItemAnimator()
        }
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        orderListAdapter.isPortrait = newConfig.orientation != Configuration.ORIENTATION_LANDSCAPE
        setupRecyclerView()
    }

    override fun onPause() {
        super.onPause()
        orderListAdapter.removeCallbacks()
    }

    override fun onDestroy() {
        super.onDestroy()
        orderListAdapter.removeCallbacks()
    }
}