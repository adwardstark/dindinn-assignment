package com.adwardstark.dd_mini_assignment.ui.fragments

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import com.adwardstark.dd_mini_assignment.R
import com.adwardstark.dd_mini_assignment.databinding.FragmentOrderListBinding
import com.adwardstark.dd_mini_assignment.ui.showHomeUp

/**
 * Created by Aditya Awasthi on 08/09/21.
 * @author github.com/adwardstark
 */
class OrderListFragment : Fragment() {

    private lateinit var viewBinder: FragmentOrderListBinding

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
}