package com.silexsecure.arusdriver.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.silexsecure.arusdriver.driverregistration.DriverInfoFragment
import com.silexsecure.arusdriver.driverregistration.LocationAddressFragment
import com.silexsecure.arusdriver.driverregistration.PhotoLicenseFragment
import com.silexsecure.arusdriver.driverregistration.VehicleInformationFragment

class StartFromScratchPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {
    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> {
                DriverInfoFragment()
            }
            1 -> {
                VehicleInformationFragment()
            }
            2 -> {
                LocationAddressFragment()
            }
            3 -> {
                PhotoLicenseFragment()
            }
            else -> {
                return VehicleInformationFragment()
            }
        }
    }

    override fun getCount(): Int {
        return 4
    }

    override fun getPageTitle(position: Int): CharSequence {
        return when (position) {
            0 -> ""
            1 -> ""
            2 -> ""
            3 -> ""
            else -> {
                return ""
            }
        }
    }

}