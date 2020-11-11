package com.silexsecure.arusdriver

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.navigation.fragment.NavHostFragment
import kotlinx.android.synthetic.main.activity_sell_product.*


class SellFuel : AppCompatActivity() {

    lateinit var toolbar: Toolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sell_product)

        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        fun onSupportNavigateUp() = NavHostFragment.findNavController(nav_host_fragment).navigateUp()

    }

    override fun onSupportNavigateUp(): Boolean {
        return super.onSupportNavigateUp()
        finish()
    }
}