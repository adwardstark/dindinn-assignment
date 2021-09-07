package com.adwardstark.dd_mini_assignment.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.adwardstark.dd_mini_assignment.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var viewBinder: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinder = ActivityMainBinding.inflate(layoutInflater)
        setContentView(viewBinder.root)
    }
}