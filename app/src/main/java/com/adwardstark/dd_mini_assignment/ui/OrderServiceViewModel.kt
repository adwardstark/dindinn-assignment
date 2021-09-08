package com.adwardstark.dd_mini_assignment.ui

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.adwardstark.dd_mini_assignment.data.OrderDetail
import com.adwardstark.dd_mini_assignment.data.OrdersResponse
import com.adwardstark.dd_mini_assignment.network.OrderService
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject
import javax.inject.Named

/**
 * Created by Aditya Awasthi on 07/09/21.
 * @author github.com/adwardstark
 */
@HiltViewModel
class OrderServiceViewModel @Inject constructor(
    @Named("OrderService") private val orderService: OrderService
    ): ViewModel() {

    companion object {
        private val TAG = OrderServiceViewModel::class.java.simpleName
    }

    init {
        Log.d(TAG, "Using service: $orderService")
    }

    private var _orderList = MutableLiveData<List<OrderDetail>>()
    val orderList: LiveData<List<OrderDetail>>
        get() = _orderList

    fun getOrders() {
        orderService.getOrders()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(getOrderListObserver())
    }

    private fun getOrderListObserver(): Observer<OrdersResponse> {
        return object :Observer<OrdersResponse> {
            override fun onSubscribe(d: Disposable) {
                // Do nothing
            }
            override fun onNext(t: OrdersResponse) {
                Log.d(TAG, "->onNext() $t")
                _orderList.postValue(t.data)
            }
            override fun onError(e: Throwable) {
                Log.d(TAG, "->onError() ${e.message}")
            }
            override fun onComplete() {
                // Do nothing
            }
        }
    }
}