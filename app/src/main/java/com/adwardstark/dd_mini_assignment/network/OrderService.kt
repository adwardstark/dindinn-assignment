package com.adwardstark.dd_mini_assignment.network

import com.adwardstark.dd_mini_assignment.data.IngredientResponse
import com.adwardstark.dd_mini_assignment.data.OrdersResponse
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Created by Aditya Awasthi on 07/09/21.
 * @author github.com/adwardstark
 */
interface OrderService {

    @GET("orders")
    fun getOrders(): Observable<OrdersResponse>

    @GET("search")
    fun getIngredient(): Observable<IngredientResponse>
}