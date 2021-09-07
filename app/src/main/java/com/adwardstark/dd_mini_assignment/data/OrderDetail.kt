package com.adwardstark.dd_mini_assignment.data

import com.google.gson.annotations.SerializedName

/**
 * Created by Aditya Awasthi on 07/09/21.
 * @author github.com/adwardstark
 */
data class OrderDetail(
    val id: Int,
    val title: String,
    val addon: List<AddonDetail>,
    val quantity: Int,
    @SerializedName("created_at") val createdAt: String,
    @SerializedName("alerted_at") val alertedAt: String,
    @SerializedName("expired_at") val expiredAt: String
)