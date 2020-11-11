package com.silexsecure.arusdriver

import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.MenuItem
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.tabs.TabLayout
import com.silexsecure.arusdriver.adapters.StartFromScratchPagerAdapter
import com.silexsecure.arusdriver.model.RegisterDriverRequest
import com.silexsecure.arusdriver.model.User
import com.silexsecure.arusdriver.util.CustomViewPager
import com.silexsecure.arusdriver.util.Helper

class RegisterActivity : AppCompatActivity() {

    lateinit var tabLayout: TabLayout
    lateinit var viewPager: CustomViewPager
    lateinit var tvTitle: TextView
    val TAG = "RegistrationTAG"

    companion object {
        var currentTab = 0

        lateinit var helper: Helper
        lateinit var userMe: User
        lateinit var registerRequest: RegisterDriverRequest
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        viewPager = findViewById(R.id.viewpager_main)
        tabLayout = findViewById(R.id.viewpagertab)
        tvTitle= findViewById(R.id.tvTitle)

        registerRequest = RegisterDriverRequest()

        supportActionBar!!.title = "Driver Registration"

        val fragmentAdapter = StartFromScratchPagerAdapter(supportFragmentManager)
        viewPager.adapter = fragmentAdapter

        tabLayout.setupWithViewPager(viewPager)

        tabLayout.isSmoothScrollingEnabled = false
        viewPager.setPagingEnabled(false)
        tabLayout.isEnabled = false

        tabLayout.canScrollHorizontally(0)

        setUntouchableTab()

        viewPager.addOnPageChangeListener(
                TabLayout.TabLayoutOnPageChangeListener(
                        tabLayout
                )
        )

        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {

                when (tab.position) {
                    0 -> tvTitle.text = getString(R.string.driver_information)
                    1 -> tvTitle.text = getString(R.string.vehicle_information)
                    2 -> tvTitle.text = getString(R.string.location_n_address)
                    3 -> tvTitle.text = getString(R.string.photo_n_license)
                }

                viewPager.currentItem = tab.position
                currentTab = tab.position
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {
                //onTabUnselected implementation
            }

            override fun onTabReselected(tab: TabLayout.Tab) {
                //onTabSelected implementation
            }
        })

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setUntouchableTab() {
        tabLayout.setupWithViewPager(viewPager, true)
        tabLayout.clearOnTabSelectedListeners()
        for (v in tabLayout.touchables) {
            v.isEnabled = false
        }
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        return if (keyCode == KeyEvent.KEYCODE_BACK) {
            Log.d(TAG, "onKeyDown: back pressed")
            if (currentTab == 0) {
                finish()
                Log.d(TAG, "onKeyDown: current position at the beginning $currentTab")

            } else {
                Log.d(TAG, "onKeyDown: current position is $currentTab")
                currentTab--
                viewPager.setCurrentItem(currentTab, true)
            }
            true

        } else {
            super.onKeyDown(keyCode, event)
        }
    }
}