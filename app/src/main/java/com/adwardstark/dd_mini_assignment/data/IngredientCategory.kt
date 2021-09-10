package com.adwardstark.dd_mini_assignment.data

import com.google.gson.annotations.SerializedName

/**
 * Created by Aditya Awasthi on 10/09/21.
 * @author github.com/adwardstark
 */
data class IngredientCategory(
    @SerializedName("category_id") val categoryID: String,
    @SerializedName("category_title") val categoryTitle: String,
    val items: List<IngredientDetail>
)
