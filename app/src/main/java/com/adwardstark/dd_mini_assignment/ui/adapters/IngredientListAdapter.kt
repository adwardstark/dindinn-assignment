package com.adwardstark.dd_mini_assignment.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.adwardstark.dd_mini_assignment.R
import com.adwardstark.dd_mini_assignment.data.IngredientDetail
import com.adwardstark.dd_mini_assignment.databinding.LayoutIngredientItemBinding
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import java.util.*

/**
 * Created by Aditya Awasthi on 10/09/21.
 * @author github.com/adwardstark
 */
class IngredientListAdapter: RecyclerView.Adapter<IngredientListAdapter.IngredientViewHolder>(),
    Filterable {

    inner class IngredientViewHolder(val viewBinder: LayoutIngredientItemBinding)
        : RecyclerView.ViewHolder(viewBinder.root)

    private val differCallback = object: DiffUtil.ItemCallback<IngredientDetail>() {
        override fun areItemsTheSame(oldItem: IngredientDetail, newItem: IngredientDetail): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: IngredientDetail, newItem: IngredientDetail): Boolean {
            return oldItem == newItem
        }
    }

    private val _differList = AsyncListDiffer(this, differCallback)
    private var _originalList = listOf<IngredientDetail>()
    private fun currentList(): List<IngredientDetail> = _differList.currentList
    fun newList(list: List<IngredientDetail>) {
        _differList.submitList(list)
        _originalList = list
    }
    private fun filterList(list: List<IngredientDetail>) = _differList.submitList(list)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IngredientViewHolder {
        return IngredientViewHolder(LayoutIngredientItemBinding
            .inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: IngredientViewHolder, position: Int) {
        with(holder) {
            with(currentList()[position]) {
                viewBinder.ingredientTitleText.text = title
                viewBinder.availableQuantityText.text = quantity.toString()
                Glide.with(itemView)
                    .load(image)
                    .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                    .into(viewBinder.ingredientImageView)
                if(quantity > 10) {
                    viewBinder.availableQuantityHeader
                        .setTextColor(itemView.resources.getColor(R.color.black, null))
                    viewBinder.availableQuantityText
                        .setTextColor(itemView.resources.getColor(R.color.light_grey, null))
                    viewBinder.availableQuantityHeader
                        .setBackgroundColor(itemView.resources.getColor(R.color.light_grey, null))
                    viewBinder.availableCard.strokeColor = itemView.resources
                        .getColor(R.color.light_grey, null)
                } else {
                    viewBinder.availableQuantityText
                        .setTextColor(itemView.resources.getColor(R.color.red, null))
                    viewBinder.availableQuantityHeader
                        .setTextColor(itemView.resources.getColor(R.color.white, null))
                    viewBinder.availableQuantityHeader
                        .setBackgroundColor(itemView.resources.getColor(R.color.red, null))
                    viewBinder.availableCard.strokeColor = itemView.resources
                        .getColor(R.color.red, null)
                }
            }
        }
    }

    override fun getItemCount(): Int = _differList.currentList.size

    override fun getFilter(): Filter = object: Filter() {
        override fun performFiltering(charSequence: CharSequence): FilterResults {
            val searchedText = charSequence.trim().toString().lowercase(Locale.getDefault())
            val filteredList = when {
                searchedText.isBlank() -> _originalList
                else -> {
                    currentList().filter {
                        it.title.lowercase(Locale.getDefault()).contains(searchedText)
                    }
                }
            }
            val filterResults = FilterResults()
            filterResults.values = filteredList
            return filterResults
        }
        override fun publishResults(text: CharSequence, results: FilterResults) {
            @Suppress("UNCHECKED_CAST")
            filterList(results.values as List<IngredientDetail>)
        }
    }
}