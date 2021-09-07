package com.adwardstark.dd_mini_assignment.network

import co.infinum.retromock.meta.Mock
import co.infinum.retromock.meta.MockResponse
import com.adwardstark.dd_mini_assignment.data.IngredientResponse
import com.adwardstark.dd_mini_assignment.data.OrdersResponse
import io.reactivex.Observable

/**
 * Created by Aditya Awasthi on 07/09/21.
 * @author github.com/adwardstark
 */
interface MockedOrderService: OrderService {

    @Mock
    @MockResponse(body = "orders.json")
    override fun getOrders(): Observable<OrdersResponse>

    @Mock
    @MockResponse(body = "ingredients.json")
    override fun searchIngredient(query: String): Observable<IngredientResponse>
}