package com.adwardstark.dd_mini_assignment.ui.fragments

import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.LinearLayout
import androidx.appcompat.widget.SearchView
import androidx.cardview.widget.CardView
import androidx.core.view.get
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.adwardstark.dd_mini_assignment.R
import com.adwardstark.dd_mini_assignment.data.IngredientCategory
import com.adwardstark.dd_mini_assignment.databinding.FragmentIngredientListBinding
import com.adwardstark.dd_mini_assignment.ui.OrderServiceViewModel
import com.adwardstark.dd_mini_assignment.ui.adapters.IngredientListAdapter
import com.adwardstark.dd_mini_assignment.utils.showHomeUp
import com.adwardstark.dd_mini_assignment.utils.showToast
import com.google.android.material.tabs.TabLayout
import dagger.hilt.android.AndroidEntryPoint

/**
 * Created by Aditya Awasthi on 08/09/21.
 * @author github.com/adwardstark
 */
@AndroidEntryPoint
class IngredientListFragment : Fragment() {

    companion object {
        private val TAG = IngredientListFragment::class.java.simpleName
    }

    private lateinit var viewBinder: FragmentIngredientListBinding
    private val onSearchQueryTextListener = object: SearchView.OnQueryTextListener {
        override fun onQueryTextSubmit(query: String?): Boolean {
            return true
        }
        override fun onQueryTextChange(newText: String?): Boolean {
            Log.d(TAG, "->onSearchQueryTextChange() $newText")
            ingredientListAdapter.filter.filter(newText)
            return false
        }
    }
    private val orderServiceViewModel: OrderServiceViewModel by viewModels()
    private lateinit var selectedTab: String
    private val onTabSelectedListener = object: TabLayout.OnTabSelectedListener {
        override fun onTabSelected(tab: TabLayout.Tab?) {
            selectedTab = tab?.text.toString()
            Log.d(TAG, "->selectedTab() $selectedTab")
            if(ingredientCategoryList.isNotEmpty()) {
                ingredientListAdapter.newList(ingredientCategoryList
                    .first { it.categoryTitle == selectedTab }.items)
            }
        }
        override fun onTabUnselected(tab: TabLayout.Tab?) {
            // Do nothing
        }
        override fun onTabReselected(tab: TabLayout.Tab?) {
            // Do nothing
        }
    }

    private val ingredientListAdapter: IngredientListAdapter by lazy { IngredientListAdapter() }
    private val ingredientCategoryList = arrayListOf<IngredientCategory>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View {
        setHasOptionsMenu(true)
        showHomeUp(true)
        // Inflate the layout for this fragment
        viewBinder = FragmentIngredientListBinding
            .inflate(layoutInflater, container, false)
        return viewBinder.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.search_view_menu, menu)
        val searchItem = menu.findItem(R.id.action_search).actionView as LinearLayout
        val searchView = (searchItem[0] as CardView)[0] as SearchView
        searchView.setOnQueryTextListener(onSearchQueryTextListener)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        orderServiceViewModel.ingredientList.observe(viewLifecycleOwner) { listOfIngredients ->
            if(listOfIngredients.isNotEmpty()) {
                ingredientCategoryList.clear()
                ingredientCategoryList.addAll(listOfIngredients)
                setupTabs(listOfIngredients.map { it.categoryTitle })
            } else {
                showToast("No ingredients found")
            }
        }
        orderServiceViewModel.getIngredients()

        viewBinder.tabLayout.addOnTabSelectedListener(onTabSelectedListener)
    }

    private fun setupTabs(tabTitles: List<String>) {
        viewBinder.tabLayout.removeAllTabs()
        tabTitles.forEach { title ->
            viewBinder.tabLayout.addTab(viewBinder.tabLayout.newTab().setText(title))
        }
    }

    private fun setupRecyclerView(isPortrait: Boolean = true) {
        viewBinder.rvIngredientList.apply {
            layoutManager = if(isPortrait) {
                GridLayoutManager(this.context, 2, RecyclerView.VERTICAL, false)
            } else {
                GridLayoutManager(this.context, 4, RecyclerView.VERTICAL, false)
            }
            adapter = ingredientListAdapter
            itemAnimator = DefaultItemAnimator()
        }
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        setupRecyclerView(newConfig.orientation != Configuration.ORIENTATION_LANDSCAPE)
    }
}