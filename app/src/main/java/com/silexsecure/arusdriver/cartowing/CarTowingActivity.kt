package com.silexsecure.arusdriver.cartowing

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.navigation.fragment.NavHostFragment
import com.silexsecure.arusdriver.R
import kotlinx.android.synthetic.main.activity_car_towing.*

class CarTowingActivity : AppCompatActivity() {

    lateinit var toolbar: Toolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_car_towing)

        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        NavHostFragment.findNavController(car_towing_host).navigateUp()

    }

    override fun onSupportNavigateUp(): Boolean {
        return super.onSupportNavigateUp()
        finish()
    }
}