package com.adwardstark.dd_mini_assignment.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.navigation.Navigation.findNavController
import androidx.navigation.ui.onNavDestinationSelected
import com.adwardstark.dd_mini_assignment.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

/**
 * Created by Aditya Awasthi on 08/09/21.
 * @author github.com/adwardstark
 */
@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var viewBinder: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinder = ActivityMainBinding.inflate(layoutInflater)
        setContentView(viewBinder.root)
        setSupportActionBar(viewBinder.toolbar)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return item.onNavDestinationSelected(findNavController(viewBinder.navHostFragment))
                || super.onOptionsItemSelected(item)
    }

    override fun onSupportNavigateUp(): Boolean {
        findNavController(viewBinder.navHostFragment).navigateUp()
        return true
    }
}