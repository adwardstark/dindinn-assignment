package com.adwardstark.dd_mini_assignment.ui.fragments

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import com.adwardstark.dd_mini_assignment.databinding.FragmentIngredientListBinding
import com.adwardstark.dd_mini_assignment.utils.showHomeUp

/**
 * Created by Aditya Awasthi on 08/09/21.
 * @author github.com/adwardstark
 */
class IngredientListFragment : Fragment() {

    private lateinit var viewBinder: FragmentIngredientListBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View {
        setHasOptionsMenu(false)
        showHomeUp(true)
        // Inflate the layout for this fragment
        viewBinder = FragmentIngredientListBinding
            .inflate(layoutInflater, container, false)
        return viewBinder.root
    }
}