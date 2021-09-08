package com.adwardstark.dd_mini_assignment.ui

import androidx.fragment.app.Fragment

/**
 * Created by Aditya Awasthi on 08/09/21.
 * @author github.com/adwardstark
 */
fun Fragment.showHomeUp(enabled: Boolean) {
    if(this.activity is MainActivity) {
        (this.activity as MainActivity).supportActionBar?.setDisplayHomeAsUpEnabled(enabled)
    }
}